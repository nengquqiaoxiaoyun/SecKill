package com.huakai.mapper.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.user_id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.item_id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private Integer itemId;

    /**
     * 秒杀商品
     */
    private Integer promoId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.price
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private BigDecimal price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.amount
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private Integer amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.total_price
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private BigDecimal totalPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_info
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.id
     *
     * @return the value of order_info.id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.id
     *
     * @param id the value for order_info.id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.user_id
     *
     * @return the value of order_info.user_id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.user_id
     *
     * @param userId the value for order_info.user_id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.item_id
     *
     * @return the value of order_info.item_id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.item_id
     *
     * @param itemId the value for order_info.item_id
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.price
     *
     * @return the value of order_info.price
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.price
     *
     * @param price the value for order_info.price
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.amount
     *
     * @return the value of order_info.amount
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.amount
     *
     * @param amount the value for order_info.amount
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.total_price
     *
     * @return the value of order_info.total_price
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.total_price
     *
     * @param totalPrice the value for order_info.total_price
     *
     * @mbggenerated Thu Sep 02 14:40:36 CST 2021
     */
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