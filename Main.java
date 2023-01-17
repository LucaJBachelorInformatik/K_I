import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private static final int HIDDEN_AMOUNT = 3;
    private static Inputneuron[] input = new Inputneuron[3];
    private static Hiddenneuron[] hidden = new Hiddenneuron[HIDDEN_AMOUNT];
    private static Outputneuron output = new Outputneuron();
    private static final int NUMBER_DATA_SETS = 12;
    private static final int NUMBER_PARAMETERS = 2;
    private static double[][] data = new double[NUMBER_DATA_SETS][NUMBER_PARAMETERS+1];
    private static final int EPOCH = 200;
    private static int currentDataSet;
    private static double alpha = 0.05;
    private static int amountErrors;

    public static void main(String[] args){
        for(int i = 0; i<EPOCH;i++) {
            currentDataSet = 0;
            amountErrors = 0;
            for (int j = 0; j < NUMBER_DATA_SETS; j++) {
                initDataSets();
                //TestDataSets();
                initInput();
                //initTestInput();
                initHidden();
                //initTestHidden();
                //printOutWeights();
                forward();
                boolean isDeltaZero = calculateDelta();
                if (!isDeltaZero) {
                    amountErrors++;
                    backward();
                }
                currentDataSet++;
                for(int k = 1; k<input.length;k++){
                    input[k].setIn(0);
                }
                for(int k = 1; k<hidden.length;k++){
                    hidden[k].setIn(0);
                }
                output.setIn(0);

            }
            if (amountErrors == 0) {
                System.out.println("Dataset complete. Predicted without error.");
                break;
            } else {
                System.out.println(amountErrors + " errors in predicting datasets.");
            }
        }
    }

    private static void printOutWeights() {
        for(int i = 0; i<input.length;i++){
            System.out.println("Input " + i + " Gewichte: ");
            for(int j = 0; j<input[i].getAmountWeights();j++) {
                System.out.print(input[i].getWeight(j) + ", ");
            }
        }
    }

    private static boolean calculateDelta() {
        int actual = (int)data[currentDataSet][2];
        int expected = output.checkExpectation();
        boolean isDeltaZero = output.calculateDelta(actual,expected) == 0;
        return isDeltaZero;
    }

    private static void TestDataSets() {
        for(int i = 0; i<NUMBER_DATA_SETS;i++){
            System.out.println("DataSet Number " + i + " : ");
            System.out.print(data[i][0] + ", ");
            System.out.print(data[i][1] + ", ");
            System.out.print(data[i][2] + ", " + "\n");
        }
    }

    private static void initDataSets() {
        int i = 0;
        try {
            Scanner scanner = new Scanner(new File("src/wetter.txt"));
            while (scanner.hasNext()) {
                double x1 = Double.valueOf(scanner.next());
                double x2 = Double.valueOf(scanner.next());
                int y = Integer.valueOf(scanner.next());
                data[i][0] = x1;
                data[i][1] = x2;
                data[i][2] = y;
                i++;
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void backward () {
        output.calculateDeltaTotal();
        setNewHiddenWeights();
        calculateDeltaHidden();
        setNewInputWeights();
    }
    private static void setNewInputWeights() {
        for(int i = 0; i< input.length; i++){
            Inputneuron current = input[i];
            for(int j =0; j<input[i].getAmountWeights(); j++){
                double currentWeight = current.getWeight(j);
                double currentOut = current.calculateOut();
                input[i].setWeight(currentWeight + alpha * currentOut * hidden[j].getDeltaHidden(),j);
            }
        }
    }

    private static void calculateDeltaHidden() {
        double result = 0;

        for (int i = 0; i < hidden.length; i++){
            result += hidden[i].getWeight() * output.getDeltaTotal();
            result *= Hiddenneuron.sigDerivative(hidden[i].getIn());
            hidden[i].setDeltaHidden(result);
        }
    }

    private static void setNewHiddenWeights() {
        for(int i = 0; i< HIDDEN_AMOUNT;i++){
            Hiddenneuron current = hidden[i];
            double weight = current.getWeight();
            double result = alpha * current.getOut() * output.getDeltaTotal();
            double deltaHidden = weight + result;
            hidden[i].setWeight(deltaHidden);
        }
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

    private static void forward() {
        calculateHiddenIns();
        calculateHiddenOuts();
        calculateOutputIn();
        calculateOutputOut();
    }

    private static void calculateOutputOut() {
        output.calculateOut();
    }

    private static void calculateOutputIn() {
        for(int i = 0; i< hidden.length;i++){
            output.addIn(hidden[i].getOut() * hidden[i].getWeight());
        }
    }

    private static void calculateHiddenOuts() {
        for(int i = 0; i<hidden.length;i++){
            hidden[i].calculateOut();
        }
    }

    private static void calculateHiddenIns() {
        for(int i = 1; i<hidden.length;i++){
            // Hiddenlength = 3: Führt 1, 2 aus
            for(int j = 0; j<input.length;j++) {
                // Hidden 1,2 addIn inPut 0,1,2 * input 0,1,2 . getWeight 0,1
                hidden[i].addIn(input[j].getIn() * input[j].getWeight(i-1));
                // Gewicht 0 von input[i] geht zu hidden[1]
            }
        }
    }

    private static void initHidden() {
        for(int i = 0; i<hidden.length;i++){
            hidden[i] = new Hiddenneuron();
        }
        // Bias
        hidden[0].setAsBias();
        hidden[0].setIn(1);

    }
    private static void initInput() {
        for(int i = 0; i<input.length; i++){
            input[i] = new Inputneuron(HIDDEN_AMOUNT);
        }
        // Bias
        input[0].setIn(1);
    }
}
