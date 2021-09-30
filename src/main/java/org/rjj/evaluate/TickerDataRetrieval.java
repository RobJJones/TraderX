package org.rjj.evaluate;

import com.opencsv.bean.CsvToBeanBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.rjj.model.Ticker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class TickerDataRetrieval {

    private static final int MAXIMUM_RUNNING_EXTRACTS = 5;
    private static AtomicInteger running_extracts_count = new AtomicInteger(0);

    @ConfigProperty(name = "stockscraper.downloads.ticker.directory")
    private String tickerDirectory;

    @ConfigProperty(name = "stockscraper.downloads.project.directory")
    private String stockScraperProjectDirectory;

    @Inject
    private DecisionEngine decisionEngine;

    private static void incrementCounter() {
        running_extracts_count.getAndIncrement();
    }

    private static void decrementCounter() {
        running_extracts_count.getAndDecrement();
    }

    public void executeTickersDataExtract(List<Ticker> tickers) {
        tickers.parallelStream().forEach(this::executeTickerDataExtract);
    }

    private void executeTickerDataExtract(Ticker ticker) {

        final String tickerSymbol = ticker.getSymbol();

        if (running_extracts_count.intValue()<MAXIMUM_RUNNING_EXTRACTS) {
            incrementCounter();
            executeTickerDataExtract(tickerSymbol);

            try {
                final TickerDetails tickerAlertDetails = getTicker(ticker);
                final boolean buyStock = buyStock(tickerAlertDetails);

                System.out.println(ticker.getSymbol()+ " - BUY? - "+buyStock);

            } catch (Exception e) {
                //Ignore stock
                System.out.println("Exception running extracts for ticker - "+tickerSymbol);
                e.printStackTrace();
            } finally {
                decrementCounter();
            }
        } else {
            System.out.println("Maximum running extracts running, ignoring "+tickerSymbol);
        }


    }

    private boolean buyStock(TickerDetails tickerList) throws Exception {
        //LET'S GO!!!!
        return decisionEngine.buyStock(tickerList);
    }

    private void executeTickerDataExtract(String ticker) {

        final List<String> command = getCypressCommand(ticker);
        ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();
        processBuilder.command(command);
        processBuilder.directory(new File(stockScraperProjectDirectory));
        try {
            System.out.println("Starting new extract, running "+running_extracts_count.intValue()+" extracts.");
            processBuilder.start().waitFor();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException processing "+ticker);
        } catch (IOException e) {
            System.out.println("IOException processing "+ticker);
        }
    }

    private TickerDetails getTicker(Ticker ticker) throws FileNotFoundException, TickerDataNotFound {

        final File csvFile = new File(tickerDirectory+"/"+convertTickerToFileName(ticker.getSymbol(),
                "csv"));

        List<TickerDetails> tickers = new CsvToBeanBuilder(new FileReader(csvFile))
                .withType(TickerDetails.class)
                .build()
                .parse();

        Collections.sort(tickers, new Comparator<TickerDetails>() {
            @Override
            public int compare(TickerDetails o1, TickerDetails o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        return extractTickerDetail(ticker, tickers);
    }

    private TickerDetails extractTickerDetail(Ticker ticker, List<TickerDetails> tickers) throws TickerDataNotFound {

        GregorianCalendar calendar = new GregorianCalendar();
        //2021-08-04T13:44:00Z
        calendar.setTime(ticker.getDateTime());
//        calendar.set(Calendar.HOUR_OF_DAY, 14); //Remove
//        calendar.set(Calendar.MINUTE, 44); //Remove
        calendar.set(Calendar.SECOND, 0); //Don't remove
        calendar.set(Calendar.MILLISECOND, 0); //Don't remove

        TickerDetails tickerDetails = getFilterAlertTicker(tickers, calendar);

        if (tickerDetails==null) {
            //Alert ticker doesn't exist, try the minute before
            System.out.println("*** "+ticker.getSymbol()+" doesn't exist, try minute before ...");
            calendar.add(Calendar.MINUTE, -1);
            tickerDetails = getFilterAlertTicker(tickers, calendar);
        }

        if (tickerDetails==null) throw new TickerDataNotFound(ticker);

        return tickerDetails;
    }

    private TickerDetails getFilterAlertTicker(List<TickerDetails> tickers, GregorianCalendar calendar) {
        return tickers.stream().filter(ticker -> ticker.getTime().compareTo(calendar.getTime()) == 0)
                .findAny()
                .orElse(null);
    }

    private String convertTickerToFileName(String ticker, String fileType) {
        return ticker.replace(':', '_')+", 1."+ fileType;
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
        command.add("--quiet");
        return command;

    }
}
