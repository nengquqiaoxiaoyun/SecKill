package com.huakai.mapper.dataobject;

import java.io.Serializable;

public class StockLogDO implements Serializable {
    private String stockLogId;

    private Integer itemId;

    private Integer amount;

    private Byte status;

    private static final long serialVersionUID = 1L;

    public String getStockLogId() {
        return stockLogId;
    }

    public void setStockLogId(String stockLogId) {
        this.stockLogId = stockLogId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}