package com.investiq.portfolio_mgmt_svc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "investment_transactions")
public class InvestmentTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "portfolio_id", nullable = false) private Portfolio portfolio;
    @Column(nullable = false) private long schemeCode;
    @Column(nullable = false) private String schemeName;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TransactionType transactionType;
    @Column(nullable = false, precision = 20, scale = 6) private BigDecimal units;
    @Column(nullable = false, precision = 20, scale = 6) private BigDecimal nav;
    @Column(nullable = false) private LocalDate occurredOn;
    protected InvestmentTransaction() { }
    public InvestmentTransaction(Portfolio portfolio, long schemeCode, String schemeName, TransactionType transactionType, BigDecimal units, BigDecimal nav, LocalDate occurredOn) {
        this.portfolio = portfolio; this.schemeCode = schemeCode; this.schemeName = schemeName; this.transactionType = transactionType; this.units = units; this.nav = nav; this.occurredOn = occurredOn;
    }
    public long getSchemeCode() { return schemeCode; }
    public String getSchemeName() { return schemeName; }
    public TransactionType getTransactionType() { return transactionType; }
    public BigDecimal getUnits() { return units; }
    public BigDecimal getNav() { return nav; }
    public LocalDate getOccurredOn() { return occurredOn; }
}
