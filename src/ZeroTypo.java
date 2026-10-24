import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ZeroTypo extends JFrame {
    private JPanel mainPanel;
    private JTextArea textDisplay;
    private JTextField inputField;


    private final String[] wordBank = {
            "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
            "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
            "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
            "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
            "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
            "java", "code", "type", "zero", "speed", "fast", "logic", "swing", "class"
    };

    private String targetText = "";

    public ZeroTypo() {

        setTitle("ZeroTypo");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();


        generateWords(30); // Generates 30 random words
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        textDisplay = new JTextArea();
        textDisplay.setFont(new Font("Monospaced", Font.PLAIN, 24));
        textDisplay.setLineWrap(true);
        textDisplay.setWrapStyleWord(true);
        textDisplay.setEditable(false);
        textDisplay.setFocusable(false);

        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        mainPanel.add(textDisplay, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);
        add(mainPanel);
    }


    private void generateWords(int wordCount) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < wordCount; i++) {
            int randomIndex = random.nextInt(wordBank.length);
            sb.append(wordBank[randomIndex]);


            if (i < wordCount - 1) {
                sb.append(" ");
            }
        }

        targetText = sb.toString();
        textDisplay.setText(targetText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ZeroTypo().setVisible(true);
        });
    }
}