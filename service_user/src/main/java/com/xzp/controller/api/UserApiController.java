package com.xzp.controller.api;

import com.xzp.model.user.User;
import com.xzp.result.Result;
import com.xzp.service.UserService;
import com.xzp.util.AuthHelper;
import com.xzp.util.TokenHelper;
import com.xzp.vo.user.LoginVo;
import com.xzp.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/9 17:03
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户接口")
public class UserApiController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody LoginVo user){
       Map<String,Object> map= userService.login(user);
       return Result.success(map);
    }

    @GetMapping("/sendcode")
    @ApiOperation("得到验证码并存入redis")
    public Result sendCode(String phone){
        userService.sendcode(phone);
        return Result.success();
    }

    @ApiOperation("根据id查询用户信息")
    @GetMapping("/auth/getuserinfo")
    public Result getUserByid(String token,HttpServletRequest request){
//        工具类获取用户id
        Long userId = AuthHelper.getUserId(request,token);
        User user = userService.getById(userId);
        return Result.success(user);
    }

    @ApiOperation("用户认证")
    @PostMapping("/auth/userauth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo){
        userService.userauthMethod(userAuthVo,TokenHelper.getUserId((String) userAuthVo.getParam().get("token")));
        return Result.success();
    }

    @ApiOperation("用户证件上传")
    @PostMapping("/auth/upload")
    public Result upload(MultipartFile file){
        Map<String,Object> result= userService.uploadfile(file);
        return Result.success(result);
    }

}
