package com.surendra.oauth.server.logger;

import java.time.Instant;

public class OauthAuditLogFormatter {

    private static String OAUTHSTRING = "%s;%s;%s;%s;%s;%s;%s;%s";

    private static final OauthAuditLogger logger = OAuthLoggerFactory.getLogger(OauthAuditLogFormatter.class);

    public static String formatAuditLog(String logCode, String clientId, String transactUser, String tokenId, String transacAPI, String scopes, String expirationTime) {
        return String.format(OAUTHSTRING, Instant.now(), logCode, clientId, transactUser, tokenId, transacAPI, scopes, expirationTime);
    }
}
