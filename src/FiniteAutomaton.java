import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<String> acceptingStates;
    private Map<String, Map<String, String>> transitions;
    private String initialState;

    public FiniteAutomaton(Set<String> states, Set<String> acceptingStates, Map<String, Map<String, String>> transitions, String initialState) {
        this.states = states;
        this.acceptingStates = acceptingStates;
        this.transitions = transitions;
        this.initialState = initialState;
    }

    public boolean accepts(String input) {
        String currentState = initialState;
        for (char c : input.toCharArray()) {
            if (!transitions.get(currentState).containsKey(Character.toString(c))) {
                return false;
            }
            currentState = transitions.get(currentState).get(Character.toString(c));
        }
        return acceptingStates.contains(currentState);
    }
}