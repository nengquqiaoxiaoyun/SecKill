package com.huakai.mapper.dataobject;

import java.io.Serializable;

public class UserPasswordDO implements Serializable {
    /**
     * This field corresponds to the database column user_password.id
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    private Integer id;

    /**
     * This field corresponds to the database column user_password.encrypt_password
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    private String encryptPassword;

    /**
     * This field corresponds to the database column user_password.user_id
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    private Integer userId;

    /**
     * This field corresponds to the database table user_password
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.id
     *
     * @return the value of user_password.id
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.id
     *
     * @param id the value for user_password.id
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.encrypt_password
     *
     * @return the value of user_password.encrypt_password
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    public String getEncryptPassword() {
        return encryptPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.encrypt_password
     *
     * @param encryptPassword the value for user_password.encrypt_password
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.user_id
     *
     * @return the value of user_password.user_id
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.user_id
     *
     * @param userId the value for user_password.user_id
     *
     * @mbggenerated Mon Aug 30 14:58:59 CST 2021
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}