package cn.hfbin.seckill.controller;

import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.mq.MQSender;
import cn.hfbin.seckill.param.UserParam;
import cn.hfbin.seckill.redis.RedisService;
import cn.hfbin.seckill.result.CodeMsg;
import cn.hfbin.seckill.result.Result;
import cn.hfbin.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;
    @Autowired
    MQSender mqSender;
    @GetMapping("/info")
    @ResponseBody
    public Result<List<User>> getUserInfoByKeyword(String keyword){
        Result<List<User>> result = userService.getUserInfoByKeyword(keyword);
        return result;
    }
    @PostMapping("/create")
    @ResponseBody
    public Result<User> createUser(@Valid UserParam userParam) throws Exception {

        // 调用Service层的创建用户方法
        Result<User> result = userService.createUser(userParam);
        return result;
    }



}
