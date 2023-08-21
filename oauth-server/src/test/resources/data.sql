TRUNCATE TABLE OAUTH2_REGISTERED_CLIENT;
INSERT INTO OAUTH2_REGISTERED_CLIENT (ID,AUTHORIZATION_GRANT_TYPES,CLIENT_AUTHENTICATION_METHODS,CLIENT_ID,CLIENT_ID_ISSUED_AT,
CLIENT_NAME,CLIENT_SECRET,CLIENT_SECRET_EXPIRES_AT,CLIENT_SETTINGS,REDIRECT_URIS,SCOPES,TOKEN_SETTINGS) VALUES
('adrefr-06c7-4045-ui3d-13f7ac26551c','refresh_token,client_credentials','client_secret_post,client_secret_basic',
'${transact.client.id}','2023-03-21 13:43:59.149337','a37f20a6-06c7-4049-b03d-13f7ac26551c','{bcrypt}$2a$10$PA47wYxFL7L87SUyMZ8g4eri0GJYKR53ae54mOoW1vh.YSix4tSd6',
null,'settings.client.require-proof-key',
'http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc','openid,message.read,message.write',
'settings.token.id-token-signature-algorithm');
