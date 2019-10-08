package tleibert.yahtzee.model.dice;

import java.util.Random;

/**
 * Represents one six-sided Die used in Yahtzee
 * @author Trevor Leibert
 */
public class Die {

	/** Maximum value of Die */
    public static int MAX_VALUE = 6;

	/** value of Die */
    private int value;

	/** Random number generator */
    private Random rand;

	/**
	* Constructs Die object.
	*/
    public Die() {
        value = 0;
        rand = new Random();
    }

	/**
	* Rolls the Die
	*/
    public void roll() {
        
        value = rand.nextInt(MAX_VALUE) + 1;
    }

	/**
	* Getter method for Die value
	*/
    public int getValue() {
        return value;
    }
	
	/**
	* Resets the value of the Die to zero
	*/
    public void reset() {
        value = 0;
    }

    /**
     * Setter method for Die value. Intended for testing only.
     * @param value the value to set the Die's value to.
     */
    public void setValue(int value) {
        if (value < 1 || value > 6) {
            throw new IllegalArgumentException("Value must be between one and six");
        }
        this.value = value;
    }
	
	/**
	* Returns a String representing the Die
	*/
    public String toString() {
        return "d" + value;
    }
}
