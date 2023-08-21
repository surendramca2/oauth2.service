package com.surendra.oauth.server;

import com.surendra.oauth.server.logger.OAuthLoggerFactory;
import com.surendra.oauth.server.logger.OauthAuditLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Surendra pal singh
 * @since 0.0.1
 */
@SpringBootApplication
public class DefaultAuthorizationServerApplication  extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DefaultAuthorizationServerApplication.class);
	}
    private static final OauthAuditLogger logger = OAuthLoggerFactory.getLogger(DefaultAuthorizationServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DefaultAuthorizationServerApplication.class, args);
    }

}
