package com.huakai.mapper.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PromoDo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promo.id
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promo.item_id
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private Integer itemId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promo.promo_name
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private String promoName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promo.price
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private BigDecimal price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promo.start_date
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private Date startDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column promo.end_date
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private Date endDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table promo
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promo.id
     *
     * @return the value of promo.id
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promo.id
     *
     * @param id the value for promo.id
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promo.item_id
     *
     * @return the value of promo.item_id
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promo.item_id
     *
     * @param itemId the value for promo.item_id
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promo.promo_name
     *
     * @return the value of promo.promo_name
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public String getPromoName() {
        return promoName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promo.promo_name
     *
     * @param promoName the value for promo.promo_name
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promo.price
     *
     * @return the value of promo.price
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promo.price
     *
     * @param price the value for promo.price
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promo.start_date
     *
     * @return the value of promo.start_date
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promo.start_date
     *
     * @param startDate the value for promo.start_date
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column promo.end_date
     *
     * @return the value of promo.end_date
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column promo.end_date
     *
     * @param endDate the value for promo.end_date
     *
     * @mbggenerated Fri Sep 03 10:28:51 CST 2021
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}