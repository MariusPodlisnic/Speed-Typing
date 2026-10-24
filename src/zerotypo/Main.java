package zerotypo;

import javax.swing.SwingUtilities;
import zerotypo.ui.TypingFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TypingFrame().setVisible(true);
        });
    }
}