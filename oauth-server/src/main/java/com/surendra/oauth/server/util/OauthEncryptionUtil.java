package com.surendra.oauth.server.util;

import com.surendra.oauth.server.exception.CryptographyException;

/**
 * Utility Class used to encrypt and decrypt the password.
 */
public class OauthEncryptionUtil {

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	public static String getEncryptedPasswordString(final String password, final String passwordPrefix) throws CryptographyException {
		String encryptedPasswordString = password;
		if (password != null && !isPasswordStringEncrypted(password,passwordPrefix)) {
			encryptedPasswordString = EncryptorDecryptor.getEncryptorDecryptor().encryptString(password) + passwordPrefix;
		}
		return encryptedPasswordString;
	}

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	public static String getDecryptedPasswordString(final String password,final String passwordPrefix) throws CryptographyException {
		String decryptedPasswordString = password;
		if (isPasswordStringEncrypted(password, passwordPrefix)) {
				int passwordSuffixIndex = password.lastIndexOf(passwordPrefix);
				String passwordWithoutSuffix = password.substring(0, passwordSuffixIndex);
				decryptedPasswordString = EncryptorDecryptor.getEncryptorDecryptor().decryptString(passwordWithoutSuffix);
		}
		return decryptedPasswordString;
	}

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	public static boolean isPasswordStringEncrypted(final String password,final String passwordPrefix) {
		return password != null && password.endsWith(passwordPrefix);
	}

}
