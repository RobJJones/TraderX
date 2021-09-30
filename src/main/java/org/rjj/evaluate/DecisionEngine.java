package org.rjj.evaluate;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@ApplicationScoped
public class DecisionEngine {

    private static final boolean[] decision = {true, false};

    private static CostSensitiveClassifier model;
    private Instances dataTemplate;

    public static void main(String[] args) throws Exception {

        //0.000307,18:30,1.072183,0.256493,-5.359322,19841.2,1.075768,0.412076,1.739459,0.110218,Buy
        final DecisionEngine decisionEngine = new DecisionEngine();
        TickerDetails ticker = new TickerDetails();
        Calendar calendar = new GregorianCalendar();

        //False Example
//        calendar.set(Calendar.HOUR_OF_DAY, 19);
//        calendar.set(Calendar.MINUTE, 30);
//        ticker.setAverageTrueRange(BigDecimal.valueOf(0.000307));
//        ticker.setTime(calendar.getTime());
//        ticker.setEma200(BigDecimal.valueOf(0.41270299142059336));
//        ticker.setKeltnerUpper(BigDecimal.valueOf(0.40937232463367246));
//        ticker.setDailyOpen(BigDecimal.valueOf(0.4314476695181912));
//        ticker.setVolumeMA(BigDecimal.valueOf(19841.2));
//        ticker.setVwap(BigDecimal.valueOf(0.41271762824012986));
//        ticker.setVwapLowerBand(BigDecimal.valueOf(0.4100076098544533));
//        ticker.setVwapUpperBand(BigDecimal.valueOf(0.41542764662580645));
//        ticker.setVwma(BigDecimal.valueOf(0.4087750492782904));
//        ticker.setClose(BigDecimal.valueOf(0.408325));

        //True Example
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 18);
        ticker.setAverageTrueRange(BigDecimal.valueOf(1.618032859024187));
        ticker.setTime(calendar.getTime());
        ticker.setEma200(BigDecimal.valueOf(103.02864259294127));
        ticker.setKeltnerUpper(BigDecimal.valueOf(100.85531944297473));
        ticker.setDailyOpen(BigDecimal.valueOf(107.19293063122355));
        ticker.setVolumeMA(BigDecimal.valueOf(24138.6));
        ticker.setVwap(BigDecimal.valueOf(99.08938487365155));
        ticker.setVwapLowerBand(BigDecimal.valueOf(97.0162569880171));
        ticker.setVwapUpperBand(BigDecimal.valueOf(101.162512759286));
        ticker.setVwma(BigDecimal.valueOf(99.10715222681738));
        ticker.setClose(BigDecimal.valueOf(96.28427500000001));
        //ticker.setValue(10, "?"); //Set to Leave

        decisionEngine.buyStock(ticker);
        System.out.println(decisionEngine.buyStock(ticker));
    }

    public DecisionEngine() throws Exception {
        loadModel();
        loadTestData();
    }

    public boolean buyStock(TickerDetails ticker) throws Exception {

        System.out.println(ticker.toDecisionEngineString());
        final Instances mappedInstances = mapInstance(ticker);
        return evaluateModel(mappedInstances);
    }

    private Instances mapInstance(TickerDetails ticker) throws ParseException {
        Instances newInstances = new Instances(dataTemplate); //Copy original template
        final Instance instance = newInstances.firstInstance();

        instance.setValue(0, ticker.getAverageTrueRange().doubleValue());
        instance.setValue(1, instance.attribute(1).parseDate(ticker.getFormattedTime()));
        instance.setValue(2, ticker.getEma200Percentage().doubleValue());
        instance.setValue(3, ticker.getKeltnerUpperPercentage().doubleValue());
        instance.setValue(4, ticker.getPercentageChangeOpen().doubleValue());
        instance.setValue(5, ticker.getVolumeMA().doubleValue());
        instance.setValue(6, ticker.getVwapPercentage().doubleValue());
        instance.setValue(7, ticker.getVwapLowerBandPercentage().doubleValue());
        instance.setValue(8, ticker.getVwapUpperBandPercentage().doubleValue());
        instance.setValue(9, ticker.getVwmaPercentage().doubleValue());

        return newInstances;
    }

    private boolean evaluateModel(Instances data) throws Exception {
        Evaluation evaluation = new Evaluation(data);
        //evaluation.evaluateModel(model, data);

        final double result = evaluation.evaluateModelOnce(model, data.firstInstance());

        //System.out.println("Result: "+result);
        //System.out.println(evaluation.toSummaryString());
        //System.out.println(evaluation.toMatrixString());

        return (result == 0) ? true : false;
    }

    private void loadTestData() throws Exception {
        ArffLoader loader = new ArffLoader();
        //loader.setFile(new File("/Users/robjones/dev/stockData/Strategies/Oversold5Minute/arffFiles/newOversoldFromView.arff"));
        loader.setFile(new File("/Users/robjones/dev/stockData/Strategies/Oversold5Minute/arffFiles/templateOversold.arff"));

        dataTemplate = loader.getDataSet();
        dataTemplate.setClassIndex(dataTemplate.numAttributes()-1);
    }

    private void loadModel() throws Exception {
        final String path = DecisionEngine.class.getClassLoader().getResource("costSensitiveRandomTree.model").getPath();
        model = (CostSensitiveClassifier) SerializationHelper.read(path);
        System.out.println(model.getClass().getName() + " loaded.");
    }
}
