package zerotypo.logic;

import java.util.Random;

public class WordGenerator {
    private final String[] sentenceBank = {
            "The quick brown fox jumps over the lazy dog.",
            "Programming is the art of algorithm design and the craft of debugging.",
            "A journey of a thousand miles begins with a single step.",
            "To be or not to be, that is the question.",
            "Java is a high-level and object-oriented programming language.",
            "Speed and accuracy are both essential for a good typing test score.",
            "Always remember to close your brackets and semicolons.",
            "The best way to predict the future is to invent it.",
            "Success is not final, failure is not fatal.",
            "In the middle of every difficulty lies opportunity.",
            "Software and cathedrals are much the same, first we build them, then we pray.",
            "Clean code always looks like it was written by someone who cares.",
            "Typing fast requires muscle memory and relaxed hands."
    };

    public String generateText(int sentenceCount) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sentenceCount; i++) {
            sb.append(sentenceBank[random.nextInt(sentenceBank.length)]);
            if (i < sentenceCount - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}