package org.vstu.printed;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vstu.printed.service.sms.SmsSender;
import ru.dezhik.sms.sender.SenderService;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class PrintedApplication {

  @Bean
  public PasswordEncoder passwordEncoder() {
    PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
    String idForEncode = "bcrypt";
    Map encoders = new HashMap<>();
    encoders.put(idForEncode, bcryptEncoder);

    DelegatingPasswordEncoder myEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
    myEncoder.setDefaultPasswordEncoderForMatches(bcryptEncoder);

    return myEncoder;
  }

  @Bean
  public Tika tikaParser() {
    return new Tika();
  }

  @Bean
  public SmsSender senderService() {
    return new SmsSender();
  }

  public static void main(String[] args) {
    SpringApplication.run(PrintedApplication.class, args);
  }

}
