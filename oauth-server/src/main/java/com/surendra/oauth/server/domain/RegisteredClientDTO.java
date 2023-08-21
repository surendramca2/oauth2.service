package com.surendra.oauth.server.domain;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import java.util.List;

@Generated
@Data
@NoArgsConstructor
public class RegisteredClientDTO {

    private String id;

    @NotBlank(message = "Client name can not be blank or null")
    private String clientName;

    @NotBlank(message = "Client Id can not be blank or null")
    private String clientId;

    @NotBlank(message = "Secret can not be blank or null")
    private String clientSecret;

    @NotBlank(message = "Oauth client Secret can not be blank or null")
    private String oauthClientSecret;

    private List<String> roles;

    private String secretExpirationDate;

    private Long accessTokenValidity;

    private String clientIdIssuedAt;

}
