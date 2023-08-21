package com.surendra.oauth.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import com.surendra.oauth.server.exception.CryptographyException;
import org.junit.jupiter.api.Test;

public class OauthEncryptionUtilTest {
  /**
   * TestCase: Method used to test getEncryptedPasswordString.
   * Setup: An encrypted password passed.
   * Action: OauthEncryptionUtil.getEncryptedPasswordString() method called.
   * Expected: method returns the same password which was passed as input.
   */
  @Test
  public void testGetEncryptedPasswordString_WhenPasswordIsAlreadyEncrypted() throws CryptographyException {
    String encryptedPass= OauthEncryptionUtil.getEncryptedPasswordString("JKLMNPQRS","QRS");
    assertEquals("JKLMNPQRS",encryptedPass);
  }

  /**
   * TestCase: Method used to test getEncryptedPasswordString.
   * Setup: An NULL password passed.
   * Action: OauthEncryptionUtil.getEncryptedPasswordString() method called.
   * Expected: method returns the NULL password as encrypted.
   */
  @Test
  public void testGetEncryptedPasswordString_WhenPasswordIsNull() throws CryptographyException {
    String encryptedPass= OauthEncryptionUtil.getEncryptedPasswordString(null,"word");
    assertNull(encryptedPass);
  }
  /**
   * TestCase: Method used to test getDecryptedPasswordString.
   * Setup: An decrypted password passed
   * Action: OauthEncryptionUtil.getDecryptedPasswordString() method called.
   * Expected: method returns the same password which was passed as input.
   */
  @Test
  public void testGetDecryptedPasswordString_PasswordIsAlreadyDecrypted() throws CryptographyException {
    String decryptedPass= OauthEncryptionUtil.getDecryptedPasswordString("password","pre");
    assertEquals("password",decryptedPass);
  }
}
