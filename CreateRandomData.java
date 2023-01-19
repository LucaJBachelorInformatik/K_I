import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class CreateRandomData {

    public static void fillFileWithInput(String fileName, int amountOfData) throws IOException {
        createFile(fileName);
        FileWriter myWriter = new FileWriter(fileName);
        String currentLineOfInput = "";
        DecimalFormat df = new DecimalFormat("0.00");
        for(int i = 0; i<amountOfData;i++) {
            currentLineOfInput = "";
            double[] input = createInput();
            for(double d: input) {
                currentLineOfInput += df.format(d);
                currentLineOfInput += "\t";
            }
            myWriter.append(currentLineOfInput + "\n");
        }
        myWriter.close();
    }

    private static double[] createInput() {
        double param1 = Math.random();
        double param2 = Math.random();
        double result = Math.round(Math.random());

        double[] resultArray = new double[3];
        resultArray[0] = param1;
        resultArray[1] = param2;
        resultArray[2] = result;
        return resultArray;
    }

    private static void createFile(String fileName){
        try {
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                myObj.delete();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
