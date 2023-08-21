package com.surendra.oauth.server.security;

import com.ephesoft.secure.token.authenticator.TokenAuthentication;
import com.ephesoft.secure.token.exception.HandshakeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * A helper utility bean for token operations
 */
@Component
public class TokenUtil {

    Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * Parse security token from input {@link HttpServletRequest} which is generally a Bearer token
     *
     * @param request HttpServletRequest
     * @return Extracted token
     */
    public String parseToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    /**
     * Validate a token received in the request
     *
     * @param token Token
     * @return If token is valid or not
     */
    public Boolean validateToken(String token) {
        try {
            //Token expires after 30 min
            TokenAuthentication.verifyToken(token, 1800000);
            return true;
        } catch (HandshakeException handshakeException) {
            logger.error(handshakeException.getMessage());
            return false;
        }
    }


}
