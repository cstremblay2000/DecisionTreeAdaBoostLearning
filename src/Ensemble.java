/*
 * @filename Ensemble.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/230231
 *
 * Description:
 *  This file contains an implementation of AdaBoost algorithm. Using
 * decision stumps as the weak learning algorithm.
 */

import java.io.Serializable;
import java.util.*;

/**
 * The Ensemble, stores integer list of weights and a list of other
 * Hypothesises so that the weight and value value of the hypothesis
 * can be queried after the ensemble is created
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/11/2021
 */
public class Ensemble implements Serializable, Hypothesis {

    /** The array of weights */
    private final double[] weights;

    /** The list of hypothesises */
    private final DecisionTree[] hypothesises;

    /** prevent divide by zero error */
    private static final double EPSILON = 0.00000001;

    public static HashMap<Integer, Integer> count = new HashMap<>();

    /**
     * Create a new Ensemble, private so that you can only the adaboostLearn
     * algorithm can create a new one
     *
     * @param hypothesises the array of hypothesises
     * @param weights the array associative array of weights
     */
    private Ensemble(DecisionTree[] hypothesises, double[] weights ){
        this.hypothesises = hypothesises;
        this.weights = weights;
    }

    /**
     * An implementation of the AdaBoost algorithm from Russel & Norvig
     *
     * @param examples The training examples
     * @param K how many iterations to do
     * @param attr the list of attributes
     * @return the Ensemble of hypothesises
     */
    public static Ensemble adaboostLearn(List<Element> examples, int K,
                                         List<Integer> attr){
        double[] w = new double[examples.size()];
        Arrays.fill(w, 1.0 / examples.size());
        DecisionTree[] h = new DecisionTree[K];
        double[] z = new double[K];
        Arrays.fill(z, 0);
        int N = examples.size();

        for(int k = 0; k < K; k++) {
            h[k] = stumpLearn(examples, attr, examples, w, 1);
            double error = 0;

            for (int j = 0; j < N; j++) {
                List<Boolean> features = examples.get(j).getFeatures();
                String expected = examples.get(j).getLabel();

                if (!h[k].ask(features).equals(expected))
                    error += w[j];
            }

            if(error != 0) {
                for (int j = 0; j < N; j++) {
                    List<Boolean> features = examples.get(j).getFeatures();
                    String expected = examples.get(j).getLabel();
                    if (h[k].ask(features).equals(expected)) {
                        w[j] = w[j] * (error / (1.0 - error));
                    }
                }
                Utils.normalize(w);
                z[k] = Math.log((1.0 - error)/error);
            } else {
                z[k] = Double.MAX_VALUE;
            }
        }
        return new Ensemble(h, z);
    }

    /**
     * A weak learning algorithm to make decision tree stumps
     *
     * @param examples the training data
     * @param attr the list of attributes
     * @param weights the list of weights
     * @return the weighted decision stump
     */
    public static DecisionTree stumpLearn(List<Element> examples,
                                          List<Integer> attr,
                                          List<Element> parentExamples,
                                          double[] weights,
                                          int depth) {
        // limit the depth
        if(depth == 0)
            return new Leaf(Utils.countMajority(examples, weights));

        // no examples left, return most abundant label of examples
        if(examples.isEmpty())
            return new Leaf(Utils.countMajority(parentExamples, weights));

        // if all examples are the same label, return that label
        if(Utils.sameClassification(examples))
            return new Leaf(examples.get(0).getLabel());

        // if there are no attributes to test, return most abundant label in
        // examples
        if(attr.isEmpty())
            return new Leaf(Utils.countMajority(examples, weights));

        // Get best attribute
        int mostImportant = Utils.mostImportant(examples, attr, weights);
        if(count.containsKey(mostImportant))
            count.put(mostImportant, count.get(mostImportant) + 1);
        else
            count.put(mostImportant, 1);

        DecisionTree tree = new DecisionTree(mostImportant);

        // calculate examples that go into new list
        List<Element> pExs = new ArrayList<>();
        List<Element> nExs = new ArrayList<>();
        List<Integer> newAttrs = new ArrayList<>();
        List<Integer> newAttrs1 = new ArrayList<>();

        // Create new list of attributes
        for(int i : attr){
            if(i == mostImportant)
                continue;
            newAttrs.add(i);
            newAttrs1.add(i);
        }

        // split elements based on attribute
        for(Element e : examples)
            if(e.getFeature(mostImportant))
                pExs.add(e);
            else
                nExs.add(e);

        // create subtrees
        tree.addSubtree(stumpLearn(nExs, newAttrs, parentExamples,
                weights, depth-1));
        tree.addSubtree(stumpLearn(pExs, newAttrs1, parentExamples,
                weights, depth-1));

        return tree;
    }

    /**
     * Ask the hypothesis a question
     *
     * @param x the list of features calculated from question
     * @return the predicted label
     */
    @Override
    public String ask(List<Boolean> x) {
        double guess = 0;
        String tempLabel;
        for(int i = 0; i < weights.length; i++){
            tempLabel = hypothesises[i].ask(x);
            if(tempLabel.equalsIgnoreCase(Utils.NL)){
                guess += weights[i];
            } else if(tempLabel.equalsIgnoreCase(Utils.EN)){
                guess -= weights[i];
            }
        }
        return threshold(guess);
    }

    /**
     * The threshold function that determines what the label is based
     * on the guess
     *
     * @param guess the sum of the guesses
     * @return the label associates with the weights
     */
    private String threshold(double guess){
        if(guess >= 0)
            return Utils.NL;
        else
            return Utils.EN;
    }

    /**
     * Make it look pretty
     *
     * @return the pretty string
     */
    @Override
    public String toString() {
        return "Ensemble{" +
                "weights=" + Arrays.toString(weights) +
                ", hypothesises=" + Arrays.toString(hypothesises) +
                '}';
    }
}
