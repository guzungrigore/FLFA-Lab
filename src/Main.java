import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> VN = new HashSet<>(Arrays.asList("S", "B", "C", "D"));
        Set<String> VT = new HashSet<>(Arrays.asList("a", "b", "c"));
        Map<String, List<String>> productions = new HashMap<>();
        productions.put("S", Arrays.asList("aB"));
        productions.put("B", Arrays.asList("bS", "aC", "b"));
        productions.put("C", Arrays.asList("bD"));
        productions.put("D", Arrays.asList("a", "bC", "cS"));
        Grammar grammar = new Grammar(VN, VT, productions, "S");
//        List<String> validStrings = grammar.generateValidStrings(5);
//        System.out.println("valid strings: " + validStrings);
//
//        Set<String> states = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3", "q4"));
//        Set<String> acceptingStates = new HashSet<>(Arrays.asList("q0"));
//        Map<String, Map<String, String>> transitions = new HashMap<>();
//        transitions.put("q0", new HashMap<>(Map.of("a", "q1")));
//        transitions.put("q1", new HashMap<>(Map.of("b", "q0", "a", "q2")));
//        transitions.put("q2", new HashMap<>(Map.of("b", "q3")));
//        transitions.put("q3", new HashMap<>(Map.of("a", "q0", "b", "q2", "c", "q0")));
//
//        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(states, acceptingStates, transitions, "q0");
//
//        List<String> testInputs = Arrays.asList("ab", "aaba", "bb", "bab", "ababab");
//        for (String input : testInputs) {
//            System.out.println("Input \"" + input + "\" is: " + finiteAutomaton.accepts(input));
//        }


        System.out.println(grammar.classifyGrammar());


        Set<String> Q = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
        Set<String> Sigma = new HashSet<>(Arrays.asList("a", "b", "c"));
        Map<Pair<String, String>, String> delta = new HashMap<>();
        delta.put(new Pair<>("q0", "a"), "q0");
        delta.put(new Pair<>("q0", "a"), "q1");
        delta.put(new Pair<>("q2", "a"), "q2");
        delta.put(new Pair<>("q1", "b"), "q2");
        delta.put(new Pair<>("q2", "c"), "q3");
        delta.put(new Pair<>("q3", "c"), "q3");
        String q0 = "q0";
        Set<String> F = new HashSet<>(List.of("q3"));

        FA fa = new FA(Q, Sigma, delta, q0, F);

        Grammar rg = fa.toRegularGrammar();
        System.out.println("Grammar:");
        System.out.println(rg);

        if (fa.isDeterministic()) {
            System.out.println("Deterministic.");
        } else {
            System.out.println("Non-deterministic.");
        }

        DFA dfa = fa.toDFA();
        System.out.println("DFA:");
        System.out.println(dfa);

    }
}
