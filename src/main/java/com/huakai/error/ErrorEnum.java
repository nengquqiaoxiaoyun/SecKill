package com.huakai.error;

/**
 * @author: huakaimay
 * @since: 2021-08-31
 * 错误枚举类，可以根据需求增加响应码和信息
 */
public enum ErrorEnum implements CommonError {
    /**
     * 通用错误信息，可根据实际情况改动
     * e.g.
     * {@code
     * new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "邮箱不能为空")
     * BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "手机号不能为空")
     * }
     */
    PARAMTER_VALIDATION_ERROR(10001, "参数错误"),
    UNKNOWN_ERROR(10009, "未知错误"),

    USER_NOT_EXIST(20001, "用户不存在"),
    LOGIN_FAIL(20002, "登录失败，手机号或密码错误");

    ErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private int errorCode;
    private String errorMsg;

    @Override
    public int getErrCode() {
        return errorCode;
    }

    @Override
    public String getErrMsg() {
        return errorMsg;
    }

    @Override
    public CommonError setCommonError(String errMsg) {
        this.errorMsg = errMsg;
        return this;
    }
}
