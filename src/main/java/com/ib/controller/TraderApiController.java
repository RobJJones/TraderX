package com.ib.controller;

public class TraderApiController extends ApiController{

    public TraderApiController(IConnectionHandler handler) {
        super(handler);
    }

    public TraderApiController(IConnectionHandler handler, ApiConnection.ILogger inLogger, ApiConnection.ILogger outLogger) {
        super(handler, inLogger, outLogger);
    }

    @Override
    public boolean checkConnection() {
        return super.checkConnection();
    }
}
