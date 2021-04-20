/*
 * @filename Utils.java
 * @author Chris Tremblay (cst1465)
 * @date 4/12/2021
 *
 * Description:
 *  This file contains a bunch of utility function for the machine learning
 * algorithm. This was designed to de-clutter the DecisionTree.java
 * and Ensemble.java files
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The library of helper functions for the machine learning algorithms
 * Contains entropy, and other useful things that are just brushed over
 * in the algorithm in the text book
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/11/2021
 */
public abstract class Utils {

    /** this is the label we are maximizing */
    public static final String NL = "nl";

    /** The other label */
    public static final String EN = "en";

    /**
     * Counts the examples and finds which label has the majority number
     *
     * @param examples the list of examples to check
     * @return the label most present in the list of examples
     */
    public static String countMajority(List<Element> examples){
        HashMap<String, Integer> counts = new HashMap<>();
        String label = null;
        int maxValue = Integer.MIN_VALUE;
        for(Element e : examples) {
            if(!counts.containsKey(e.getLabel()))
                counts.put(e.getLabel(), 1);
            else
                counts.put(e.getLabel(), counts.get(e.getLabel()) + 1);
            if(counts.get(e.getLabel()) > maxValue){
                label = e.getLabel();
                maxValue = counts.get(e.getLabel());
            }
        }
        return label;
    }

    /**
     * Figures out which label in the set of examples has the highest weight
     * and returns it. Examples and weight are associative arrays
     *
     * @param examples the list of training data
     * @param weights the associated array of weights
     * @return the label with the highest weight
     */
    public static String countMajority(List<Element> examples,
                                               double[] weights ){

        HashMap<String, Double> counts = new HashMap<>();
        String label = null;
        Element e;
        double maxWeight = Double.MIN_VALUE;
        for(int i = 0; i < examples.size(); i++) {
            e = examples.get(i);
            if(!counts.containsKey(e.getLabel()))
                counts.put(e.getLabel(), weights[i]);
            else
                counts.put(e.getLabel(), counts.get(e.getLabel()) + weights[i]);
            if(counts.get(e.getLabel()) > maxWeight){
                label = e.getLabel();
                maxWeight = counts.get(e.getLabel());
            }
        }
        return label;
    }

    /**
     * Check to see if the examples are all the same classification.
     * Check to see if they all have the same label
     *
     * @param examples the list of examples to check
     * @return true if all labels are the same, false if not
     */
    public static boolean sameClassification(List<Element> examples){
        if(examples.size() == 0 )
            return false;
        String label = examples.get(0).getLabel();
        for(int i = 1; i < examples.size(); i++){
            if(!examples.get(i).getLabel().equals(label))
                return false;
        }
        return true;
    }

    /**
     * Calculate the most important attribute based the given examples
     *
     * @param examples the list of examples to check
     * @param attr the attributes available to check
     * @return the most important attribute
     */
    public static int mostImportant(List<Element> examples,List<Integer> attr){
        int championAttr = -1;
        double championRemainder = Double.MAX_VALUE, rem;
        for( int i : attr ){
            rem = remainder(examples, i);
            if( rem < championRemainder ){
                championAttr = i;
                championRemainder = rem;
            }
        }

        return championAttr;
    }

    /**
     * Figures out the most important attribute based on the weights of the
     * examples at one time.
     *
     * @param examples the training data
     * @param attr the attributes available to test
     * @param weights the weights associated with the examples
     * @return the most important attribute
     */
    public static int mostImportant(List<Element> examples, List<Integer> attr,
                                    double[] weights ){
        int championAttr = -1;
        double championRemainder = Double.MAX_VALUE;
        for( int a : attr ){
            double rem = remainder(examples, a, weights);
            if( rem < championRemainder ){
                championAttr = a;
                championRemainder = rem;
            }
        }

        return championAttr;
    }

    /**
     * Normalizes the array of weights by summing them all up
     * and diving by the total
     *
     * @param weights the array of weights to normalize
     */
    public static void normalize(double[] weights){
        double total = 0;
        for(double d : weights)
            total += d;
        for(int i = 0; i < weights.length; i++)
            weights[i] /= total;
    }

    /**
     * Calculates the remainder for an attribute given example data
     *
     * @param examples the training example
     * @param attr the attribute to calculate
     * @return the remainder
     */
    private static double remainder(List<Element> examples, int attr){
        List<Element> trueSubset = new ArrayList<>();
        List<Element> falseSubset = new ArrayList<>();
        double positives = 0, remainder = 0, frac;

        // split into examples where feature is true
        for(Element e : examples){
            if(e.getFeature(attr))
                trueSubset.add(e);
            else
                falseSubset.add(e);
        }

        // find remainder of true features
        if(!trueSubset.isEmpty()) {
            for (Element e : trueSubset)
                if (e.getLabel().equals(NL))
                    positives++;
            frac = (double) trueSubset.size() / examples.size();
            remainder += frac * B(positives / trueSubset.size());
        }

        // find remainder of false features
        if(!falseSubset.isEmpty()) {
            positives = 0;
            for (Element e : falseSubset)
                if (e.getLabel().equals(NL))
                    positives++;
            frac = (double) falseSubset.size() / examples.size();
            remainder += frac * B(positives / falseSubset.size());
        }

        return remainder;
    }

    /**
     * Calculates the remainder of a set of weighted training examples
     *
     * @param examples the training examples
     * @param attr the attribute to split on
     * @param weights teh associative array of weights with examples
     * @return the remainder of weighted training attributes on given feature
     */
    private static double remainder(List<Element> examples, int attr,
                                        double[] weights){
        List<Integer> trueSubset = new ArrayList<>();
        List<Integer> falseSubset = new ArrayList<>();
        double totalSize = 0, remainder = 0, positives = 0, frac,
                subTotalSize = 0;

        for(int i = 0; i < examples.size(); i++){
            if(examples.get(i).getFeature(attr))
                trueSubset.add(i);
            else
                falseSubset.add(i);
            totalSize += weights[i];
        }

        if(!trueSubset.isEmpty()){
            for(int i : trueSubset) {
                if (examples.get(i).getLabel().equals(NL))
                    positives += weights[i];
                subTotalSize += weights[i];
            }
            frac = subTotalSize/totalSize;
            remainder += frac * B(positives/subTotalSize);
        }

        if(!falseSubset.isEmpty()){
            positives = 0;
            subTotalSize = 0;
            for(int i : falseSubset) {
                if (examples.get(i).getLabel().equals(NL))
                    positives += weights[i];
                subTotalSize += weights[i];
            }
            frac = subTotalSize/totalSize;
            remainder += frac * B(positives/subTotalSize);
        }
        return remainder;
    }

    /**
     * Implementation of boolean entropy function
     *
     * @param q the number to calculate entropy from
     * @return the entropy of q
     */
    private static double B( double q ){
        if(q == 0)
            return 0;
        if(q==1)
            return 0;
        return -(q*log2(q) + (1-q)*log2(1-q));
    }

    /**
     * Calculate log base 2 of a given number
     *
     * @param number the argument
     * @return log2( number )
     */
    private static double log2(double number) {
        if(number == 0 )
            return Double.MIN_VALUE;
        return Math.log(number) / Math.log(2);
    }
}
