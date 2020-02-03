package org.vstu.printed.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.vstu.printed.security.jwt.JwtConfigurer;
import org.vstu.printed.security.jwt.JwtTokenProvider;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private JwtTokenProvider tokenProvider;

  @Autowired
  public WebSecurityConfig(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  private static final String LOGIN_ENDPOINT = "/login";
  private static final String SIGNUP_ENDPOINT = "/signup";

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
    http
      .requiresChannel()
      .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
      .requiresSecure()
      .and()
      .httpBasic().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/spots/*/orders")
            .hasAnyAuthority("manager", "admin")
        .antMatchers(HttpMethod.PATCH, "/users/**/account")
            .hasAnyAuthority("client", "manager", "admin")
        .antMatchers(HttpMethod.POST, "/documents")
            .hasAnyAuthority("client", "admin")
        .antMatchers(HttpMethod.GET, "/users/*/orders")
            .hasAnyAuthority("client", "manager", "admin")
        .antMatchers(HttpMethod.GET, "/users/*/orders/*")
            .hasAnyAuthority("client", "manager", "admin")
        .antMatchers(HttpMethod.POST, "/spots")
            .hasAnyAuthority("manager", "admin")
        .antMatchers(HttpMethod.PATCH, "/spots/*")
            .hasAnyAuthority("manager", "admin")
        .antMatchers(HttpMethod.GET, "/documents/download/*")
            .permitAll()
        .antMatchers(HttpMethod.PATCH, "/orders/*")
            .hasAnyAuthority("client", "manager", "admin")
        .antMatchers(HttpMethod.PUT, "/orders/*")
            .hasAnyAuthority("client", "admin")
        .antMatchers(HttpMethod.DELETE, "/orders/*")
            .hasAnyAuthority("client", "admin")
        .antMatchers(HttpMethod.GET, "/spots")
            .hasAnyAuthority("manager", "admin")
        .antMatchers(HttpMethod.GET, "/spots/*")
            .hasAnyAuthority("manager", "client", "admin")
        .antMatchers(HttpMethod.DELETE, "/spots/*")
            .hasAnyAuthority("manager", "admin")
        .antMatchers(HttpMethod.GET, "/spots/all")
            .hasAuthority("admin")
        .antMatchers(HttpMethod.GET, "/orders")
            .hasAuthority("admin")
        .antMatchers(HttpMethod.POST, "/orders/new")
            .hasAnyAuthority("client", "admin")
        .antMatchers(SIGNUP_ENDPOINT)
            .permitAll()
        .antMatchers(LOGIN_ENDPOINT)
            .permitAll()
        .antMatchers(HttpMethod.GET, "/users")
            .hasAuthority("admin")
        .antMatchers(HttpMethod.DELETE, "/users/*")
            .authenticated()
        .anyRequest()
            .authenticated()
      .and()
      .apply(new JwtConfigurer(tokenProvider));
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration configuration = new CorsConfiguration();
    final List<String> PERMIT_ALL = Collections.unmodifiableList(Collections.singletonList("*"));

    configuration.setAllowedOrigins(PERMIT_ALL);
    configuration.setAllowedMethods(Collections.unmodifiableList(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.POST.name(),
            HttpMethod.DELETE.name()
    )));
    configuration.setAllowedHeaders(PERMIT_ALL);
    configuration.setExposedHeaders(Collections.unmodifiableList(Arrays.asList(HttpHeaders.CONTENT_DISPOSITION)));

    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
