package org.rjj;

import org.rjj.ib.TraderInteractiveBrokerInterface;
import org.rjj.model.Account;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/trade")
public class TraderService {

    @Inject
    TraderInteractiveBrokerInterface interactiveBrokerInterface;

    @Inject
    Account account;

    @Path("buy")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    //@Consumes(MediaType.APPLICATION_JSON)
    public Response buyStocks(@QueryParam("list") List<String> stockList) {

        System.out.println("List of stocks man!");

        if (interactiveBrokerInterface.connect()) {

            interactiveBrokerInterface.updateAccount();

            stockList.forEach((stock) -> {
                System.out.println(stock);
            });

        } else {

            return Response.status(Response.Status.fromStatusCode(500))
                    .entity("TWS Connection unavailable.")
                    .build();
        }

        //this.interactiveBrokerInterface.disconnect();

        return Response.status(200)
                .entity("Posted - "+stockList.toString())
                .build();
    }
}