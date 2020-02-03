package org.vstu.printed.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider tokenProvider;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, JwtAuthenticationException {
    String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

    System.out.println("User token" + token + "; token is valid: " + tokenProvider.isValid(token));

    if(token != null && tokenProvider.isValid(token)) {
      Authentication auth = tokenProvider.getAuthentication(token);

      if(auth != null)
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }
}
