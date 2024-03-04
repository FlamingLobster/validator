package org.michael;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsecutiveVowelGenerator extends Generator<List<Character>>{
    private static final int iterationLimit = 100;
    private static final List<Character> vowels = List.of('a', 'e', 'i', 'o', 'u');
    private static final List<Character> consonants = List.of('b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','y','z');
    private static final int maxLength = 20;
    public ConsecutiveVowelGenerator(Class<List<Character>> type) {
        super(type);
    }

    @Override
    public List<Character> generate(SourceOfRandomness random, GenerationStatus _status) {
        List<Character> generated = new ArrayList<>();
        int length = random.nextInt(3, maxLength - 3); // leave 3 for the repeat vowel which we will insert later
        int extraVowelAt = random.nextInt(1, Math.max(Math.floorDiv(length, 2), 1)) * 2; // insert extra vowels at an even index
        for(int i = length; i>0; i--) {
            if (i == extraVowelAt) { // insert three extra vowel so the input becomes invalid
                generated.addAll(pickVowels(random));
                continue;
            }
            // we want to generate inputs that breaks and only breaks this consecutive vowel rule
            // the generated will be alternating vowel and consonant
            if (i % 2 == 0) {
                generated.add(vowels.get(random.nextInt(vowels.size() - 1)));
            } else {
                generated.add(consonants.get(random.nextInt(consonants.size() - 1)));
            }
        }
        return generated;
    }

    private List<Character> pickVowels(SourceOfRandomness random) {
        Set<Integer> indices = new HashSet<>();

        int i = 0;
        while(i != iterationLimit && indices.size() != 3) {
            indices.add(random.nextInt(ConsecutiveVowelGenerator.vowels.size()));
            i++;
        }

        if (indices.size() != 3) {
            return List.of('q','u','e','u','e');
        }

        return indices.stream().map(ConsecutiveVowelGenerator.vowels::get).collect(Collectors.toList());
    }
}
