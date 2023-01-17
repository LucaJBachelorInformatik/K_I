import java.text.DecimalFormat;

public class Outputneuron {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private double in;
    private double out;
    private int delta;


    public int calculateDelta(int actual, int expected){
        delta = actual - expected;
        return delta;
    }
    public double calculateDeltaTotal(){
        double result = sigDerivative(in) * (delta-out);
        return result;
    }
    public double getIn(){
        return in;
    }

    public void addIn(double d) {
        in += d;
    }

    public void calculateOut(){
        out = sig(in);
    }

    private double sig(double d){
        double sigmoid = 1/(1+Math.pow (Math.E,-d) );
        return sigmoid;
    }

    private double sigDerivative(double d){
        double result = sig(d) * (1 - sig(d));
        return result;
    }
}
