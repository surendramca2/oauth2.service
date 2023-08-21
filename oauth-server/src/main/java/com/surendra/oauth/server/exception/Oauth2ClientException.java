package com.surendra.oauth.server.exception;

/**
 * Customer exception used
 */
public class Oauth2ClientException extends Exception{

    /**
     * Customer exception constructor to raise the exception.
     * @param message
     */
    public Oauth2ClientException(String message){

       super(message);
    }
}
