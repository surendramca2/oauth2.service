package com.surendra.oauth.server.logger;

/**
 * <code>OauthAuditLogger</code> a wrapper of <code>org.apache.logging.log4j.Logger</code> for logging info and oAuthAudit logging level log.
 * statements.
 *
 * @author Ephesoft
 *
 * @see org.apache.logging.log4j.Logger
 *
 */
public interface OauthAuditLogger {

    /**
     * Log a message at the INFO level.
     */
    void info(final String msg);
    /**
     * An API to write to audit log in a seperate log file.
     *
     */
    void oAuthAudit(String message);
}

