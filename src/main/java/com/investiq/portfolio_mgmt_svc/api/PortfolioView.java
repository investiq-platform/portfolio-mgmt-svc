package com.investiq.portfolio_mgmt_svc.api;
import java.util.List;
public record PortfolioView(long id, String name, List<HoldingView> holdings) { }
