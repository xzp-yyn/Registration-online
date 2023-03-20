package com.xzp.controller.api;

import com.xzp.model.user.Patient;
import com.xzp.result.Result;
import com.xzp.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/13 10:09
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/patient")
@Api(tags = "就诊人接口")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/auth/listall")
    @ApiOperation("获得所有就诊人信息")
    public Result getPatientList(String token){
        List<Patient> patientList= patientService.getallPatient(token);
        return Result.success(patientList);
    }

    @GetMapping("/auth/{id}")
    @ApiOperation("根据ID查询就诊人信息")
    public Result findByid(@PathVariable String id){
        Patient patient = patientService.selectOnePacked(id);
        return Result.success(patient);
    }

    @PostMapping("/auth/save")
    @ApiOperation("新增就诊人")
    public Result savePatient(@RequestBody Patient patient){
        patientService.savepatient(patient);
        return Result.success();
    }

    @ApiOperation("更新就诊人")
    @PostMapping("/auth/update")
    public Result updatePatient(@RequestBody Patient patient){
        patientService.updateById(patient);
        return Result.success();
    }
}
