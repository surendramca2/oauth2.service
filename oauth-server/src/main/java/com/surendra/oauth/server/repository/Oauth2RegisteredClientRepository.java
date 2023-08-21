package com.surendra.oauth.server.repository;

import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring JPA class:Oauth2RegisteredClientRepository
 * to manage the DB operation and provide
 *  * the default implementation of DB table.
 */
public interface Oauth2RegisteredClientRepository extends JpaRepository<Oauth2RegisteredClient, String> {

    /**
     * Method used to get the client based on the client ID.
     * @param clientId
     * @return
     */
    @Transactional(readOnly = true)
     Oauth2RegisteredClient findByClientId(@Param("clientId") String clientId);


    @Transactional(readOnly = true)
    Oauth2RegisteredClient findByClientIdOrClientName(String clientId, String clientName);

    @Transactional(readOnly = true)
    public List<Oauth2RegisteredClient> findAllByOrderByClientIdIssuedAtAsc();

}
