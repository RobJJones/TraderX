package org.rjj.ib;

import org.rjj.model.Ticker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVProcessor {

    private static final String COMMA = ",";

    public static List<Ticker> processCSVData(String csvData) {

        List<Ticker> inputList = new ArrayList<>();

        // skip the header of the csv
        inputList = csvData.lines().skip(1).map(mapToItem).collect(Collectors.toList());

        return inputList ;
    }

    private static Function<String, Ticker> mapToItem = (line) -> {

        String[] p = line.split(COMMA);// a CSV has comma separated lines

        Ticker item = new Ticker();

        item.setSymbol(p[0]);//<-- this is the first column in the csv file

        item.setLastPrice(Double.parseDouble(p[3]));

        //more initialization goes here

        return item;
    };

}
