package com.surendra.oauth.server;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import javax.transaction.Transactional;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SqlGroup({
    @Sql(value = "classpath:data/oauth2_registered_client.sql"),
    @Sql(value = "classpath:data/oauth2_authorization.sql")

})
@Transactional
public class OauthServerApplicationTestConfig {

}
