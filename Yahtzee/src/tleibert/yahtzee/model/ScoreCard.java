package tleibert.yahtzee.model;

import java.util.Arrays;

import tleibert.yahtzee.model.dice.DiceBlock;

/**
 * Represents the scorecard for one player in Yahtzee.
 * This class handles the scoring of the various dice
 * configurations in Yahtzee.
 *
 * @author Trevor Leibert
 */
public class ScoreCard {

    // cases for scoring
    /** scoring for ones is represented by 0 */
    public static final int ONES = 0;

    /** scoring for twos is represented by 1 */
    public static final int TWOS = 1;

    /** scoring for threes is represented by 2 */
    public static final int THREES = 2;

    /** scoring for fours is represented by 3 */
    public static final int FOURS = 3;

    /** scoring for fives is represented by 4 */
    public static final int FIVES = 4;

    /** scoring for sixes is represented by 5 */
    public static final int SIXES = 5;

    /** scoring for three of a kind is represented by 6 */
    public static final int THREE_OF_KIND = 6;

    /** scoring for four of a kind is represented by 7 */
    public static final int FOUR_OF_KIND = 7;

    /** scoring for a full house is represented by 8 */
    public static final int FULL_HOUSE = 8;

    /** scoring for a small straight is represented by 9 */
    public static final int SMALL_STRAIGHT = 9;

    /** scoring for a large straight is represented by 10 */
    public static final int LARGE_STRAIGHT = 10;

    /** scoring for a yahtzee is represented by 11 */
    public static final int YAHTZEE = 11;

    /** scoring for chance is represented by 12 */
    public static final int CHANCE = 12;

    // point constants

    /** amount of points that a full house is worth */
    public static final int FULL_HOUSE_POINTS = 25;

    /** amount of points that a small straight is worth */
    public static final int SMALL_STRAIGHT_POINTS = 30;

    /** amount of points that a large straight is worth */
    public static final int LARGE_STRAIGHT_POINTS = 40;

    /** amount of points that the first Yahtzee a player gets is worth */
    public static final int YAHTZEE_POINTS = 50;

    /** amount of points that every subsequent Yahtzee is worth */
    public static final int YAHTZEE_BONUS = 100;

    /** if the upper score section points are equal to or above this value, bonus points are added */
    public static final int BONUS_SCORE_THRESHOLD = 63;

    /** amount of bonus points to add for the upper score bonus  */
    public static final int BONUS_SCORE_POINTS = 35;

    // instance variables
    /** the current score that the player has */
    private int score;

    /** the total upper score that the player has */
    private int upperScore;

    /** stores whether each upper score box has been used or not */
    private boolean[] upperScoreChecked;

    /** stores whether a player has had a Yahtzee this game */
    private boolean hadYahtzee;

    /** stores whether a player has recieved the upper score bonus this game */
    private boolean hadBonusScore;
    
    /**
     * Constructor for the ScoreCard class. A new ScoreCard will have its scores
     * set to zero, and all boolean variables set to false.
     */
    public ScoreCard() {
        score = 0;
        upperScore = 0;
        hadYahtzee = false;
        upperScoreChecked = new boolean[SIXES + 1];
        Arrays.fill(upperScoreChecked, false);
        hadBonusScore = false;
    }

    /**
     * This method scores a given set of dice, based on the scoring criteria
     * selected by the player. It will add the score gained to the player's total score, 
     * as well as returning the amount of points that were added.
     *  
     * @param dice a DiceBlock object representing the dice that will
     *             be scored.
     * @param scoringType the selected criteria of scoring the dice.
     * @return the score given by this call of the method. Will be zero
     *         if the scoring requirements for the chosen criteria of scoring
     *         were not met.
     * @throws IllegalArgumentException if scoring type isn't between 0 and 12 (inclusive)
     */
    public int score(DiceBlock dice, int scoringType) {
        if (scoringType < 0 || scoringType > 12) {
            throw new IllegalArgumentException("Invalid scoring type");
        }
        
        int tempScore = 0;
        int[] diceTally = dice.getTally();

        switch (scoringType) {
        // upper score
        // ones
            case ONES:
                tempScore = diceTally[ONES] * (ONES + 1);
                upperScore += tempScore;
                upperScoreChecked[ONES] = true;
                bonusScore();
                break;

            // twos
            case TWOS :
                tempScore = diceTally[TWOS] * (TWOS + 1);
                upperScore += tempScore;
                upperScoreChecked[TWOS] = true;
                bonusScore();
                break;
            // threes  
            case THREES :
                tempScore = diceTally[THREES] * (THREES + 1);
                upperScore += tempScore;
                upperScoreChecked[THREES] = true;
                bonusScore();
                break;
            
            // fours
            case FOURS : 
                tempScore = diceTally[FOURS] * (FOURS + 1);
                upperScore += tempScore;
                upperScoreChecked[FOURS] = true;
                bonusScore();
                break;

            // fives
            case FIVES :
                tempScore = diceTally[FIVES] * (FIVES + 1);
                upperScore += tempScore;
                upperScoreChecked[FIVES] = true;
                bonusScore();
                break;
            
            // sixes
            case SIXES :
                tempScore = diceTally[SIXES] * (SIXES + 1);
                // check if bonus score applies
                upperScore += tempScore;
                upperScoreChecked[SIXES] = true;
                bonusScore();
                break;
            
            // lower score
            // three of a kind
            case THREE_OF_KIND : 
                for (int i = 0; i < diceTally.length; i++) {
                    if (diceTally[i] >= 3) {
                        tempScore = dice.getSumOfDice();
                    }
                }
                break;

            // four of a kind
            case FOUR_OF_KIND :
                for (int i = 0; i < diceTally.length; i++) {
                    if (diceTally[i] >= 4) {
                        tempScore = dice.getSumOfDice();
                    }
                }
                break;
                
            // full house
            case FULL_HOUSE :
                boolean had2 = false;
                boolean had3 = false;
                for (int i = 0; i < diceTally.length; i++) {
                    if (diceTally[i] == 2) {
                        had2 = true;
                    } else if (diceTally[i] == 3) {
                        had3 = true;
                    }
                }
                if (had2 && had3) {
                    tempScore = FULL_HOUSE_POINTS;
                }

                break;
            
            // small straight
            case SMALL_STRAIGHT :
                if (isSmallStraight(diceTally)) {
                    tempScore = SMALL_STRAIGHT_POINTS;
                }
                break;
            
            // large straight
            case LARGE_STRAIGHT :
                if (isLargeStraight(diceTally)) {
                    tempScore = LARGE_STRAIGHT_POINTS;
                }
                break;
            
            case YAHTZEE :
                if (isYahtzee(diceTally)) {
                    if (!hadYahtzee) {
                        tempScore = YAHTZEE_POINTS;
                        hadYahtzee = true;
                    } else {
                        tempScore = YAHTZEE_BONUS;
                    }
                }
                break;

            case CHANCE :
                tempScore = dice.getSumOfDice();
                break;
            default :   
        }
        
        this.score += tempScore;
        return tempScore;
    }

    /**
     * This method will add bonus points to the player's score if their
     * upper score total is greater than or equal to the limit.
     */
    private void bonusScore() {
        if (!hadBonusScore) {
            boolean[] allTrue = new boolean[upperScoreChecked.length];
            Arrays.fill(allTrue, true);
            if (Arrays.equals(upperScoreChecked, allTrue)) {
                if (upperScore >= BONUS_SCORE_THRESHOLD) {
                    score += BONUS_SCORE_POINTS;
                }
            }
            hadBonusScore = true;
        }
    }

    /**
     * Returns if a user has recieved bonus score or not.
     * @return true if the player has recieved bonus points,
     *         false if they have not.
     */
    public boolean getHadBonusScore() {
        return hadBonusScore;
    }

    /**
     * Tests if the given arrangement of dice is a Yahtzee (5 of a kind)
     * 
     * @param diceTally tally array with 6 elements, with the one at [0]
     *                  representing the number of ones, and the one at [5]
     *                  repersenting the number of sixes.
     * @return true if the given tally represents a Yahtzee, false if it doesn't
     */
    private static boolean isYahtzee(int[] diceTally) {
        for (int i = 0; i < diceTally.length; i++) {
            if (diceTally[i] == 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if a given arrangment of dice is a small straight (4 in a row)
     * @param diceTally tally array with 6 elements, with the one at [0]
     *                  representing the number of ones, and the one at [5]
     *                  repersenting the number of sixes.
     * @return true if the given tally is a small straight, false if it isn't.
     */
    private static boolean isSmallStraight(int[] diceTally) {
        //case 1 -- 1234
        if(diceTally[0] >= 1)
        {
            if(diceTally[1] >= 1)
            {
                if(diceTally[2] >= 1)
                {
                    if(diceTally[3] >= 1)
                    {
                        return true;
                    }
                }
            }
        }
        //case 2 -- 2345
        if(diceTally[1] >= 1)
        {
            if(diceTally[2] >= 1)
            {
                if(diceTally[3] >= 1)
                {
                    if(diceTally[4] >= 1)
                    {
                        return true;
                    }
                }
            }
        }
        //case 3 -- 3456
        if(diceTally[2] >= 1)
        {
            if(diceTally[3] >= 1)
            {
                if(diceTally[4] >= 1)
                {
                    if(diceTally[5] >= 1)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Tests if the entered tally represents a large straight or not.
     * @param diceTally tally array representing the amount of each dice value.
     * @return true if the given tally array represents a large straight.
     */
    private static boolean isLargeStraight(int[] diceTally) {
        // large straight is five in a row, which can only happen in two ways.
        int[] possibility1 = {1, 1, 1, 1, 1, 0};
        int[] possibility2 = {0, 1, 1, 1, 1, 1};

        return Arrays.equals(diceTally, possibility1) ||
               Arrays.equals(diceTally, possibility2);
    }
 
    /**
     * Returns the user's current total score.
     * @return the total score.
     */
    public int getScore() {
        return score;
    }
}
