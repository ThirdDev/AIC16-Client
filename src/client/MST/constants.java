package client.MST;

/**
 * Created by me on 11/02/2016.
 */
public final class constants { //todo: rename coefficients
    public static double c1 = 1.0 / 2.0;
    public static double c2 = 3.0 / 4.0;

    public static int c3 = 10;
    public static int c4 = 30;

    public static final int minimumRecommendedForceInBordersFailSafe = 200;
    public static int countOfArmyToAttackToOwnerlessNeighbors = 1;
    public static int minimumNumberOfUnitsLeftInEachNode = 0;

    public static double factorOfSendingToNewNodeWhenCurrentMightBeInDanger = 0.2;
}
