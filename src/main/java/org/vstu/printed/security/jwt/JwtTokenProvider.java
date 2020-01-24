package org.vstu.printed.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
  @Value("${jwt.token.secret}")
  private String secret;

  @Value("${jwt.token.expire}")
  private long expireTime;

  @Autowired
  private JwtUserDetailsService userDetailsService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @PostConstruct
  protected void init() {
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
  }

  public String createToken(String phoneNumber) {
    Claims claims = Jwts.claims().setSubject(phoneNumber);

    Date now = new Date();
    Date validThrough = new Date(now.getTime() + expireTime);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validThrough)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if(bearerToken != null && bearerToken.startsWith("Bearer_"))
      return bearerToken.substring(7, bearerToken.length());
    return null;
  }

  public Authentication getAuthentication(String token) {
    UserDetails userInfo = userDetailsService.loadUserByUsername(getPhoneNumber(token));
    return new UsernamePasswordAuthenticationToken(userInfo, "", userInfo.getAuthorities());
  }

  public String getPhoneNumber(String token) {
    return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean isValid(String token) throws JwtAuthenticationException {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
      if(new Date().after(claims.getBody().getExpiration()))
        return false;
      return true;
    } catch(JwtException | IllegalArgumentException e) {
      throw new JwtAuthenticationException("JWT token is expired or invalid");
    }
  }
}
