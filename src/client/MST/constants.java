package client.MST;

/**
 * Created by me on 11/02/2016.
 */
public final class constants { //todo: rename coefficients
    public static double c1 = 1.0 ; // Used in Mahdi.MarzbananBePish and Ahmadalli.attackWeakestNearEnemy
    public static double c2 = 3.0 / 4.0;

    public static int c3 = 10;
    public static int c4 = 30;

    public static final int minimumRecommendedForceInBordersFailSafe = 200;
    public static int countOfArmyToAttackToOwnerlessNeighbors = 1;
    public static int minimumNumberOfUnitsLeftInEachNode = 0;

    public static double factorOfSendingToNewNodeWhenCurrentMightBeInDanger = 0.2;
    public static double factorOfSendingToNewNodeWhenCurrentIsSafe = 1;
    public static double factorOfSendingToNewNodeWhenCurrentIsSafe2 = 0.25;

    public static final int GetRouteEndlessLoopThreshold = 5000;

    public static final int EnemySoCloseDistance = 1;

    public static final int UnderEstimateValue = 0;
    public static final double UnderEstimateCoefficient = 1.0 / 1.0;
}
