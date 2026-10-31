package com.investiq.portfolio_mgmt_svc.api;
import com.fasterxml.jackson.annotation.JsonProperty; import java.math.BigDecimal; import java.time.LocalDate; import java.util.List;
public record NavHistoryView(@JsonProperty("scheme_code") long schemeCode, @JsonProperty("scheme_name") String schemeName, List<NavPoint> navs) { public record NavPoint(@JsonProperty("nav_date") LocalDate navDate, BigDecimal nav) { } }
