/*
 * @filename DecisionTree.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/2021
 *
 * Description:
 *  This class contains an implementation of Russel&Norvig
 * decision tree learning algorithm. The algorithm offers
 * depth restricted and non depth restricted decision tree generation
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Decision tree. It has a static method that learns a set of given examples
 * then creates and returns a tree based on the training examples
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/11/2021
 */
public class DecisionTree implements Serializable, Hypothesis {

    /** The list of subtrees */
    private final List<DecisionTree> subtrees;

    /** The question this tree checks first */
    private final int label;

    /**
     * Create a new DecisionTree. Make it private so that only
     * the static method DecisionTree.decisionTreeLearn() can create them
     *
     * @param label The label the tree has
     */
    public DecisionTree(int label){
        this.label = label;
        this.subtrees = new ArrayList<>();
    }

    /**
     * Exported constructor
     */
    public DecisionTree(){
        this.subtrees = null;
        this.label = -1;
    }

    /**
     * Implementation of Decision tree learning from Russel and Norvig
     *
     * @param examples list of training examples
     * @param attr list of attributes (just list of numbers)
     * @param parentExamples the examples from the iteration above it
     * @param depth how deep we want the tree to go
     * @return the decision tree of specified depth that learns the examples
     */
    public static DecisionTree decisionTreeLearn(List<Element> examples,
                                         List<Integer> attr,
                                                 List<Element> parentExamples,
                                                    int depth){
        if(depth == 0)
            return new Leaf(Utils.countMajority(examples));
        if(examples.isEmpty())
            return new Leaf(Utils.countMajority(parentExamples));
        if(Utils.sameClassification(examples))
            return new Leaf(examples.get(0).getLabel());
        if(attr.isEmpty())
            return new Leaf(Utils.countMajority(examples));

        int mostImportant = Utils.mostImportant(examples, attr);
        DecisionTree tree = new DecisionTree(mostImportant);

        // calculate examples that go into new list
        List<Element> pExs = new ArrayList<>();
        List<Element> nExs = new ArrayList<>();
        List<Integer> newAttrs = new ArrayList<>();
        List<Integer> newAttrs1 = new ArrayList<>();

        for(int i : attr){
            if(i == mostImportant)
                continue;
            newAttrs.add(i);
            newAttrs1.add(i);
        }

        for(Element e : examples)
            if(e.getFeature(mostImportant))
                pExs.add(e);
            else
                nExs.add(e);

        tree.addSubtree(decisionTreeLearn(nExs, newAttrs, examples, depth-1));
        tree.addSubtree(decisionTreeLearn(pExs, newAttrs1, examples, depth-1));

        return tree;
    }

    /**
     * ask the hypothesis a question
     *
     * @param x the list of features calculated from question
     * @return the label that best fits the features
     */
    @Override
    public String ask(List<Boolean> x){
        boolean val = x.get(label);
        if(val)
            return subtrees.get(1).ask(x);
        else
            return subtrees.get(0).ask(x);
    }

    /**
     * Add a subtree to the list of subtrees
     *
     * @param tree the subtree to be added
     */
    public void addSubtree(DecisionTree tree){
        subtrees.add(tree);
    }

    /**
     * Pretty print the object
     *
     * @return the nicely formatted object
     */
    @Override
    public String toString() {
        return "Tree{" +
                "subtrees=" + subtrees +
                ", label=" + label +
                '}';
    }
}
