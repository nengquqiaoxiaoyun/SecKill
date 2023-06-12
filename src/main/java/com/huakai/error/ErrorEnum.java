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
    LOGIN_FAIL(20002, "登录失败，手机号或密码错误"),
    ITEM_NOT_EXIST(20003, "商品不存在"),
    STOCK_NOT_ENOUGH(20004, "库存不足"),
    USER_NOT_LOGIN(20005, "用户未登录"),
    PROMO_NOT_EXIST(20006, "活动不存在"),
    PROMO_NO_ITEM(20007, "活动没有对应商品"),
    PROMO_TOKEN_ERROR(30002, "活动令牌获取失败"),
    MQ_SEND_FAIL(30001, "异步消息发送失败")
    ;

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
