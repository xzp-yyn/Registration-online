package com.xzp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzp.model.user.User;
import com.xzp.result.Result;
import com.xzp.service.UserService;
import com.xzp.vo.user.UserAdminQueryVo;
import com.xzp.vo.user.UserStatusVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/13 17:18
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户平台管理")
public class UserManager {

    @Autowired
    private UserService userService;

    /**
     * 用户列表
     *
     * @return {@link Result}
     */
    @ApiOperation("用户列表")
    @GetMapping("/list/{page}/{limit}")
    public Result userList(@PathVariable("page")int page,
                           @PathVariable("limit")int limit,
                           UserAdminQueryVo adminQueryVo){
        Page<User> page1 = new Page<>(page, limit);
        Page<User> page2= userService.userList(page1,adminQueryVo);
        return Result.success(page2);
    }

    @ApiOperation("修改状态与认证状态")
    @PostMapping("/updatestatus")
    public Result updatestatus(@RequestBody UserStatusVo statusVo){
        userService.updateStatus(statusVo);
        return Result.success();
    }

    @ApiOperation("得到用户信息详情")
    @GetMapping("/{id}")
    public Result selectOne(@PathVariable Long id){
        Map<String,Object> model= userService.selectUserAndPatientByid(id);
        return Result.success(model);
    }



}
