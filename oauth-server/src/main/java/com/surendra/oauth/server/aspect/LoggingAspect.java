package com.surendra.oauth.server.aspect;

import com.surendra.oauth.server.constant.OAuthConstants;
import com.surendra.oauth.server.constant.TransactAPIs;
import com.surendra.oauth.server.domain.RegisteredClientDTO;
import com.surendra.oauth.server.logger.OAuthLoggerFactory;
import com.surendra.oauth.server.logger.OauthAuditLogFormatter;
import com.surendra.oauth.server.logger.OauthAuditLogger;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    /**
     * OauthAuditLogger LOGGER
     */
    private static final OauthAuditLogger LOGGER = OAuthLoggerFactory.getLogger(LoggingAspect.class);

    private static final String TRANSACT_USER_HEADER = "TRANSACTUSER";

    /**
     * This method is used to log the method start and end along with the execution time.
     * It tracks all the methods marked with @ExecutionLogsRequired annotation.
     *
     * @param joinPoint joint point for the aspect
     * @return object
     * @throws Throwable
     */
    @Around(value = "@annotation(com.ephesoft.oauth.server.aspect.ExecutionLogsRequired)")
    public Object logAuditMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String logCode = null;
        String transactAPI = "";
        Object[] args = joinPoint.getArgs();
        String clientId = "";
        String transactUser = "";
        HttpServletRequest httpRequest;
        switch (methodName) {
            case "createNewClient":
                logCode = OAuthConstants.OAUTH_CLIENT_CREATED.name();
                transactAPI = TransactAPIs.CREATE_API.getApi();
                RegisteredClientDTO rdto = (RegisteredClientDTO) args[0];
                clientId = rdto.getClientId();
                httpRequest = (HttpServletRequest) args[1];
                transactUser = httpRequest.getHeader(TRANSACT_USER_HEADER);
                break;
            case "downloadFile":
                logCode = OAuthConstants.OAUTH_CREDENTIALS_DOWNLOADED.name();
                transactAPI = TransactAPIs.GET_DOWNLOADS_CREDS_API.getApi();
                clientId = (String) args[0];
                httpRequest = (HttpServletRequest) args[1];
                transactUser = httpRequest.getHeader(TRANSACT_USER_HEADER);
                break;
            case "deleteRegisteredClientById":
                logCode = OAuthConstants.OAUTH_CLIENT_DELETED.name();
                transactAPI = TransactAPIs.DELETE_CLIENT_API.getApi();
                clientId = (String) args[0];
                httpRequest = (HttpServletRequest) args[1];
                transactUser = httpRequest.getHeader(TRANSACT_USER_HEADER);
                break;
            case "getAllRegisteredClient":
                logCode = OAuthConstants.OAUTH_CLIENT_LIST_LOADED.name();
                transactAPI = TransactAPIs.GET_ALL_CLIENTS_API.getApi();
                httpRequest = (HttpServletRequest) args[0];
                transactUser = httpRequest.getHeader(TRANSACT_USER_HEADER);
                break;
            default:
                break;

        }
        String auditLog = OauthAuditLogFormatter.formatAuditLog(logCode, clientId, transactUser, "", transactAPI, "", "");
        LOGGER.oAuthAudit(auditLog);
        return joinPoint.proceed();
    }


}
