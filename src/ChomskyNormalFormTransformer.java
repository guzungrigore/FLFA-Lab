import java.util.*;

public class ChomskyNormalFormTransformer {

    private Map<String, List<String>> rules; // Map to store the production rules
    private Map<String, Set<String>> nonTerminals; // Map to store the non-terminals
    private Set<String> productive; // Set to store the productive non-terminals
    private Set<String> accessible; // Set to store the accessible non-terminals

    public ChomskyNormalFormTransformer(Map<String, List<String>> rules) {
        this.rules = new HashMap<>(rules);
        nonTerminals = new HashMap<>();
        productive = new HashSet<>();
        accessible = new HashSet<>();
        computeNonTerminals();
        eliminateEpsilonProductions();
        eliminateRenaming();
        eliminateInaccessibleSymbols();
        eliminateNonProductiveSymbols();
    }

    // Method to compute the non-terminals
    private void computeNonTerminals() {
        for (String lhs : rules.keySet()) {
            Set<String> nt = new HashSet<>();
            for (char c : lhs.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    nt.add(String.valueOf(c));
                }
            }
            nonTerminals.put(lhs, nt);
        }
    }

    // Method to eliminate epsilon productions
    private void eliminateEpsilonProductions() {
        boolean epsilonFound;
        do {
            epsilonFound = false;
            Map<String, List<String>> newRules = new HashMap<>();
            for (String lhs : rules.keySet()) {
                List<String> rhs = rules.get(lhs);
                List<String> newRhs = new ArrayList<>();
                for (String s : rhs) {
                    if (s.equals("ε")) {
                        epsilonFound = true;
                        for (String t : nonTerminals.keySet()) {
                            if (nonTerminals.get(t).contains(lhs)) {
                                for (String u : nonTerminals.get(t)) {
                                    if (!newRhs.contains(u)) {
                                        newRhs.add(u);
                                    }
                                }
                            }
                        }
                    } else {
                        newRhs.add(s);
                    }
                }
                newRules.put(lhs, newRhs);
            }
            rules = newRules;
        } while (epsilonFound);
    }



    // Method to eliminate renaming
    private void eliminateRenaming() {

        boolean renamingFound;
        do {
            renamingFound = false;
            for (String lhs : rules.keySet()) {
                List<String> rhs = rules.get(lhs);
                if (rhs.size() == 1 && nonTerminals.containsKey(rhs.get(0))) {
                    renamingFound = true;
                    String nt = rhs.get(0);
                    nonTerminals.get(lhs).addAll(nonTerminals.get(nt));
                    for (String s : rules.get(nt)) {
                        String newS = s.replaceAll(nt, lhs);
                        if (!rhs.contains(newS)) {
                            rhs.add(newS);
                        }
                    }
                    rules.put(lhs, rhs);
                    rules.remove(nt);
                    nonTerminals.remove(nt);
                }
            }
        } while (renamingFound);
    }

    // Method to eliminate inaccessible symbols
    private void eliminateInaccessibleSymbols() {
        accessible.add("S"); // Start symbol is always accessible
        boolean accessibleFound;
        do {
            accessibleFound = false;
            for (String lhs : rules.keySet()) {
                List<String> rhs = rules.get(lhs);
                for (String s : rhs) {
                    boolean allAccessible = true;
                    for (char c : s.toCharArray()) {
                        if (Character.isUpperCase(c) && !accessible.contains(String.valueOf(c))) {
                            allAccessible = false;
                            break;
                        }
                    }
                    if (allAccessible && !accessible.contains(lhs)) {
                        accessible.add(lhs);
                        accessibleFound = true;
                    }
                }
            }
        } while (accessibleFound);
        rules.keySet().retainAll(accessible);
        nonTerminals.keySet().retainAll(accessible);
    }

    // Method to eliminate non-productive symbols
    private void eliminateNonProductiveSymbols() {
        for (String lhs : rules.keySet()) {
            List<String> rhs = rules.get(lhs);
            boolean isProductive = false;
            for (String s : rhs) {
                boolean allProductive = true;
                for (char c : s.toCharArray()) {
                    if (Character.isUpperCase(c) && !productive.contains(String.valueOf(c))) {
                        allProductive = false;
                        break;
                    }
                }
                if (allProductive) {
                    isProductive = true;
                    break;
                }
            }
            if (isProductive) {
                productive.add(lhs);
            }
        }
        rules.keySet().retainAll(productive);
        nonTerminals.keySet().retainAll(productive);
    }

    // Method to obtain the Chomsky Normal Form
    public Map<String, List<String>> getChomskyNormalForm() {
        Map<String, List<String>> cnf = new HashMap<>();
        int counter = 1;
        for (String lhs : rules.keySet()) {
            List<String> rhs = rules.get(lhs);
            List<String> newRhs = new ArrayList<>();
            for (String s : rhs) {
                if (s.length() == 1 && !Character.isUpperCase(s.charAt(0))) {
                    newRhs.add(s);
                } else {
                    for (int i = 0; i < s.length(); i++) {
                        String nt = "X" + counter++;
                        String a = Character.toString(s.charAt(i));
                        String b = nt;
                        if (i == s.length() - 1) { // Only one terminal
                            newRhs.add(Character.toString(s.charAt(i)));
                        } else if (i == s.length() - 2) { // Two nonterminals
                            newRhs.add(s.substring(i, i + 2));
                        } else { // More than two symbol
                            newRhs.add(a + b);
                        }
                        cnf.put(nt, Collections.singletonList(a)); // Add new nonterminal
                    }
                }
            }
            cnf.put(lhs, newRhs);
        }
        return cnf;
    }

}

