package org.rjj.purchase;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.rjj.model.MovingAverageTicker;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/buyStock")
@RegisterRestClient(configKey = "trader-api")
public interface TradingService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String purchaseStock(MovingAverageTicker ticker);

}
