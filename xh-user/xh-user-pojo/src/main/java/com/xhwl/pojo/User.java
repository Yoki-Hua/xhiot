package com.xhwl.pojo;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "xhwl_user")
@Data
public class User implements Serializable {
    @Id
    private String username;//用户名
    private String password;//密码，加密存储
    private String phone;//注册手机号
    private String email;//注册邮箱
    private java.util.Date create_time;//创建时间
    private java.util.Date update_time;//修改时间
    private String name;//真实姓名
    private String status;//使用状态（1正常 0非正常）
    private String qq;//QQ号码
    private String sex;//性别，1男，0女
    private java.util.Date birthday;//出生年月日


}
