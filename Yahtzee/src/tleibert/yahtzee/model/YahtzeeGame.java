package tleibert.yahtzee.model;
import java.util.*;

import tleibert.yahtzee.model.dice.DiceBlock;

/**
 * The YahtzeeGame class runs the Yahtzee Game.
 * 
 * @author Charlee Sherrill
 * @author Trevor Leibert
 * @author Nathan Woelfel
 */
public class YahtzeeGame 
{
    /** list of possible scoring types */
    public static final String[] SCORING_TYPES = {"ONES", "TWOS", "THREES", "FOURS", 
        "FIVES", "SIXES", "Three of a Kind", "Four of a Kind",
        "Full House", "Small Straight",
        "Large Straight", "YAHTZEE", "CHANCE"};

    /** minimum number of players in a game */
    public static final int MIN_PLAYERS = 2;

    /** maximum number of players in a game */
    public static final int MAX_PLAYERS = 4;

    /** each player can take 13 turns in Yahtzee */
    public static final int NUM_TURNS_PER_PLAYER = 13;
    
    /** DiceBlock that holds the dice */
    private DiceBlock diceBlock;

    /** player scores are handled by an array of ScoreCard objects */
    private ScoreCard[] scoreCards;

    /** number of players in the game */
    private int numPlayers;

    /** number of rolls per turn */
    private int numRolls;

    /** category player scores in */
    private int category;

    /** player who will take the next turn */
    private int selectedPlayer;

    /** the number of turns that have been taken so far */
    private int numTurns;

    /** the maximum number of turns that can be taken this game */
    private int maxNumTurns;

    /** random number generator */
    private Random rand;

    /** boolean array of selected die to re roll */
    private boolean[] selected;
    
    /** whether or not there is a tie */
    private boolean tie;

    /** whether the game is over or not */
    private boolean isOver;
    
    /**
    * Constructs the Yahtzee game
    *
    * @param players number of players in game
    */
    public YahtzeeGame(int players)
    {
        if (players < MIN_PLAYERS || players > MAX_PLAYERS) {
            throw new IllegalArgumentException("Invalid number of players");
        }

        numPlayers = players;
        scoreCards = new ScoreCard[players];
        
        for (int i = 0; i < scoreCards.length; i++) {
            scoreCards[i] = new ScoreCard();
        }
        
        diceBlock = new DiceBlock();
        numRolls = 0;
        category = 0;
        rand = new Random();
        selectedPlayer = rand.nextInt(numPlayers);
        numTurns = 0;
        maxNumTurns = NUM_TURNS_PER_PLAYER * (numPlayers);
        isOver = false;
    }
    
    /**
    * updates the boolean array of selected die
    *
    * @param newSelected new boolean array of selected die
    */
    public DiceBlock getDiceBlock() {
        return diceBlock;
    }
    
    /**
    * Getter method for the scoreCard objects
    *
    * @return ScoreCarda
    */
    public ScoreCard[] getScoreCards() {
        return scoreCards;
    }
    
    /**
    * Getter method for the number of players
    *
    * @return number of players
    */
    public int getNumPlayers() {
        return numPlayers;
    }
    
    /**
    * Getter method for the number of rolls
    *
    * @return number of rolls
    */
    public int getNumRolls() {
        return numRolls;
    }
    
    /**
    * Getter method for category player scores in
    *
    * @return category
    */
    public int getCategory() {
        return category;
    }
    
    /**
    * Getter metyhod for boolean array of selected dice
    *
    * @return selected array
    */
    public boolean[] getSelected() {
        return selected;
    }
    
    /**
     * Getter method for tie
     *
     * @return whether or not there is a tie
     */
    public boolean getTie() {
        return tie;
    }
    
    /**
     * Changes the array of selected dice
     */
    public void changeSelected(boolean[] newSelected)
    {
        for(int i = 0; i < selected.length; i++)
        {
            if(selected[i] != newSelected[i])
            {
                selected[i] = newSelected[i];
            }
        }
    }
    
    /**
     * The main method to be used by YahtzeeGameGUI. Takes a turn with
     * the selected dice configuration and scorin type, then increments
     * the player turn counter so that the next time this method is called,
     * the next player's turn is taken.
     * 
     * @param dice DiceBlock to be scored
     * @param scoringType method of scoring for this diceblock
     * @return the score recieved from this turn.
     */
    public int takeTurn(DiceBlock dice, int scoringType) {
        if (numTurns < maxNumTurns) {
            int temp = scoreCards[selectedPlayer].score(dice, scoringType);
            selectedPlayer++;
            if (selectedPlayer == numPlayers) {
                selectedPlayer = 0;
            }
            numTurns++;
            isOver = maxNumTurns == numTurns;
            return temp;

        } else {
            return -1;
        }
    }

    /**
     * Returns true if the game is over, false if it isn't
     * @return whether game is over
     */
    public boolean getIsOver() {
        return isOver;
    }

    /** Returns the selected player in an index format
     * @return selectedPlayer
     */
    public int getSelectedPlayer() {
        return selectedPlayer;
    }

    /**
     * Returns the selected player in a displayable format.
     * @return selectedPlayer + 1
     */
    public int getTurn() {
        return selectedPlayer + 1;
    }

    /**
     * Gets the score of the current player.
     * @return current player's score
     */
    public int getCurrentPlayerScore() {
        return scoreCards[selectedPlayer].getScore();
    }

    /**
     * Returns an int array with the current scores of each player
     * @return player scores
     */
    public int[] getScores() {
        int[] scores = new int[numPlayers];
        
        for (int i = 0; i < numPlayers; i++) {
            scores[i] = scoreCards[i].getScore();
        }
        return scores;
    }

    /**
     * Returns the player number (0-3) of the player with the highest score
     * 
     * @return player number
     */
    public int getWinner() {
        int[] scores = getScores();

        int highScore = Integer.MIN_VALUE;
        for (int i = 0; i < scores.length; i++) {
            highScore = Math.max(highScore, scores[i]);
        }

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == highScore) {
                return i;
            }
        }
        return -1;
    }
         
}