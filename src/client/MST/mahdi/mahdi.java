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
                            Mahdi.Movement(node, neighbour, army);
                            curForces -= moveCount;
                            if (curForces < 0)
                                break;
                        }
                }
            }
        }
    }

    public static ArrayList<Node> getOnlyEnemyNeighbors(Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node neighbor : node.getNeighbours()) {
            if (neighbor.getOwner() != node.getOwner() && (neighbor.getOwner() != -1)) {
                nodes.add(neighbor);
            }
        }

        return nodes;
    }

    public static void GoGrabOwnerlessNodes(Node source) {
        //Ahmadalli.log("a " + source.getIndex());
        if (IsMovingSrc(source))
            return;
        //Ahmadalli.log("b");
        ArrayList<Node> ownerlessNeighbors = Ahmadalli.getOwnerlessNeighbors(source);

        int count = 0;
        for (Node ownerless : ownerlessNeighbors) {
            if (!IsMovingDest(ownerless)) {
                count++;
            }
        }
        //Ahmadalli.log("c " + count);


        if (count == 0)
            return;

        double factor = constants.factorOfSendingToNewNodeWhenCurrentMightBeInDanger;
        if (getOnlyEnemyNeighbors(source).size() == 0)
            factor = 1;

        //Ahmadalli.log("d " + factor + " " + getOnlyEnemyNeighbors(source).size());

        for (Node ownerless : ownerlessNeighbors) {
            if (!IsMovingDest(ownerless)) {
                Movement(source, ownerless, (int)(source.getArmyCount() * factor));
            }
        }
    }

    public static void InitMovements() {
        attacks = new ArrayList<>();
    }

    public static boolean IsMovingSrc(Node n) {
        for (AttackData d : attacks)
            if (d.source == n)
                return true;
        return false;
    }

    public static boolean IsMovingDest(Node n) {
        for (AttackData d : attacks)
            if (d.dest == n)
                return true;
        return false;
    }

    public static void CancelMovementSrc(Node n) {
        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).source == n) {
                attacks.remove(attacks.get(i));
            }
    }

    public static void CancelMovementDest(Node n) {
        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).dest == n) {
                attacks.remove(attacks.get(i));
            }
    }

    public static boolean Movement(Node src, Node dest, int count) {
        if (count <= 0) {
            Ahmadalli.log("Invalid Movement Command from " + src.getIndex() + " to " + dest.getIndex() + " with count of " + count);
            return false;
        }

        if (IsMovingSrc(src))
            return false;

        AttackData data = new AttackData();
        data.source = src;
        data.dest = dest;
        data.count = count;

        attacks.add(data);

        return true;
    }

    public static void ApplyMovements(World world) {
        for (AttackData d : attacks)
            world.moveArmy(d.source, d.dest, d.count);
    }
}
