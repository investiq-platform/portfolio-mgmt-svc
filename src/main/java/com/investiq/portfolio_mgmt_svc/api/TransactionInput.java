package com.investiq.portfolio_mgmt_svc.api;
import com.investiq.portfolio_mgmt_svc.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
public record TransactionInput(long portfolioId, long schemeCode, String schemeName, TransactionType type, BigDecimal units, BigDecimal nav, LocalDate occurredOn) { }
