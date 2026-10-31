package com.investiq.portfolio_mgmt_svc.market;
import com.investiq.portfolio_mgmt_svc.api.FundView;
import com.investiq.portfolio_mgmt_svc.api.IngestionView;
import com.investiq.portfolio_mgmt_svc.api.NavHistoryView;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
@Component
public class MarketDataClient {
  private final RestClient client;
  public MarketDataClient(@Value("${market-data.base-url}") String baseUrl, @Value("${market-data.connect-timeout}") java.time.Duration connectTimeout, @Value("${market-data.read-timeout}") java.time.Duration readTimeout) {
    var factory=new JdkClientHttpRequestFactory(java.net.http.HttpClient.newBuilder().connectTimeout(connectTimeout).build()); factory.setReadTimeout(readTimeout);
    this.client=RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
  }
  public List<FundView> search(String q) { try { return List.of(client.get().uri("/funds/search?q={q}",q).retrieve().body(FundView[].class)); } catch(Exception e) { throw new MarketDataUnavailableException(e); } }
  public IngestionView ingest(long schemeCode) { try { return client.post().uri("/funds/{id}/ingest",schemeCode).retrieve().body(IngestionView.class); } catch(Exception e){throw new MarketDataUnavailableException(e);} }
  public NavHistoryView storedNavs(long schemeCode, int limit) { try { var h=client.get().uri("/funds/{id}/stored-navs?limit={limit}",schemeCode,limit).retrieve().body(NavHistoryView.class); if(h==null||h.navs()==null||h.navs().isEmpty()) throw new MarketDataUnavailableException(); return h; } catch(MarketDataUnavailableException e){throw e;} catch(Exception e){throw new MarketDataUnavailableException(e);} }
  public BigDecimal latestNav(long schemeCode) { return storedNavs(schemeCode, 1).navs().getFirst().nav(); }
  public static class MarketDataUnavailableException extends RuntimeException { public MarketDataUnavailableException(){super("Market data is unavailable");} public MarketDataUnavailableException(Throwable cause){super("Market data is unavailable",cause);} }
}
