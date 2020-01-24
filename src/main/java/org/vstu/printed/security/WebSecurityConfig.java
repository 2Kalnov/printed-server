package org.vstu.printed.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import java.util.Arrays;

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
    http.csrf().disable();
    http.requiresChannel()
      .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
      .requiresSecure()
      .and()
      .httpBasic().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
        .antMatchers(HttpMethod.PATCH, "/users/**/account")
            .hasAnyAuthority("client", "manager")
        .antMatchers(HttpMethod.POST, "/documents")
            .hasAuthority("client")
        .antMatchers(HttpMethod.GET, "/users/*/orders")
            .hasAnyAuthority("client", "manager")
        .antMatchers(HttpMethod.GET, "/users/*/orders/*")
            .hasAnyAuthority("client", "manager")
        .antMatchers(HttpMethod.POST, "/spots")
            .hasAuthority("manager")
        .antMatchers(HttpMethod.PATCH, "/spots/*")
            .hasAuthority("manager")
        .antMatchers(HttpMethod.GET, "/documents")
            .authenticated()
        .antMatchers(HttpMethod.POST, "/orders")
            .hasAuthority("client")
        .antMatchers(HttpMethod.GET, "/orders")
            .authenticated()
        .antMatchers(HttpMethod.PATCH, "/orders/*")
            .hasAnyAuthority("client", "manager")
        .antMatchers(HttpMethod.GET, "/spots")
            .authenticated()
        .antMatchers(SIGNUP_ENDPOINT)
            .permitAll()
        .antMatchers("/users")
            .authenticated()
        .antMatchers(LOGIN_ENDPOINT)
            .permitAll()
      .and()
            .cors()
      .and()
      .apply(new JwtConfigurer(tokenProvider));
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/documents", configuration);
    return source;
  }

}
