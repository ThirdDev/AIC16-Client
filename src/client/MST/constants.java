package client.MST;

/**
 * Created by me on 11/02/2016.
 */
public final class constants { //todo: rename coefficients
    public static final double c1 = 1.0 / 2.0;
    public static final double c2 = 3.0 / 4.0;

    public static final int c3 = 10;
    public static final int c4 = 30;

    public static final int minimumRecommendedForceInBordersFailSafe = 200;

    /**
     * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     * and so on. Thus, the caller should be prevented from constructing objects of
     * this class, by declaring this private constructor.
     */
    private constants() {
    }
}
