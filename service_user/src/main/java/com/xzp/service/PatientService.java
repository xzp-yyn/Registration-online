package com.xzp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzp.model.user.Patient;

import java.util.List;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/13 10:08
 * @Version 1.0
 */
public interface PatientService extends IService<Patient> {
    List<Patient> getallPatient(String token);

    Patient selectOnePacked(String id);

    void savepatient(Patient patient);

}
