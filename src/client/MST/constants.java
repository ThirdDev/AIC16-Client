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

    public static final int GetRouteEndlessLoopThreshold = 5000;

    public static final int UnderEstimateValue = 0;
    public static final double UnderEstimateCoefficient = 1.0 / 1.0;

    public static final double taneLashC1 = 1.0;
    public static final double taneLashC2 = 1.0;

    public static final double HaramC = 0.9;
    public static final int HaramV = 0;

    public static final int CriticalBorderEdgeCount = 3;
    public static final double CriticalBorderFactor = 1;
    public static final int CriticalBorderMaxDistanceFromEnemy = 2;

    public static final int[] OwnerAvgValues1 = {6, 21, 40};
}
