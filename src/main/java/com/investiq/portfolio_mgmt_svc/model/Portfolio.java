package com.investiq.portfolio_mgmt_svc.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id", nullable = false, updatable = false)
    private String ownerId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    @Version
    private long version;
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("occurredOn ASC, id ASC")
    private List<InvestmentTransaction> transactions = new ArrayList<>();

    protected Portfolio() {
    }

    public Portfolio(String ownerId, String name) {
        this.ownerId = ownerId;
        this.name = name;
    }
    public Long getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public List<InvestmentTransaction> getTransactions() { return transactions; }
    public void addTransaction(InvestmentTransaction transaction) { transactions.add(transaction); }
}
