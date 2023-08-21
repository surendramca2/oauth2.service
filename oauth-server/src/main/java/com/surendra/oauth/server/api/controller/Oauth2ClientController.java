package com.surendra.oauth.server.api.controller;

import com.surendra.oauth.server.api.service.Oauth2ClientService;
import com.surendra.oauth.server.aspect.ExecutionLogsRequired;
import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.domain.RegisteredClientDTO;
import com.surendra.oauth.server.exception.CryptographyException;
import com.surendra.oauth.server.logger.OAuthLoggerFactory;
import com.surendra.oauth.server.logger.OauthAuditLogger;
import com.surendra.oauth.server.util.ConstantUtil;
import com.surendra.oauth.server.util.Oauth2ClientUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Class expose the end point for management APIs.
 */
@RestController
@RequestMapping("api/management")
public class Oauth2ClientController {
    private static final OauthAuditLogger logger = OAuthLoggerFactory.getLogger(Oauth2ClientController.class);


    /** Request header 'hostName' */
    private static final String HOST_NAME_HEADER = "hostName";

    @Autowired
    private Oauth2ClientService oauth2RegisteredClientService;

    /**
     * Get endpoint to retrieve the client setup data.
     *
     * @return the client basic client setup data.
     */
    @GetMapping("/client/setup")
    public RegisteredClientDTO getClientSetup(HttpServletRequest httpRequest) throws CryptographyException {
        return oauth2RegisteredClientService.getClientSetup();
    }

    /**
     * Post endpoint to create new client API.
     *
     * @param client
     * @return Oauth2RegisteredClient
     */
    @ExecutionLogsRequired
    @PostMapping("/client")
    public RegisteredClientDTO createNewClient(@RequestBody RegisteredClientDTO client, HttpServletRequest httpRequest) {
        return oauth2RegisteredClientService.createNewClient(client);
    }

    /**
     * Get endpoint expose to get all registered clients.
     *
     * @return list of all existing clients.
     */
    @ExecutionLogsRequired
    @GetMapping("/clients")
    public ResponseEntity<List<RegisteredClientDTO>> getAllRegisteredClient(HttpServletRequest httpRequest) {
        List<RegisteredClientDTO> registeredClients = oauth2RegisteredClientService.getAllRegisteredClient();
        if (CollectionUtils.isEmpty(registeredClients)) {
            return new ResponseEntity(registeredClients, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(registeredClients, HttpStatus.OK);
        }
    }

    /**
     * Get endpoint expose to get the credential based on the client Id.
     *
     * @param clientId
     * @return ResponseEntity
     * @throws Exception: throw exception in failed to download.
     */
    @ExecutionLogsRequired
    @GetMapping("/client/{clientId}/credentials/download")
    public HttpEntity<byte[]> downloadFile(@PathVariable String clientId, HttpServletRequest httpRequest) throws Exception {
        String hostName = httpRequest.getHeader(HOST_NAME_HEADER);
        Oauth2RegisteredClient oauth2RegisteredClient = oauth2RegisteredClientService.getRegisteredClient(clientId);
        String tokenEndPointUrl = oauth2RegisteredClientService.getTokenEndpointUrl(hostName);
        byte[] clientCredentials = Oauth2ClientUtil.getClientCredentialDataBytes(oauth2RegisteredClient, tokenEndPointUrl);
        String fileName = Oauth2ClientUtil.getFileName(oauth2RegisteredClient.getClientId());
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        header.setContentLength(clientCredentials.length);
        header.setContentType(MediaType.parseMediaType("application/octet-stream"));
        return new HttpEntity<>(clientCredentials, header);
    }

    /**
     * Delete endpoint expose to delete the client based on the client Id.
     *
     * @param clientId
     * @return status code
     */
    @ExecutionLogsRequired
    @DeleteMapping(value = "/client/{clientId}")
    public ResponseEntity<HttpStatus> deleteRegisteredClientById(@PathVariable String clientId, HttpServletRequest httpRequest) {
        oauth2RegisteredClientService.deleteRegisteredClientById(clientId);
        return new ResponseEntity(ConstantUtil.DELETE_SUCCESS, HttpStatus.OK);
    }

}
