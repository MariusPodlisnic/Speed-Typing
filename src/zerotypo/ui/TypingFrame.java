package zerotypo.ui;

import zerotypo.logic.WordGenerator;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class TypingFrame extends JFrame {
    private JPanel mainPanel;
    private JTextPane textDisplay;
    private JTextField inputField;
    private String targetText;
    private WordGenerator wordGenerator;

    public TypingFrame() {
        wordGenerator = new WordGenerator();

        setTitle("ZeroTypo");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        startNewTest();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        textDisplay = new JTextPane();
        textDisplay.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textDisplay.setEditable(false);
        textDisplay.setFocusable(false);

        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkTyping(); }
            public void removeUpdate(DocumentEvent e) { checkTyping(); }
            public void changedUpdate(DocumentEvent e) { checkTyping(); }
        });

        mainPanel.add(new JScrollPane(textDisplay), BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void startNewTest() {
        targetText = wordGenerator.generateWords(30);
        textDisplay.setText(targetText);
        inputField.setText("");
        highlightText("");
    }

    private void checkTyping() {
        String typed = inputField.getText();
        highlightText(typed);
    }

    private void highlightText(String typed) {
        StyledDocument doc = textDisplay.getStyledDocument();

        Style defaultStyle = textDisplay.addStyle("Default", null);
        StyleConstants.setForeground(defaultStyle, Color.GRAY);

        Style correctStyle = textDisplay.addStyle("Correct", null);
        StyleConstants.setForeground(correctStyle, new Color(40, 167, 69));

        Style incorrectStyle = textDisplay.addStyle("Incorrect", null);
        StyleConstants.setForeground(incorrectStyle, Color.RED);
        StyleConstants.setBackground(incorrectStyle, new Color(255, 200, 200));

        doc.setCharacterAttributes(0, targetText.length(), defaultStyle, true);

        int checkLength = Math.min(typed.length(), targetText.length());

        for (int i = 0; i < checkLength; i++) {
            if (typed.charAt(i) == targetText.charAt(i)) {
                doc.setCharacterAttributes(i, 1, correctStyle, true);
            } else {
                doc.setCharacterAttributes(i, 1, incorrectStyle, true);
            }
        }
    }
}