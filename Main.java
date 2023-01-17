public class Main {

    private static final int HIDDEN_AMOUNT = 3;
    private static Inputneuron[] input = new Inputneuron[3];
    private static Hiddenneuron[] hidden = new Hiddenneuron[HIDDEN_AMOUNT];
    private static Outputneuron output = new Outputneuron();
    public static void main(String[] args){
        initInput();
        initHidden();
        forward();
    }

    private static void forward() {
        calculateHiddenIns();
        calculateHiddenOuts();
        calculateOutputIn();
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
