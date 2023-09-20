package cn.hfbin.seckill.service.ipml;

import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.dao.UserMapper;
import cn.hfbin.seckill.param.LoginParam;
import cn.hfbin.seckill.param.UserParam;
import cn.hfbin.seckill.result.CodeMsg;
import cn.hfbin.seckill.result.Result;
import cn.hfbin.seckill.service.UserService;
import cn.hfbin.seckill.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 12:01
 * Such description:
 */
@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Override
    public Result<User> login(LoginParam loginParam) {

        User user = userMapper.checkPhone(loginParam.getMobile());
        if(user == null){
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPwd= user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if(!StringUtils.equals(dbPwd , calcPass)){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);
    }

    public Result<List<User>> getUserInfoByKeyword(String keyword){
        List<User> userList = userMapper.getUserInfoByKeyword(keyword);
        if (userList == null || userList.size() <= 0){
            return Result.error(CodeMsg.USER_NOT_FOUND);
        }
        return Result.success(userList);
    }

    public Result<User> createUser(UserParam userParam) {
        // 创建User对象并设置属性
        User user = new User();
        user.setUserName(userParam.getUser_name());
        user.setPassword(userParam.getPassword());
        user.setPhone(userParam.getPhone());
        user.setSalt(MD5Util.salt);
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
