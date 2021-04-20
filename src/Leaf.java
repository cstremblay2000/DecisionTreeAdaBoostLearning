/*
 * @filename Leaf.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/2021
 *
 * Description:
 *  This file contains an implementation for a leaf node for the
 * DecisionTree.java class. It stops the recursive ask search and returns the
 * value held within the node.
 */

import java.io.Serializable;
import java.util.List;

/**
 * A leaf node. Contains the answer to the question asked to it trained
 * by example data.
 *
 * @author Chris Tremblay (Cst1465)
 * @version 4/11/2021
 */
public class Leaf extends DecisionTree implements Serializable, Hypothesis{

    /** The label that was decided is the answer */
    private final String label;

    /**
     * Create a leaf node
     *
     * @param label the label of the data
     */
    public Leaf(String label){
        this.label = label;
    }

    /**
     * Ask what the leaf node holds for a value
     *
     * @param x the list of attributes
     * @return the label of the leaf node
     */
    @Override
    public String ask(List<Boolean> x){
        return label;
    }

    /**
     * Pretty print the leaf node
     *
     * @return the nicely formatted string
     */
    @Override
    public String toString() {
        return "Leaf{" +
                "label='" + label + '\'' +
                '}';
    }
}
