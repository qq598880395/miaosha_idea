package com.imooc.miaosha.controller;


import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static javafx.scene.input.KeyCode.T;

@Controller
@RequestMapping("/demo")
public class SampleController {
@Autowired
    UserService userService;
@Autowired
    RedisService redisService;
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
    //1.rest api json输出 2.页面
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello,imooc");
        // return new Result(0, "success", "hello,imooc");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
        //return new Result(500102, "XXX");
    }

    @RequestMapping("/thymeleaf")
    public String  thymeleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public  Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public  Result<Long> redisGet(){
        Long v1 = redisService.get("key1",Long.class);

        return Result.success(v1);
    }
    @RequestMapping("/redis/set")
    @ResponseBody
    public  Result<String> redisSet(){
        boolean ret   = redisService.set("key2","hello imooc");
        String  str   = redisService.get("key2",String.class);

        return Result.success(str);
    }
}
