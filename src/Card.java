public class Card {
    private final Suit suit;
    private final Rank rank;
    private final int numericRank;
    private boolean shown;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        numericRank = rank.getNumericValue();
        shown = true;
    }
    public Card(Suit suit, Rank rank, boolean shown){
        this.suit = suit;
        this.rank = rank;
        numericRank = rank.getNumericValue();
        this.shown = shown;
    }

    public int getNumericRank() {
        return numericRank;
    }

    public Rank getRank() {
        return rank;
    }
    public Suit getSuit(){return suit;}

    public boolean isShown(){
        return this.shown;
    }
    public void setShown(boolean b){this.shown = b;}

    @Override
    public String toString() {
        if(!this.shown)
            return "X";
        if(this.rank == Rank.J || this.rank == Rank.Q || this.rank == Rank.K || this.rank == Rank.A)
            return rank.toString() + "." + suit.toString();
        return rank.getNumericValue() + suit.toString();
    }
}
