# Teachable Companion: Algorithm Demos

This repo contains several algorithms for the Teachable Companion app.

Teachable Companion aims to facilitate teaching math skills by providing an interactive agent that can be "taught" by
students.

## Algorithms

The algorithm AI is supposed to emulate a learning student when interacted with, so we are looking for the following
properties:

- Continuously improving
    - Generally, the more examples you give it, the better should AI be
- Smooth learning curve
    - Just as a human, AI should *not* jump from 10% accuracy to 90%
- Full coverage of the reasoning tree
    - AI must require specific instructions for all branches of the reasoning tree
      (a.k.a. corner cases). That will ensure that students master the skill fully while interacting with the AI.
- Anthropomorphism
    - We prefer that AI falls into the same reasoning pitfalls and makes same mistakes as real students do. That should
      help students understand their blindspots while interacting with the AI.

### Bayesian Modeling

To imitate how a human would learn to perform a mathematical operation we used a Bayesian Model which, given enough
examples, learns to choose the correct predictive function (a.k.a. hypothesis) from a range of false hypotheses.

### Sum of Integers

Given 2 integers `x` and `y` predict their sum.

[MaxXYIntSumLearner.kt](src/main/kotlin/integer/sum/MaxXYIntSumLearner.kt)

### Sum of Fractions

Given 2 fractions `num1/dim1` and `num2/dim2` predict their sum `num/dim`.

[FracSumLearner.kt](src/main/kotlin/fraction/sum/FracSumLearner.kt)

## Building and Running

> ./gradlew build test