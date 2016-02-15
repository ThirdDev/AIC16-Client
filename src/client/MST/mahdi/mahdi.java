package client.MST.mahdi;

import client.model.Node;

import java.util.ArrayList;

/**
 * Created by me on 11/02/2016.
 */
public class Mahdi {

    int minimumRecommendedForceInBorders = 10;
    int minimumRecommendedForceInBordersFailSafe = 200;

    public int getMinimumRecommendedForceInBorders() {
        return minimumRecommendedForceInBorders;
    }

    public int increaseMinimumRecommendedForceInBorders() {
        minimumRecommendedForceInBorders++;
    }

}
