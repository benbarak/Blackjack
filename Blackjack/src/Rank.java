public enum Rank {
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    J(10, "jack"),
    Q(10, "queen"),
    K(10, "king"),
    A(11, "ace");

    private final int numericValue;
    private final String name;
    Rank(int i, String s) {
        numericValue = i;
        name = s;
    }

    public int getNumericValue() {return numericValue;}
    public String toString(){return name;}
}
