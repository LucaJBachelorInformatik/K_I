import java.text.DecimalFormat;

public class Inputneuron {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private double in;
    private double out;
    private double[] weight;
    private double deltaInput;

    public Inputneuron(int hiddenAmount){
        setHiddenAmount(hiddenAmount);
        initializeWeights();
    }

    public void initializeWeights(){
        double formattedRandom = Double.parseDouble(df.format(Math.random()));
        for(int i = 0; i<weight.length;i++){
            if(Math.random() >= 0.5){
                weight[i] = formattedRandom;
            } else {
                weight[i] = formattedRandom*-1;
            }
        }
    }
    public void setWeight(double d, int weightIndex){
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
        weight = new double[amount];
    }
    public double getIn(){
        return in;
    }
    public void calculateDeltaInput(){

    }
    public double getDeltaInput(){
        return deltaInput;
    }
    public int getAmountWeights(){
        return weight.length;
    }
    public double calculateOut(){
        out = Hiddenneuron.sig(in);
        return out;
    }
    public double[] getAllWeights(){
        return weight;
    }

}
