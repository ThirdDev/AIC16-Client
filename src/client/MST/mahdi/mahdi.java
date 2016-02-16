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

    private static class AttackData {
        Node source;
        Node dest;
        int count;
    }

    static ArrayList<AttackData> attacks;

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

        for (Node node : borderNodes) {
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
        Ahmadalli.log("method: Mahdi.taneLash - untochedNodes.size() = " + untouchedNodes.size());
        for (Node node : untouchedNodes) {
            if (node.getOwner() == world.getMyID()) {
                int nearerNeighborCount = 0;
                for (Node neighbour : node.getNeighbours()) {
                    if (neighbour.getOwner() == world.getMyID())
                        if (minDistanceToBorder.get(neighbour) < minDistanceToBorder.get(node)) {
                            nearerNeighborCount++;
                        }
                }

                if (nearerNeighborCount == 0)
                    continue;

                int curForces = node.getArmyCount();
                int moveCount = node.getArmyCount() / nearerNeighborCount;

                Ahmadalli.log("method: Mahdi.taneLash - node #" + node.getIndex() + " minDistanceToBorder = " + minDistanceToBorder.get(node));
                for (Node neighbour : node.getNeighbours()) {
                    if (neighbour.getOwner() == world.getMyID())
                        if (minDistanceToBorder.get(neighbour) < minDistanceToBorder.get(node)) {
                            int army = Math.min(moveCount, curForces);
                            Ahmadalli.log("method: Mahdi.taneLash - from:" + node.getIndex() +
                                    " - to: " + neighbour.getIndex() + " - army: " + army);
                            world.moveArmy(node, neighbour, army);
                            curForces -= moveCount;
                            if (curForces < 0)
                                break;
                        }
                }
            }
        }
    }


    public static void InitAttacks() {
        attacks = new ArrayList<>();
    }

    public static boolean IsAttackingSrc(Node n) {
        for (AttackData d : attacks)
            if (d.source == n)
                return true;
        return false;
    }

    public static boolean IsAttackingDest(Node n) {
        for (AttackData d : attacks)
            if (d.dest == n)
                return true;
        return false;
    }

    public static void CancelAttackSrc(Node n) {
        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).source == n) {
                attacks.remove(attacks.get(i));
            }
    }

    public static void CancelAttackDest(Node n) {
        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).dest == n) {
                attacks.remove(attacks.get(i));
            }
    }

    public static boolean Attack(Node src, Node dest, int count) {
        if (IsAttackingSrc(src))
            return false;

        AttackData data = new AttackData();
        data.source = src;
        data.dest = dest;
        data.count = count;

        attacks.add(data);

        return true;
    }

    public static void ApplyAttacks(World world) {
        for (AttackData d : attacks)
            world.moveArmy(d.source, d.dest, d.count);
    }
}
