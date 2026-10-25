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
    private JPanel statsPanel;
    private JPanel controlPanel;
    private JTextPane textDisplay;
    private JTextField inputField;
    private JLabel timerLabel;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JButton restartButton;
    private JComboBox<String> timeSelector;

    private String targetText;
    private WordGenerator wordGenerator;

    private Timer timer;
    private int selectedDuration = 30;
    private int timeLeft = 30;
    private boolean isRunning = false;
    private long startTime = 0;

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

        statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        accuracyLabel = new JLabel("Acc: 0%");
        accuracyLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        statsPanel.add(wpmLabel);
        statsPanel.add(accuracyLabel);

        timerLabel = new JLabel("Time: " + selectedDuration);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setForeground(new Color(100, 100, 100));

        controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        String[] timeOptions = {"30s", "1m", "1m 30s", "2m"};
        timeSelector = new JComboBox<>(timeOptions);
        timeSelector.setFont(new Font("Monospaced", Font.BOLD, 16));
        timeSelector.setFocusable(false);
        timeSelector.addActionListener(e -> {
            updateSelectedDuration();
            startNewTest();
        });

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> startNewTest());

        controlPanel.add(timeSelector);
        controlPanel.add(restartButton);

        topPanel.add(statsPanel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.CENTER);
        topPanel.add(controlPanel, BorderLayout.EAST);

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

    private void updateSelectedDuration() {
        int index = timeSelector.getSelectedIndex();
        switch (index) {
            case 0: selectedDuration = 30; break;
            case 1: selectedDuration = 60; break;
            case 2: selectedDuration = 90; break;
            case 3: selectedDuration = 120; break;
            default: selectedDuration = 30; break;
        }
    }

    private void setupTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            updateLiveStats();
            if (timeLeft <= 0) {
                endTest();
            }
        });
    }

    private void startNewTest() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        targetText = wordGenerator.generateText(3);
        textDisplay.setText(targetText);

        Runnable resetInput = () -> {
            inputField.setText("");
            inputField.setEditable(true);
            inputField.requestFocus();
        };
        SwingUtilities.invokeLater(resetInput);

        timeLeft = selectedDuration;
        timerLabel.setText("Time: " + timeLeft);
        wpmLabel.setText("WPM: 0");
        accuracyLabel.setText("Acc: 0%");
        isRunning = false;
        startTime = 0;

        highlightText("");
    }

    private void checkTyping() {
        String typed = inputField.getText();

        if (!isRunning && typed.length() > 0 && inputField.isEditable()) {
            isRunning = true;
            startTime = System.currentTimeMillis();
            timer.start();
        }

        if (targetText.length() - typed.length() < 80) {
            String newText = " " + wordGenerator.generateText(2);
            targetText += newText;

            try {
                StyledDocument doc = textDisplay.getStyledDocument();
                doc.insertString(doc.getLength(), newText, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        highlightText(typed);

        if (isRunning) {
            updateLiveStats();
        }
    }

    private void updateLiveStats() {
        String typed = inputField.getText();
        if (typed.isEmpty() || startTime == 0) return;

        int correctChars = 0;
        int checkLength = Math.min(typed.length(), targetText.length());

        for (int i = 0; i < checkLength; i++) {
            if (typed.charAt(i) == targetText.charAt(i)) {
                correctChars++;
            }
        }

        long now = System.currentTimeMillis();
        double elapsedMinutes = (now - startTime) / 60000.0;

        double wpm = 0;
        if (elapsedMinutes > 0) {
            wpm = (correctChars / 5.0) / elapsedMinutes;
        }

        double accuracy = ((double) correctChars / typed.length()) * 100;

        wpmLabel.setText(String.format("WPM: %.0f", wpm));
        accuracyLabel.setText(String.format("Acc: %.1f%%", accuracy));
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
        int incorrectChars = 0;
        int checkLength = Math.min(typed.length(), targetText.length());

        for (int i = 0; i < checkLength; i++) {
            if (typed.charAt(i) == targetText.charAt(i)) {
                correctChars++;
            } else {
                incorrectChars++;
            }
        }

        double elapsedMinutes = selectedDuration / 60.0;
        double wpm = (correctChars / 5.0) / elapsedMinutes;
        double accuracy = typed.length() == 0 ? 0 : ((double) correctChars / typed.length()) * 100;

        JPanel resultPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Test Complete!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 28));

        JLabel wpmResult = new JLabel(String.format("WPM: %.0f", wpm), SwingConstants.CENTER);
        wpmResult.setFont(new Font("Monospaced", Font.BOLD, 36));
        wpmResult.setForeground(new Color(40, 167, 69));

        JLabel accResult = new JLabel(String.format("Accuracy: %.1f%%", accuracy), SwingConstants.CENTER);
        accResult.setFont(new Font("Monospaced", Font.BOLD, 24));

        JLabel statsResult = new JLabel("Correct: " + correctChars + "  |  Incorrect: " + incorrectChars, SwingConstants.CENTER);
        statsResult.setFont(new Font("Monospaced", Font.PLAIN, 16));
        statsResult.setForeground(Color.GRAY);

        resultPanel.add(titleLabel);
        resultPanel.add(wpmResult);
        resultPanel.add(accResult);
        resultPanel.add(statsResult);

        JOptionPane.showMessageDialog(this, resultPanel, "ZeroTypo Results", JOptionPane.PLAIN_MESSAGE);

        startNewTest();
    }
}