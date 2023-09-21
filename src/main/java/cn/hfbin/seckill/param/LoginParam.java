package cn.hfbin.seckill.param;

import cn.hfbin.seckill.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 10:11
 * Such description:
 */
@Getter
@Setter
@ToString
public class LoginParam {

    @NotNull(message = "手机号不能为空")
    @IsMobile()
    private String mobile;
    @NotNull(message="密码不能为空")
    @Length(max = 40, message = "密码长度需要在40个字以内")
    private String password;
}
