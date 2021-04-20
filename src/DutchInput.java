/*
 * @filename DutchInput.java
 * @author Chris Tremblay (cst1465)
 * @date 4/11/2021
 *
 * Description:
 *  Parses lines from the example data and creates elements that the
 * algorithms can work with. Finds various features that are used to
 * tell dutch and english apart
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The format for the line of example data should be in the form of
 * "[en|ne] | [a-zA-Z0-1,.?!' ]*"
 * ex
 * en | hello word, this is an example file
 *
 * @author Chris Tremblay (cst1465)
 * @version 4/19/2021
 */
public class DutchInput implements InputProcessor {

    /** Set that contains dutch pronouns */
    private Set<String> pronouns;

    /** Set that contains dutch to be conjugation */
    private Set<String> toBe;

    /** Set that contains dutch to have conjugation */
    private Set<String> toHave;

    /** File path to file containing dutch pronouns */
    private static final String PRONOUN_PATH =
            "src/FeatureData/DutchPronouns.txt";

    /** File path to very to be file */
    private static final String TO_BE_PATH = "src/FeatureData/DutchToBe.txt";

    /** File path to very to have file */
    private static final String TO_HAVE_PATH =
            "src/FeatureData/DutchToHave.txt";

    /** The in Dutch */
    private static final String DE = "de";

    /** The in Dutch */
    private static final String HET = "het";

    /** use to check weird consecutive letters */
    private static final String VR = "vr";

    /** use to check weird consecutive letters */
    private static final String JN = "jn";

    /** use to check weird consecutive letters */
    private static final String JK = "jk";

    /** use to check weird consecutive letters */
    private static final String JF = "jf";

    /** use to check double letters */
    private static final String AA = "aa";

    /** use to check double letters */
    private static final String OO = "oo";

    /** use to check double letters */
    private static final String EE = "ee";

    /** use to check double letters */
    private static final String GG  = "gg";

    /** use to check double letters */
    private static final String NN = "nn";

    /** use to check double letters */
    private static final String LL = "ll";

    /** use to check double letters */
    private static final String KK = "kk";

    /** Indefinite article check */
    private static final String EEN = "een";

    /**
     * Create an input processor for the language files
     * in the format of
     *
     * " [en|nl] | [a-zA-Z0-1]*"
     */
    public DutchInput(){
        loadPronouns();
        loadVerbs();
    }

    /**
     * Load the verbs into sets
     */
    private void loadVerbs(){
        this.toBe = new HashSet<>();
        this.toHave = new HashSet<>();
        try(
                Scanner toBeScanner = new Scanner(new File(TO_BE_PATH));
                Scanner toHaveScanner = new Scanner(new File(TO_HAVE_PATH))
                ){
            while(toBeScanner.hasNext())
                toBe.add(toBeScanner.nextLine());
            while(toHaveScanner.hasNext())
                toHave.add(toHaveScanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Get the pronouns from the pronouns file
     */
    private void loadPronouns(){
        this.pronouns = new HashSet<>();
        try(Scanner s = new Scanner(new File(PRONOUN_PATH))){
            while(s.hasNext())
                this.pronouns.add(s.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Get the boolean values for defined features for a given string of data
     * Check if the language is dutch by testing a bunch of features as
     * shown in the functions below
     *
     * @param data the string of data to process
     * @return the list of features and their values
     * @precondition the line should be in all lowercase
     */
    @Override
    public Element createElement(String data) {
        // put to lowercase
        data = data.toLowerCase();

        // separate label from words
        String[] split = data.split("\\|");
        String label = split[0];
        String words = split[1];

        // get features
        List<Boolean> features = getFeatures(words);

        // new element
        return new Element(features, label);
    }

    /**
     * Returns a list of boolean features from the given input
     *
     * @param line the line to get features from
     * @return the feature vector
     */
    public List<Boolean> getFeatures(String line){
        String[] lineSplit = line.split(" ");
        // calculate attributes
        boolean hasPronouns = containsDutchPronouns(line);
        boolean hasDefiniteArticles = containsDutchDefiniteArticles(lineSplit);
        boolean hasWeirdLetters = weirdConsecutiveLetters(line);
        boolean hasDoubleLetters = doubleLetters(line);
        boolean hasIndefiniteArticle = dutchIndefiniteArticle(lineSplit);
        boolean hasToBe = dutchToBe(lineSplit);
        boolean hasToHave = dutchToHave(lineSplit);

        // make list of attributes
        List<Boolean> features = new ArrayList<>();
        features.add(hasPronouns);
        features.add(hasDefiniteArticles);
        features.add(hasWeirdLetters);
        features.add(hasDoubleLetters);
        features.add(hasIndefiniteArticle);
        features.add(hasToBe);
        features.add(hasToHave);

        return features;
    }

    /**
     * Check to see if the words contain the dutch conjugation of the
     * verb to be
     *
     * @param line the example data
     * @return true if contained, false if not
     */
    private boolean dutchToBe(String[] line){
        for(String word : line)
            if(toBe.contains(word))
                return true;
        return false;
    }

    /**
     * Check to see if the words contain the dutch conjugation of the
     * verb to have
     *
     * @param line the example data
     * @return true if contained, false if not
     */
    private boolean dutchToHave(String[] line){
        for(String word : line )
            if(toHave.contains(word))
                return true;
        return false;
    }

    /**
     * Check to see if the indefinite article 'een' is contained
     *
     * @param line the example data
     * @return true if contained, false if not
     */
    private boolean dutchIndefiniteArticle(String[] line){
        for(String word : line)
            if(word.equals(EEN))
                return true;
        return false;
    }

    /**
     * Check to see if there are any double vowels contained within the work
     * this is a weaker check, although a lot of dutch words contain double
     * vowels. Especially 'aa'
     *
     * @param line the example data
     * @return true if it contains double vowels, false if not
     */
    private boolean doubleLetters(String line){
        return line.contains(AA) || line.contains(EE) || line.contains(OO)
                || line.contains(LL) || line.contains(KK) ||line.contains(NN)
                || line.contains(GG);
    }

    /**
     * Check to see if there are weird letters next to each other like
     * "vr", "jn", "jk", or "jf"
     *
     * @param line the line of example data
     * @return true if contained, false if not
     */
    private boolean weirdConsecutiveLetters(String line){
        return line.contains(VR)
                || line.contains(JN)
                || line.contains(JK)
                || line.contains(JF);
    }

    /**
     * Check to see if the dutch definite articles "de" and "het" are contained
     * within the line from the example data
     *
     * @param line the example data
     * @return true if contained, false if not
     */
    private boolean containsDutchDefiniteArticles(String[] line){
        for(String word : line)
            if(word.equals(HET))
                return true;
            else if(word.equals(DE))
                return true;
        return false;
    }

    /**
     * Check to see if the line contains dutch pronouns
     *
     * @param line the line of text to check for pronouns
     * @return true if contains dutch pronouns, false if not
     */
    private boolean containsDutchPronouns(String line){
        boolean hasPronouns = false;
        for(String pronoun : this.pronouns)
            hasPronouns = hasPronouns || line.contains(pronoun);
        return hasPronouns;
    }
}
