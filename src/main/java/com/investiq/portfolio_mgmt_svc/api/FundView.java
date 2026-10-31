package com.investiq.portfolio_mgmt_svc.api;
import com.fasterxml.jackson.annotation.JsonProperty;
public record FundView(@JsonProperty("scheme_code") long schemeCode, @JsonProperty("scheme_name") String schemeName) { }
