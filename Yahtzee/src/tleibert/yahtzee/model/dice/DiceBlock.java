package tleibert.yahtzee.model.dice;

/**
 * Represents the 5 dice used in Yahtzee
 * 
 * @author Trevor Leibert
 */
public class DiceBlock {
    /** number of dice used in Yahtzee */
    public static final int NUM_DICE = 5;

    /** array of Die objects */
    private Die[] dice;

    /**
     * Constructor for the DiceBlock class. Each DiceBlock has five dice.
     */
    public DiceBlock() {
        dice = new Die[NUM_DICE];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new Die();
        }
    }

    /**
     * Sets the value of each Die in this DiceBlock to zero.
     */
    public void resetDice() {
        for (int i = 0; i < dice.length; i++) {
            dice[i].reset();
        }
    }

    /**
     * Rolls all of the dice in the block. Intended for use
     * at the start of a player's turn.
     */
    public void initialRoll() {
        for (int i = 0; i < dice.length; i++) {
            dice[i].roll();
        }
    }

    /**
     * Rolls the selected dice
     * @param selected a boolean array representing which dice
     *                 are selected to be rolled. This is intended
     *                 for use for a player's second and third rolls.
     */
    public void rollSelected(boolean[] selected) {
        for (int i = 0; i < dice.length; i++) {
            if (selected[i]) {
                dice[i].roll();
            }
        }
    }

    /** 
     * Returns an integer tally of the values of the dice in this DiceBlock
     * @return tally int array of the values of the dice in this DiceBlock
     */
    public int[] getTally() {
        int[] values = new int [Die.MAX_VALUE];

        for (int i = 0; i < dice.length; i++) {
            values[dice[i].getValue() - 1]++;
        }

        return values;
    }

    /**
     * Returns the sum of the values of the dice in this DiceBlock
     * @return the sum of the dice values.
     */
    public int getSumOfDice() {
        int sum = 0;
        for (int i = 0; i < dice.length; i++) {
            sum += dice[i].getValue();
        }
        return sum;
    }

    /**
     * Returns an integer array representation of the dice in this DiceBlock
     * @return integer array
     */
    public int[] toIntArray() {
        int[] array = new int[NUM_DICE];
        for (int i = 0; i < array.length; i++) {
            array[i] = dice[i].getValue();
        }
        return array;
    }

    /**
     * Sets the the dice's values in this DiceBlock to be equal
     * to the values in the entered integer array.
     * Intended for testing only.
     * @param values values to set the dice to.
     */
    public void setDiceValues(int[] values) {
        if (values.length != 5) {
            throw new IllegalArgumentException("Must have five elements");
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i] < 1 || values[i] > 6) {
                throw new IllegalArgumentException("Values must be between one and six");
            }
        }

        for (int i = 0; i < values.length; i++) {
            dice[i].setValue(values[i]);
        }
    }

    /**
     * Returns a string representation of the dice in this DiceBlock. 
     * Intended primarily for testing.
     * @return string representing dice
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < dice.length; i++) {
            s += "[" + dice[i].getValue() + "]\n";
        }
        return s;
    }
}
