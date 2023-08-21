package com.surendra.oauth.server.api.validation;

import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.domain.RegisteredClientDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class used to validate the auth sever register client.
 */
@Component
public class Oauth2AuthorizationValidator {

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private Validator validator = validatorFactory.getValidator();

    private static final Logger log =
            LoggerFactory.getLogger(Oauth2AuthorizationValidator.class);

    /**
     * Method validate the mandatory client data and add the error in case of null or blank.
     *
     * @param clientDTO
     * @param registeredClient
     * @throws ResponseStatusException
     */
    public void validateClient(RegisteredClientDTO clientDTO, Oauth2RegisteredClient registeredClient) throws ResponseStatusException {

        log.info("Client validation starts");
        Set<ConstraintViolation<RegisteredClientDTO>> violation = validator.validate(clientDTO);
        Set<String> error = new HashSet<>();

        if (!violation.isEmpty()) {
            log.debug("validateClient:: all violation {}", violation);
            error.addAll(violation.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));
        }
        if (null != registeredClient) {
            validateClientId(registeredClient, error);
            validateClientName(registeredClient, error);
        }
        if (!error.isEmpty()) {
            log.error("There are validation errors. Size: {}",
                    error.size());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.join(", ", error));
        }
        log.info("Client validation completed");
    }

    /**
     * Method validate the client name and add the error in case of client id either
     * blank or null.
     *
     * @param registeredClient
     * @param error
     */
    private void validateClientName(Oauth2RegisteredClient registeredClient, Set<String> error) {
        if (StringUtils.isNotBlank(registeredClient.getClientName())) {
            error.add("Duplicate client name,please use unique name");
        }
    }

    /**
     * Method validate the client id and add the error in case of null or blank.
     *
     * @param registeredClient
     * @param error
     */
    private void validateClientId(Oauth2RegisteredClient registeredClient, Set<String> error) {

        if (StringUtils.isNotBlank(registeredClient.getClientId())) {
            error.add("Duplicate client id,please use unique id");
        }
    }
}
