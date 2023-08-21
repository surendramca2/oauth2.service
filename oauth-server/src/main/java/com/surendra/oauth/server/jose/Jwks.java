package com.surendra.oauth.server.jose;

import com.surendra.oauth.server.util.ConstantUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Utility class for JWT(Json web token) hold the key generation methods.
 * @author Surendra pal singh
 * @since 0.0.1
 */
public final class Jwks {

	private Jwks() {
	}

	/**
	 * Utility method generate the RSA key based on public and private key.
	 * @return
	 */
	public static RSAKey generateRsa(Resource resource, String keyStorePassword, String keyPassword) {
		String keyId = UUID.fromString(ConstantUtil.UUID_KEY).toString();
		//Loading keys from a key store would allow us to use the same keys for each instance in case
		//of a multi node environment
		KeyStoreKeyFactory keyStoreKeyFactory =
				new KeyStoreKeyFactory(resource,
						keyStorePassword.toCharArray());
		KeyPair keyPair = keyStoreKeyFactory.getKeyPair(ConstantUtil.KEY_ALIAS, keyPassword.toCharArray());

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// @formatter:off
		return new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.RS256)
				.keyID(keyId)
				.build();
		// @formatter:on
	}

	/**
	 * Utility method to generate elliptic curve (EC) key based on public and private key.
	 * @return
	 */
	public static ECKey generateEc() {
		KeyPair keyPair = KeyGeneratorUtils.generateEcKey();
		ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
		ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
		Curve curve = Curve.forECParameterSpec(publicKey.getParams());
		// @formatter:off
		return new ECKey.Builder(curve, publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		// @formatter:on
	}

	/**
	 * Utility method to generate the Octet sequence key based on the secret key.
	 * @return
	 */
	public static OctetSequenceKey generateSecret() {
		SecretKey secretKey = KeyGeneratorUtils.generateSecretKey();
		// @formatter:off
		return new OctetSequenceKey.Builder(secretKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		// @formatter:on
	}
}
