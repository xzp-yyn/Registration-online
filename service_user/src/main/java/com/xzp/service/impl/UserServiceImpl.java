package com.xzp.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzp.cmn.CmnClient;
import com.xzp.enums.AuthStatusEnum;
import com.xzp.mapper.UserMapper;
import com.xzp.model.user.Patient;
import com.xzp.model.user.User;
import com.xzp.result.ResultCode;
import com.xzp.service.PatientService;
import com.xzp.service.UserService;
import com.xzp.utils.CmnClientUtil;
import com.xzp.utils.FileCommonUtil;
import com.xzp.util.YyghException;
import com.xzp.utils.CodeGeneratior;
import com.xzp.util.TokenHelper;
import com.xzp.vo.user.LoginVo;
import com.xzp.vo.user.UserAdminQueryVo;
import com.xzp.vo.user.UserAuthVo;
import com.xzp.vo.user.UserStatusVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/9 17:05
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate<String,Object> template;

    @Autowired
    private PatientService patientService;

    @Autowired
    private CmnClientUtil clientUtil;

    /**
     * 登录
     *
     * @param user 用户
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> login(LoginVo user) {
        //判断条件是否为空
        if(user.getPhone()==null || user.getCode()==null){
            throw new YyghException(ResultCode.DATA_ERROR);
        }
        //TODO 判断验证码是否一致
        String key="phone:"+user.getPhone();
        String rediscode = (String) template.opsForValue().get(key);
        if(rediscode==null){
            throw new YyghException(ResultCode.CODE_EXPIRED);
        }
        if(!user.getCode().equals(rediscode)){
            throw new YyghException(ResultCode.CODE_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",user.getPhone());
        User user1 = baseMapper.selectOne(wrapper);
        if(user1==null){
            user1 = new User();
            user1.setName("");
            user1.setPhone(user.getPhone());
            user1.setNickName(user.getPhone());
            baseMapper.insert(user1);
            user1=baseMapper.selectOne(wrapper);
        }
        //判断状态是否禁用
        if(user1.getStatus()==0){
            throw new YyghException(ResultCode.LOGIN_DISABLED_ERROR);
        }
        //返回用户信息
        Map<String,Object> map=new HashMap<>();
        String username = user1.getName();
        if(StringUtils.isEmpty(username)){
            if(StringUtils.isEmpty(user1.getNickName())){
                username=user1.getPhone();
            }
            username=user1.getNickName();
        }
        String token = TokenHelper.createToken(user1.getId(), username);
        map.put("name",username);
        map.put("token",token);
        return map;
    }

    /**
     * 发送验证码
     *
     * @param phone 电话
     */
    @Override
    public void sendcode(String phone) {
        //判断redis中是否存在手机号
        String key="phone:"+phone;
        String phonecode = (String) template.opsForValue().get(key);
        if(phonecode!=null){
            throw new YyghException(ResultCode.PHONE_CODE_EXISTS);
        }
        String code= CodeGeneratior.generateCode(6);
        template.opsForValue().set(key,code,60, TimeUnit.SECONDS);
    }

    /**
     * userauth方法
     *
     * @param userAuthVo 用户身份验证签证官
     * @param userid     用户标识
     */
    @Override
    public void userauthMethod(UserAuthVo userAuthVo, Long userid) {
        User user = baseMapper.selectById(userid);
        if(user==null){
            throw new YyghException(ResultCode.FETCH_USERINFO_ERROR);
        }
        user.setName(userAuthVo.getName());
        user.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        user.setCertificatesNo(userAuthVo.getCertificatesNo());
        user.setCertificatesType(userAuthVo.getCertificatesType());
        user.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        baseMapper.updateById(user);
    }

    /**
     *
     * @param file
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> uploadfile(MultipartFile file) {
        String url = FileCommonUtil.upload(file);
        Map<String, Object> map = new HashMap<>();
        map.put("url","http://localhost:9003/"+url);
        return map;
    }

    /**
     * 用户列表
     *
     * @param page1        所述
     * @param adminQueryVo 管理员查询签证官
     * @return {@link Page}<{@link User}>
     */
    @Override
    public Page<User> userList(Page<User> page1, UserAdminQueryVo adminQueryVo) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(adminQueryVo.getKeyword()),User::getName,adminQueryVo.getKeyword());
        wrapper.eq(adminQueryVo.getAuthStatus()!=null,User::getAuthStatus,adminQueryVo.getAuthStatus());
        wrapper.eq(adminQueryVo.getStatus()!=null,User::getStatus,adminQueryVo.getStatus());
        wrapper.ge(!StringUtils.isEmpty(adminQueryVo.getCreateTimeBegin()),User::getCreateTime,adminQueryVo.getCreateTimeBegin());
        wrapper.le(!StringUtils.isEmpty(adminQueryVo.getCreateTimeEnd()),User::getCreateTime,adminQueryVo.getCreateTimeEnd());
        Page<User> userPage = baseMapper.selectPage(page1, wrapper);
        return userPage;
    }

    @Override
    public void updateStatus(UserStatusVo statusVo) {
        User user = new User();
        BeanUtils.copyProperties(statusVo,user);
        baseMapper.updateById(user);
    }

    @Override
    public Map<String, Object> selectUserAndPatientByid(Long id) {
        Map<String,Object> map= new HashMap<>();
        //封装用户信息
        User user = baseMapper.selectById(id);
        map.put("user",user);
        //封装就诊人信息
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId,id);
        List<Patient> patients = patientService.list(wrapper);
        patients.stream().forEach(item->{
            if(item.getCertificatesType()!=null || !item.getCertificatesType().equals("")){
                String typename =clientUtil.getByNotEmpty(item.getCertificatesType());
                item.getParam().put("typename",typename.replaceAll("\"",""));
                String province = clientUtil.getByNotEmpty(item.getProvinceCode());
                String city = clientUtil.getByNotEmpty(item.getCityCode());
                String dis = clientUtil.getByNotEmpty(item.getDistrictCode());
                item.getParam().put("address",(province+city+dis+item.getAddress()).replaceAll("\"",""));
            }
        });
        map.put("patientList",patients);
        return map;
    }


}
