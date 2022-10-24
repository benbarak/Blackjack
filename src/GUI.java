import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private static GUI instance; //singleton

    static GUI getInstance(){
        if(instance == null){instance = new GUI();}
        return instance;
    }

    //constants
    private static final boolean PLAYER = true, DEALER = false;
    private static final int STARTING_POT = 3000;
    //fields
    private Game game;
    private JPlayer jdealer;
    private JPlayer jplayer;
    private JText jtext;
    private JButtonBar jbuttonBar;

    void setGame(Game game){
        this.game = game;
    }
    //constructor
    private GUI(){
        initSetup();
        initComponents();
    }
    //helper methods for constructor
    //set up the frame
    private void initSetup() {
        this.setSize(800,800);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    //initialize and add components to the frame
    private void initComponents() {
        this.jdealer = new JPlayer(DEALER);
        this.jplayer = new JPlayer(PLAYER);
        this.jtext = new JText();
        this.jbuttonBar = new JButtonBar();
        this.add(this.jdealer);
        this.add(this.jtext);
        this.add(this.jplayer);
        this.add(this.jbuttonBar);
        this.setVisible(true);
    }

    //methods
    void instructions() {
        this.jtext.setMassage("press start");
        this.jbuttonBar.setButtonsActive(false, false, false, false, true);
    }

    void startGame() {
        this.jbuttonBar.restartButton.setText("restart");
        this.jplayer.setPot(STARTING_POT);
        this.game.startRound();
    }

    void startRound() {
        this.jdealer.clearHand();
        this.jplayer.clearHand();
        this.jtext.setBet(0);
        this.jtext.setMassage("Place bet:");
        this.jtext.showInputBox(true);
        this.jbuttonBar.setButtonsActive(false, false, true, false, true);
    }


    void bet(){
        this.jtext.setBet(this.game.getBet());
        this.jplayer.setPot(game.getPlayerPot());
    }
    int getBet(){
        int ret = this.jtext.getBet();
        this.jtext.clearInputBox();
        this.jtext.showInputBox(false);
        this.jtext.showMassageBox(false);
        return ret;
    }

    void invalidBet(int bet) {
        String msg = (bet > 0) ? "can't bet more than you have," : "bet must be a positive number,";
        this.jtext.setMassage(msg +"\nplace new bet:");
        this.jtext.showInputBox(true);
        this.jbuttonBar.setButtonsActive(false, false, true, false, true);
    }

    void hitOrStand(){
        this.jbuttonBar.setButtonsActive(true, true, false, false, true);
    }



    void lost(){
        this.jtext.setMassage("You lost!");
        this.jtext.setBet(0);
        this.jbuttonBar.setButtonsActive(false, false, false, true, true);
    }

    void won(){
        this.jtext.setMassage("You won!");
        this.jtext.setBet(0);
        this.jplayer.setPot(this.game.getPlayerPot());
        this.jbuttonBar.setButtonsActive(false, false, false, true, true);
    }

    void burn(boolean player){
        String msg = (player) ? "You " : "Dealer ";
        this.jtext.setMassage(msg + "burnt");
    }

    void blackjack() {
        this.jtext.setMassage("BLACKJACK!");
    }

    public void gameOver() {
        this.jtext.setMassage("GAME OVER\nPress restart");
        this.jbuttonBar.setButtonsActive(false, false, false, false, true);
    }

    void addCard(Card card, boolean player){
        JPlayer hand = (player) ? this.jplayer : this.jdealer;
        hand.addCard(card);
    }

    void showDealerHiddenCard(Card card) {
        this.jdealer.remove(0);
        String address = "img/" + card.getRank().toString() + "_of_"+card.getSuit().toString() + ".png";
        ImageIcon imageIcon = this.getScaledImageIcon(address);
        JLabel label = new JLabel(imageIcon);
        this.jdealer.add(label, 0);
    }

    private ImageIcon getScaledImageIcon(String address){
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(address)); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(90, 160,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        return imageIcon;
    }

    //inner class JPlayer
    private class JPlayer extends JPanel{
        private JTextField pot;

        public JPlayer(boolean player){
            //set up
            int y = (player) ? 600 : 100; // different locations for jdealer and jplayer
            setBounds(0, y, 800, 200);
            setBackground(Color.GREEN);
            //add pot
            if(player){
                this.pot = new JTextField();
                this.pot.setEditable(false);
                this.setPot(STARTING_POT);
                add(pot);
            }
        }

        private void setPot(int x) {
            this.pot.setText("Pot: " + x);
        }

        private void addCard(Card card){
            String address = (card.isShown()) ?
                    "img/" + card.getRank().toString() + "_of_"+card.getSuit().toString() + ".png" :
                    "img/hidden_card.png";
            ImageIcon imageIcon = getScaledImageIcon(address);
            JLabel label = new JLabel(imageIcon);
            this.add(label);
        }

        private void clearHand(){
            Component[] comps = this.getComponents();
            for(Component c : comps){
                if(c instanceof JLabel){
                    this.remove(c);
                }
            }
        }
    }


    //inner class JText
    private class JText extends JPanel{
        private JTextField bet;
        private JTextArea massage;
        private JTextField input;

        public JText(){
            //set up
            this.setBounds(0, 300, 800, 100);
            this.setBackground(Color.LIGHT_GRAY);
            //initialize pot text field
            this.bet = new JTextField();
            this.bet.setVisible(false);
            //initialize massage text area
            this.massage = new JTextArea();
            this.massage.setVisible(false);
            //initialize input text field
            this.input = new JTextField();
            this.input.setPreferredSize(new Dimension(100, 20));
            this.input.setVisible(false);
            //add text components
            this.add(this.bet);
            this.add(this.massage);
            this.add(this.input);
        }

        private void setBet(int x){
            this.bet.setText("Bet: " + x);
            this.bet.setVisible(true);
            this.bet.setEditable(false);
        }

        private void setMassage(String s){
            this.massage.setText(s);
            this.massage.setVisible(true);
            this.massage.setEditable(false);
        }

        private int getBet(){
            try{
                return Integer.parseInt(this.input.getText());
            } catch (NumberFormatException e) {
                System.out.println("must enter number");
            }
            return 0;
        }

        private void showMassageBox(boolean b){
            this.massage.setVisible(b);
        }
        private void showInputBox(boolean b) {this.input.setVisible(b);}
        private void clearInputBox(){
            this.input.setText("");
        }
    }


    //inner class JButtonBar
    private class JButtonBar extends JPanel{
        private JButton hitButton;
        private JButton standButton;
        private JButton betButton;
        private JButton continueButton;
        private JButton restartButton;

        public JButtonBar(){
            //set up
            this.setLayout(null);
            this.setBounds(0, 700, 800, 100);
            this.setBackground(Color.BLACK);
            //hitButton: init, add to buttonBar, add action listener
            this.hitButton = new JButton("hit");
            this.add(this.hitButton);
            hitButton.setBounds(50, 25, 80, 50);
            hitButton.addActionListener(e -> game.hit(PLAYER));
            this.hitButton.setVisible(false);
            //standButton: init, add to buttonBar, add action listener
            this.standButton = new JButton("stand");
            this.add(this.standButton);
            this.standButton.setBounds(150, 25, 80, 50);
            this.standButton.addActionListener(e -> game.dealerPlay());
            this.standButton.setVisible(false);
            //betButton: init, add to buttonBar, add action listener
            this.betButton = new JButton("bet");
            this.add(this.betButton);
            this.betButton.setBounds(400, 25, 80, 50);
            this.betButton.addActionListener(e -> game.bet());
            this.betButton.setVisible(false);
            //continueButton: init, add to buttonBar, add action listener
            this.continueButton = new JButton("continue");
            this.add(this.continueButton);
            this.continueButton.setBounds(700, 25, 80, 50);
            this.continueButton.addActionListener(e -> game.startRound());
            this.continueButton.setVisible(false);
            //restartButton: init, add to buttonBar, add action listener
            this.restartButton = new JButton("start");
            this.add(this.restartButton);
            this.restartButton.setBounds(700, 25, 80, 50);
            this.restartButton.addActionListener(e -> game.startGame());
            this.restartButton.setVisible(false);
        }

        private void setButtonActive(String button, boolean b){
            switch (button) {
                case "hit" -> this.hitButton.setVisible(b);
                case "stand" -> this.standButton.setVisible(b);
                case "bet" -> this.betButton.setVisible(b);
                case "continue" -> this.continueButton.setVisible(b);
                case "restart" -> this.restartButton.setVisible(b);
            }
        }

        private void setButtonsActive(boolean hit, boolean stand, boolean bet, boolean cont, boolean restart){
            this.setButtonActive("hit", hit);
            this.setButtonActive("stand", stand);
            this.setButtonActive("bet", bet);
            this.setButtonActive("continue", cont);
            this.setButtonActive("restart", restart);
        }

    }
}
