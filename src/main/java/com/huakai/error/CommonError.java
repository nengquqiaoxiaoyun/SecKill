package com.huakai.error;

/**
 * @author: huakaimay
 * @since: 2021-08-31
 */
public interface CommonError {

    int getErrCode();
    String getErrMsg();

    /**
     * 根据实际情况改动通用错误信息
     */
    CommonError setCommonError(String errMsg);

}
