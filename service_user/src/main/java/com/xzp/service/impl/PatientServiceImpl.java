package com.xzp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzp.mapper.PatientMapper;
import com.xzp.model.user.Patient;
import com.xzp.result.ResultCode;
import com.xzp.service.PatientService;
import com.xzp.util.TokenHelper;
import com.xzp.util.YyghException;
import com.xzp.utils.CmnClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/13 10:08
 * @Version 1.0
 */
@Service
public class PatientServiceImpl  extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private CmnClientUtil cmnClient;

    @Override
    public List<Patient> getallPatient(String token) {
        if(token.equals("") || token==null){
            throw new YyghException(ResultCode.DATA_ERROR);
        }
        Long userId = TokenHelper.getUserId(token);
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId,userId);
        List<Patient> patients = baseMapper.selectList(wrapper);
        patients.stream().forEach(item->{
            String CertificatesTypename  = cmnClient.getByNotEmpty(item.getCertificatesType());
            String contactsType = item.getContactsCertificatesType();
            String  ContactsCertificatesType = cmnClient.getByNotEmpty(contactsType);

            String province = cmnClient.getByNotEmpty(item.getProvinceCode());
            String cityname = cmnClient.getByNotEmpty(item.getCityCode());
            String distractname = cmnClient.getByNotEmpty(item.getDistrictCode());
            String fulladdress=(province==null?"":province+cityname==null?"":cityname+distractname==null?"":distractname+item.getAddress()).replaceAll("\"","");
            item.getParam().put("fulladdress",fulladdress);
            item.getParam().put("CertificatesTypename",CertificatesTypename.replaceAll("\"",""));
            item.getParam().put("ContactsCertificatesType",ContactsCertificatesType.replaceAll("\"",""));
        });
        return patients;
    }
    @Override
    public Patient selectOnePacked(String id) {
        Patient patient = baseMapper.selectById(id);
        if(patient==null){
            throw new YyghException(ResultCode.FETCH_USERINFO_ERROR);
        }
        String CertificatesTypename  = cmnClient.getByNotEmpty(patient.getCertificatesType()).replaceAll("\"","");
        String province = cmnClient.getByNotEmpty(patient.getProvinceCode()).replaceAll("\"","");
        String cityname = cmnClient.getByNotEmpty(patient.getCityCode()).replaceAll("\"","");
        String distractname = cmnClient.getByNotEmpty(patient.getDistrictCode()).replaceAll("\"","");
        patient.getParam().put("certificatesTypename",CertificatesTypename);
        // provinceString 
        patient.getParam().put("provinceString",province);
        patient.getParam().put("cityString",cityname);
        patient.getParam().put("districtString",distractname);
        return patient;
    }

    @Override
    public void savepatient(Patient patient) {
        String token = (String) patient.getParam().get("token");
        Long userId = TokenHelper.getUserId(token);
        patient.setUserId(userId);
        baseMapper.insert(patient);
    }
}
