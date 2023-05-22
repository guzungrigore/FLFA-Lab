import java.util.*;

public class Chomsky {
    private String startSymbol;
    private List<String> nonTerminal;
    private List<String> terminals;
    private Map<String, List<String>> productions;

    public Chomsky(String startSymbol, List<String> nonTerminal, List<String> terminals, Map<String, List<String>> productions) {
        this.startSymbol = startSymbol;
        this.nonTerminal = nonTerminal;
        this.terminals = terminals;
        this.productions = productions;
    }

    public void eliminateEpsilonProductions() {
        Set<String> nullables = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            if (entry.getValue().contains("ε")) {
                nullables.add(entry.getKey());
            }
        }

        Map<String, List<String>> newProductions = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String variable = entry.getKey();
            List<String> oldProductions = entry.getValue();
            List<String> newProductionsList = new ArrayList<>();

            for (String production : oldProductions) {
                if (!production.equals("ε")) {
                    boolean addProduction = true;
                    for (String nullable : nullables) {
                        if (production.contains(nullable.toString())) {
                            String newProduction = production.replace(nullable.toString(), "");
                            if (!newProductionsList.contains(newProduction)) {
                                newProductionsList.add(newProduction);
                            }
                        }
                    }
                    if (!newProductionsList.contains(production)) {
                        newProductionsList.add(production);
                    }
                }
            }

            Iterator<String> iterator = newProductionsList.iterator();
            while (iterator.hasNext()) {
                String production = iterator.next();
                if (production.equals("")) {
                    iterator.remove();
                }
            }

            newProductions.put(variable, newProductionsList);
        }

        productions = newProductions;
    }


    public void eliminateUnitProductions() {
        for (String symbol : this.productions.keySet()) {
            List<String> unit_productions = new ArrayList<>();
            for (String prod : this.productions.get(symbol)) {
                if (prod.length() == 1 && Character.isUpperCase(prod.charAt(0))) {
                    unit_productions.add(prod);
                }
            }

            while (!unit_productions.isEmpty()) {
                String unit = unit_productions.remove(0);
                this.productions.get(symbol).remove(unit);
                for (String prod : this.productions.get(unit)) {
                    if (!this.productions.get(symbol).contains(prod)) {
                        this.productions.get(symbol).add(prod);
                    }
                }
                for (String prod : this.productions.get(symbol)) {
                    if (prod.length() == 1 && Character.isUpperCase(prod.charAt(0))) {
                        unit_productions.add(prod);
                    }
                }
            }
        }
    }

    public void eliminateInaccessibleSymbols() {
        Set<String> visited = new HashSet<>();
        visit(this.startSymbol, visited);

        List<String> newNonTerminal = new ArrayList<>();
        Map<String, List<String>> newProductions = new HashMap<>();
        for (String nt : this.nonTerminal) {
            if (visited.contains(nt)) {
                newNonTerminal.add(nt);
                newProductions.put(nt, this.productions.get(nt));
            }
        }

        this.nonTerminal = newNonTerminal;
        this.productions = newProductions;
    }

    private void visit(String symbol, Set<String> visited) {
        if (!visited.contains(symbol)) {
            visited.add(symbol);
            for (String prod : this.productions.get(symbol)) {
                for (int i = 0; i < prod.length(); i++) {
                    String s = prod.charAt(i) + "";
                    if (this.nonTerminal.contains(s)) {
                        visit(s, visited);
                    }
                }
            }
        }
    }

    public void eliminateNonproductive() {
        Set<String> productive = new HashSet<>();
        productive.add(startSymbol);
        Set<String> oldProductive = new HashSet<>();

        while (!oldProductive.equals(productive)) {
            oldProductive = new HashSet<>(productive);
            for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
                String symbol = entry.getKey();
                List<String> rhs = entry.getValue();
                if (!productive.contains(symbol)) {
                    for (String prod : rhs) {
                        boolean allInProductive = true;
                        for (char s : prod.toCharArray()) {
                            if (!productive.contains(String.valueOf(s)) && !terminals.contains(String.valueOf(s))) {
                                allInProductive = false;
                                break;
                            }
                        }
                        if (allInProductive) {
                            productive.add(symbol);
                            break;
                        }
                    }
                }
            }
        }

        Set<String> nonproductive = new HashSet<>(nonTerminal);
        nonproductive.removeAll(productive);

        for (String symbol : nonproductive) {
            productions.remove(symbol);
        }

        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String symbol = entry.getKey();
            List<String> rhs = entry.getValue();
            List<String> newRhs = new ArrayList<>();
            for (String prod : rhs) {
                boolean allInProductive = true;
                for (char s : prod.toCharArray()) {
                    if (!productive.contains(String.valueOf(s)) && !terminals.contains(String.valueOf(s))) {
                        allInProductive = false;
                        break;
                    }
                }
                if (allInProductive) {
                    newRhs.add(prod);
                }
            }
            productions.put(symbol, newRhs);
        }

        List<String> productiveList = new ArrayList<>(productive);
        Collections.sort(productiveList);
        nonTerminal = productiveList;
    }

    public void toCnf() {
        Map<String, List<String>> newProductions = new HashMap<>();
        int nextNewVarX = 1;
        int nextNewVarT = 1;
        Map<String, String> terminalVarMap = new HashMap<>();

        for (String var : productions.keySet()) {
            newProductions.put(var, new ArrayList<String>());

            for (String prod : productions.get(var)) {
                if (prod.length() >= 3) {
                    List<String> prodVars = new ArrayList<>();
                    for (int i = 0; i < prod.length() - 1; i++) {
                        prodVars.add("X" + (nextNewVarX + i));
                    }
                    nextNewVarX += prod.length() - 1;
                    nonTerminal.addAll(prodVars);

                    newProductions.get(var).add(prod.charAt(0) + prodVars.get(0));
                    for (int i = 0; i < prod.length() - 2; i++) {
                        String newVar = prodVars.get(i);
                        newProductions.putIfAbsent(newVar, new ArrayList<String>());
                        newProductions.get(newVar).add(prod.charAt(i + 1) + prodVars.get(i + 1));
                    }
                    newProductions.putIfAbsent(prodVars.get(prodVars.size() - 1), new ArrayList<String>());
                    newProductions.get(prodVars.get(prodVars.size() - 1)).add(prod.substring(prod.length() - 1));

                } else if (prod.length() == 2 && prod.chars().allMatch(c -> nonTerminal.contains("" + (char) c))) {
                    newProductions.get(var).add(prod);

                } else {
                    String newProd = prod;
                    for (int i = 0; i < prod.length(); i++) {
                        String sym = "" + prod.charAt(i);
                        if (terminals.contains(sym)) {
                            if (!terminalVarMap.containsKey(sym)) {
                                String newVar = "T" + nextNewVarT;
                                nextNewVarT++;
                                nonTerminal.add(newVar);
                                newProductions.putIfAbsent(newVar, new ArrayList<String>());
                                newProductions.get(newVar).add(sym);
                                terminalVarMap.put(sym, newVar);
                            }
                            newProd = newProd.replaceFirst(sym, terminalVarMap.get(sym));
                        }
                    }
                    newProductions.get(var).add(newProd);
                }
            }
        }
        productions = newProductions;
        nonTerminal = new ArrayList<>(new TreeSet<>(nonTerminal));
    }


    public Chomsky cfgToCnf() {
        eliminateEpsilonProductions();
        eliminateUnitProductions();
        eliminateInaccessibleSymbols();
        eliminateNonproductive();
        toCnf();
        return this;
    }

    public void printGrammar() {
        for (String var : productions.keySet()) {
            System.out.println(var + " -> " + String.join(" | ", productions.get(var)));
        }
    }

    public List<String> getNonTerminal() {
        return nonTerminal;
    }

    public Map<String, List<String>> getProductions() {
        return productions;
    }
}
