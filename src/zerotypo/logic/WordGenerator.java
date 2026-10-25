package zerotypo.logic;

import java.util.Random;

public class WordGenerator {

    private final String[] easyBank = {
            "the cat sat on the mat", "we are going to the park",
            "typing is a fun game", "hello world how are you",
            "keep your hands relaxed", "speed comes with practice",
            "zero typo is the best app", "just keep practicing daily",
            "water is good for health", "i love to code in java"
    };

    private final String[] mediumBank = {
            "The quick brown fox jumps over the lazy dog.",
            "Programming is the art of algorithm design.",
            "A journey of a thousand miles begins with a single step.",
            "Java is a high-level and object-oriented language.",
            "Speed and accuracy are both essential for a good score.",
            "Always remember to close your brackets and semicolons.",
            "The best way to predict the future is to invent it.",
            "Clean code always looks like it was written by someone who cares.",
            "Typing fast requires muscle memory and relaxed hands."
    };

    private final String[] hardBank = {
            "public static void main(String[] args) {",
            "System.out.println(\"Hello, World! Error: 404\");",
            "Cost = $4,599.99 (including 7.5% tax!).",
            "Regex: ^[a-zA-Z0-9_.-]+@[a-zA-Z0-9.-]+$",
            "for (int i = 0; i <= 100; i++) { count += i; }",
            "User_ID_9843: connection timed out at 19:45:00 UTC.",
            "What's the meaning of Life, the Universe, & Everything? 42.",
            "Map<String, Integer> map = new HashMap<>();",
            "[Warning] OutOfMemoryError: Java heap space (Size: 2GB)."
    };

    public String generateText(int count, String difficulty) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String[] selectedBank;

        switch (difficulty) {
            case "Easy":
                selectedBank = easyBank;
                break;
            case "Hard":
                selectedBank = hardBank;
                break;
            case "Medium":
            default:
                selectedBank = mediumBank;
                break;
        }

        for (int i = 0; i < count; i++) {
            sb.append(selectedBank[random.nextInt(selectedBank.length)]);
            if (i < count - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}