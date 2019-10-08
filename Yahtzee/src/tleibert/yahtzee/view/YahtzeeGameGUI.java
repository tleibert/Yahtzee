package tleibert.yahtzee.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import tleibert.yahtzee.model.YahtzeeGame;
import tleibert.yahtzee.model.dice.DiceBlock;

import java.util.Arrays;
/**
 * The YahtzeeGameGUI class runs the Yahtzee game and
 * provides graphics for it.
 *
 * @author Trevor Leibert
 * @author Charlee Sherrill
 * 
 */
public class YahtzeeGameGUI extends JFrame implements ActionListener {
    
    /** default serializable id */
	private static final long serialVersionUID = 1L;
	
	//**************************************************************************** */
    /** Width of GUI Window */ 
    public static final int WIDTH = 950;
    /** Height of GUI Window */
    public static final int HEIGHT = 560;
    /** Path to (directory for) card images */
    public static final String PATH = "/img/DieFace0";
    /** Extension of card images */
    public static final String EXTENSION = ".png";
    /** Time (milliseconds) that cards that are not a match are displayed */
    public static final int DELAY = 1500;
    /** Large bold font used for game instructions */
    public static final Font LARGE_BOLD = new Font("Courier", 1, 18);
    /** Normal font used for game statistics */
    public static final Font NORMAL = new Font("Courier", 0, 16);

    public static final int YAHTZEE_INDEX = 11;
    
    //***************************************************************** */
    private JPanel buttonPanel;

    private JPanel headerPanel;

    /** buttons for scoring options */
    private JButton[][] scoringButtons;

    /** label reading "Scoring Type:" */
    private JLabel scoringLabel;

    /** label showing the selected scoring type */
    private JLabel selectedScoringLabel;

    private JTextField labelScores;


    
    //****************************************************************************
    /** YahtzeeGame (model) instance */
    private YahtzeeGame yahtzeeGame;
    /** Die buttons */
    private JToggleButton[][] diceButtons;
    /** Panel to display grid of dice */
    private JPanel gridPanel;
    /** buttons to control rolls and turns */
    private JButton[][] controlButtons;
    /** Panel to display control buttons */
    private JPanel controlPanel;
    /** number of rolls */
    private int numRolls;
    /**number of players */
    private int numPlayers;

    /** keeps track of which scoring buttons have been used by which players */
    private boolean[][] enabledScoringButtons;

    /** stores which dice have been selected to be rerolled */
    private boolean[] selected;

    /** stores the dice for use in the game */
    private DiceBlock dice;

    /** stores the selected scoring type */
    private int selectedScoring;

    /** scores the index number of the current player */
    private int currentPlayer;

    /** stores the number of turns each player has taken */
    private int[] turnNumbers;

    /**
     * Constructs a new YahtzeeGameGUI, which contains a YahtzeeGame,
     * and prompts for input related to the game.
     */
    public YahtzeeGameGUI() 
    {
        
        super("Yahtzee Game GUI");

        // initialize the number of players, will be overwritten by the InputDialog box
        int Player_number = 1;

        // reprompts until a valid number of players has been inputted
        while (Player_number < YahtzeeGame.MIN_PLAYERS ||
                Player_number > YahtzeeGame.MAX_PLAYERS) {
            String Player_Text = JOptionPane.showInputDialog("Choose a number of players between 2-4");
            if(Player_Text == null){
                System.exit(1);
            }
            
            try { 
                Player_number = Integer.parseInt(Player_Text);

                if (Player_number < 2 || Player_number > 4){
                    throw new Exception();
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "the player number you have entered is invalid", 
                                                "Invalid player number", JOptionPane.ERROR_MESSAGE);
            }
        }

        // initiaize number of rolls to be 0
        numRolls = 0;
        
        // start creating the GUI
        setSize(WIDTH, HEIGHT);
        setLocation(100, 100);
        Container c = getContentPane();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        // header panel, contains the current player number,
        // their number of turns taken, and their current score
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 1));

        labelScores = new JTextField("Score :");
        //labelScores.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        labelScores.setHorizontalAlignment(JTextField.CENTER);
        labelScores.setEditable(false);
        labelScores.setBackground(Color.WHITE);

        //labelGameNum = new JTextField("Game Number");
        //labelGameNum.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        //labelGameNum.setEditable(false);
        //labelGameNum.setBackground(Color.WHITE);

        headerPanel.add(labelScores);
        //headerPanel.add(labelGameNum);




        // scoring buttons
        buttonPanel = new JPanel();

        // set up for two labels, and 13 scoring buttons from the YahtzeeGame class
        buttonPanel.setLayout(new GridLayout(YahtzeeGame.SCORING_TYPES.length + 2, 1));
        scoringLabel = new JLabel("Selected Scoring Type: ");
        buttonPanel.add(scoringLabel);

        selectedScoringLabel = new JLabel();
        buttonPanel.add(selectedScoringLabel);

        scoringButtons = new JButton[YahtzeeGame.SCORING_TYPES.length][1];
        
        // initialize the buttons using the String array from YahtzeeGame for the labels
        for (int i = 0; i < YahtzeeGame.SCORING_TYPES.length; i++) {
            JButton button = new JButton(YahtzeeGame.SCORING_TYPES[i]);
            button.addActionListener(this);
            button.setEnabled(false);
            scoringButtons[i][0] = button;
            buttonPanel.add(button);
        }
        
        // initialize the number of players
        numPlayers = Player_number;

        // create a 2-d boolean array showing which scoring options are available to each player
        // each column represents one player, and each row a different scoring type
        // all scoring types are enabled at the start of the game
        enabledScoringButtons = new boolean[YahtzeeGame.SCORING_TYPES.length][numPlayers];
        for (int i = 0; i < enabledScoringButtons.length; i++) {
            for (int j = 0; j < enabledScoringButtons[0].length; j++) {
                enabledScoringButtons[i][j] = true;
            }
        }

        // set up dice buttons
        gridPanel = new JPanel(new GridLayout(1, DiceBlock.NUM_DICE));

        // toggle buttons are used to represent which dice are selected
        diceButtons = new JToggleButton[1][DiceBlock.NUM_DICE];
        for (int i = 0; i < diceButtons.length; i++) {
            for (int j = 0; j < diceButtons[i].length; j++) {
                JToggleButton button = new JToggleButton();
                button.addActionListener(this);

                // dice are initially set to be a blank die face, before they have been rolled
                button.setIcon (new ImageIcon(getClass().getResource(PATH + "0" + EXTENSION)));
                button.setEnabled(false);
                diceButtons[i][j] = button;
                gridPanel.add(button);
             }
        }

        c.add(gridPanel,BorderLayout.CENTER);
        controlPanel = new JPanel(new GridLayout(1, 2));
        controlButtons = new JButton[1][2];
        for(int i = 0; i < controlButtons.length; i++)
        {
            for(int j = 0; j < controlButtons[i].length; j++)
            {
                JButton button = new JButton();
                button.addActionListener(this);
                button.setIcon(null);
                button.setEnabled(true);
                controlButtons[i][j] = button;
                controlPanel.add(button);
            }
        }
        controlButtons[0][0].setText("Roll");
        controlButtons[0][1].setText("End Turn");
        controlButtons[0][1].setEnabled(false);
        
        c.add(controlPanel, BorderLayout.SOUTH);

        //infoPanel = new JPanel(new GridLayout(2, 1));
        //turnLabel = new JTextArea("test");

        //infoPanel.add(turnLabel);

        c.add(headerPanel, BorderLayout.NORTH);

        c.add(buttonPanel, BorderLayout.EAST);
        
        selected = new boolean[DiceBlock.NUM_DICE];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
        }
        numRolls = 0;
        dice = new DiceBlock();
        yahtzeeGame = new YahtzeeGame(Player_number);
        currentPlayer = yahtzeeGame.getSelectedPlayer();
        
        //turnLabel.setEditable(false);
        //turnLabel.setText("Player\n" + yahtzeeGame.getTurn() + "\nturn");

        labelScores.setText("Player " + yahtzeeGame.getTurn() +
            "\t\tTurn: 1" +
            "\t\tScore: " + yahtzeeGame.getCurrentPlayerScore());

        turnNumbers = new int[numPlayers];
        Arrays.fill(turnNumbers, 1);
        turnNumbers[currentPlayer]++;
        setVisible(true);
    }
    
    /**
     * Handles buttons in the GUI
     */
    public void actionPerformed(ActionEvent e) 
    {
        
        // scoring selection
        for (int i = 0; i < scoringButtons.length; i++) {
            if (e.getSource() == scoringButtons[i][0]) {
                selectedScoring = i;
                selectedScoringLabel.setText(YahtzeeGame.SCORING_TYPES[i]);
                controlButtons[0][1].setEnabled(true);
            }
        }
        
        // dice selection - toggles the boolean value in the array
        // representing which dice will be rerolled
        for (int i = 0; i < diceButtons[0].length; i++) {
            if (e.getSource() == diceButtons[0][i]) {
                toggleSelected(i);
            }
        }

        // take turn button
        if (e.getSource() == controlButtons[0][1]) {
            int currentPlayer = yahtzeeGame.getSelectedPlayer();
            int temp = yahtzeeGame.takeTurn(dice, selectedScoring);
            
            // yahtzee can be clicked multiple times if a yahtzee is achieved
            if (temp >= 0) {
                if (selectedScoring != YAHTZEE_INDEX) {
                    enabledScoringButtons[selectedScoring][currentPlayer] = false;
                } else {
                    if (temp > 0) {

                        enabledScoringButtons[YAHTZEE_INDEX][currentPlayer] = true;
                    } else {
                        enabledScoringButtons[YAHTZEE_INDEX][currentPlayer] = false;
                    }
                }

                //turnLabel.setText("Player\n" + yahtzeeGame.getTurn() + "\nturn");
                
                // sets all dice to be zero and resets the roll counter for the beginning of the 
                // next player's turn

                dice.resetDice();
                numRolls = 0;
                int[] diceStates = dice.toIntArray();
                for (int i = 0; i < diceButtons[0].length; i++) {
                    diceButtons[0][i].setIcon(new ImageIcon(getClass().getResource(PATH + diceStates[i] + EXTENSION)));

                    // deselect and disable all dice buttons until the first roll of the next
                    selected[i] = false;
                    diceButtons[0][i].setSelected(false);
                    diceButtons[0][i].setEnabled(false);
                }
                
                // disable the end turn button until a scoring method is selected
                controlButtons[0][1].setEnabled(false);
                controlButtons[0][0].setEnabled(true);

                // temporarily set the scoring method to 0, this will not be used
                selectedScoring = -1;
                selectedScoringLabel.setText("");

                // update the currently selected player
                currentPlayer = yahtzeeGame.getSelectedPlayer();

                // lock out all scoring buttons until the first roll of the 
                // next turn si sperformed
                for (int i = 0; i < scoringButtons.length; i++) {
                    scoringButtons[i][0].setEnabled(false);
                }
                
                // update the label at the top to show the current player number, turn number
                // and that player's current score
                labelScores.setText("Player " + yahtzeeGame.getTurn() +
                    "\t\tTurn: " + turnNumbers[currentPlayer] +
                    "\t\tScore: " + yahtzeeGame.getCurrentPlayerScore());

                // increase the current player's turn count
                turnNumbers[currentPlayer]++;

                // display the winner at the end of the game, and ask if the player wants to play again
                if (yahtzeeGame.getIsOver()) {
                    JOptionPane.showMessageDialog(null, "Player " + (yahtzeeGame.getWinner() + 1) + " wins!");
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play again?", 2);
                    if (choice == 0) {
                        new YahtzeeGameGUI();
                    }
                }
            }
        }

        // roll button
        if (e.getSource() == controlButtons[0][0]) {

            // first roll rolls all the dice and enables selection of 
            // individual dice for reroll
            if (numRolls == 0) {
                dice.initialRoll();
                numRolls++;
                
                for (int i = 0; i < diceButtons[0].length; i++) {
                    diceButtons[0][i].setEnabled(true);
                }

                // update current player
                currentPlayer = yahtzeeGame.getSelectedPlayer();

                // enable all scoring buttons that the current player has not locked out
                for (int i = 0; i < scoringButtons.length; i++) {
                    scoringButtons[i][0].setEnabled(enabledScoringButtons[i][currentPlayer]);
                }

                // for the second and third rolls, only the selected dice will be rolled
            } else if (numRolls > 0 && numRolls < 3) {
                dice.rollSelected(selected);
                numRolls++;
                // disable the roll button after the third roll
                if (numRolls == 3) {
                    controlButtons[0][0].setEnabled(false);
                }
            }

            // update the dice images
            int[] diceStates = dice.toIntArray();
            for (int i = 0; i < diceButtons[0].length; i++) {
                diceButtons[0][i].setIcon(new ImageIcon(getClass().getResource(PATH + diceStates[i] + EXTENSION)));
            }

        }

    }

    /**
     * Toggles the boolean value at the given index, setting it to true
     * if it was false, or false if it was true
     * 
     * @param idx the index of the boolean value to toggle
     */
    private void toggleSelected(int idx) {
        if (selected[idx]) {
            selected[idx] = false;
        } else {
            selected[idx] = true;
        }    
    }

    /**
     * Starts program
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new YahtzeeGameGUI();
           
    }
}