public class Main {

    private static final int HIDDEN_AMOUNT = 3;
    private static Inputneuron[] input = new Inputneuron[3];
    private static Hiddenneuron[] hidden = new Hiddenneuron[HIDDEN_AMOUNT];
    private static Outputneuron output = new Outputneuron();
    public static void main(String[] args){
        //initInput();
        initTestInput();
        //initHidden();
        initTestHidden();
        forward();
        testOutput();

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
