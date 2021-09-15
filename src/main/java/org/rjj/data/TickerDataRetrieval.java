package org.rjj.data;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class TickerDataRetrieval {

    @ConfigProperty(name = "stockscraper.downloads.ticker.directory")
    private String tickerDirectory;

    @ConfigProperty(name = "stockscraper.downloads.project.directory")
    private String stockScraperProjectDirectory;

    public void executeTickersDataExtract(String[] tickers) {
        Arrays.stream(tickers).parallel().forEach(this::executeTickerDataExtract);
    }

    private void executeTickerDataExtract(String ticker) {

        final List<String> command = getCypressCommand(ticker);
        ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();
        processBuilder.command(command);
        processBuilder.directory(new File(stockScraperProjectDirectory));
        try {
            processBuilder.start().waitFor();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException processing "+ticker);
        } catch (IOException e) {
            System.out.println("IOException processing "+ticker);
        }
    }

    private List<String> getCypressCommand(String ticker) {

        List<String> command = new LinkedList<>();
        command.add("npx");
        command.add("cypress");
        command.add("run");
        command.add("--project");
        command.add(stockScraperProjectDirectory);
        command.add("--spec");
        command.add(stockScraperProjectDirectory+"/"+
                "cypress/integration/stock_data_import/tradingViewMLTickData.spec.js");
        command.add("--browser");
        command.add("chrome");
        command.add("--config");
        command.add("downloadsFolder="+tickerDirectory);
        command.add("--env");
        command.add("ticker=\""+ticker+"\"");
        return command;

    }
}
