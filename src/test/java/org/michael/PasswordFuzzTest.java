package org.michael;

import com.pholser.junit.quickcheck.From;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(JQF.class)
public class PasswordFuzzTest {
    // I'm not going to fuzz test this. Fuzz testing is more beneficial in a few circumstances:
    //  1. No access to source code, i.e. black box testing
    //  2. Code is too complex to trace, i.e. complex beyond human know-ability

    // Neither of these apply to the Password class. The first choice for testing is always proof by exhaustion.
    // See PasswordTest for existing tests. The tests were written specifically for the code that was written, not in
    // a couple-to-the-implementation sense but in a covers-all-scenarios sense.

    // Suppose we were to fuzz test this. The inputs can be broken into a few buckets, we'll pick one representative
    // from each bucket and just test each bucket once:
    //  * Rule 1: no vowels
    //  * Rule 2: no consecutive vowels
    //  * Rule 3: no consecutive consonants
    //  * Rule 4: no repeated letters
    //  * Rule 4.a: no repeated letters except 'e'
    //  * Rule 4.b: no repeated letters except 'o'
    // These rules can be further broken down, for example
    //  * Rule 2.a: no consecutive vowels at beginning
    //  * Rule 2.b: no consecutive vowels in the middle
    //  * ...
    // But such a breakdown is unnecessary because we have access to the source and can see that it's unnecessary.

    // Of course, we must be judicious when applying the above strategy
    // and be careful to not make any reasoning errors that cannot be absorbed by the error budget.

    /**
     * That being said, this is an example of how fuzz testing will have to be done for this problem. Due to the sheer
     * size of the input space (length of 20 and 26 letters to choose from). It's not possible to exhaustively test
     * the entire input space. Also, due to the dynamic nature of the expected value; accepted or not accepted, it's
     * easier again,to separate the inputs into buckets and fuzz each bucket individually. This test will test the consecutive
     * vowel rule.
     * @param characters randomly generated input
     */
    @Fuzz
    public void fuzzPassword(@From(ConsecutiveVowelGenerator.class) List<Character> characters) {
        StringBuilder word = new StringBuilder();
        for (Character c : characters) {
            word.append(c);
        }
        String expectedOutput = String.format("<%s> is not acceptable.", word);
        String expectedReason = "Rule 2 broken. Consecutive vowel found at";
        //System.out.println(expectedOutput);
        Validatable actual = Password.from(characters);
        assertEquals(expectedOutput, actual.toOutput());
        //System.out.println(actual.getReason());
        assertTrue(actual.getReason().contains(expectedReason));
    }
}