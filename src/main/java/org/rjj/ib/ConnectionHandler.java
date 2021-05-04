package org.rjj.ib;

import com.ib.controller.ApiController;
import com.ib.controller.Formats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements ApiController.IConnectionHandler {

    private Logger LOGGER = LoggerFactory.getLogger(ConnectionHandler.class);
    private ApiController controller;
    private List<String> acctList = new ArrayList<>();

    @Override
    public void connected() {
        show( "connected");

        getController().reqCurrentTime(time -> show( "Server date/time is " + Formats.fmtDate(time * 1000) ));

        getController().reqBulletins( true, (msgId, newsType, message, exchange) -> {
            String str = String.format( "Received bulletin:  type=%s  exchange=%s", newsType, exchange);
            show( str);
            show( message);
        });
    }

    @Override
    public void disconnected() {
        show( "disconnected");
    }

    @Override
    public void accountList(List<String> list) {

        acctList.clear();
        acctList.addAll( list);
    }

    @Override
    public void error(Exception e) {
        show( e.toString() );
    }

    @Override
    public void message(int id, int errorCode, String errorMsg) {
        show( id + " " + errorCode + " " + errorMsg);
    }

    @Override
    public void show(String s) {
        //TODO Make this an event at some later stage
        LOGGER.info(s);
    }

    public ApiController getController() {
        return controller;
    }

    public void setController(ApiController controller) {
        this.controller = controller;
    }
}
