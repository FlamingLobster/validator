package org.michael;

import com.google.common.collect.Streams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    // from the problem document
    private static final String[] in = {"a",
            "tv",
            "ptoui",
            "bontres",
            "zoggax",
            "wiinq",
            "eep",
            "houctuh",
            "ei",
            "cd",
            "bcdfghjklmnpqrstvwxy",
            "ee",
            "oo",
            "jj",
            "aei",
            "vwx",
            "to",
            "in",
            "try",
            "ask",
            "bot",
            "abcodefgohijkolmonop",
            "jkloxoxoxoxoxoxoxoxo",
            "mamamamajklamamamama",
            "egegegegegegegegejkl",
            "aeiququququququququq",
            "orororaeirororororor",
            "zezezezezezezezezoua",
            "eeprop",
            "peerop",
            "propee",
            "ooplant",
            "poolant",
            "plantoo",
            "ssuper",
            "supper",
            "superr",
            "aarmada",
            "armaada",
            "armadaa",
            "banana",
            "rhythm",
            "breakneck"};

    // from the problem document

    private static final String[] out = {"<a> is acceptable.",
            "<tv> is not acceptable.",
            "<ptoui> is not acceptable.",
            "<bontres> is not acceptable.",
            "<zoggax> is not acceptable.",
            "<wiinq> is not acceptable.",
            "<eep> is acceptable.",
            "<houctuh> is acceptable.",
            "<ei> is acceptable.",
            "<cd> is not acceptable.",
            "<bcdfghjklmnpqrstvwxy> is not acceptable.",
            "<ee> is acceptable.",
            "<oo> is acceptable.",
            "<jj> is not acceptable.",
            "<aei> is not acceptable.",
            "<vwx> is not acceptable.",
            "<to> is acceptable.",
            "<in> is acceptable.",
            "<try> is not acceptable.",
            "<ask> is acceptable.",
            "<bot> is acceptable.",
            "<abcodefgohijkolmonop> is acceptable.",
            "<jkloxoxoxoxoxoxoxoxo> is not acceptable.",
            "<mamamamajklamamamama> is not acceptable.",
            "<egegegegegegegegejkl> is not acceptable.",
            "<aeiququququququququq> is not acceptable.",
            "<orororaeirororororor> is not acceptable.",
            "<zezezezezezezezezoua> is not acceptable.",
            "<eeprop> is acceptable.",
            "<peerop> is acceptable.",
            "<propee> is acceptable.",
            "<ooplant> is acceptable.",
            "<poolant> is acceptable.",
            "<plantoo> is acceptable.",
            "<ssuper> is not acceptable.",
            "<supper> is not acceptable.",
            "<superr> is not acceptable.",
            "<aarmada> is not acceptable.",
            "<armaada> is not acceptable.",
            "<armadaa> is not acceptable.",
            "<banana> is acceptable.",
            "<rhythm> is not acceptable.",
            "<breakneck> is acceptable."};
    // -----------------------------------------------------------------------------------------------------------------
    // Add entries here for easy manual testing
    private static final List<TestInput> additionalExamples = List.of(
            new TestInput("a", true),
            new TestInput("zoggax", false)
    );
    //------------------------------------------------------------------------------------------------------------------

    private record TestInput(String input, boolean shouldAccept) {
        public Arguments toArgument() {
            Object[] argument = {this.input.toCharArray(), this.shouldAccept ? String.format("<%s> is acceptable.", this.input) : String.format("<%s> is not acceptable.", this.input)};
            return Arguments.of(argument);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Stream<Arguments> passwords() {
        return Stream.concat(Streams.zip(
                Arrays.stream(in).map(String::toCharArray),
                Arrays.stream(out),
                (in, out) -> {
                    Object[] argument = {in, out};
                    return Arguments.of(argument);
                }), additionalExamples.stream().map(TestInput::toArgument));
    }

    @ParameterizedTest
    @MethodSource("passwords")
    void testValidate(char[] input, String expected) {
        Validatable line = Password.from(input);
        assertEquals(expected, line.toOutput());
    }

    @Test
    void test_reason_whenNoVowels() {
        List<Character> input = List.of('x', 'y');
        Validatable line = Password.from(input);
        assertEquals("Rule 1 broken. No vowels.", line.getReason());
    }

    @Test
    void test_reason_whenConsecutiveVowels() {
        List<Character> input = List.of('a', 'e', 'i', 'o', 'u');
        Validatable line = Password.from(input);
        assertEquals("Rule 2 broken. Consecutive vowel found at i", line.getReason());
    }

    @Test
    void test_reason_whenConsecutiveConsonants() {
        List<Character> input = List.of('g', 'h', 'j', 'k', 'l');
        Validatable line = Password.from(input);
        assertEquals("Rule 2 broken. Consecutive consonant found at j", line.getReason());
    }

    @Test
    void test_reason_whenRepeatedLetter() {
        List<Character> input = List.of('a', 'a', 'r', 'o', 'n');
        Validatable line = Password.from(input);
        assertEquals("Rule 3 broken. Repeated letter at a", line.getReason());
    }

    @Test
    void test_valid_whenRepeatedLetter_letterIsE() {
        List<Character> input = List.of('b', 'e', 'e', 'f');
        Validatable line = Password.from(input);
        assertEquals("<beef> is acceptable.", line.toOutput());
    }

    @Test
    void test_valid_whenRepeatedLetter_letterIsO() {
        List<Character> input = List.of('b', 'o', 'o', 't');
        Validatable line = Password.from(input);
        assertEquals("<boot> is acceptable.", line.toOutput());
    }
}