package com.investiq.portfolio_mgmt_svc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

/** Time-weighted analytics helpers. XIRR uses Newton-Raphson and rejects non-converging cash flows. */
@Service
public class ReturnsService {
  public BigDecimal cagr(BigDecimal beginning, BigDecimal ending, LocalDate start, LocalDate end) {
    if (beginning.signum() <= 0 || ending.signum() < 0 || !end.isAfter(start)) return BigDecimal.ZERO;
    double years = ChronoUnit.DAYS.between(start, end) / 365.25d;
    return BigDecimal.valueOf(Math.pow(ending.divide(beginning, 12, RoundingMode.HALF_UP).doubleValue(), 1 / years) - 1).setScale(6, RoundingMode.HALF_UP);
  }
  public BigDecimal xirr(List<CashFlow> flows) {
    if (flows.size() < 2 || flows.stream().noneMatch(f -> f.amount.signum() < 0) || flows.stream().noneMatch(f -> f.amount.signum() > 0)) throw new IllegalArgumentException("XIRR needs both investment and return cash flows");
    LocalDate base = flows.stream().map(CashFlow::date).min(LocalDate::compareTo).orElseThrow(); double r = .10d;
    for (int i=0;i<100;i++) { double f=0, df=0; for(var c:flows) { double y=ChronoUnit.DAYS.between(base,c.date)/365.25d, d=Math.pow(1+r,y); f+=c.amount.doubleValue()/d; df-=y*c.amount.doubleValue()/Math.pow(1+r,y+1); } double next=r-f/df; if(Math.abs(next-r)<1e-8) return BigDecimal.valueOf(next).setScale(6,RoundingMode.HALF_UP); r=next; }
    throw new IllegalArgumentException("XIRR did not converge");
  }
  public record CashFlow(LocalDate date, BigDecimal amount) { }
}
