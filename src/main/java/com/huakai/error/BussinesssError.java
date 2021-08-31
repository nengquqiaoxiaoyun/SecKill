package com.huakai.error;

/**
 * @author: huakaimay
 * @since: 2021-08-31
 */
public class BussinesssError extends Exception implements CommonError{

    private CommonError commonError;

    /**
     * 接收CommonError的实现（ErrorEnum）用于构造业务异常
     */
    public BussinesssError(CommonError commonError) {
        // 子类构造会调用父类的无参构造方法
        super();
        this.commonError = commonError;
    }


    /**
     * 自定义异常
     */
    public BussinesssError(CommonError commonError, String errorMsg) {
        // 子类构造会调用父类的无参构造方法
        super();
        this.commonError = commonError;
        this.commonError.setCommonError(errorMsg);
    }


    @Override
    public int getErrCode() {
        return commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return commonError.getErrMsg();
    }

    @Override
    public CommonError setCommonError(String errMsg) {
        commonError.setCommonError(errMsg);
        return this;
    }
}
