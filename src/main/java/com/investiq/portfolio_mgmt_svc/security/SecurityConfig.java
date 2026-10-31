package com.investiq.portfolio_mgmt_svc.security;
import org.springframework.context.annotation.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.web.*; import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
@Configuration @EnableWebSecurity
public class SecurityConfig {
 @Bean SecurityFilterChain security(HttpSecurity http, HeaderAuthenticationFilter header) throws Exception { return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/actuator/health", "/graphiql/**").permitAll().anyRequest().authenticated()).addFilterBefore(header, AnonymousAuthenticationFilter.class).build(); }
}
