public enum Suit {
    C("clubs"),
    D("diamonds"),
    H("hearts"),
    S("spades");

    private final String string;

    public String toString(){return string;}

    Suit(String str) {
        string = str;
    }
}
