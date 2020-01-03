package org.vstu.printed.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final JwtTokenProvider tokenProvider;

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    JwtTokenFilter tokenFilter = new JwtTokenFilter(tokenProvider);
    httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
