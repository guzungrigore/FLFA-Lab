import java.util.*;
public class FA {
    private Set<String> Q;
    private Set<String> Sigma;
    private Map<Pair<String, String>, String> delta;
    private String q0;
    private Set<String> F;

    public FA(Set<String> Q, Set<String> Sigma, Map<Pair<String, String>, String> delta, String q0, Set<String> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public Grammar toRegularGrammar() {
        Map<String, List<String>> productions = new HashMap<>();
        Set<String> VN = new HashSet<>();
        Set<String> VT = new HashSet<>();
        String startSymbol = "S";

        for (String q : Q) {
            VN.add(q);
        }

        for (Pair<String, String> transition : delta.keySet()) {
            String fromState = transition.getFirst();
            String inputSymbol = transition.getSecond();
            String toState = delta.get(transition);

            String production = inputSymbol + " " + toState;

            if (!productions.containsKey(fromState)) {
                productions.put(fromState, new ArrayList<String>());
            }
            productions.get(fromState).add(production);

            VT.add(inputSymbol);
        }

        for (String q : F) {
            if (!productions.containsKey(q)) {
                productions.put(q, new ArrayList<String>());
            }
            productions.get(q).add("");
        }

        productions.put(startSymbol, new ArrayList<String>());
        productions.get(startSymbol).add(q0);

        return new Grammar(VN, VT, productions, startSymbol);
    }

    public boolean isDeterministic() {
        for (String q : Q) {
            for (String a : Sigma) {
                Set<String> nextStates = getNextStates(q, a);
                if (nextStates.size() != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<String> getNextStates(String currentState, String inputSymbol) {
        Set<String> nextStates = new HashSet<>();
        for (Pair<String, String> transition : delta.keySet()) {
            if (transition.getFirst().equals(currentState) && transition.getSecond().equals(inputSymbol)) {
                nextStates.add(delta.get(transition));
            }
        }
        return nextStates;
    }


    public DFA toDFA() {
        Map<Set<String>, String> dfaStates = new HashMap<>();
        Map<Pair<String, String>, Set<String>> dfaDelta = new HashMap<>();
        Set<Set<String>> visited = new HashSet<>();
        String dfaQ0 = q0;
        Set<String> dfaF = new HashSet<>();

        Set<String> initialState = new HashSet<>();
        initialState.add(q0);
        dfaStates.put(initialState, q0);

        Queue<Set<String>> queue = new LinkedList<>();
        queue.add(initialState);

        while (!queue.isEmpty()) {
            Set<String> currentState = queue.poll();

            visited.add(currentState);

            String dfaStateName = dfaStates.get(currentState);
            for (String q : currentState) {
                if (F.contains(q)) {
                    dfaF.add(dfaStateName);
                    break;
                }
            }

            for (String inputSymbol : Sigma) {
                Set<String> nextStates = new HashSet<>();
                for (String q : currentState) {
                    Pair<String, String> transition = new Pair<>(q, inputSymbol);
                    if (delta.containsKey(transition)) {
                        nextStates.add(delta.get(transition));
                    }
                }

                if (!nextStates.isEmpty()) {
                    if (!dfaStates.containsKey(nextStates)) {
                        String nextStateName = getNextStateName(dfaStates.size());
                        dfaStates.put(nextStates, nextStateName);
                        if (!visited.contains(nextStates)) {
                            queue.add(nextStates);
                        }
                    }

                    Pair<String, String> dfaTransition = new Pair<>(dfaStateName, inputSymbol);
                    dfaDelta.put(dfaTransition, nextStates);
                }
            }
        }

        Set<String> dfaQ = new HashSet<>(dfaStates.values());
        return new DFA(dfaQ, Sigma, dfaDelta, dfaQ0, dfaF);
    }

    private String getNextStateName(int count) {
        int numChars = 1;
        while (count > Math.pow(26, numChars)) {
            count -= Math.pow(26, numChars);
            numChars++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numChars; i++) {
            int digit = (count / (int) Math.pow(26, numChars - i - 1)) % 26;
            sb.append((char) ('A' + digit));
        }
        return sb.toString();
    }

}