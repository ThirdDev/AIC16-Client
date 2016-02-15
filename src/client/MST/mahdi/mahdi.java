package client.MST.mahdi;

import client.MST.ahmadalli.Ahmadalli;
import client.MST.constants;
import client.World;
import client.model.Node;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by me on 11/02/2016.
 */
public class Mahdi {

    static int minimumRecommendedForceInBorders = 10;

    public static int getMinimumRecommendedForceInBorders() {
        return minimumRecommendedForceInBorders;
    }

    public static void increaseMinimumRecommendedForceInBorders() {
        minimumRecommendedForceInBorders++;
    }

    public static ArrayList<Node> getWeakBorderNodes(ArrayList<Node> borderNodes) {
        if (getMinimumRecommendedForceInBorders() > constants.minimumRecommendedForceInBordersFailSafe) {
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

    public static void taneLash(World world, ArrayList<Node> untouchedNodes, Map<Node, Integer> minDistanceToBorder) {
        for (Node node: untouchedNodes) {
            int nearerNeighborCount = 0;
            for (Node neighbour : node.getNeighbours()) {
                if (minDistanceToBorder.get(neighbour) < minDistanceToBorder.get(node)) {
                    nearerNeighborCount++;
                }
            }

            int curForces = node.getArmyCount();
            int moveCount = node.getArmyCount() / nearerNeighborCount;

            for (Node neighbour : node.getNeighbours()) {
                if (minDistanceToBorder.get(neighbour) < minDistanceToBorder.get(node)) {
                    int army =Math.min(moveCount, curForces);
                    Ahmadalli.log("method: ahmadalli.attackWeakestNearEnemy - section: ownerless - from:" + node.getIndex() +
                            " - to: " + neighbour.getIndex() + " - army: " + army);
                    world.moveArmy(node, neighbour,army );
                    curForces -= moveCount;
                    if (curForces < 0)
                        break;
                }
            }
        }
    }
}
