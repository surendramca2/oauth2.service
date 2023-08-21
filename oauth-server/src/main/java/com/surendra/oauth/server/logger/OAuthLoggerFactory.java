package com.surendra.oauth.server.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class OAuthLoggerFactory {

    // private constructor to prevent instantiation
    private OAuthLoggerFactory() {

    }

    /**
     * Returns an instance of EphesoftLogger from factory.
     *
     * @param classType {@link Class} - Class name for which logger is required.
     * @return instance of {@link OauthLogger}
     */
    public static OauthAuditLogger getLogger(final Class<?> classType) {

        final Logger logger = LogManager.getLogger(classType.getName());
        return new OauthAuditLoggerImpl(logger);

    }
}
