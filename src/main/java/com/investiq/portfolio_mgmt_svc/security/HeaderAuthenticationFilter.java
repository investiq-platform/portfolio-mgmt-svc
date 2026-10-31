package com.investiq.portfolio_mgmt_svc.security;
import jakarta.servlet.*; import jakarta.servlet.http.HttpServletRequest; import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component;
@Component
public class HeaderAuthenticationFilter extends GenericFilter {
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    String user=((HttpServletRequest)request).getHeader("X-User-Id");
    if(user!=null&&!user.isBlank()) SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.trim(), null, java.util.List.of()));
    chain.doFilter(request,response);
  }
}
