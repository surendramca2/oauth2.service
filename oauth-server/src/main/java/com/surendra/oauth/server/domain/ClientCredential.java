package com.surendra.oauth.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Generated
@Data
@AllArgsConstructor
public class ClientCredential implements Serializable {

    @NotBlank(message = "client Id can not be blank.")
    private String clientId;

    @NotBlank(message = "client secret can not be blank.")
    private String clientSecret;

    @NotBlank(message = "client roles can not be blank.")
    private List<String> roles;

    @NotBlank(message = "client tokenEndpoint can not be blank.")
    private String tokenEndpoint;

}
