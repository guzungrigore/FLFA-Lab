import java.util.*;

public class Grammar {
    private Set<String> VN;
    private Set<String> VT;
    private Map<String, List<String>> productions;
    private String startSymbol;

    public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> productions, String startSymbol) {
        this.VN = VN;
        this.VT = VT;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }

    public String generateString() {
        return generateString(startSymbol);
    }

    private String generateString(String symbol) {
        if (VT.contains(symbol)) {
            return symbol;
        }

        List<String> possibleProductions = productions.get(symbol);
        String chosenProduction = possibleProductions.get(new Random().nextInt(possibleProductions.size()));
        String[] productionSymbols = chosenProduction.split("");
        StringBuilder sb = new StringBuilder();
        for (String s : productionSymbols) {
            sb.append(generateString(s));
        }
        return sb.toString();
    }

    public List<String> generateValidStrings(int count) {
        List<String> validStrings = new ArrayList<>();
        while (validStrings.size() < count) {
            String s = generateString();
            if (isValid(s)) {
                validStrings.add(s);
            }
        }
        return validStrings;
    }

    private boolean isValid(String s) {
        for (char c : s.toCharArray()) {
            if (!VT.contains(Character.toString(c))) {
                return false;
            }
        }
        return true;
    }

    public String classifyGrammar() {
        for (String leftSymbol : productions.keySet()) {
            if (productions.get(leftSymbol).contains("")) {
                return "Type-0";
            }
        }

        for (String leftSymbol : productions.keySet()) {
            for (String production : productions.get(leftSymbol)) {
                String[] sides = production.split(" ");
                if (sides.length == 1) {
                    continue; // Skip productions of the form A -> ε
                }
                String lhs = sides[0];
                String rhs = production.substring(lhs.length() + 1);
                if (lhs.length() <= rhs.length()) {
                    return "Type-1";
                }
            }
        }

        for (String leftSymbol : productions.keySet()) {
            for (String production : productions.get(leftSymbol)) {
                String[] sides = production.split(" ");
                if (sides.length == 1 && VN.contains(leftSymbol)) {
                    return "Type-2";
                }
            }
        }

        return "Type-3";
    }
}
