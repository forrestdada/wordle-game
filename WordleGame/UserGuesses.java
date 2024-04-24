

import java.util.Random;
import sheffield.*;

public class UserGuesses extends BaseGame {

  protected char[] availableChars;

  public UserGuesses(EasyReader k, EasyWriter s) {
    super(k, s);
    initializeAvailableChars();
  }

  // Initialise the 26-letter alphabet that prompts the user
  private void initializeAvailableChars() {
    availableChars = new char[26];
    for (int i = 0; i < 26; i++) {
      availableChars[i] = (char) ('a' + i);
    }
  }

  // Updating the alphabet after each round of word guessing
  public void updateAvailableChars() {
    for (int i = 0; i < responseOut.length; i++) {
      if (responseOut[i] == 'X') {
        char incorrectChar = guessIn[i]; // Get the wrong letter
        for (int j = 0; j < availableChars.length; j++) {
          // Replace the corresponding letter with '_'
          if (availableChars[j] == incorrectChar) {
            availableChars[j] = '_';
            break; // Stops the loop because the letter has been found and replaced
          }
        }
      }
    }
  }

  public boolean checkWordValidity(String guess) {

    boolean isValid = true; // Assuming the word is initially valid
    writer.println("Checking -" + guess + "- is valid");

    if (guess.length() != 5) {
      // Incorrect word length, marked as invalid
      writer.println("Word -" + guess + "- is not the right length\nWord is not valid - try again");
      isValid = false;
    } else if (!guess.matches("[a-z]+")) {
      // Words containing illegal characters are marked as invalid
      writer.println("Word -" + guess + "- contains incorrect characters, use a-z only\nWord is not valid - try again");
      isValid = false;
    } else if (java.util.Arrays.asList(historyWords).contains(guess)) {
      // Word has been tried are marked as invalid
      writer.println("Word -" + guess + "- has already been tried\nWord is not valid - try again");
      isValid = false;
    } else {
      writer.println("Word -" + guess + "contains a-z only\nWord -" + guess + "- is valid");
    }

    return isValid;
  };

  // Actions and instructions at the start of the game
  public void computerPickWord() {
    writer.println("Constructing a new game with user guesses.");
    // Randomly get words from wordsPool
    Random random = new Random();
    int randomIndex = random.nextInt(wordsPool.size());
    String selectedWord = wordsPool.get(randomIndex);
    correctWord = selectedWord.toCharArray();
    writer.println("The computer has selected a word, and you have 6 attempts to guess it.");
    writer.println("The computer will respond with:");
    writer.println("+ to denote a correct character in the right location.");
    writer.println("* to denote a correct character.");
    writer.println("X to denote an incorrect character.");
  };

  // Prompts at the start of each round
  public void turnBeginRemider() {
    writer.println();
    writer.println("Attempt " + (turnCount + 1) + " of 6");
    writer.println("Available letters: ");
    for (char c : availableChars) {
      writer.print(c);
    }
    writer.println();
  };

  // Get the words that the user enters from the keyboard
  public void getWord() {
    // writer.println("---------------");
    String guess;
    boolean validInput;
    do {
      guess = reader.readString("Please enter a word, 5 characters long : ");
      validInput = checkWordValidity(guess);
    } while (!validInput);
    guessIn = guess.toCharArray();
    // writer.println(guessIn);
  };

  // Displays the result of comparing the guessed word with the correct word
  public void turnEndRemider() {
    writer.println();
    writer.println("Your guess : " + new String(guessIn));
    writer.print("Response : ");
    for (char c : responseOut)
      writer.print(c);
    writer.println();
    // writer.println("--------------------");
  };

  @Override
  public void run() {
    computerPickWord();
    while (gameActive) {
      turnBeginRemider();
      getWord();
      recordGuess();
      matchOnChar();
      updateAvailableChars();
      turnEndRemider();
      checkGameState("The user");
    }

  }

}
