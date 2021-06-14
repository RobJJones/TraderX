package org.rjj;

import org.rjj.ib.BrokerConnectionException;
import org.rjj.ib.CSVProcessor;
import org.rjj.ib.TraderInteractiveBrokerInterface;
import org.rjj.model.Ticker;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/trade")
public class TraderService {

    @Inject
    TraderInteractiveBrokerInterface interactiveBrokerInterface;

    @Path("/scannerList")
    @POST
    public Response tradeStocks(@DefaultValue ("GBP") @QueryParam("currency") String currency,
                                String data) {

        List<Ticker> tickers = CSVProcessor.processCSVData(data);
        try {
            interactiveBrokerInterface.buyStock(tickers.get(0), currency);
        } catch (BrokerConnectionException e) {
            return Response.status(Response.Status.fromStatusCode(500))
                    .entity("TWS Connection unavailable.")
                    .build();
        }

        return Response.status(200).build();
    }

    @Path("/retrieveOrders")
    @GET
    public Response retrieveOrders() {

        try {
            interactiveBrokerInterface.retrieveOrders();
        } catch (BrokerConnectionException e) {
            return Response.status(Response.Status.fromStatusCode(500))
                    .entity("TWS Connection unavailable.")
                    .build();
        }

        return Response.status(200).build();
    }
}