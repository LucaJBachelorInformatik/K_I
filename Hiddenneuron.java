import java.text.DecimalFormat;

public class Hiddenneuron {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private double in;
    private double out;
    private double weight;
    private double deltaHidden;
    private boolean isBias = false;

    public Hiddenneuron(){
        in = 0;
        initializeWeights();
    }
    public double getIn(){
        return in;
    }
    public void setIn(double d){
        in = d;
    }
    public void setAsBias(){
        isBias = true;
    }
    public void initializeWeights(){
        weight = Double.parseDouble(df.format(Math.random()));
    }
    public void setWeight(double d){
        weight= Double.parseDouble(df.format(d));
    }
    public double getWeight(){
        return weight;
    }
    public void addIn(double d) {
        in += d;
    }
    public void calculateOut(){
        if(isBias){
            out = 1;
        } else {
            out = sig(in);
        }

    }
    public double getOut(){
        return out;
    }
    public static double sig(double d){
        double sigmoid = 1/(1+Math.pow (Math.E,-d) );
        return sigmoid;
    }
    public static double sigDerivative(double d){
        double result = sig(d) * (1 - sig(d));
        return result;
    }
    public void setDeltaHidden(double deltaHidden){
        this.deltaHidden = deltaHidden;
    }
    public double getDeltaHidden(){
        return deltaHidden;
    }

}
