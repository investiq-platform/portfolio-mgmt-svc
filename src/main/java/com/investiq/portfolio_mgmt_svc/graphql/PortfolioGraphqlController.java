package com.investiq.portfolio_mgmt_svc.graphql;
import com.investiq.portfolio_mgmt_svc.api.*; import com.investiq.portfolio_mgmt_svc.market.MarketDataClient; import com.investiq.portfolio_mgmt_svc.model.*; import com.investiq.portfolio_mgmt_svc.service.*;
import java.math.BigDecimal; import java.security.Principal; import java.util.*;
import org.springframework.graphql.data.method.annotation.*; import org.springframework.stereotype.Controller;
@Controller
public class PortfolioGraphqlController {
 private final PortfolioService portfolios; private final HoldingService holdings; private final MarketDataClient market;
 public PortfolioGraphqlController(PortfolioService portfolios,HoldingService holdings,MarketDataClient market){this.portfolios=portfolios;this.holdings=holdings;this.market=market;}
 @QueryMapping public List<FundView> funds(@Argument String query){return market.search(query);}
 @QueryMapping public java.math.BigDecimal latestNav(@Argument long schemeCode){return market.latestNav(schemeCode);}
 @QueryMapping public NavHistoryView navHistory(@Argument long schemeCode, @Argument Integer limit){return market.storedNavs(schemeCode, limit == null ? 100 : limit);}
 @QueryMapping public Portfolio portfolio(@Argument long id, Principal principal){return portfolios.getOwned(id,principal.getName());}
 @QueryMapping public List<Portfolio> portfolios(@Argument Integer page,@Argument Integer size,Principal principal){return portfolios.list(principal.getName(),page==null?0:page,size==null?20:size);}
 @MutationMapping public Portfolio createPortfolio(@Argument String name,Principal principal){return portfolios.create(principal.getName(),name);}
 @MutationMapping public Portfolio recordTransaction(@Argument TransactionInput input,Principal principal){return portfolios.record(principal.getName(),input);}
 @MutationMapping public IngestionView synchronizeFund(@Argument long schemeCode){return market.ingest(schemeCode);}
 @BatchMapping(typeName="Portfolio", field="holdings") public Map<Portfolio,List<HoldingView>> holdings(List<Portfolio> values){ Map<Portfolio,List<HoldingView>> result=new LinkedHashMap<>(); for(var p:values) result.put(p,holdings.calculate(p,market::latestNav)); return result; }
 @SchemaMapping(typeName="Holding", field="currentValue") public BigDecimal currentValue(HoldingView h){return h.currentValue();}
}
