/*

BaseGame.java
Game base framework class

Assignment for COM6471 Fundamentals of Object Oriented Programming (Autumn 2023-24)
This assignment contains three student-completed java files and a java startup file provided by the tutor

Author: Haolin Yang
Registration No.: 230222782
Creat Date : 15/11/2023
Complete Date : 26/12/2023

*/

import java.io.IOException;
import java.util.ArrayList;
import sheffield.*;

public abstract class BaseGame {

    protected boolean gameActive;
    protected boolean isWin;
    protected int turnCount;
    protected String[] historyWords;
    protected char[] guessIn;
    protected char[] responseOut;
    protected char[] correctWord;
    protected ArrayList<String> wordsPool;
    protected EasyReader reader;
    protected EasyWriter writer;

    public BaseGame(EasyReader k, EasyWriter s) {
        this.gameActive = true;
        this.isWin = false;
        this.turnCount = 0;
        this.historyWords = new String[6];
        this.guessIn = new char[5];
        this.responseOut = new char[5];
        this.correctWord = new char[5];
        this.wordsPool = new ArrayList<>(5757);
        this.reader = k;
        this.writer = s;
        loadWordsFromFile();
    }

    // Load words from the word file into the list
    private void loadWordsFromFile() {
        String filePath = "sgb-words.txt";
        try (EasyReader fileReader = new EasyReader(filePath)) {
            fileReader.readString(); // Read and skip the first line (number line)
            while (!fileReader.eof()) {
                String word = fileReader.readString();
                if (!word.isEmpty()) {
                    this.wordsPool.add(word);
                }
            }
            // writer.println(wordsPool.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void run();

    // Compare the guessed word with the correct word
    public void matchOnChar() {
        // Mark the correctly positioned character as '+'
        for (int i = 0; i < this.guessIn.length; i++) {
            if (this.guessIn[i] == this.correctWord[i]) {
                this.responseOut[i] = '+'; // If the positions are the same and the
                                           // characters are the same, mark the
                                           // corresponding positions with '+'
            }
        }

        // Then mark only the character correct as '*'.
        for (int i = 0; i < this.guessIn.length; i++) {
            for (int j = 0; j < this.correctWord.length; j++) {
                if (this.guessIn[i] == this.correctWord[j] && this.responseOut[i] != '+') {
                    this.responseOut[i] = '*'; // Mark '*' only if a match is found
                                               // and the current position is not a '+'.
                    break; // Exit the inner loop when a match is found
                }
            }
        }

        // Finally set all untagged positions to 'X'
        for (int i = 0; i < this.responseOut.length; i++) {
            if (this.responseOut[i] == '\u0000') { // Check if it's empty
                this.responseOut[i] = 'X'; // Set the empty space to 'X'
            }
        }

        // for (char c : responseOut) {
        // writer.print(c);
        // }
    }

    // Record the guessed word in the history list
    public void recordGuess() {

        historyWords[turnCount] = new String(guessIn);
    };

    // Check if the game continues
    public void checkGameState(String playerType) {

        boolean allCorrect = true;
        for (char c : responseOut) {
            if (c != '+') { // If there's not a "+" in any position,
                            // you haven't guessed correctly.
                allCorrect = false;
                break;
            }
        }

        // Game Over Determination
        if (allCorrect || turnCount >= 5) { // Guess right or it's six rounds in.
            gameActive = false;
            if (allCorrect) { // Guess right
                isWin = true;
                writer.println(playerType + " guessed the right word, congratulations!");
            } else { // No round left, game over
                writer.println(playerType + " did not manage to guess the word this time.");
            }
            writer.print("The correct answer was : ");
            for (char c : correctWord) {
                writer.print(c);
            }
            writer.println();
        }

        turnCount++;
        // writer.println(turnCount);

        // End of round, clear guessIn and responseOut
        for (int i = 0; i < guessIn.length; i++) {
            guessIn[i] = '\u0000';
        }
        for (int i = 0; i < responseOut.length; i++) {
            responseOut[i] = '\u0000';
        }

    };

}
