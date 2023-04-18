import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
//        Set<String> VN = new HashSet<>(Arrays.asList("S", "B", "C", "D"));
//        Set<String> VT = new HashSet<>(Arrays.asList("a", "b", "c"));
//        Map<String, List<String>> productions = new HashMap<>();
//        productions.put("S", Arrays.asList("aB"));
//        productions.put("B", Arrays.asList("bS", "aC", "b"));
//        productions.put("C", Arrays.asList("bD"));
//        productions.put("D", Arrays.asList("a", "bC", "cS"));
//        Grammar grammar = new Grammar(VN, VT, productions, "S");
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
//
//
//        System.out.println(grammar.classifyGrammar());
//
//
//        Set<String> Q = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
//        Set<String> Sigma = new HashSet<>(Arrays.asList("a", "b", "c"));
//        Map<Pair<String, String>, String> delta = new HashMap<>();
//        delta.put(new Pair<>("q0", "a"), "q0");
//        delta.put(new Pair<>("q0", "a"), "q1");
//        delta.put(new Pair<>("q2", "a"), "q2");
//        delta.put(new Pair<>("q1", "b"), "q2");
//        delta.put(new Pair<>("q2", "c"), "q3");
//        delta.put(new Pair<>("q3", "c"), "q3");
//        String q0 = "q0";
//        Set<String> F = new HashSet<>(List.of("q3"));
//
//        FA fa = new FA(Q, Sigma, delta, q0, F);
//
//        Grammar rg = fa.toRegularGrammar();
//        System.out.println("Grammar:");
//        System.out.println(rg);
//
//        if (fa.isDeterministic()) {
//            System.out.println("Deterministic.");
//        } else {
//            System.out.println("Non-deterministic.");
//        }
//
//        DFA dfa = fa.toDFA();
//        System.out.println("DFA:");
//        System.out.println(dfa);

//        //String input = "x = 42 + (y - 3) * 2";
//        String input = "IF x = 10 RETURN (Good Job)";
//        //String input = "#";
//        Lexer.Lexer lexer = new Lexer.Lexer(input);
//        List<Token> tokens = null;
//        try {
//            tokens = lexer.tokenize();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        for (Token token : tokens) {
//            System.out.println(token);
//        }

        Chomsky chomsky = new Chomsky("S",
                Arrays.asList("S", "A", "B", "C", "D"),
                Arrays.asList("a", "b"),
                new HashMap<>() {{
                    put("S", Arrays.asList("aB", "bA", "A"));
                    put("A", Arrays.asList("B", "Sa", "bBA", "b"));
                    put("B", Arrays.asList("b", "bS", "aD", "ε"));
                    put("C", Arrays.asList("Ba"));
                    put("D", Arrays.asList("AA"));
                }});

        //System.out.println(chomsky.getProductions());
        //System.out.println(chomsky.getNonTerminal());

        System.out.println("Original:");
        chomsky.printGrammar();

        chomsky.eliminateEpsilonProductions();
        System.out.println("Without Epsilon:");
        chomsky.printGrammar();

        chomsky.eliminateUnitProductions();
        System.out.println("Without Unit productions:");
        chomsky.printGrammar();

        chomsky.eliminateInaccessibleSymbols();
        System.out.println("Without Inaccessible Symbols:");
        chomsky.printGrammar();

        chomsky.eliminateNonproductive();
        System.out.println("Without Nonproductive Characters:");
        chomsky.printGrammar();

        chomsky.toCnf();
        System.out.println("Chomsky Normal Form:");
        chomsky.printGrammar();



        Chomsky chomsky1 = new Chomsky("S",
                Arrays.asList("S", "A", "B", "C", "E"),
                Arrays.asList("a", "b"),
                new HashMap<>() {{
                    put("S", Arrays.asList("bA", "B"));
                    put("A", Arrays.asList("a", "aS", "bAaAb"));
                    put("B", Arrays.asList("AC", "bS", "aAa"));
                    put("C", Arrays.asList("ε", "AB"));
                    put("E", Arrays.asList("BA"));
                }});

        System.out.println("------------");
        System.out.println("Original:");
        chomsky1.printGrammar();

        chomsky1.cfgToCnf();
        System.out.println("Chomsky Normal Form:");
        chomsky1.printGrammar();
    }
}
