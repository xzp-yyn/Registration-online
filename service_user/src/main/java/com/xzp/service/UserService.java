package com.xzp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xzp.model.user.User;
import com.xzp.vo.user.LoginVo;
import com.xzp.vo.user.UserAdminQueryVo;
import com.xzp.vo.user.UserAuthVo;
import com.xzp.vo.user.UserStatusVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/9 17:04
 * @Version 1.0
 */
public interface UserService extends IService<User> {
    Map<String, Object> login(LoginVo user);

    void sendcode(String phone);

    void userauthMethod(UserAuthVo userAuthVo, Long token);

    Map<String, Object> uploadfile(MultipartFile file);

    Page<User> userList(Page<User> page1, UserAdminQueryVo adminQueryVo);

    void updateStatus(UserStatusVo statusVo);

    Map<String, Object> selectUserAndPatientByid(Long id);

}
