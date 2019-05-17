package com.hq.model.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class SysUserEntity extends SuperEntity<SysUserEntity> implements Serializable {

    private static final long serialVersionUID = -5502509492623797566L;

    private String username;

    private String password;

    private String userPhone;

    private String email;

    private int age;

    private String sex;

    private String status;

    public String toStringCN() {
        return "注册用户信息： [姓名=" + username
                + ", 联系方式=" + userPhone + ", 邮件=" + email + ", 年龄="
                + age + ", 性别=" + sex + ", 状态=" + status + "]";
    }

}
