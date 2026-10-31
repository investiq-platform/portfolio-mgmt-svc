package com.investiq.portfolio_mgmt_svc.api;
import java.math.BigDecimal;
public record HoldingView(long schemeCode, String schemeName, BigDecimal units, BigDecimal investedCost, BigDecimal currentNav, BigDecimal currentValue, BigDecimal unrealizedGain, BigDecimal allocationPercent) { }
