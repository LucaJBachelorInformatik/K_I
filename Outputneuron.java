import java.text.DecimalFormat;

public class Outputneuron {

    private static final DecimalFormat df = new DecimalFormat("#.00");
    private double in;
    private double out;
    private int delta;
    private double deltaTotal;

    public int calculateDelta(int actual){
        int expected = checkExpectation(getOut());
        delta = actual - expected;
        return delta;
    }
    public double calculateDeltaTotal(){
        double result = sigDerivative(in) * (delta-out);
        deltaTotal = result;
        return deltaTotal;
    }
    public double getIn(){
        return in;
    }

    public void addIn(double d) {
        in += d;
    }
    public double calculateOut(){
        out = sig(in);
        return out;
    }
    private double sig(double d){
        double sigmoid = 1/(1+Math.pow (Math.E,-d) );
        return sigmoid;
    }
    private double sigDerivative(double d){
        double result = sig(d) * (1 - sig(d));
        return result;
    }
    public double getOut(){
        return sig(in);
    }

    public int checkExpectation(double d){
        if(d >= 0.5) {
            return 1;
        } else {
            return 0;
        }
    }
    public double getDeltaTotal(){
        return deltaTotal;
    }
    public void setIn(double d){
        in = d;
    }
}
