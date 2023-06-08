package com.huakai.controller.dto;

/**
 * @author: huakaimay
 * @since: 2023-06-05
 */

public class ItemAmount {

    /**
     * 商品id
     */
    private Integer id;

    private Integer userId;

    private Integer promoId;

    private Integer amount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
