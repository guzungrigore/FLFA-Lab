import java.util.*;

public class DFA {
    private Set<String> Q;
    private Set<String> Sigma;
    private Map<Pair<String, String>, Set<String>> delta;
    private String q0;
    private Set<String> F;

    public DFA(Set<String> Q, Set<String> Sigma, Map<Pair<String, String>, Set<String>> delta, String q0, Set<String> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean accepts(String input) {
        String currentState = q0;
        for (char c : input.toCharArray()) {
            if (!delta.containsKey(new Pair<>(currentState, Character.toString(c)))) {
                return false;
            }
            currentState = delta.get(new Pair<>(currentState, Character.toString(c))).toString();
        }
        return F.contains(currentState);
    }
}
