package com.investiq.portfolio_mgmt_svc.service;
import com.investiq.portfolio_mgmt_svc.api.HoldingView;
import com.investiq.portfolio_mgmt_svc.model.*;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;
@Service
public class HoldingService {
  public List<HoldingView> calculate(Portfolio p, java.util.function.LongFunction<BigDecimal> navs) {
    Map<Long, List<InvestmentTransaction>> grouped = new LinkedHashMap<>();
    for (var t : p.getTransactions()) grouped.computeIfAbsent(t.getSchemeCode(), k -> new ArrayList<>()).add(t);
    List<Raw> raw = new ArrayList<>();
    for (var e : grouped.entrySet()) { BigDecimal units=BigDecimal.ZERO, cost=BigDecimal.ZERO; String name=e.getValue().getFirst().getSchemeName();
      for (var t:e.getValue()) if(t.getTransactionType()==TransactionType.REDEMPTION) { if(units.signum()>0) cost=cost.subtract(cost.multiply(t.getUnits()).divide(units, 6, RoundingMode.HALF_UP)); units=units.subtract(t.getUnits()); } else { units=units.add(t.getUnits()); cost=cost.add(t.getUnits().multiply(t.getNav())); }
      if(units.signum()>0) { BigDecimal nav=navs.apply(e.getKey()); raw.add(new Raw(e.getKey(),name,units,cost,nav,units.multiply(nav))); }
    }
    BigDecimal total=raw.stream().map(Raw::value).reduce(BigDecimal.ZERO,BigDecimal::add);
    return raw.stream().map(r -> new HoldingView(r.code,r.name,r.units,r.cost,r.nav,r.value,r.value.subtract(r.cost),total.signum()==0?BigDecimal.ZERO:r.value.multiply(BigDecimal.valueOf(100)).divide(total,2,RoundingMode.HALF_UP))).toList();
  }
  private record Raw(long code,String name,BigDecimal units,BigDecimal cost,BigDecimal nav,BigDecimal value) { }
}
