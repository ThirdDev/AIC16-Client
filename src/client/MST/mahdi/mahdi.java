package client.MST.mahdi;

import client.MST.constants;
import client.model.Node;

import java.util.ArrayList;

/**
 * Created by me on 11/02/2016.
 */
public class Mahdi {

    int minimumRecommendedForceInBorders = 10;
        if (getMinimumRecommendedForceInBorders() > constants.minimumRecommendedForceInBordersFailSafe) {

    public int getMinimumRecommendedForceInBorders() {
        return minimumRecommendedForceInBorders;
    }

    public int increaseMinimumRecommendedForceInBorders() {
        minimumRecommendedForceInBorders++;
    }

    public ArrayList<Node> getWeakBorderNodes(ArrayList<Node> borderNodes) {

        if (getMinimumRecommendedForceInBorders() > minimumRecommendedForceInBordersFailSafe) {
            return new ArrayList<>();
        }

        ArrayList<Node> output = new ArrayList<>();

        int minRecom = getMinimumRecommendedForceInBorders();

        for (Node node: borderNodes) {
            if (node.getArmyCount() < minRecom)
                output.add(node);
        }

        if (output.size() == 0) {
            increaseMinimumRecommendedForceInBorders();
            return getWeakBorderNodes(borderNodes);
        }

        return output;
    }
}
