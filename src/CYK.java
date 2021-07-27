public class CYK {
    private Rule[] rule;
    private String inputString;
    private String[][] table;

    public CYK() {
    }

    public void solve() {
        // Construct table
        table = new String[inputString.length()][inputString.length()];
        for (int i = 0; i < inputString.length(); i++) {
            for (int j = 0; j < inputString.length() - i; j++) {
                table[i][j] = "*";
            }
        }
        System.out.println(inputString);
        printTable();

        // Change * at first line in table to non-terminal if found match rule
        for (int i = 0; i < inputString.length(); i++) {
            String letter = Character.toString(inputString.charAt(i));
            for (int j = 0; j < rule.length; j++) {
                for (int k = 0; k < rule[j].terminal.size(); k++) {
                    if (rule[j].terminal.get(k).equals(letter)) {
                        if (table[0][i].equals("*")) {
                            table[0][i] = rule[j].nonTerminal;
                        } else {
                            table[0][i] = table[0][i] + "," + rule[j].nonTerminal;
                        }
                    }
                }
            }
        }
        System.out.println(inputString);
        printTable();

        // Change each line in table to non-terminal if (concatNontermi match rule)
        for (int i = 1; i < inputString.length(); i++) {
            for (int j = 0; j < inputString.length() - i; j++) {
                int rowCount_1 = i;
                int rowCount_2 = 0;
                int colCount = j;
                while (rowCount_2 < i) {
                    if (table[rowCount_2][j].length() > 1 || table[rowCount_1 - 1][colCount + 1].length() > 1) {
                        String[] str1 = table[rowCount_2][j].split(",");
                        String[] str2 = table[rowCount_1 - 1][colCount + 1].split(",");
                        for (int m = 0; m < str1.length; m++) {
                            for (int n = 0; n < str2.length; n++) {
                                String concatNontermi = str1[m].concat(str2[n]);
                                for (int k = 0; k < rule.length; k++) {
                                    for (int l = 0; l < rule[k].terminal.size(); l++) {
                                        if (rule[k].terminal.get(l).equals(concatNontermi)) {
                                            if (table[i][j].equals("*")) {
                                                table[i][j] = rule[k].nonTerminal;
                                            } else {
                                                table[i][j] = table[i][j] + "," + rule[k].nonTerminal;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String concatNontermi = table[rowCount_2][j].concat(table[rowCount_1 - 1][colCount + 1]);
                        for (int k = 0; k < rule.length; k++) {
                            for (int l = 0; l < rule[k].terminal.size(); l++) {
                                if (rule[k].terminal.get(l).equals(concatNontermi)) {
                                    if (table[i][j].equals("*")) {
                                        table[i][j] = rule[k].nonTerminal;
                                    } else {
                                        table[i][j] = table[i][j] + "," + rule[k].nonTerminal;
                                    }
                                }
                            }
                        }
                    }
                    rowCount_1--;
                    rowCount_2++;
                    colCount++;
                }
            }
        }
        System.out.println(inputString);
        printTable();
    }

    public void printTable() {
        for (int i = 0; i < inputString.length(); i++) {
            for (int j = 0; j < inputString.length() - i; j++) {
                System.out.print(table[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public void setInputString(String inpuString) {
        this.inputString = inpuString;
    }

    public void setRule(Rule[] rule) {
        this.rule = rule;
    }

    public String[][] getTable() {
        return table;
    }
}