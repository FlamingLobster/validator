package org.michael;

/**
 * Most exist to make testing easier. I've never liked mocks
 */
public interface Validatable {
    String toOutput();

    String getReason();
}
