package hscsm.core.api.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Created by xieshuai on 2018/3/12.
 */
@ExtensionAttribute(disable=true)
@Table(name = "yx_hscs_user")
public class HscsUser extends BaseDTO {

    @Id
    @GeneratedValue
    private Long userId;

    private String userName;
    private String userDept;
    private Long userSalary;
    private String userPwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public Long getUserSalary() {
        return userSalary;
    }

    public void setUserSalary(Long userSalary) {
        this.userSalary = userSalary;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
