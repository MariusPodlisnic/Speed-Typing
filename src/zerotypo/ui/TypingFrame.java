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
    private JPanel topPanel;
    private JTextPane textDisplay;
    private JTextField inputField;
    private JLabel timerLabel;
    private JButton restartButton;

    private String targetText;
    private WordGenerator wordGenerator;

    private Timer timer;
    private int timeLeft = 30;
    private boolean isRunning = false;

    public TypingFrame() {
        wordGenerator = new WordGenerator();

        setTitle("ZeroTypo");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setupTimer();
        startNewTest();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        topPanel = new JPanel(new BorderLayout());

        timerLabel = new JLabel("Time: 30");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setForeground(new Color(100, 100, 100));

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> startNewTest());

        topPanel.add(timerLabel, BorderLayout.CENTER);
        topPanel.add(restartButton, BorderLayout.EAST);

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

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(textDisplay), BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void setupTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                endTest();
            }
        });
    }

    private void startNewTest() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        targetText = wordGenerator.generateWords(30);
        textDisplay.setText(targetText);

        Runnable resetInput = () -> {
            inputField.setText("");
            inputField.setEditable(true);
            inputField.requestFocus();
        };
        SwingUtilities.invokeLater(resetInput);

        timeLeft = 30;
        timerLabel.setText("Time: " + timeLeft);
        isRunning = false;

        highlightText("");
    }

    private void checkTyping() {
        String typed = inputField.getText();

        if (!isRunning && typed.length() > 0 && inputField.isEditable()) {
            isRunning = true;
            timer.start();
        }

        // --- NEW IN COMMIT 6: Dynamic Appending ---
        if (targetText.length() - typed.length() < 50) {
            String newWords = " " + wordGenerator.generateWords(15);
            targetText += newWords;

            try {
                StyledDocument doc = textDisplay.getStyledDocument();
                doc.insertString(doc.getLength(), newWords, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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

    private void endTest() {
        timer.stop();
        inputField.setEditable(false);
        isRunning = false;

        String typed = inputField.getText();
        int correctChars = 0;
        int checkLength = Math.min(typed.length(), targetText.length());

        for (int i = 0; i < checkLength; i++) {
            if (typed.charAt(i) == targetText.charAt(i)) {
                correctChars++;
            }
        }

        double wpm = (correctChars / 5.0) / 0.5;
        double accuracy = typed.length() == 0 ? 0 : ((double) correctChars / typed.length()) * 100;

        String message = String.format("Time's up!\n\nWPM: %.0f\nAccuracy: %.1f%%", wpm, accuracy);
        JOptionPane.showMessageDialog(this, message, "Test Complete", JOptionPane.INFORMATION_MESSAGE);

        startNewTest();
    }
}