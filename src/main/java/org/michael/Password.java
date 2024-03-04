package org.michael;

import com.google.common.primitives.Chars;

import java.util.List;
import java.util.Set;

public class Password implements Validatable {
    private final static String NOT_ACCEPTED = "<%s> is not acceptable.";
    private final static String ACCEPTED = "<%s> is acceptable.";
    // !This assumes that anything not a vowel is a consonant.
    private final static Set<Character> VOWELS = Set.of('a', 'e', 'i', 'o', 'u');
    private final ValidationResult validationResult;
    private final String output;

    public Password(char[] password) {
        this.validationResult = new ValidationResult();
        this.validate(password);
        this.output = validationResult.isValid ? String.format(ACCEPTED, Chars.join("", password)) : String.format(NOT_ACCEPTED, Chars.join("", password));
    }

    @Override
    public String toOutput() {
        return this.output;
    }

    @Override
    public String getReason() {
        return this.validationResult.isValid ? "Accepted" : this.validationResult.reason;
    }

    private void validate(char[] password) {
        // Must contain at least one vowel.
        boolean hasVowel = false;
        // Cannot contain three consecutive vowels or three consecutive consonants.
        int consecutiveVowelCount = 0;
        int consecutiveConsonantCount = 0;
        // Cannot contain two consecutive occurrences of the same letter, except for ' ee ' or ' oo '.
        char previousLetter = '0';

        // single pass to validate, probably good enough
        for(char letter : password){
            if ((letter != 'e' && letter != 'o') && previousLetter == letter) {
                this.validationResult.fail(String.format("Rule 3 broken. Repeated letter at %s", letter));
                return;
            }
            previousLetter = letter;

            if (VOWELS.contains(letter)) {
                hasVowel = true;
                consecutiveVowelCount++;
                consecutiveConsonantCount = 0;
                if (consecutiveVowelCount > 2) {
                    this.validationResult.fail(String.format("Rule 2 broken. Consecutive vowel found at %s", letter));
                    return;
                }
            } else {
                consecutiveConsonantCount++;
                consecutiveVowelCount = 0;
                if (consecutiveConsonantCount > 2) {
                    this.validationResult.fail(String.format("Rule 2 broken. Consecutive consonant found at %s", letter));
                    return;
                }
            }
        }
        if (!hasVowel) {
            this.validationResult.fail("Rule 1 broken. No vowels.");
            return;
        }

        this.validationResult.accept();
    }

    /**
     * helper function for quick instantiation
     */
    public static Validatable from(List<Character> word) {
        char[] charArray = new char[word.size()];
        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = word.get(i);
        }
        return new Password(charArray);
    }

    /**
     * helper function for quick instantiation
     */
    public static Validatable from(char[] word) {
        return new Password(word);
    }

    /**
     * Packages together results
     */
    private static class ValidationResult {
        private String reason;
        private boolean isValid = false;

        private void fail(String reason) {
            this.isValid = false;
            this.reason = reason;
        }

        private void accept() {
            this.isValid = true;
        }
    }
}
