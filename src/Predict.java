/*
 * @filename Predict.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/2021
 *
 * Description:
 *  Reads in a file created from train and asks it a question and
 * returns the result
 */

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * The predict class unpacks the hypothesis and reads in the lines from the
 * file given on the command line the queries the hypothesis for each
 * label of the data
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/11/2021
 */
public class Predict {

    /** The usage message */
    private static final String USAGE = "Usage: java Predict <hypothesis> <file>";

    /**
     * The driver class. Extract hypothesis from file then query
     * data from it
     *
     * @param args the command line args
     */
    public static void main(String[] args) {
        // Check arguments are right
        if(args.length != 2){
            System.err.println(USAGE);
            System.exit(0);
        }

        // get the hypothesis and load it
        Hypothesis hypothesis = null;
        try(
                FileInputStream in = new FileInputStream(args[0]);
                ObjectInputStream ois = new ObjectInputStream(in)
        ){
            Object o = ois.readObject();
            if(o instanceof DecisionTree ) {
                hypothesis = (DecisionTree) o;
            }  else if(o instanceof Ensemble) {
                hypothesis = (Ensemble) o;
            } else {
                throw new Exception("Does not implement Hypothesis");
            }

        } catch (FileNotFoundException e) {
            System.err.printf("Predict: '%s' File not found\n", args[0]);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException ioException) {
            System.err.printf("Predict: '%s' IO exception\n", args[0]);
            ioException.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.printf("Predict: '%s' class not found exception", args[0]);
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Predict: General error, " +
                    "probably doesn't implement Hypothesis");
            e.printStackTrace();
            System.exit(1);
        }

        // Get list of example data from next file and ask the hypothesis
        String line;
        InputProcessor inputProcessor = new DutchInput();
        try(Scanner s = new Scanner(new File(args[1]))){
            while(s.hasNext()) {
                line = s.nextLine();
                List<Boolean> features = inputProcessor.getFeatures(line);
                String l = hypothesis.ask(features);
                System.out.println(l);
            }
        } catch (FileNotFoundException e) {
            System.err.printf("Predict: '%s' File not found\n", args[1]);
            e.printStackTrace();
        }
    }
}
