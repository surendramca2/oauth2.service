package com.surendra.oauth.server.constant;

public enum TransactAPIs {

     CREATE_API("createNewClient","/client"),
     GET_ALL_CLIENTS_API("","/clients") ,
     GET_DOWNLOADS_CREDS_API("downloadFile","/client/{clientId}/credentials/download"),
     DELETE_CLIENT_API("deleteRegisteredClientById","/client/{clientId}");
    private String apiHandlerName;
    private String api;

    TransactAPIs(String apiHandlerName,String api){
        this.apiHandlerName=apiHandlerName;
        this.api=api;
    }

    public String getApi(){
        return api;
    }
}
