package Monsterpedia.it.Monsterpedia.enumerated;

public enum Tier { S, A, B, C, D, F, X
    ;
    public static Tier fromRating(int rating) {
        return switch (rating) {
            case 10 -> S;
            case 8, 9 -> A;
            case 6, 7 -> B;
            case 4, 5 -> C;
            case 2, 3 -> D;
            case 0, 1 -> F;
            default -> X;
        };
    }
}
