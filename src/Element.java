/*
 * @filename Element.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/2021
 *
 * Description:
 *  This file contains an object representation of an element
 * that has features and a label. These are used to package up the training
 * data nicely into something the algorithm can use
 */

import java.io.Serializable;
import java.util.List;

/**
 * The Element. It has a boolean list of features that are pre created and by
 * the user in a different class, and the same with the label
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/11/2021
 */
public class Element implements Serializable {

    /** The list of boolean features */
    private final List<Boolean> features;

    /** The label for the data */
    private final String label;

    /**
     * Create a new element
     *
     * @param features the list of features
     * @param label the element
     */
    public Element(List<Boolean> features, String label){
        this.features = features;
        this.label = label;
    }

    /**
     * Get a feature[i] from the feature list
     *
     * @param i the index to get
     * @return the feature at index i
     */
    public boolean getFeature(int i){
        return features.get(i);
    }

    /**
     * Returns the whole list of features
     *
     * @return the whole list of features
     */
    public List<Boolean> getFeatures(){
        return this.features;
    }

    /**
     * Gets the label
     * @return the label
     */
    public String getLabel(){
        return label;
    }

    @Override
    public String toString() {
        return "Element{" +
                "features=" + features +
                ", label='" + label + '\'' +
                '}';
    }
}
