/*

    WordleGame.java

    Class to instantiate and start a wordle game,
    provided as a starting point for the COM6471
    assignment in 2023.

    Zhixiang Chen, October 2023

*/

import sheffield.*;

public class WordleGame {
    public WordleGame() {
        System.out.println("A new game has been created");

        EasyReader keyboard = new EasyReader();
        EasyWriter screen = new EasyWriter();
        boolean keepPlaying = true;

        while (keepPlaying) {
            // select player, user or computer
            int gameType = 0;
            while ((gameType < 1) || (gameType > 2))
                gameType = keyboard.readInt("Please select either (1) User guesses or (2) Computer guesses : ");

            // set up game
            BaseGame theGame;

            if (gameType == 1)
                theGame = new UserGuesses(keyboard, screen);
            else
                theGame = new ComputerGuesses(keyboard, screen);

            // run the game
            theGame.run();

            String playAgain = keyboard.readString("Press y to play again, any other key to exit : ");
            if (!playAgain.equals("y")) {
                keepPlaying = false;
            }
        }
    }

    // main method to instantiate a WordleGame object
    public static void main(String[] args) {
        WordleGame newGame = new WordleGame();
    }
}