package com.huakai.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: huakaimay
 * @since: 2021-09-03
 * 秒杀经销活动
 */
public class PromoDto {

    private Integer id;

    private Integer itemId;

    /**
     * 秒杀状态
     * 1：未开始
     * 2：正在进行中
     * 3：已经结束
     */
    private Integer status;

    /**
     * 活动名
     */
    private String promoName;

    /**
     * 秒杀的价格
     */
    private BigDecimal price;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startDate;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
