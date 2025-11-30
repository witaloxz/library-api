package com.witalo.libraryapi.dto;

import com.witalo.libraryapi.model.Client;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Clients")
public class ClientDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Client secret is required")
    @Size(min = 8, message = "Client secret must have at least 8 characters")
    private String clientSecret;

    @NotBlank(message = "Redirect URI is required")
    private String redirectURI;

    @NotBlank(message = "Scope is required")
    private String scope;


    public ClientDTO(Client client){
        this.id = client.getId();
        this.clientId = client.getClientId();
        this.clientSecret = client.getClientSecret();
        this.redirectURI = client.getRedirectURI();
        this.scope = client.getScope();
    }


}
