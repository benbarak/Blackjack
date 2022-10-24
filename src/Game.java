import java.util.*;

public class Game {
    private static Game instance; //singleton

    static Game getInstance() {
        if (Game.instance == null){
            Game.instance = new Game();
        }
        return Game.instance;
    }
    //constants
    private static final boolean PLAYER = true;
    private static final boolean DEALER = false;
    private static final int STARTING_POT = 3000;
    //fields
    private GUI gui;

    private int pot;
    private int bet;

    private final ArrayList<Card> deck;
    private int deckIndex;

    private final ArrayList<Card> playerHand;
    private final ArrayList<Card> dealerHand;

    //constructor
    private Game() {
        this.pot = STARTING_POT;
        this.bet = 0;
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.initDeck();
    }

    //methods
    //setters
    void setGUI(GUI gui){
        this.gui = gui;
    }
    //getters
    int getPlayerPot() {
        return this.pot;
    }
    int getBet(){
        return this.bet;
    }

    //starting methods
    void startGame() {
        this.clear();
        this.pot = STARTING_POT;
        this.gui.startGame();
    }

    void startRound() {
        this.clear();
        this.shuffle();
        this.gui.startRound();
    }
    //helper
    private void clear(){
        this.playerHand.clear();
        this.dealerHand.clear();
        this.bet = 0;
        this.deckIndex = 0;
    }
    //deck methods
    void shuffle() {
        Collections.shuffle(this.deck, new Random(new GregorianCalendar().get(Calendar.SECOND)));
    }

    private void initDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                this.deck.add(new Card(suit, rank, true));
            }
        }
        this.deckIndex = 0;
    }

    private ArrayList<Card> drawCards(int i) {
        ArrayList<Card> drawn = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            drawn.add(deck.get(deckIndex + j));
        }
        this.deckIndex += i;
        return drawn;
    }


    //deal methods
    void deal() {
        ArrayList<Card> cards = drawCards(4);
        cards.get(1).setShown(false);
        for (int i = 0; i < 4; i++) {
            this.addCard(cards.get(i), i % 2 == 0); //give the odds to dealer and even to player
        }
        if(this.isBlackjack(PLAYER)){
            this.blackjack();
        }
        else if(this.isBlackjack(DEALER)){
            this.lost();
        }
        else{
            this.gui.hitOrStand();
        }
    }

    private void addCard(Card card, boolean player) {
        if (player) {
            this.playerHand.add(card);
        } else {
            this.dealerHand.add(card);
        }
        this.gui.addCard(card, player);
        this.gui.setVisible(true);
    }

    //gameplay methods
    void hit(boolean player) {
        this.addCard(this.drawCards(1).get(0), player);
        if(this.isBurn(player)){
            this.burn(player);
        }
    }

    void dealerPlay() {
        while (this.isDealerSmall()) {
            this.hit(DEALER);
        }
        if(!this.isBurn(DEALER)){
            this.result();
        }
    }

    //helpers
    private boolean isBurn(boolean player) {
        return this.calculateHand(player) > 21;
    }

    private boolean isDealerSmall() {
        return this.calculateHand(DEALER) < 17;
    }

    private boolean isBlackjack(boolean player) {
        return this.calculateHand(player) == 21;
    }

    //bet methods
    void bet(){
        this.setBet(this.gui.getBet());
        if(this.isValidBet()){
            this.pot -= this.bet;
            this.deal();
            this.gui.bet();
        }
        else{
            this.gui.invalidBet(this.bet);
        }
    }

    private void setBet(int bet) {
        this.bet = bet;
    }

    private boolean isValidBet() {
        return this.bet <= this.pot && this.bet > 0;
    }

    //result methods
    private void result() {
        if (this.calculateHand(PLAYER) > this.calculateHand(DEALER)) {
            this.won(2*bet);
        } else {
            this.lost();
        }
    }

    private void burn(boolean player) {
        if(player){
            this.lost();
        }
        else{
            this.won(2*bet);
        }
        this.gui.burn(player);
    }

    private void won(int amount) {
        this.pot += amount;
        this.gui.won();
        this.showDealerHiddenCard();
    }

    private void lost() {
        if(this.getPlayerPot() > 0){
            this.gui.lost();
        }
        else{
            this.gui.gameOver();
        }
        this.showDealerHiddenCard();
    }

    private void blackjack() {
        this.won((int) (2.5*bet));
        this.gui.blackjack();
    }

    //helpers
    private int calculateHand(boolean player) {
        ArrayList<Card> hand = (player) ? this.playerHand : this.dealerHand;
        int sum = 0;
        int aces = 0;
        for (Card card : hand) {
            sum += card.getNumericRank();
            if (card.getRank() == Rank.A)
                aces++;
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }

    private void showDealerHiddenCard() {
        Card card = this.dealerHand.get(0);
        card.setShown(true);
        this.gui.showDealerHiddenCard(card);
    }
}
