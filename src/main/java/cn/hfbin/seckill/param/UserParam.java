package cn.hfbin.seckill.param;

import cn.hfbin.seckill.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserParam {
    @NotNull(message = "手机号不能为空")
    @IsMobile()
    private String phone;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "姓名不能为空")
    private String user_name;

    private String head;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate;
    private int loginCount;
    private String salt;
}
