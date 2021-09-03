package com.huakai.controller.dto;

import java.math.BigDecimal;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 * 交易订单模型
 */
public class OrderDto {

    /**
     * 订单号
     * yyyyMMdd
     * 六位自增序列
     * 两位分区
     */
    private String id;

    private Integer userId;

    private Integer itemId;

    /**
     * 购买时商品单价
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 购买的总价
     */
    private BigDecimal totalPrice;


    /**
     * 秒杀id
     */
    private Integer promoId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
