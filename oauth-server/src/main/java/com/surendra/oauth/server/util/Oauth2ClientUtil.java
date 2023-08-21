package com.surendra.oauth.server.util;

import com.surendra.oauth.server.domain.ClientCredential;
import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.exception.CryptographyException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for Oauth2 management clients.
 */
public class Oauth2ClientUtil {

    private static final int DAYS = 365;
    private static final int YEAR = 99;

    private static final Logger log = LoggerFactory.getLogger(Oauth2ClientUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String SECRET = "secret";
    private static final String OAUTH_CLIENT_SECRET = "oauth.client-secret";
    public static final String CLIENT_SECRET = "client_secret_";

    /**
     * Utility method to convert object into byte array.
     *
     * @param oauth2RegisteredClient
     * @return byte array of credentials
     */
    public static byte[] getClientCredentialDataBytes(Oauth2RegisteredClient oauth2RegisteredClient, String tokenEndPointUrl) throws CryptographyException {
        List<String> roles = Arrays.asList(oauth2RegisteredClient.getScopes().split(","));
        Map<String, Object> clientSettingsMaps = parseMap(oauth2RegisteredClient.getClientSettings());
        String ephesoftClientEncryptedSecret = String.valueOf(clientSettingsMaps.get(OAUTH_CLIENT_SECRET));
        String decryptSecret = OauthEncryptionUtil.getDecryptedPasswordString(ephesoftClientEncryptedSecret, SECRET);
        ClientCredential clientCredential = new ClientCredential(oauth2RegisteredClient.getClientId(), decryptSecret, roles, tokenEndPointUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(clientCredential).getBytes(Charset.defaultCharset());
    }

    /**
     * Method return the file name based on the client name.
     *
     * @param clientId
     * @return
     */
    public static String getFileName(String clientId) {
        return CLIENT_SECRET +clientId.replace(" ", "_") + ".json";
    }

    /**
     * Method used to get encoder password.
     *
     * @return PasswordEncoder
     */
    public static PasswordEncoder getPasswordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Method used to get the Instant date either based on the configuration value
     * or current date.
     *
     * @return Instant
     */
    public static Instant getDate(String daysDefinedInThePropertiesFile) {
        Instant secretExpirationDate;
        if (Integer.parseInt(daysDefinedInThePropertiesFile) <= 0) {
            secretExpirationDate = Instant.now().plus(DAYS * YEAR, ChronoUnit.DAYS);
        } else {
            secretExpirationDate = Instant.now().plus(Integer.parseInt(daysDefinedInThePropertiesFile), ChronoUnit.DAYS);
        }

        return secretExpirationDate;

    }

    /**
     * Utility method used to generate a alphanumeric random password.
     *
     * @return password
     */
    public static String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(4, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(6, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(5);
        String totalChars = RandomStringUtils.randomAlphanumeric(5);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    private static Map<String, Object> parseMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

}
