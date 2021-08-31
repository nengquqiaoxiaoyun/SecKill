package com.huakai.response;

/**
 * @author: huakaimay
 * @since: 2021-08-31
 */
public class CommonReturnType {

    /**
     * success
     * fail
     */
    private String status;

    /**
     * if status is success return object
     * if status is fail return common error
     */
    private Object data;


    /**
     * 指定状态和对象
     *
     * @param status fail or success
     */
    public static CommonReturnType create(Object data, String status) {
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setData(data);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }

    /**
     * 不指定状态码默认为success
     */
    public static CommonReturnType create(Object data) {
        return create(data, "success");
    }

    private CommonReturnType() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
