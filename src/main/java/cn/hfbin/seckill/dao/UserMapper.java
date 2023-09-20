package cn.hfbin.seckill.dao;

import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.param.UserParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * My Blog : www.hfbin.cn
 * github: https://github.com/hfbin
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 11:29
 * Such description:
 */
public interface UserMapper {

    User selectByPhoneAndPassword(@Param("phone") String phone , @Param("password") String password);

    User checkPhone(@Param("phone") String phone );

    List<User> getUserInfoByKeyword(@Param("keyword") String keyword);

    boolean creat1User(@Param("user_name") String user_name  , @Param("phone") String phone , @Param("password") String password,
                      @Param("head") String head , @Param("salt") String salt, @Param("login_count") int login_count);

    boolean createUser(User user);

}
