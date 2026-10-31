package com.investiq.portfolio_mgmt_svc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    protected Portfolio() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
