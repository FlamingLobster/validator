package org.michael;

import com.google.common.io.CharSource;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void testValidate_whenEncounteringEnd_stops() throws IOException {
        CharSequence input = "line\nagain\nend\ndont\nread\nmore";
        InputStream inputStream = CharSource.wrap(input).asByteSource(StandardCharsets.UTF_8).openStream();
        StringWriter actual = new StringWriter();
        BufferedWriter writer = new BufferedWriter(actual);

        Main.validate(inputStream, writer, Identity::new);
        writer.flush();

        assertEquals("line" + System.lineSeparator() + "again" + System.lineSeparator(), String.valueOf(actual));
    }

    /**
     * validatable that does nothing
     */
    private static class Identity implements Validatable {
        private final String word;

        public Identity(List<Character> c) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Character character : c) {
                stringBuilder.append(character);
            }
            this.word = stringBuilder.toString();
        }

        @Override
        public String toOutput() {
            return word;
        }

        @Override
        public String getReason() {
            return "";
        }
    }
}