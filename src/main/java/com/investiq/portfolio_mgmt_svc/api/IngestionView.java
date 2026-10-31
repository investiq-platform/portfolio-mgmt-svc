package com.investiq.portfolio_mgmt_svc.api;
import com.fasterxml.jackson.annotation.JsonProperty;
public record IngestionView(@JsonProperty("scheme_code") long schemeCode, @JsonProperty("scheme_name") String schemeName, @JsonProperty("nav_records_received") int navRecordsReceived, @JsonProperty("nav_records_upserted") int navRecordsUpserted) { }
