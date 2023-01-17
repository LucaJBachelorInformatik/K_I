import java.text.DecimalFormat;

public class Inputneuron {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private double in;
    private double[] weight;
    public Inputneuron(int hiddenAmount){
        // NACHHER ÄNDERN; IN SOLL VON DATEI GELESEN WERDEN
        setIn(Math.random());
        setHiddenAmount(hiddenAmount);
        initializeWeights();
    }

    public void initializeWeights(){
        for(int i = 1; i<weight.length;i++){
            weight[i] = Double.parseDouble(df.format(Math.random()));
        }
    }
    public void setWeights(double d, int weightIndex){
        weight[weightIndex] = Double.parseDouble(df.format(d));
    }

    public double getWeight(int weightIndex){
        return weight[weightIndex];
    }
    public void setIn(double d){
        in = d;
    }
    public void setHiddenAmount(int amount){
        // Anzahl der Gewichte immer gleich Anzahl Hidden Neuronen - 1!
        weight = new double[amount-1];
    }
    public double getIn(){
        return in;
    }
}
