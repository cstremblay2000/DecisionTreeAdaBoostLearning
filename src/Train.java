/*
 * @filename Train.java
 * @author Chris Tremblay (cst1465)
 * @date 03/30/2021, National Turkey Neck Soup Day!
 *
 * This file learns a decision tree based on creating a
 * decision tree or the adaboost algorithm
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Chris Tremblay (cst1465)
 * @version 1.0
 *
 * This is the class that actually learns the data set.
 * It parses the input data and serializes and object
 * that can then be asked questions
 */
public class Train {

    /** The usage message */
    private static final String USAGE = "Usage: java Train <examples> <hypothesisOut> <learning-type>";

    /** How many hypothesis to make */
    private static final int K = 1200;

    /**
     * The driver function.
     * Given an example file, with lines formatted below
     *  '[nl|en]|[\w*]' ex en|hello world, my name is Chris
     * then create a hypothesis and serialize it to the output file.
     * Specify the learning algorithm with the learning type either
     *  'dt' or 'ada'
     * @param args the command line args
     */
    public static void main(String[] args) {

        // make sure correct number of command line args were given
        if(args.length != 3){
            System.err.println(USAGE);
            System.exit(1);
        }

        // get and open examples file
        System.out.printf("Opening file '%s'...", args[0]);
        Scanner examplesScanner = null;
        try{
            examplesScanner = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            error(e, args[0]);
            System.exit(1);
        }
        System.out.println("opened");

        // get learning type
        boolean decisionTree;
        if(args[2].equalsIgnoreCase("dt"))
            decisionTree = true;
        else if(args[2].equalsIgnoreCase("ada"))
            decisionTree = false;
        else {
            decisionTree = false;
            error(args[2]);
            System.exit(1);
        }
        System.out.printf("Using %s learning\n", decisionTree?" Decision Tree":
                "AdaBoost");

        // Get input from examples file and put it into an array to pass
        // to the learning algorithm
        System.out.print("Parsing training examples...");
        List<Element> examples = getExamples(examplesScanner);
        List<Integer> attr = new ArrayList<>();
        for(int i = 0; i < examples.get(0).getFeatures().size(); i++)
            attr.add(i);
        System.out.println("done");

        System.out.print("Learning training examples...");
        Hypothesis hypothesis;
        if(decisionTree) {
            hypothesis = DecisionTree.decisionTreeLearn(examples, attr, examples, -1);
        } else {
            hypothesis = Ensemble.adaboostLearn(examples, K, attr);
        }
        System.out.println("done");

        System.out.printf("Writing out to '%s'...", args[1]);
        serializeOut(hypothesis, args[1]);
        System.out.println("done");
    }

    /**
     * Serialize an object to the output file given from the command line
     *
     * @param o the object to serialize
     * @param file the output file path
     */
    private static void serializeOut(Object o, String file){
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fOut);
            oos.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Create a List of the contents of the example file to pass to the
     * decision tree
     *
     * @param s the scanner to the input file
     * @return the contents of the example file put into an array by line
     */
    private static List<Element> getExamples(Scanner s){
        List<Element> examples = new ArrayList<>();
        String line;
        InputProcessor inputProcessor = new DutchInput();
        while(s.hasNext()) {
            line = s.nextLine();
            examples.add(inputProcessor.createElement(line));
        }
        return examples;
    }

    /**
     * Print the file not found error to standard error and exit
     * @param f the FileNotFoundException
     * @param a the file path that could not be found
     */
    private static void error(FileNotFoundException f, String a){
        f.printStackTrace(System.err);
        System.err.printf("Train: File not Found Exception '%s'\n", a);
        System.err.println(USAGE);
    }

    /**
     * Print the an error message to standard error and exit
     * @param a the invalid learning type
     */
    private static void error(String a){
        System.err.printf("Train: Invalid learning type '%s', not 'dt' " +
                "or 'ada'\n", a);
        System.err.println(USAGE);
    }
}
