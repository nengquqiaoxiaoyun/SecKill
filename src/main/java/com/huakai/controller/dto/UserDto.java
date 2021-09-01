package com.huakai.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.*;

/**
 * @author: huakaimay
 * @since: 2021-08-30
 */
public class UserDto {
    private Integer id;
    
    @NotBlank(message = "用户名不能为空")
    private String name;

    private Byte gender;

    @Min(value = 0, message = "年龄过小")
    @Max(value = 150, message = "年龄过大")
    private Integer age;

    @NotBlank(message = "手机不能为空")
    @Pattern(regexp = "(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}", message = "请输入正确的手机号")
    private String telephone;
    @JsonIgnore
    private String registerMode;
    @JsonIgnore
    private String thirdPartId;
    @JsonIgnore
    private String encryptPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartId() {
        return thirdPartId;
    }

    public void setThirdPartId(String thirdPartId) {
        this.thirdPartId = thirdPartId;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }
}
