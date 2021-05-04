package org.rjj;


import io.quarkus.arc.profile.IfBuildProfile;
import org.rjj.model.Account;
import org.rjj.model.TWS.TWSLiveAccount;
import org.rjj.model.TWS.TWSPaperAccount;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.ws.rs.Produces;

@Dependent
public class TraderConfig {

    @Produces
    @IfBuildProfile("live")
    public Account liveAccount() {
        return new TWSLiveAccount();
    }

    @ApplicationScoped
    @Produces
    @IfBuildProfile("paper")
    public Account paperAccount() {
        return new TWSPaperAccount();
    }

}
