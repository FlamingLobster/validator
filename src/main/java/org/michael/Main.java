package org.michael;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger logger = Logger.getLogger("main");

    public static void main(String[] args) {
        try (InputStream inputStream = Main.class.getResourceAsStream("/say.in");
             BufferedWriter writer = new BufferedWriter(new FileWriter("say.out"))) {
            validate(inputStream, writer, Password::from);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed when reading file", e);
        }
    }

    public static void validate(InputStream inputStream, BufferedWriter writer, Function<List<Character>, Validatable> validatableSupplier) throws IOException {
        int c;
        // I don't know too much about how to handle passwords in memory. I only know that we don't want it to go into
        // the String pool. Henceforth, password will be passed around as char[] or equivalent.
        // The array will be replaced with zeroes as soon as we are done with the password.
        // Ideally there's probably some Password library we can use off the shelf but I didn't look too hard.
        List<Character> word = new ArrayList<>();
        while ((c = Objects.requireNonNull(inputStream).read()) != -1) {
            char letter = (char) c;
            if (letter == '\n') {
                // don't log sensitive data, kinda pointless for this as single letters are valid inputs and the output
                // also prints out the entire password.
                logger.log(Level.INFO, String.format("processing word: %s", word.get(0) + "*****"));
                if (word.size() == 3 && word.get(0) == 'e' && word.get(1) == 'n' && word.get(2) == 'd') {
                    // I would not worry about this. The only cost is loading instructions when the branch predictor
                    // inevitably fails at the last entry of the processing. The cost is minimal.
                    // The cost is also predictable which could be a desirable property.
                    return;
                }
                Validatable processed = validatableSupplier.apply(word);
                logger.log(Level.INFO, processed.getReason());
                writer.write(processed.toOutput());
                writer.newLine();
                wipe(word);
                word = new ArrayList<>();
            } else {
                if (!Strings.isNullOrEmpty(CharMatcher.whitespace().removeFrom("" + letter))) {
                    word.add(letter);
                }
            }
        }

    }

    @SuppressWarnings("UnusedAssignment")
    private static void wipe(List<Character> word) {
        for (char _c : word) {
            _c = (char) (0);
        }
    }
}