package com.mastercard.developers.carbontracker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * A credit card number generator.
 *
 * @author Josef Galea
 */
public class CreditCardGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardGenerator.class);
  private final SecureRandom random = new SecureRandom();


  /**
   * @param bin    The bank identification number, a set digits at the start of the credit card
   *               number, used to identify the bank that is issuing the credit card.
   * @param length The total length (i.e. including the BIN) of the credit card number.
   * @return A randomly generated, valid, credit card number.
   */
  public String generate(String bin, int length) {
    String creditCardNumber;

    // The number of random digits that we need to generate is equal to the
    // total length of the card number minus the start digits given by the
    // user, minus the check digit at the end.
    int randomNumberLength = length - (bin.length() + 1);

    StringBuilder builder = new StringBuilder(bin);
    for (int i = 0; i < randomNumberLength; i++) {
      int digit = this.random.nextInt(10);
      builder.append(digit);
    }

    // Do the Luhn algorithm to generate the check digit.
    int checkDigit = this.getCheckDigit(builder.toString());
    builder.append(checkDigit);
    creditCardNumber = builder.toString();
    LOGGER.info("Generated creditCardNumber {}", creditCardNumber);
    String lastFourDigits = creditCardNumber.substring(creditCardNumber.length() - 4);
    LOGGER.info("lastFourDigits of creditCardNumber {} ", lastFourDigits);
    return creditCardNumber;
  }

  /**
   * Generates the check digit required to make the given credit card number
   * valid (i.e. pass the Luhn check)
   *
   * @param number The credit card number for which to generate the check digit.
   * @return The check digit required to make the given credit card number
   * valid.
   */
  private int getCheckDigit(String number) {

    // Get the sum of all the digits, however we need to replace the value
    // of the first digit, and every other digit, with the same digit
    // multiplied by 2. If this multiplication yields a number greater
    // than 9, then add the two digits together to get a single digit
    // number.
    //
    // The digits we need to replace will be those in an even position for
    // card numbers whose length is an even number, or those is an odd
    // position for card numbers whose length is an odd number. This is
    // because the Luhn algorithm reverses the card number, and doubles
    // every other number starting from the second number from the last
    // position.
    int sum = 0;
    for (int i = 0; i < number.length(); i++) {

      // Get the digit at the current position.
      int digit = Integer.parseInt(number.substring(i, (i + 1)));

      if ((i % 2) == 0) {
        digit = digit * 2;
        if (digit > 9) {
          digit = (digit / 10) + (digit % 10);
        }
      }
      sum += digit;
    }

    // The check digit is the number required to make the sum a multiple of
    // 10.
    int mod = sum % 10;
    return ((mod == 0) ? 0 : 10 - mod);
  }
}
