package com.surendra.oauth.server.repository;

import com.surendra.oauth.server.domain.Oauth2Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Spring JPA class:Oauth2Authorization to manage the DB operation and provide
 * the default implementation of DB table.
 *
 */
public interface Oauth2AuthorizationRepository extends JpaRepository<Oauth2Authorization, String> {

    @Transactional
    int deleteByAccessTokenExpiresAtBefore(Date date);

    @Transactional
    Oauth2Authorization findFirstByRegisteredClientIdOrderByAccessTokenIssuedAtDesc(String registeredClientId);
}
