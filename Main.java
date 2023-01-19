import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final int NUMBER_RANDOMLY_GENERATED_LINES = 5;
    private static final int NUMBER_PARAMETERS = 2;
    private static final int HIDDEN_AMOUNT = 2;
    private static final Inputneuron[] input = new Inputneuron[3];
    private static final Hiddenneuron[] hidden = new Hiddenneuron[HIDDEN_AMOUNT+1];
    private static final Outputneuron output = new Outputneuron();
    private static final int EPOCH = 2000;

    private static final double alphaAdjustmentRate = 0.95;
    private static int[] dataSetPattern;

    private static int lineCount;

    private static double[][]  data;

    private static int currentDataSet;
    private static double  alpha = 0.1;
    private static double alphaForInput = alpha * 5;

    // Daten zum debuggen damit man das Array im Debugger besser anschauen kann
    private static double[] hiddenIns;
    private static double[] hiddenOuts;

    private static double[] inputIns;

    private static double outputIn = 0;
    private static final double initialAlphaValue = alpha;

    private static void createRandomData() throws IOException {
        CreateRandomData.fillFileWithInput("wetterRandom.txt",NUMBER_RANDOMLY_GENERATED_LINES);
    }
    private static void initDataSets() {
        inputIns = new double[input.length];
        Arrays.fill(inputIns, 0);
        int i = 0;
        try {
            String fileName = "wetterRandom.txt";
            File myFile = new File(fileName);
            lineCount = (int)Files.lines(Paths.get(fileName)).count();
            data = new double[lineCount][NUMBER_PARAMETERS+1];
            Scanner scanner = new Scanner(myFile);
            while (scanner.hasNext() && i < lineCount) {
                double x1 = Double.parseDouble(scanner.next());
                double x2 = Double.parseDouble(scanner.next());
                double y = Double.parseDouble(scanner.next());
                data[i][0] = x1;
                data[i][1] = x2;
                data[i][2] = y;
                i++;
            }
            dataSetPattern = new int[i];
            for(int k = 0; k< dataSetPattern.length;k++){
                dataSetPattern[k] = k;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initInput() {
        for(int i = 1; i<input.length; i++){
            input[i] = new Inputneuron(HIDDEN_AMOUNT);
            inputIns[i] = data[dataSetPattern[currentDataSet]][i-1];
            input[i].setIn(inputIns[i]);
        }
        // Bias
        input[0] = new Inputneuron(HIDDEN_AMOUNT);
        inputIns[0] = 1;
        input[0].setIn(1);
    }

    private static void initHidden() {
        for(int i = 0; i<hidden.length;i++){
            hidden[i] = new Hiddenneuron();
        }
        // Bias
        hidden[0].setAsBias();
    }

    private static void initRandomDatasetPattern(){
        for(int i=0;i<dataSetPattern.length;i++) {
            double helperVariable = dataSetPattern.length*Math.random();
            int j = (int)helperVariable;
            int a = dataSetPattern[i];
            int b = dataSetPattern[j];
            dataSetPattern[i] = b;
            dataSetPattern[j] = a;
        }
    }

    public static void main(String[] args){
        try {
            createRandomData();
        } catch (IOException e){
            System.out.println("An error occured");
        }
        // In initDataSets() einstellen, ob man wetterRandom.txt oder wetter.txt lesen will
        // wetterRandom wird jedes mal neu erzeugt
        initDataSets();
//        logDataValues();
        initInput();
        initHidden();
//        printOutWeights();
        int amountErrors;
        int amountPreviousErrors = 0;
        for(int i = 0; i<EPOCH;i++) {
            int patternIndex = 0;
            initRandomDatasetPattern();
            currentDataSet = dataSetPattern[patternIndex];
            amountErrors = 0;

            //initTestInput();
            //initTestHidden();
//            printOutWeights();

            for (int j = 0; j < lineCount; j++) {
                currentDataSet = dataSetPattern[patternIndex];
                resetIns();
                readInput();
                forward();
//                printOutsIns();
//                printOutWeights();

                boolean isDeltaZero = calculateDelta();
                if (!isDeltaZero) {
                    amountErrors++;
                    backward();
                }
                patternIndex++;
                //logInputValues();
//                printOutsIns();
            }
            if(amountErrors != 0) {
                System.out.println("Amount of wrong Data Sets this epoch: " + amountErrors);
                if(amountPreviousErrors != 0){
                    if(amountPreviousErrors > amountErrors){
                        alpha *= alphaAdjustmentRate;
                        alphaForInput *= alphaAdjustmentRate;
                    } else if(amountPreviousErrors < amountErrors){
                        alpha = initialAlphaValue;
                        alphaForInput = initialAlphaValue*5;
                    }
                }
                amountPreviousErrors = amountErrors;
//                printOutWeights();
//                printOutsIns();
            } else {
                System.out.println("No wrong data sets this epoch. Training concluded.");
//                printOutsIns();
                break;
            }
//            printOutsIns();
        }
        //checkIfValuesCorrect();
//        printOutWeights();
    }

    private static void resetIns() {
        for(Hiddenneuron hn : hidden){
            hn.setIn(0);
        }
        output.setIn(0);
    }

    private static void readInput() {
        for(int i = 1; i < input.length;i++){
            inputIns[i] = data[currentDataSet][i-1];
            input[i].setIn(inputIns[i]);
        }
    }


    private static void forward() {
        hiddenIns  = calculateHiddenIns();
        hiddenOuts = calculateHiddenOuts();
        outputIn = calculateOutputIn();
        calculateOutputOut();
    }

    private static double[] calculateHiddenIns() {
        hiddenIns = new double[hidden.length];
        for(int i = 1; i<hiddenIns.length;i++){
            hiddenIns[i] = 0;
        }
        hiddenIns[0] = 1;
        hidden[0].setIn(1);
        for(int i = 1; i<hidden.length;i++){
            // Hiddenlength = 3: Führt 1, 2 aus
            for(int j = 0; j<input.length;j++) {
                // Hidden 1,2 addIn inPut 0,1,2 * input 0,1,2 . getWeight 0,1
                hiddenIns[i] += input[j].getIn() * input[j].getWeight(i-1);
                // Gewicht 0 von input[i] geht zu hidden[1]
            }
            hidden[i].setIn(hiddenIns[i]);
        }
        return hiddenIns;
    }

    private static double[] calculateHiddenOuts() {

        hiddenOuts = new double[hidden.length];
        for(int i = 0; i< hiddenOuts.length;i++){
            hiddenOuts[i] = 0;
        }

        for(int i = 0; i<hidden.length;i++){
            hiddenOuts[i] = hidden[i].calculateOut();
        }
        return hiddenOuts;
    }

    private static double calculateOutputIn() {
        outputIn = 0;
        for(int i = 0; i< hidden.length;i++){
            outputIn += hidden[i].getOut() * hidden[i].getWeight();
        }
        output.setIn(outputIn);
        return outputIn;
    }

    private static double calculateOutputOut() {
        double outputOut = output.calculateOut();
        return outputOut;
    }

    private static boolean calculateDelta() {
        int actual = (int)data[currentDataSet][2];
        boolean isDeltaZero = output.calculateDelta(actual) == 0;
        return isDeltaZero;
    }

    private static void backward () {
        output.calculateDelta((int)data[currentDataSet][2]);
        calculateDeltaHidden();
        assignNewWeights();
    }


    private static void calculateDeltaHidden() {
        for(int i = 0; i< HIDDEN_AMOUNT+1;i++){
            Hiddenneuron current = hidden[i];
            double result = Hiddenneuron.sigDerivative(current.getIn()) * current.getWeight() * output.calculateDeltaTotal();
            current.setDeltaHidden(result);
        }
    }

    private static void assignNewWeights(){
        for(int i = 0; i<hidden.length;i++){
            Hiddenneuron current = hidden[i];
            double result = current.getWeight() + alpha * current.getOut() * output.getDeltaTotal();
            current.setWeight(result);
        }
        for(int i = 0; i<input.length;i++){
            Inputneuron current = input[i];
            for(int j = 0; j<input[i].getAmountWeights();j++) {
                double result = current.getWeight(j) + alphaForInput * current.getIn() * hidden[j].getDeltaHidden();
                current.setWeight(result,j);
            }
        }
    }

    // Ab hier Testmethoden zum Debuggen!
    // ################################################################################################################
    private static void printOutWeights() {
        for(int i = 0; i<input.length;i++){
            System.out.print("\nInput " + i + " Gewichte: ");
            for(int j = 0; j<input[i].getAmountWeights();j++) {
                System.out.print(input[i].getWeight(j) + ", ");
            }
        }
        System.out.println();
        for(int i = 0; i<hidden.length;i++){
            System.out.print("\nHidden "+ i + " Gewichte: ");
            System.out.print(hidden[i].getWeight() + " ");
        }
        System.out.println();
    }

    private static void testOutput() {
//        for(int i = 0; i< hidden.length;i++){
//            System.out.println("Hidden " + i + " in: " + hidden[i].getIn());
//            System.out.println("Hidden " + i + " out: " + hidden[i].getOut());
//        }
//        for(int i = 0; i< input.length; i++){
//            System.out.println("Input " + i + " in: " + input[i].getIn());
//        }
        System.out.println("Output in: " + output.getIn());
        System.out.println("Output out: " + output.getOut());
    }

    private static void initTestHidden() {
        for(int i = 0; i<hidden.length;i++){
            hidden[i] = new Hiddenneuron();
        }
        // Bias
        hidden[0].setAsBias();
        hidden[0].setIn(1);

        hidden[0].setWeight(0.3);
        hidden[1].setWeight(-0.4);
        hidden[2].setWeight(-0.2);
    }

    private static void initTestInput() {
        for(int i = 0; i<input.length; i++){
            input[i] = new Inputneuron(HIDDEN_AMOUNT);
        }
        input[0].setIn(1);
        input[1].setIn(0.1);
        input[2].setIn(0.3);

        input[0].setWeight(0.1,0);
        input[0].setWeight(0.4,1);

        input[1].setWeight(-0.2,0);
        input[1].setWeight(-0.8,1);

        input[2].setWeight(0.3,0);
        input[2].setWeight(0.7,1);
    }

    private static void printOutsIns() {
//        System.out.print("Hidden Ins: \n" + hiddenIns[0]);
//        for(int i = 1; i<hiddenIns.length;i++){
//            System.out.print(", " + hiddenIns[i]);
//        }
//        System.out.println();
//
//        System.out.print("Hidden Outs: \n" + hiddenOuts[0]);
//        for(int i = 1; i<hiddenOuts.length;i++){
//            System.out.print(", " +hiddenOuts[i]);
//        }
//        System.out.println();
        System.out.println("Output In: " + output.getIn());
        System.out.println("Output Out: " + output.getOut() + "\n");
    }

    private static void logInputValues() {
        System.out.print("Input Ins: \n" + inputIns[0]);
        for(int i = 1; i<input.length; i++){
            System.out.print(", " + inputIns[i]);
        }
        System.out.println();
    }

    private static void logDataValues(){
        for(int row = 0; row < data.length; row++){
            for(int col = 0; col < data[row].length;col++){
                System.out.print(data[row][col] + " ");
            }
            System.out.println("\n");
        }
    }
    private static void checkIfValuesCorrect() {
        for(int i = 0; i<input.length;i++){
            if (input[i].getIn() != inputIns[i]){
                System.out.println("Values not correct: input at index " + i + "'s 'in' Value does not match inputIns at index " + i);

            }
            if(hidden[i].getIn() != hiddenIns[i])
            {
                System.out.println("Values not correct: hidden at index " + i + "'s 'in' Value does not match hiddenIns at index " + i);
            }
            if(hidden[i].getOut() != hiddenOuts[i])
            {
                System.out.println("Values not correct: hidden at index " + i + "'s 'out' Value does not match hiddenOut at index " + i);
            }
        }
    }
}
