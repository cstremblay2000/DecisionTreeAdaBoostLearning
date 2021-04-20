/*
 * @filename InputProcessor.java
 * @author Chris Tremblay (cst1465)
 * @date 03/30/2021, National Turkey Neck Soup Day!
 *
 * This file define a functional interface for
 * dealing with data
 */

import java.util.List;

/**
 * This interface allows for different types of data
 * to be processed without screwing up the algorithm. The user must define
 * the features and how they are processed from the data
 *
 * @author Chris Tremblay (cst1465)
 * @version 1.0
 */
public interface InputProcessor {

    /**
     * Get the boolean values for defined features for a given string of data
     * @param data the string of data to process
     * @return the list of features and their values
     */
    Element createElement(String data);

    /**
     * Gets the features from line of data
     *
     * @param data the line of data to search for features in
     * @return the list of boolean features
     */
    List<Boolean> getFeatures(String data);
}
