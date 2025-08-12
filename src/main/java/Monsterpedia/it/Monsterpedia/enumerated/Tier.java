package Monsterpedia.it.Monsterpedia.enumerated;

public enum Tier { S, A, B, C, D, F, X
    ;
    public static Tier fromRating(int rating) {
        return switch (rating) {
            case 5 -> S;
            case 4 -> A;
            case 3 -> B;
            case 2 -> C;
            case 1 -> D;
            case 0 -> F;
            default -> X;
        };
    }
}
