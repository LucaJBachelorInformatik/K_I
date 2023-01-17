import java.text.DecimalFormat;

public class Hiddenneuron {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private double in;
    private double out;
    private double weight;

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
        out = sig(in);
    }
    public double getOut(){
        return out;
    }
    private double sig(double d){
        double sigmoid = 1/(1+Math.pow (Math.E,-d) );
        return sigmoid;
    }
}
