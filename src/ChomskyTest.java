import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ChomskyTest {

    @Test
    public void testEliminateEpsilonProductions() {
        Chomsky chomsky = new Chomsky("S",
                Arrays.asList("S", "A", "B", "C", "D"),
                Arrays.asList("a", "b"),
                new HashMap<>() {{
                    put("S", Arrays.asList("AC", "bA", "B", "aA"));
                    put("A", Arrays.asList("ε", "aS", "ABAb"));
                    put("B", Arrays.asList("a", "AbSA"));
                    put("C", Arrays.asList("abC"));
                    put("D", Arrays.asList("AB"));
                }});

        chomsky.eliminateEpsilonProductions();

        Map<String, List<String>> expected = new HashMap<>() {{
            put("S", Arrays.asList("C", "AC", "b", "bA", "B", "a", "aA"));
            put("A", Arrays.asList("aS", "Bb", "ABAb"));
            put("B", Arrays.asList("a", "bS", "AbSA"));
            put("C", Arrays.asList("abC"));
            put("D", Arrays.asList("B", "AB"));
        }};

        assertEquals(expected, chomsky.getProductions());
    }

    @Test
    public void testEliminateInaccessibleSymbols1() {
        Chomsky chomsky = new Chomsky("S",
                Arrays.asList("S", "A", "B", "C"),
                Arrays.asList("a", "b"),
                new HashMap<>() {{
                    put("S", Arrays.asList("aA", "B"));
                    put("A", Arrays.asList("a"));
                    put("B", Arrays.asList("b"));
                    put("C", Arrays.asList("S", "aB"));
                }});

        chomsky.eliminateInaccessibleSymbols();

        Map<String, List<String>> expected = new HashMap<>() {{
            put("S", Arrays.asList("aA", "B"));
            put("A", Arrays.asList("a"));
            put("B", Arrays.asList("b"));
        }};

        assertEquals(expected, chomsky.getProductions());
    }

    @Test
    public void testEliminateInaccessibleSymbols2() {
        Chomsky chomsky = new Chomsky("S",
                Arrays.asList("S", "A", "B", "C", "D"),
                Arrays.asList("a", "b"),
                new HashMap<>() {{
                    put("S", Arrays.asList("AC", "bA", "B", "aA"));
                    put("A", Arrays.asList("aS", "ABAb"));
                    put("B", Arrays.asList("a", "AbSA"));
                    put("C", Arrays.asList("abC"));
                    put("D", Arrays.asList("AB"));
                }});

        chomsky.eliminateInaccessibleSymbols();

        Map<String, List<String>> expected = new HashMap<>() {{
            put("S", Arrays.asList("AC", "bA", "B", "aA"));
            put("A", Arrays.asList("aS", "ABAb"));
            put("B", Arrays.asList("a", "AbSA"));
            put("C", Arrays.asList("abC"));
        }};

        assertEquals(expected, chomsky.getProductions());
    }

    @Test
    public void testEliminateNonproductive() {
        Chomsky chomsky = new Chomsky("S",
                Arrays.asList("S", "A", "B", "C", "D"),
                Arrays.asList("a", "b"),
                new HashMap<>() {{
                    put("S", Arrays.asList("AC", "bA", "B", "aA"));
                    put("A", Arrays.asList("ε", "aS", "ABAb"));
                    put("B", Arrays.asList("a", "AbSA"));
                    put("C", Arrays.asList("abC"));
                    put("D", Arrays.asList("AB"));
                }});

        chomsky.eliminateNonproductive();

        Map<String, List<String>> expected = new HashMap<>() {{
            put("S", Arrays.asList("bA", "B", "aA"));
            put("A", Arrays.asList("aS", "ABAb"));
            put("B", Arrays.asList("a", "AbSA"));
            put("D", Arrays.asList("AB"));
        }};

        assertEquals(expected, chomsky.getProductions());
    }

}
