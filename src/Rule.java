import java.util.ArrayList;

class Rule {
    public String nonTerminal;
    public ArrayList<String> terminal;

    public Rule(String nonTerminal, ArrayList<String> terminal) {
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
    }
}