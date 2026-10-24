package zerotypo.logic;

import java.util.Random;

public class WordGenerator {
    private final String[] wordBank = {
            "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
            "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
            "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
            "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
            "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
            "java", "code", "type", "zero", "speed", "fast", "logic", "swing", "class"
    };

    public String generateWords(int count) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            sb.append(wordBank[random.nextInt(wordBank.length)]);
            if (i < count - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}