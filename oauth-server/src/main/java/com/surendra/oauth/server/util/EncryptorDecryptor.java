package com.surendra.oauth.server.util;

import com.surendra.oauth.server.exception.CryptographyException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Random;


public class EncryptorDecryptor {

	private static final EncryptorDecryptor encryptor = new EncryptorDecryptor();

	private static final Logger log = LoggerFactory.getLogger(EncryptorDecryptor.class);
	private final Random random = new Random();

	private EncryptorDecryptor() {
		super();
	}

	/**
	 * Returns {@link EncryptorDecryptor} object
	 * @return the {@link EncryptorDecryptor} object
	 */
	public static EncryptorDecryptor getEncryptorDecryptor() {
		return encryptor;
	}

	/**
	 * Starts the crypting operation.
	 *
	 * @param data         data to encrypt/decrypt
	 * @param salt         salt
	 * @param isEncryption flag for operation
	 * @return encrypted/decrypted bytes
	 * @throws CryptographyException
	 */
	public synchronized byte[] startCrypting(byte[] data, byte[] salt, boolean isEncryption)
			throws CryptographyException {
		int ITERATION_COUNT = 10;
		String KEY = "encryptorKey";
		KeySpec keySpec = new PBEKeySpec(KEY.toCharArray(), salt, ITERATION_COUNT);
		SecretKey key;
		byte[] finalBytes;
		try {
			String ALGORITHM = "PBEWithMD5AndDES";
			int cipherMode = isEncryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
			log.debug("Cipher Mode: " + cipherMode);
			key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
			cipher.init(cipherMode, key, paramSpec);
			finalBytes = cipher.doFinal(data);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			throw new CryptographyException("Cipher operation failed", exception);
		}
		return finalBytes;
	}

	/**
	 * Encrypts the string.
	 *
	 * @param plainText plain string
	 * @return encrypted string
	 * @throws CryptographyException
	 */
	public String encryptString(String plainText) throws CryptographyException {
		byte[] salt = generateSalt();
		byte[] decryptedStringInBytes;
		decryptedStringInBytes = plainText.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedByte = startCrypting(decryptedStringInBytes, salt, true);
		byte[] result = ArrayUtils.addAll(salt, encryptedByte);
		result = Base64.encodeBase64(result);
		return new String(result,StandardCharsets.UTF_8);
	}

	private byte[] generateSalt() {
		random.setSeed(System.currentTimeMillis());
		byte[] randomBytes = new byte[8];
		random.nextBytes(randomBytes);
		return randomBytes;
	}

	/**
	 * Decrypts the provided string.
	 *
	 * @param encryptedString encrypted string
	 * @return decrypted string
	 * @throws CryptographyException
	 */
	public String decryptString(String encryptedString) throws CryptographyException {
		byte[] data;
		data = encryptedString.getBytes(StandardCharsets.UTF_8);
		data = Base64.decodeBase64(data);
		int SALT_LENGTH = 8;
		byte[] salt = ArrayUtils.subarray(data, 0, SALT_LENGTH);
		data = ArrayUtils.subarray(data, SALT_LENGTH, data.length);
		byte[] result = startCrypting(data, salt, false);
		return new String(result, StandardCharsets.UTF_8);
	}

}