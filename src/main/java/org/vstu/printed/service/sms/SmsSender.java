package org.vstu.printed.service.sms;

import org.springframework.beans.factory.annotation.Value;
import ru.dezhik.sms.sender.SenderService;
import ru.dezhik.sms.sender.SenderServiceConfiguration;
import ru.dezhik.sms.sender.SenderServiceConfigurationBuilder;
import ru.dezhik.sms.sender.api.smsru.send.SMSRuSendRequest;
import ru.dezhik.sms.sender.api.smsru.send.SMSRuSendResponse;

import java.util.Collections;

public class SmsSender {
  @Value("sms.sender.api_id") private String apiId;

  static final String SENDER_NAME = "PRINTED";

  private SenderServiceConfiguration configuration;
  private SenderService sender;

  public SmsSender() {
    this.configuration = SenderServiceConfigurationBuilder.create()
            .setApiId("DAE581A7-FD04-ED2C-6215-960144CD0497")
            .setFromName(SENDER_NAME)
            .setReturnPlainResponse(true)
            .setTestSendingEnabled(true)
            .build();

    sender = new SenderService(configuration);
  }

  public SMSRuSendResponse sendMessage(String receiverPhoneNumber, String message) {
    SMSRuSendRequest sendRequest = new SMSRuSendRequest();

    sendRequest.setReceivers(Collections.singleton(receiverPhoneNumber));
    sendRequest.setText(message);

    return sender.execute(sendRequest);
  }
}
