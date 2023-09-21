package cn.hfbin.seckill.service.ipml;

import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.dao.UserMapper;
import cn.hfbin.seckill.param.LoginParam;
import cn.hfbin.seckill.param.UserParam;
import cn.hfbin.seckill.redis.RedisService;
import cn.hfbin.seckill.redis.UserKey;
import cn.hfbin.seckill.result.CodeMsg;
import cn.hfbin.seckill.result.Result;
import cn.hfbin.seckill.service.UserService;
import cn.hfbin.seckill.util.EncryptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 12:01
 * Such description:
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisService redisService;
    private final Logger logger = Logger.getLogger(String.valueOf(UserServiceImpl.class));

    @Override
    public Result<User> login(LoginParam loginParam) throws Exception {

        User user = userMapper.checkPhone(loginParam.getMobile());
        if (user == null) {
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        String decPwd = EncryptionUtils.decrypt(user.getPassword());
        String calcPass = loginParam.getPassword();
        if (!StringUtils.equals(decPwd, calcPass)) {
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);
    }

    public Result<List<User>> getUserInfoByKeyword(String keyword) {
        List<User> userList = userMapper.getUserInfoByKeyword(keyword);
        if (userList == null || userList.size() <= 0) {
            return Result.error(CodeMsg.USER_NOT_FOUND);
        }
        return Result.success(userList);
    }

    //性能测试接口，查询用户数据，写入缓存，缓存过期时间5秒
    public Result<List<User>> performanceTest(String keyword) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90, 87, 76, 56, 45, 32, 21, 98, 89, 73, 61, 49, 37, 28, 15, 8, 3, 0, -5};
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换 arr[j] 和 arr[j + 1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    List<User> userList = redisService.get(UserKey.getByKeyword, keyword, List.class);
        if (userList != null) {
            System.out.println("使用缓存");
        } else {
            System.out.println("查询数据库");
            userList = userMapper.getUserInfoByKeyword(keyword);
            redisService.set(UserKey.getByKeyword,keyword,userList,5);
        }
        if (userList == null || userList.size() <= 0) {
            return Result.error(CodeMsg.USER_NOT_FOUND);
        }

        return Result.success(userList);
    }

    public Result<User> createUser(UserParam userParam) throws Exception {
        // 创建User对象并设置属性
        User user = new User();
        user.setUserName(userParam.getUser_name());
        user.setPassword(EncryptionUtils.encrypt(userParam.getPassword()));
        user.setPhone(userParam.getPhone());
        user.setHead(userParam.getHead());
        user.setLoginCount(userParam.getLoginCount());
        //取当前时间设置为注册时间
        user.setRegisterDate(new Date());
        user.setLastLoginDate(userParam.getLastLoginDate());

        // 调用Mapper接口中的insertUser方法插入数据
        boolean isSuccess = userMapper.createUser(user);
        if (!isSuccess) {
            return Result.error(CodeMsg.USER_CREATE_ERROR);
        }

        // 注意这里只是示例取第一个用户
        User createdUser = userMapper.getUserInfoByKeyword(user.getUserName()).get(0);
        return Result.success(createdUser);
    }

}
