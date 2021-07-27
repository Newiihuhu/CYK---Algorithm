import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class App_GUI extends JFrame {
    /**
    *
    */
    private static final long serialVersionUID = -3305293021914643722L;

    // Other variable
    private String inputString = "";
    private String inputRule = "";
    private String[][] table;

    // CYK instance
    private CYK cyk;

    // Rule model
    private Rule[] rule;

    // JFileChooser
    JFileChooser pathOpen = new JFileChooser();
    JFileChooser pathSave = new JFileChooser();

    // JPanel
    JPanel tablePanel = new javax.swing.JPanel();
    JPanel configPanel = new javax.swing.JPanel();
    JPanel boxSave = new JPanel();
    JPanel boxOpen = new JPanel();

    // JLabel
    JLabel grammarLabel = new JLabel();
    JLabel inputLabel = new JLabel();
    JLabel solutionLabel = new JLabel();

    // JScrollPane
    JScrollPane inputRuleBoxScrollPane = new JScrollPane();
    JScrollPane outputSolutionScrollPane = new JScrollPane();
    JScrollPane tableScrollPane = new JScrollPane();

    // JTable
    JTable resultTable = new JTable() {
        /**
         *
         */
        private static final long serialVersionUID = 2912505927095326855L;

        public boolean editCellAt(int row, int column, java.util.EventObject e) {
            return false;
        }
    };

    // MyRenderer
    MyRenderer myRenderer = new MyRenderer();

    // JTableModel
    DefaultTableModel resultTableModel = new DefaultTableModel();

    // JTextArea
    JTextArea inputRuleBox = new JTextArea();
    JTextArea outputSolutionBox = new JTextArea();

    // JTextField
    JTextField inputStringBox = new JTextField();

    // JButton
    JButton goButton = new JButton();
    JButton findStringFromGrammarButton = new JButton();

    // JMenuBar
    JMenuBar mainMenuBar = new JMenuBar();

    // JMenu
    JMenu fileMenu = new JMenu();
    JMenu editMenu = new JMenu();
    JMenu helpMenu = new JMenu();

    // JMenuItem
    JMenuItem openMenuItem = new JMenuItem();
    JMenuItem saveMenuItem = new JMenuItem();
    JMenuItem clearMenuItem = new JMenuItem();
    JMenuItem helpMenuItem = new JMenuItem();

    // Dimension
    Dimension screenSizeMain = new Dimension(800, 600);

    /**
     *
     */
    public App_GUI() {
        super();
    }

    private void initGUI() {
        // -- JLabel -- //
        grammarLabel.setText("Grammar (CNF)");
        grammarLabel.setFont(new Font("Tahoma", 0, 16));

        inputLabel.setText("Input");
        inputLabel.setFont(new Font("Tahoma", 0, 16));

        solutionLabel.setText("Solution");
        solutionLabel.setFont(new Font("Tahoma", 0, 16));
        // -- JLabel -- //

        // -- JTextField -- //
        inputStringBox.setFont(new Font("Tahoma", 0, 16));
        // -- JTextField -- //

        // -- JTextArea -- //
        inputRuleBox.setFont(new Font("Tahoma", 0, 16));
        inputRuleBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        outputSolutionBox.setFont(new Font("Tahoma", 0, 16));
        outputSolutionBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        outputSolutionBox.setEditable(false);
        // -- JTextArea -- //

        // -- JScrollPane -- //
        inputRuleBoxScrollPane.setViewportView(inputRuleBox);
        tableScrollPane.setViewportView(resultTable);
        outputSolutionScrollPane.setViewportView(outputSolutionBox);
        // -- JScrollPane -- //

        // -- JTable -- //
        resultTable.setModel(resultTableModel);
        resultTable.setDefaultRenderer(Object.class, myRenderer);
        resultTable.setRowHeight(50);
        resultTable.setFont(new Font("Tahoma", 0, 14));
        resultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                resultTableMouseClicked(evt);
            }
        });
        // -- JTable -- //

        // -- JButton -- //
        findStringFromGrammarButton.setText("Find String");
        findStringFromGrammarButton.addActionListener((ActionEvent e) -> {
            findStringFromGrammar();
        });

        goButton.setText("GO");
        goButton.addActionListener((ActionEvent e) -> {
            JFrame f = new JFrame();
            if (inputStringBox.getText().length() > 0 && inputRuleBox.getText().length() > 0) {
                int countUpperLetter = 0;
                inputString = inputStringBox.getText();
                inputRule = inputRuleBox.getText();

                for (int i = 0; i < inputString.length(); i++) {
                    if (Character.isUpperCase(inputString.charAt(i))) {
                        countUpperLetter++;
                    }
                }

                // Check input string
                if (!(countUpperLetter > 0)) {
                    // Read grammar from text box
                    readGrammarFromTextBox();
                    cyk.setRule(rule);
                    cyk.setInputString(inputString);
                    cyk.solve();

                    // Get result from CYK Class and update table
                    table = cyk.getTable();
                    updateTable(inputString);

                    // Check if string is accept or not
                    if (table[inputString.length() - 1][0].equals(rule[0].nonTerminal)) {
                        JOptionPane.showMessageDialog(f, "String " + inputString + " is accept by this grammar.");
                    } else {
                        JOptionPane.showMessageDialog(f, "String " + inputString + " is not accept by this grammar.");
                    }
                } else {
                    JOptionPane.showMessageDialog(f, "Input String only contain lower case letter.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(f, "Please fill input and grammar box.", "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        // -- JButton -- //

        // -- JPanel setup -- //
        configPanel.setBackground(new Color(204, 255, 204));
        GroupLayout configPanelLayout = new GroupLayout(configPanel);
        configPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(configPanelLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(configPanelLayout.createSequentialGroup().addContainerGap().addGroup(configPanelLayout
                        .createParallelGroup(Alignment.LEADING)
                        .addComponent(outputSolutionScrollPane, Alignment.TRAILING).addComponent(inputRuleBoxScrollPane)
                        .addGroup(configPanelLayout.createSequentialGroup()
                                .addGroup(configPanelLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(inputLabel).addComponent(grammarLabel)
                                        .addGroup(configPanelLayout.createSequentialGroup()
                                                .addComponent(inputStringBox, GroupLayout.PREFERRED_SIZE, 208,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED).addComponent(goButton,
                                                        GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(solutionLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap()));

        configPanelLayout.setVerticalGroup(configPanelLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, configPanelLayout.createSequentialGroup().addContainerGap()
                        .addComponent(inputLabel).addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(configPanelLayout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(inputStringBox, GroupLayout.PREFERRED_SIZE, 32,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(goButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(grammarLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(inputRuleBoxScrollPane, GroupLayout.PREFERRED_SIZE, 245,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(solutionLabel)
                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(outputSolutionScrollPane)
                        .addContainerGap()));

        tablePanel.setBackground(new Color(204, 255, 255));
        GroupLayout tablePanelLayout = new GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(tablePanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 465,
                                        Short.MAX_VALUE)
                                .addGroup(tablePanelLayout.createSequentialGroup()
                                        .addComponent(findStringFromGrammarButton).addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap()));
        tablePanelLayout.setVerticalGroup(tablePanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                        .addGap(10, 10, 10).addComponent(findStringFromGrammarButton, 30, 30, 30).addContainerGap()));
        // -- JPanel setup -- //

        // -- JMenuBar setup -- //
        mainMenuBar.add(fileMenu);
        mainMenuBar.add(editMenu);
        mainMenuBar.add(helpMenu);

        fileMenu.setText("File");
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);

        editMenu.setText("Edit");
        editMenu.add(clearMenuItem);

        helpMenu.setText("Help");
        helpMenu.add(helpMenuItem);

        openMenuItem.setText("Open");
        openMenuItem.addActionListener((ActionEvent e) -> {
            try {
                openButtAction();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener((ActionEvent e) -> {
            try {
                saveButtAction();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener((ActionEvent e) -> {
            clearButtAction();
        });

        helpMenuItem.setText("How to use");
        helpMenuItem.addActionListener((ActionEvent e) -> {
            helpButtAction();
        });
        // -- JMenuBar setup -- //

        // -- JFrame setup -- //
        GroupLayout frameLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(frameLayout);
        frameLayout.setHorizontalGroup(frameLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(frameLayout.createSequentialGroup()
                        .addComponent(configPanel, GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE)
                        .addComponent(tablePanel, GroupLayout.PREFERRED_SIZE, 485, Short.MAX_VALUE)));

        frameLayout.setVerticalGroup(frameLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(frameLayout.createSequentialGroup()
                        .addGroup(frameLayout.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(configPanel, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                                .addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE))));

        setJMenuBar(mainMenuBar);
        setTitle("CYK/CKY Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(screenSizeMain);
        setResizable(false);
        setIconImage(new ImageIcon("icon/app_icon.jpg").getImage());
        pack();
        // -- JFrame setup -- //
    }

    // -- Main Method-- //
    public static void main(String[] args) {
        App_GUI gui = new App_GUI();
        gui.cyk = new CYK();
        gui.initGUI();
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
    }
    // -- Main Method-- //

    public void solutionSolve(int row, int column, Rule[] rule) {
        StringBuilder outputSolut = new StringBuilder();
        StringBuilder cellInforow2up = new StringBuilder();
        String cellInfo = (String) resultTable.getValueAt(row, column);
        String[] splitCellInfo = cellInfo.split(",");
        for (int i = 0; i < splitCellInfo.length; i++) {
            System.out.println("splitCellInfo :" + splitCellInfo[i] + " i : " + i);
        }
        String nonTerminalSelect = "";
        ArrayList<String> terminalSelect = new ArrayList<String>();
        outputSolut.append(cellInfo + " = \n");
        if (cellInfo.equals("*")) {
            outputSolut.append("non match with grammar");
        } else if (row < 1) {
            for (int i = 0; i < rule.length; i++) {
                if (splitCellInfo.length > 1) {
                    for (int j = 0; j < splitCellInfo.length; j++) {
                        if (rule[i].nonTerminal.equals(splitCellInfo[j])) {
                            nonTerminalSelect = rule[i].nonTerminal;
                            terminalSelect = rule[i].terminal;
                            outputSolut.append(nonTerminalSelect + " -> " + terminalSelect.toString() + "\n");
                        }
                    }
                } else {
                    if (rule[i].nonTerminal.equals(splitCellInfo[0])) {
                        nonTerminalSelect = rule[i].nonTerminal;
                        terminalSelect = rule[i].terminal;
                        outputSolut.append(nonTerminalSelect + " -> " + terminalSelect.toString() + "\n");
                    }
                }
            }
        } else {
            for (int i = 0; i < row; i++) {
                cellInforow2up.append((String) resultTable.getValueAt(i, column));
                if (cellInforow2up.length() > 1) {
                    cellInforow2up.append(",");
                }
                cellInforow2up.append((String) resultTable.getValueAt(row - i - 1, column + i + 1));
            }
            String cellInforow2up2 = cellInforow2up.toString();
            String[] splitcellInforow2up = new String[cellInforow2up2.length()];
            for (int i = 0; i < cellInforow2up2.length(); i++) {
                if (cellInforow2up2.charAt(i) == ',') {
                    splitcellInforow2up = cellInforow2up2.split(",");
                }
            }
            // System.out.println("splitcellInforow2up.length :" +
            // splitcellInforow2up.length);
            String[] splitx = new String[rule.length];
            if (splitcellInforow2up.length > 2) {

                for (int i = 0; i < splitcellInforow2up.length; i++) {
                    // System.out.println("splitcellInforow2up :" + splitcellInforow2up[i] + " i : "
                    // + i);
                }
                StringBuilder x = new StringBuilder();
                for (int i = 0; i < splitcellInforow2up.length / 2; i++) {
                    x.append(splitcellInforow2up[i] + splitcellInforow2up[splitcellInforow2up.length / 2]);
                    x.append(",");
                    x.append(splitcellInforow2up[i] + splitcellInforow2up[1 + splitcellInforow2up.length / 2]);
                    x.append(",");
                }
                System.out.println("x :" + x.toString());
                splitx = x.toString().split(","); // สำหรับ2ตัวต่อ 1 ช่อง
                for (int i = 0; i < splitx.length; i++) {
                    // System.out.println("splitx :" + splitx[i] + " i : " + i);
                }
            }
            System.out.println("cellInforow2up :" + cellInforow2up.toString()); // 1 ตัวต่อช่อง
            // String[] splitStarCellInforow2up = cellInforow2up.toString().split("*");
            if (splitx[0] != null) {
                // System.out.println("splitx != null");
                for (int i = 0; i < splitx.length; i++) {
                    for (int j = 0; j < rule.length; j++) {
                        for (int k = 0; k < rule[j].terminal.size(); k++) {
                            // System.out.println("splitx[i] : "+splitx[i] +" rule[j].terminal.get(k) : "
                            // +rule[j].terminal.get(k));
                            if (splitx[i].equals(rule[j].terminal.get(k))) {
                                // System.out.println("splitx[i].equals(rule[j].terminal.get(k)");
                                outputSolut.append(rule[j].nonTerminal + " -> " + rule[j].terminal.get(k));
                            }
                        }
                    }
                }
            } else {
                for (int j = 0; j < rule.length; j++) {
                    for (int k = 0; k < rule[j].terminal.size(); k++) {
                        // System.out.println("splitx[i] : "+splitx[i] +" rule[j].terminal.get(k) : "
                        // +rule[j].terminal.get(k));
                        if (cellInforow2up.toString().equals(rule[j].terminal.get(k))) {
                            System.out.println("cellInforow2up.equals(rule[j].terminal.get(k))");
                            outputSolut.append(rule[j].nonTerminal + " -> " + rule[j].terminal.get(k));
                        }
                    }
                }
            }

        }
        outputSolutionBox.setText(outputSolut.toString());
    }

    public void resultTableMouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 1) {
            JTable jTable = (JTable) evt.getSource();
            int row = jTable.getSelectedRow();
            int column = jTable.getSelectedColumn();
            String cellInfo = resultTable.getValueAt(row, column) + " = ";
            System.out.println("row :" + row + " column : " + column);
            outputSolutionBox.setText(cellInfo);
            solutionSolve(row, column, rule);
        }
    }

    public void clearButtAction() {
        // Clear inputRule
        inputRuleBox.setText("");
        inputRule = "";

        // Clear inputString
        inputStringBox.setText("");
        inputString = "";

        // Clear Table
        resultTableModel.setDataVector(new Object[][] {}, new String[] {});

        // Clear Rule
        rule = null;

        // Clesr output box
        outputSolutionBox.setText("");
    }

    public void helpButtAction() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("documents/HowtoUse.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public void findStringFromGrammar() {
        JFrame f = new JFrame();
        if (rule != null) {
            // Show Dialog and get input from user
            String inputNum = JOptionPane.showInputDialog(f, "Enter the desired number.");

            if (inputNum != null) {
                int stringLength = Integer.parseInt(inputNum);
                // Find stop symbol in rule
                ArrayList<Character> sigma = new ArrayList<Character>();
                for (int k = 0; k < rule.length; k++) {
                    for (int l = 0; l < rule[k].terminal.size(); l++) {
                        if (Character.isLowerCase(rule[k].terminal.get(l).charAt(0))) {
                            if (!sigma.contains(rule[k].terminal.get(l).charAt(0))) {
                                sigma.add(rule[k].terminal.get(l).charAt(0));
                            }
                        }
                    }
                }

                // Random String until this rule accept or exceed limit
                int limitCount = 0;
                Random rand = new Random(25);
                while (limitCount < 5000) {
                    String newString = "";
                    for (int i = 0; i < stringLength; i++) {
                        int randNum = rand.nextInt(sigma.size());
                        newString += sigma.get(randNum);
                    }
                    readGrammarFromTextBox();
                    cyk.setRule(rule);
                    cyk.setInputString(newString);
                    cyk.solve();
                    this.table = cyk.getTable();
                    limitCount++;

                    if (table[stringLength - 1][0].equals(rule[0].nonTerminal)) {
                        inputStringBox.setText(newString);
                        break;
                    }
                }
                if (limitCount < 5000) {
                    JOptionPane.showMessageDialog(f, "Found a " + stringLength + "-character string that is accepted.");
                } else {
                    JOptionPane.showMessageDialog(f,
                            "Not found a " + stringLength + "-character string that is accepted.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(f, "No grammar is set.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void updateTable(String inputString) {
        String[] columnNames = new String[inputString.length()];
        for (int i = 0; i < inputString.length(); i++) {
            columnNames[i] = Character.toString(inputString.charAt(i));
        }
        myRenderer.setSize(inputString.length());
        resultTableModel.setDataVector(table, columnNames);
    }

    public void saveButtAction() throws IOException {
        pathSave.setBounds(60, 120, 750, 450);

        int ret = pathSave.showDialog(this, "Save");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File filePath = pathSave.getSelectedFile();
            String path = filePath.getPath();
            writeGrammarToFile(path);
        }
    }

    public void openButtAction() throws IOException {
        pathOpen.setBounds(60, 120, 750, 450);

        int ret = pathOpen.showDialog(this, "Open");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File filePath = pathOpen.getSelectedFile();
            String path = filePath.getPath();
            readGrammarFromFile(path);
        }
    }

    public void writeGrammarToTextBox() {
        // Clear JTextArea before set new text
        inputStringBox.setText("");
        inputRuleBox.setText("");

        // Set new text
        String rule_temp = "";
        for (int i = 0; i < rule.length; i++) {
            rule_temp += rule[i].nonTerminal + " -> ";
            for (int j = 0; j < rule[i].terminal.size(); j++) {
                if (j < rule[i].terminal.size() - 1) {
                    rule_temp += rule[i].terminal.get(j) + " | ";
                } else {
                    rule_temp += rule[i].terminal.get(j);
                }
            }
            rule_temp += "\n";
        }
        inputRuleBox.setText(rule_temp);
    }

    public void writeGrammarToFile(String path) {
        // Read grammar from jtextarea before write to file
        readGrammarFromTextBox();

        // Write grammar to file
        try (PrintWriter output = new PrintWriter(path + ".txt")) {
            for (int i = 0; i < rule.length; i++) {
                output.print(rule[i].nonTerminal + " -> ");
                for (int j = 0; j < rule[i].terminal.size(); j++) {
                    if (j < rule[i].terminal.size() - 1) {
                        output.print(rule[i].terminal.get(j) + " | ");
                    } else {
                        output.print(rule[i].terminal.get(j));
                    }
                }
                output.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readGrammarFromTextBox() {
        // Count number of rule from input text
        inputRule = inputRuleBox.getText();
        String lines[] = inputRule.split("\r?\n");

        // Read string from input
        Rule[] rule = new Rule[lines.length];
        for (int i = 0; i < lines.length; i++) {
            ArrayList<String> symbol = new ArrayList<String>();
            String[] str1 = lines[i].split(" -> ");
            String nonTermi = str1[0];
            String termi = str1[1];
            if (termi.length() > 2) {
                String[] str2 = termi.split(" \\| ");
                for (int j = 0; j < str2.length; j++) {
                    symbol.add(str2[j]);
                    // System.out.println(str2[j]);
                }
            } else {
                symbol.add(termi);
                // System.out.println(termi);
            }
            rule[i] = new Rule(nonTermi, symbol);
            // System.out.println(".");
        }
        this.rule = rule;
    }

    public void readGrammarFromFile(String path) {
        int lines = 0;
        // Count number of rule from input file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.readLine() != null) {
                lines++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read string from input
        Rule[] rule = new Rule[lines];
        try (Scanner input = new Scanner(new File(path))) {
            for (int i = 0; i < lines; i++) {
                ArrayList<String> symbol = new ArrayList<String>();
                String[] str1 = input.nextLine().split(" -> ");
                String nonTermi = str1[0];
                String termi = str1[1];
                if (termi.length() > 2) {
                    String[] str2 = termi.split(" \\| ");
                    for (int j = 0; j < str2.length; j++) {
                        symbol.add(str2[j]);
                        // System.out.println(str2[j]);
                    }
                } else {
                    symbol.add(termi);
                    // System.out.println(termi);
                }
                rule[i] = new Rule(nonTermi, symbol);
                // System.out.println(".");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        clearButtAction();
        this.rule = rule;
        writeGrammarToTextBox();
    }
}

class MyRenderer extends DefaultTableCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Variable
    int setSize;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!table.isRowSelected(row)) {
            if (row + column >= setSize) {
                c.setBackground(new java.awt.Color(204, 204, 204));
            } else {
                c.setBackground(table.getBackground());
            }
        }
        return c;
    }

    void setSize(int y) {
        setSize = y;
    }
}