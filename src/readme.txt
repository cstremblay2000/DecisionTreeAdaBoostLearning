Chris Tremblay
Professor Orfan
2205 CSCI 331 02
20 April 2021

Usage:
    - Train
        Usage: java Train <examples> <hypothesisOut> <learning-type>
    - Predict
        Usage: java Predict <hypothesis> <file>

Features:
    Most of my features were chosen by how language is structured for example
    pronouns, definite articles, etc...

    1. Dutch pronouns. There is a file './FeatureData/DutchPronouns.txt' that
       contains all of the dutch pronouns that this feature checks for. These
       are words like: me, you, we, him, etc...

    2. Definite articles. These are words like "the" If these are
       found in the line of data the feature is true

    3. Weird consecutive letters. When looking through the Dutch Wikipedia
       I found Dutch language had lots of letters next to each other like
       "jk," "jn," and other weird things like that.

    4. Double letters. The Dutch language also had a lot of double letters in
       words. English has a lot of double letters, but Dutch has more.
       Especially vowels.

    5. Indefinite articles. These are words like "a" or "an." If a word in the
       training data was found to be a Dutch Indefinite article this feature
       was true

    6. Common verbs like "to have" and "to be." The present tense conjugations
       of those verbs can be found in the './FeatureData/' folder with
       respective names. Since those are relatively common verbs in language
       I chose them.

Decision Tree:
    Decision Tree learning is when a set of training data is parsed using
    statistical techniques to determine which questions about what features
    are best to ask first.

    When testing I found there wasn't really too much variance in depth. If the
    depth was something really low, like 1,2,3 it obviously would classify
    incorrectly. Although there wasn't much of a difference if I limited the
    depth or just let it run until it stopped. When letting it run until it
    stopped, it got about 96% accuracy on the 10k_examples.dat file.

AdaBoost:
    AdaBoost is when you have a set of training data parsed into labeled
    examples with features, a label, and a now a weight. A weak learning
    algorithm is used to learn the WEIGHTED data. Everytime an iteration of
    AdaBoost happens, a new hypothesis is made and we check to see how well it
    learned the weighted examples. Then update the weights based on the error.
    This way of learning starts to prioritize the examples is thinks are better,
    and makes sure that hypothesises that don't classify very well don't
    contribute to the answer as much.

    When testing once I got past 6 or 7 iterations there was basically no
    improvement. I think one of features was significantly stronger than the
    others so the decision stumps started to only split on that one feature.

Other Notes:
    - Observations
        When testing I could not make AdaBoost out perform decision tree learning
        without doing the obvious things like limiting the decision tree to
        depth 1 or something like that

    - Code Design
        I actively tried to make this as modular as possible. I created an
        interface that defined a few functions that would process input so that
        I could write multiple classes that would let me read in different
        datasets. This was useful for using the XOR data, and hw4 data without
        drastically changing my code.

        I also did a similar thing with the Hypothesis interface. Both the
        decision tree and ensemble implement Hypothesis so that when the Train
        program is run it doesn't care what kind of class it is as long as
        it's a hypothesis.

        The Utils file also does most of the heavy lifting and calculations.