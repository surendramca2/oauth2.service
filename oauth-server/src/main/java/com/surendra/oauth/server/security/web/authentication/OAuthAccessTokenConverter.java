package com.surendra.oauth.server.security.web.authentication;

import com.surendra.oauth.server.repository.Oauth2RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


/**
 * A customization of the default {@link OAuth2TokenEndpointConfigurer} extension to provide an override for the
 * {@link AuthenticationConverter} supported via consumer interface in {@link OAuth2TokenEndpointConfigurer#accessTokenRequestConverters}
 * with {@link OAuthAccessTokenConverter}
 * Primary purpose of the customization is to remove the default {@link OAuth2ClientCredentialsAuthenticationConverter}
 * from the list of {@link OAuth2TokenEndpointConfigurer#accessTokenRequestConverters} and provide its own
 * {@link OAuthAuthenticationConverter} this is done to override the behavior of the scope
 * attribute received in the /oauth2/token request.
 *
 * @see OAuthAuthenticationConverter
 * @see org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter
 * @see org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer
 */
public class OAuthAccessTokenConverter implements Consumer<List<AuthenticationConverter>> {

    private final OAuthAuthenticationConverter customConverter;

    public OAuthAccessTokenConverter(Oauth2RegisteredClientRepository oauth2RegisteredClientRepository) {
        customConverter = new OAuthAuthenticationConverter(oauth2RegisteredClientRepository);
    }

    @Override
    public void accept(List<AuthenticationConverter> authenticationConverters) {
        Iterator<AuthenticationConverter> itr = authenticationConverters.iterator();
        while (itr.hasNext()) {
            AuthenticationConverter converter = itr.next();
            // Remove default OAuth2ClientCredentialsAuthenticationConverter from the list
            // default list can be found here, OAuth2TokenEndpointConfigurer#createDefaultAuthenticationConverters
            if (converter instanceof OAuth2ClientCredentialsAuthenticationConverter) {
                itr.remove();
            }
        }
        // Add one from Ephesoft
        authenticationConverters.add(customConverter);
    }
}
