package org.vstu.printed.service.user.verify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vstu.printed.persistence.user.VerificationCode;
import org.vstu.printed.repository.VerificationCodeRepository;
import org.vstu.printed.service.sms.SmsSender;
import org.vstu.printed.service.user.UserNotFoundException;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
  private final VerificationCodeRepository verificationCodeRepository;
  private final SmsSender smsSender;

  private static final short MAX_CODE_NUMBER = 10000;
  private static final byte CODE_LENGTH = 4;
  private static final char PREPEND_CHAR = '0';
  private static final String CODE_MESSAGE = "Код подтверждения регистрации в сервисе печати Printed: %s";

  public void sendCode(String phoneNumber) throws UserNotFoundException {
    VerificationCode code = verificationCodeRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new UserNotFoundException("Для этого номера телефона не запрашивался код подтверждения"));

    smsSender.sendMessage(phoneNumber, String.format(CODE_MESSAGE, code.getVerificationCode()));
  }

  public void verifyCode(String phoneNumber, String code) throws InvalidCodeException, UserNotFoundException {
    VerificationCode verificationInfo = verificationCodeRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new UserNotFoundException("Нет пользователя с таким номером телефона"));

    String verificationCode = verificationInfo.getVerificationCode();
    if(verificationCode.equals(code)) {
      verificationInfo.setVerified(true);
      verificationCodeRepository.save(verificationInfo);
    }
    else
      throw new InvalidCodeException("Неверный код подтверждения");
  }

  public void addCodeForNumber(String phoneNumber) throws PhoneNumberAlreadyVerified {
    // Проверка, что для заданного номера телефона нет уже проверенного кода
    Optional<VerificationCode> codeForNumber = verificationCodeRepository.findByPhoneNumber(phoneNumber);
    if(codeForNumber.isPresent() && codeForNumber.get().isVerified())
      throw new PhoneNumberAlreadyVerified();

    Random codeGenerator = new Random();
    boolean codeIsUnique = false;
    short code = 0;

    while(!codeIsUnique) {
      code = (short)codeGenerator.nextInt(MAX_CODE_NUMBER);
      codeIsUnique = isSignupCodeUnique(code);
    }

    StringBuilder codeBuilder = new StringBuilder();
    codeBuilder.append(String.valueOf(code));
    while(codeBuilder.length() < CODE_LENGTH)
      codeBuilder.insert(0, PREPEND_CHAR);

    VerificationCode verificationCode = new VerificationCode(phoneNumber, codeBuilder.toString(), false);

    verificationCodeRepository.save(verificationCode);
  }

  boolean isSignupCodeUnique(short code) {
    List<VerificationCode> codes = verificationCodeRepository.findAll();
    Iterator<VerificationCode> codesIterator = codes.iterator();

    boolean codeWasFound = false;
    while(codesIterator.hasNext() && !codeWasFound) {
      VerificationCode foundCode = codesIterator.next();
      if(Short.parseShort(foundCode.getVerificationCode()) == code)
        codeWasFound = true;
    }

    return !codeWasFound;
  }
}
