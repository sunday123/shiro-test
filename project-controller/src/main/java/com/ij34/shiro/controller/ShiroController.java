package com.ij34.shiro.controller;

import com.ij34.model.ServerResponse;
import com.ij34.shiro.model.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description ShiroController
 * @Date 2021/8/15
 * @Created by www.ij34.com
 */
@Api(tags = "shiro")
@RestController
@RequestMapping
public class ShiroController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "pub/loginPage登录界面")
    @GetMapping(value = "/pub/loginPage")
    public ServerResponse loginPage() {
        return ServerResponse.buildSuccess("重新登录！");
    }

    @ApiOperation(value = "pub/login登录接口")
    @PostMapping(value = "/pub/login")
    public ServerResponse login(Users user) {
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            subject.login(usernamePasswordToken);
            Map<String, Object> info = new HashMap<>();
            info.put("msg", "登录成功");
            info.put("session_id", subject.getSession().getId());
            return ServerResponse.buildSuccess(info);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ServerResponse.buildError("错误账号密码");
        }
    }


    @ApiOperation(value = "pub/index")
    @GetMapping(value = "/pub/index")
    public ServerResponse index() {
        return ServerResponse.buildSuccess("index success");
    }

    @ApiOperation(value = "pub/noPermit")
    @GetMapping(value = "/pub/noPermit")
    public ServerResponse noPermit() {
        return ServerResponse.buildSuccess("没有权限");
    }

    @ApiOperation(value = "article/update")
    @GetMapping(value = "/article/update")
    public ServerResponse update() {
        return ServerResponse.buildSuccess("成功article update");
    }

    @ApiOperation(value = "article/add")
    @GetMapping(value = "/article/add")
    public ServerResponse add() {
        return ServerResponse.buildSuccess("成功article add");
    }

    @ApiOperation(value = "/admin/test")
    @GetMapping(value = "/admin/test")
    public ServerResponse adminTest() {
        return ServerResponse.buildSuccess("成功admin test");
    }

    @ApiOperation(value = "user/test")
    @GetMapping(value = "/user/test")
    public ServerResponse userTest() {
        return ServerResponse.buildSuccess("成功user test");
    }

    @ApiOperation(value = "authc/test")
    @GetMapping(value = "/authc/test")
    public ServerResponse authcTest() {
        return ServerResponse.buildSuccess("成功authc test");
    }


}
