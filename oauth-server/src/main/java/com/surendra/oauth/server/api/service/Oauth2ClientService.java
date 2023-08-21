package com.surendra.oauth.server.api.service;

import com.surendra.oauth.server.api.validation.Oauth2AuthorizationValidator;
import com.surendra.oauth.server.domain.Oauth2Authorization;
import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.domain.RegisteredClientDTO;
import com.surendra.oauth.server.exception.CryptographyException;
import com.surendra.oauth.server.repository.Oauth2AuthorizationRepository;
import com.surendra.oauth.server.repository.Oauth2RegisteredClientRepository;
import com.surendra.oauth.server.security.authorization.client.OauthRegisteredClient;
import com.surendra.oauth.server.util.ConstantUtil;
import com.surendra.oauth.server.util.OauthEncryptionUtil;
import com.surendra.oauth.server.util.Oauth2ClientUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletContext;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Service class to handle all APIs calls for Oauth2 authorization server.
 * which includes behavior to manipulate the oauth2 register clients.
 */
@Service
public class Oauth2ClientService {

    private static final String SEMICOLON = ":";

    private static final String SLASH = "://";

    private static final String OAUTH_CLIENT_SECRET = "oauth.client-secret";

    @Autowired
    private Oauth2RegisteredClientRepository clientRepository;

    @Value("${secret.expiration.property}")
    private String daysDefinedInThePropertiesFile;

    @Value("${access.token.validity}")
    private String accessTokenValidity;

    @Value("${secret.value}")
    private String secretValue;

    @Value("${server.port}")
    private String serverPort;

    @Value("${download.token.endpoint.url}")
    private String tokenEndpointUrl;

    @Autowired
    private ServletContext context;

    @Autowired
    private Oauth2AuthorizationValidator oauth2AuthorizationValidator;


    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private Oauth2AuthorizationRepository oauth2AuthorizationRepository;

    private static final Logger log =
            LoggerFactory.getLogger(Oauth2ClientService.class);

    /**
     * Method used to get the basic client setup data.
     *
     * @return Oauth2RegisteredClient
     */
    public RegisteredClientDTO getClientSetup() throws CryptographyException {
        RegisteredClientDTO client = new RegisteredClientDTO();
        String clientId = UUID.randomUUID().toString();
        client.setClientId(clientId);
        String randamPass = Oauth2ClientUtil.generateCommonLangPassword();
        client.setClientSecret(Oauth2ClientUtil.getPasswordEncoder().encode(randamPass));
        String encrptedSecret = OauthEncryptionUtil.getEncryptedPasswordString(randamPass, secretValue);
        client.setOauthClientSecret(encrptedSecret);
        return client;
    }

    /**
     * Method used to get all existing registered client data.
     *
     * @return list of registered clients
     */
    public List<RegisteredClientDTO> getAllRegisteredClient() {

        List<RegisteredClientDTO> registeredClientDtoList = new ArrayList<>();
        List<Oauth2RegisteredClient> oauth2RegisteredClients = clientRepository.findAllByOrderByClientIdIssuedAtAsc();
        if (CollectionUtils.isEmpty(oauth2RegisteredClients)) {
            return Collections.emptyList();
        }
        for (Oauth2RegisteredClient oauth2RegisteredClient :
                oauth2RegisteredClients) {

            registeredClientDtoList.add(getRegisteredClientDTO(oauth2RegisteredClient));
        }
        return registeredClientDtoList;
    }

    /**
     * Method used to save a new register client.
     *
     * @param client: passing RegisteredClientDTO object.
     * @return RegisteredClientDTO.
     */
    public RegisteredClientDTO createNewClient(RegisteredClientDTO client) {
        JdbcRegisteredClientRepository registeredClientRepository = appContext.getBean(JdbcRegisteredClientRepository.class);
        Oauth2RegisteredClient oauth2RegisteredClient = clientRepository.findByClientIdOrClientName(client.getClientId(), client.getClientName());
        oauth2AuthorizationValidator.validateClient(client, oauth2RegisteredClient);
        RegisteredClient registeredClient = getRegisteredClient(client);
        log.debug("Save registeredClient {}", registeredClient);
        try {

            registeredClientRepository.save(registeredClient);
            return getRegisteredClientDTO(clientRepository.findByClientId(client.getClientId()));
        } catch (IllegalArgumentException e) {
            log.error("save:: Error encountered when saving registeredClient. Exception: {}",
                    e.getStackTrace());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    /**
     * Method used to delete the register client based on the client id from the DB.
     *
     * @param clientId : pass clientId to delete from the database.
     */
    public void deleteRegisteredClientById(String clientId) {
        Oauth2RegisteredClient registeredClient = clientRepository.findByClientId(clientId);
        if (null == registeredClient) {
            log.error("DeleteRegisterClientById::{} ", ConstantUtil.DELETE_NO_CLIENT_ID_EXIST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ConstantUtil.DELETE_NO_CLIENT_ID_EXIST);
        }
        try {
            log.info("DeleteRegisterClientById:: {} ", ConstantUtil.DELETE_SUCCESS);
            clientRepository.deleteById(registeredClient.getId());

        } catch (Exception e) {
            log.error("DeleteRegisterClientById:: Error encountered while deleting the client. Exception: {}",
                    e.getStackTrace());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    /**
     * Method create the tokenEndPoint URL.
     *
     * @param hostName
     * @return tokenUrl
     */
    public String getTokenEndpointUrl(String hostName) {
        return hostName.concat(tokenEndpointUrl);
    }

    /**
     * Method used to get the registered client Data.
     *
     * @param clientId
     * @return Oauth2RegisteredClient
     */
    public Oauth2RegisteredClient getRegisteredClient(String clientId) {
        Oauth2RegisteredClient oauth2RegisteredClient = clientRepository.findByClientId(clientId);
        if (null == oauth2RegisteredClient) {
            log.error("getClientCredential:: No register client found with id : {}", clientId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ConstantUtil.NO_CLIENT_FOUND);

        }
        return oauth2RegisteredClient;
    }

    private RegisteredClient getRegisteredClient(RegisteredClientDTO client) {
        AuthorizationGrantType grantType = AuthorizationGrantType.CLIENT_CREDENTIALS;
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.of(Integer.parseInt(accessTokenValidity), ChronoUnit.MINUTES))
                .build();
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(client.getClientId())
                .clientName(client.getClientName())
                .clientSecret(client.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(grantType)
                .clientSecretExpiresAt(Oauth2ClientUtil.getDate(daysDefinedInThePropertiesFile))
                .tokenSettings(tokenSettings).clientSettings(ClientSettings.builder().requireAuthorizationConsent(false)
                        .settings(setting -> setting.put(OAUTH_CLIENT_SECRET, client.getOauthClientSecret()))
                        .build()).build();
        return new OauthRegisteredClient(registeredClient, new HashSet<>(client.getRoles()));
    }

    /**
     * Method used to get populated register client DTO.
     *
     * @param client:passing Oauth2RegisteredClient
     * @return Oauth2RegisteredClient
     */
    private RegisteredClientDTO getRegisteredClientDTO(Oauth2RegisteredClient client) {
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.of(Integer.parseInt(accessTokenValidity), ChronoUnit.MINUTES)).build();
        RegisteredClientDTO registeredClientDto = new RegisteredClientDTO();
        registeredClientDto.setId(client.getId());
        registeredClientDto.setClientId(client.getClientId());
        registeredClientDto.setClientName(client.getClientName());
        registeredClientDto.setClientSecret(client.getClientSecret());
        if (null != client.getClientSecretExpiresAt()) {
            registeredClientDto.setSecretExpirationDate(client.getClientSecretExpiresAt().toString());
        }
        if (null != client.getClientIdIssuedAt()) {
            registeredClientDto.setClientIdIssuedAt(client.getClientIdIssuedAt().toString());
        }
        if (null != tokenSettings.getAccessTokenTimeToLive()) {
            registeredClientDto.setAccessTokenValidity(tokenSettings.getAccessTokenTimeToLive().get(ChronoUnit.SECONDS));
        }
        List<String> roles = Arrays.asList(client.getScopes().split(","));
        registeredClientDto.setRoles(roles);
        return registeredClientDto;
    }

    /**
     * Get most recent access token id for a client
     *
     * @param clientId ClientId
     * @return Most recent token id
     */
    public String getLastTokenIdByClientId(String clientId) {
        Oauth2RegisteredClient client = getRegisteredClient(clientId);
        Oauth2Authorization lastToken = oauth2AuthorizationRepository.findFirstByRegisteredClientIdOrderByAccessTokenIssuedAtDesc(client.getId());
        if (lastToken != null) {
            return lastToken.getId();
        }
        return null;
    }
}
