package org.vstu.printed.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.vstu.printed.security.jwt.JwtConfigurer;
import org.vstu.printed.security.jwt.JwtTokenProvider;

import javax.ws.rs.HttpMethod;
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

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http
      .httpBasic().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/documents")
            .hasAuthority("client")
        .antMatchers(HttpMethod.POST, "/spots")
            .hasAuthority("manager")
        .antMatchers(org.springframework.http.HttpMethod.PUT, "/spots")
            .hasAuthority("manager")
        .antMatchers(HttpMethod.GET, "/documents")
            .authenticated()
        .antMatchers(HttpMethod.POST, "/orders")
            .hasAuthority("client")
        .antMatchers(HttpMethod.GET, "/orders")
            .authenticated()
        .antMatchers(HttpMethod.GET, "/spots")
            .authenticated()
        .antMatchers("/register")
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
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/documents", configuration);
    return source;
  }

}
