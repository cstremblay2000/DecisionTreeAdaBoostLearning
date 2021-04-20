/*
 * @filename Hypothesis.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/2021
 *
 * Description:
 *  A functional interface that defines the ask() method
 * so that the decision tree and adaboost ensemble can play nice
 * when asking them a each a question
 */

import java.util.List;

/**
 * The Hypothesis Interface
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/11/2021
 */
public interface Hypothesis {

    /**
     * Ask the hypothesis a question
     *
     * @param x the list of features calculated from question
     * @return the predicted label
     */
    String ask(List<Boolean> x);
}
