package com.surendra.oauth.server.logger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

/**
 * The <code>EphesoftLoggerImpl</code> is a utility class producing Loggers for various logging APIs for various logging levels.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see org.slf4j.LoggerFactory
 * 
 */
public class OauthAuditLoggerImpl implements OauthAuditLogger {

    private static String OAUTHSTRING= "%s;%s;%s;%s;%s;%s" ;

    private static String OAuthAudit_MARKER = "OAUTHAUDIT";
	/**
	 * logger {@link Logger}.
	 */
	private final Logger logger;

	/**
	 * An instance of OauthAuditLoggerImpl.
	 *
	 * @param logger The logger instance to be used for logging.
	 */
	public OauthAuditLoggerImpl(final Logger logger) {
		this.logger = logger;
	}

    @Override
    public void info(String msgParams) {
        this.logger.info(msgParams);
    }

    @Override
	public void oAuthAudit(String message) {
		this.logger.info(MarkerManager.getMarker(OAuthAudit_MARKER), message);
	}

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 * 
	 * @return True if this Logger is enabled for the DEBUG level, false otherwise.
	 * 
	 */
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

}
