/*

ComputerGuesses.java
Computer game rules class with parent class BaseGam

Assignment for COM6471 Fundamentals of Object Oriented Programming (Autumn 2023-24)
This assignment contains three student-completed java files and a java startup file provided by the tutor

Author: Haolin Yang
Registration No.: 230222782
Creat Date : 15/11/2023
Complete Date : 26/12/2023

*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import sheffield.*;

public class ComputerGuesses extends BaseGame {

  protected int possibleGuessNum;
  protected char[] previousResult;
  protected ArrayList<String> currentWordsPool;

  public ComputerGuesses(EasyReader k, EasyWriter s) {
    super(k, s);
    this.possibleGuessNum = 26;
    this.previousResult = new char[5];
    // Copy the original word pool, which will be used for filtering and
    // modification.
    currentWordsPool = new ArrayList<>(wordsPool);
  };

  // User picks the right word for the computer
  public void userPickWord() {
    writer.println("Constructing a new game with Computer guesses");
    int wordIndex = reader.readInt("Select a word by choosing a number between 0 and 5756 : ");
    String selectedWord = wordsPool.get(wordIndex);
    correctWord = selectedWord.toCharArray();
  };

  // Algorithms for computer game playing
  public void smartWordPicker() {
    Random random = new Random();
    writer.println();
    writer.println("Attempt " + (turnCount + 1) + " of 6");
    if (turnCount == 0) {
      // First round, pick a word at random
      guessIn = currentWordsPool.get(random.nextInt(currentWordsPool.size())).toCharArray();
    } else {
      // From round 2, update the word pool based on the previous result
      ArrayList<String> newWordsPool = new ArrayList<>();
      // for (char c : responseOut) {
      // writer.print(c);
      // }
      char[] previousGuess = historyWords[turnCount - 1].toCharArray();
      for (String word : currentWordsPool) {
        boolean isValid = true;
        for (int i = 0; i < previousGuess.length; i++) {
          char guessChar = previousGuess[i];
          char wordChar = word.charAt(i);
          /*
           * '+' in responseOut means correct character and position; exclude words with
           * different character at this position.
           * 'X' in responseOut means character not in the word; exclude words with this
           * character at the same position.
           * '*' in responseOut means correct character but wrong position; exclude words
           * with this character at the same position or without this character.
           */
          if ((previousResult[i] == '+' && guessChar != wordChar) ||
              (previousResult[i] == 'X' && guessChar == wordChar) ||
              (previousResult[i] == '*' && (guessChar == wordChar || !word.contains(String.valueOf(guessChar))))) {
            isValid = false;
            break;
          }
        }
        if (isValid) {
          newWordsPool.add(word);
        }
      }
      // Update word pool
      currentWordsPool = newWordsPool;
      writer.println("Correct word : " + new String(correctWord) + ", previous guess : " + historyWords[turnCount - 1]);
      writer.println("Total number of possible words is : " + currentWordsPool.size());
      // Randomly select a word from the filtered word pool
      int randomWordIndex = random.nextInt(currentWordsPool.size());
      guessIn = currentWordsPool.get(randomWordIndex).toCharArray();
      writer.println("Selecting a random word number : " + (randomWordIndex + 1) + "/" + currentWordsPool.size());

    }

  };

  // Output computer guesses and comparison results
  public void displayGuess() {
    writer.print("guess is ");
    for (char c : guessIn) {
      writer.print(c);
    }
    writer.println();

    for (char c : responseOut) {
      writer.print(c);
    }
    writer.println();

    // Since responsOut is emptied at the end of each round, it needs to be recorded
    // for filtering the word pool for the next round.
    previousResult = Arrays.copyOf(responseOut, responseOut.length);
  };

  @Override
  public void run() {
    userPickWord();
    while (gameActive) {
      smartWordPicker();
      recordGuess();
      matchOnChar();
      displayGuess();
      checkGameState("The computer");
    }
  };

}
