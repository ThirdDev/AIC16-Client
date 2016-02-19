package client.MST.ahmadalli;

import client.MST.constants;
import client.MST.mahdi.Mahdi;
import client.World;
import client.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by me on 11/02/2016.
 */
public class Ahmadalli {
    public static ArrayList<Node> getEnemyNeighbors(Node node, boolean emptyNeighbors) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node neighbor : node.getNeighbours()) {
            if ((neighbor.getOwner() != node.getOwner() && neighbor.getOwner() != -1) || (emptyNeighbors && neighbor.getOwner() == -1)) {
                nodes.add(neighbor);
            }
        }

        return nodes;
    }

    public static ArrayList<Node> getFriendlyNeighbors(Node node, boolean emptyNeighbors) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node neighbor : node.getNeighbours()) {
            if (neighbor.getOwner() == node.getOwner() || (emptyNeighbors && neighbor.getOwner() == -1)) {
                nodes.add(neighbor);
            }
        }

        return nodes;
    }

    public static ArrayList<Node> getOwnerlessNeighbors(Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node neighbor : node.getNeighbours()) {
            if (neighbor.getOwner() == -1) {
                nodes.add(neighbor);
            }
        }

        return nodes;
    }

    public static int getNodeState(Node node) {
        return getScoreState(node.getArmyCount(), false);
    }

    public static int getNodeState(Node node, boolean underestimate) {
        return getScoreState(node.getArmyCount(), underestimate);
    }

    public static int getScoreState(int score) {
        return getScoreState(score, false);
    }

    public static int getScoreState(int score, boolean underestimate) {
        if (underestimate)
            score = (int) (score * constants.UnderEstimateCoefficient - constants.UnderEstimateValue);
        if (score <= constants.c3)
            return 0;
        if (score <= constants.c4)
            return 1;
        return 2;
    }

    public static int attackWeakestNearEnemy(World world, Node source) {
        Node weakest = null;

        for (Node neighbor : getEnemyNeighbors(source, true)) {
            if (weakest == null || neighbor.getArmyCount() > weakest.getArmyCount()) {
                if (neighbor.getOwner() != -1)
                    weakest = neighbor;
            }
        }

        if (weakest != null && weakest.getArmyCount() <= getNodeState(source)) {
            int army = (int) ((double) (source.getArmyCount() * constants.c1));
            Ahmadalli.log("method: ahmadalli.attackWeakestNearEnemy - from:" + source.getIndex() +
                    " - to: " + weakest.getIndex() + " - army: " + army);

            if (Mahdi.IsMovingSrc(source))
                Mahdi.CancelMovementSrc(source);
            Mahdi.Movement(source, weakest, army);
            return 1; // We attacked the enemy.
        }

        if (weakest == null)
            return 0; // There were no enemy.
        else
            return -1; // The enemy was more powerful than us.
    }

    public static void moveRandomlyToFriendNeighbors(World world, Node source) {
        ArrayList<Node> friendlyNeighbors = getFriendlyNeighbors(source, true);
        if (friendlyNeighbors.size() > 0) {
            int army = (int) (source.getArmyCount() * constants.c2);
            Node randomFriendlyNeighbor = friendlyNeighbors.get((int) (friendlyNeighbors.size() * Math.random()));
            Ahmadalli.log("method: ahmadalli.moveRandomlyToFriendNeighbors - section:  - from:" + source.getIndex() +
                    " - to: " + randomFriendlyNeighbor.getIndex() + " - army: " + army);
            Mahdi.Movement(source, randomFriendlyNeighbor, army);
        }
    }

    public static void layer1Move(World world, Node source) {
        if (attackWeakestNearEnemy(world, source) != 1)
            moveRandomlyToFriendNeighbors(world, source);
    }

    public static boolean isBorderNode(Node node) {
        try {
            int ownerId = node.getOwner();
            for (Node neighborNode : node.getNeighbours()) {
                if (neighborNode.getOwner() != ownerId)
                    return true;
            }
        } catch (Exception e) {
        }
        return false;

    }

    public static ArrayList<Node> getBorderNodes(World world) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : world.getMyNodes()) {
            if (isBorderNode(node)) {
                nodes.add(node);
            }
        }

        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node a, Node b) {
                return Integer.signum(getOwnerlessNeighbors(a).size() - getOwnerlessNeighbors(b).size());
            }
        });

        return nodes;
    }

    public static void logException(Exception e) {
        log(e.getMessage());
    }

    public static void log(String text) {
        log(text, 1);
    }

    public static void log(String text, int level) {
        if (level <= 2)
            System.out.println(text);
    }

    //ham border va ham critical

    public static void getCriticalNotFriendlyNodesSorted(World world, ArrayList<Node> borders, ArrayList<Node> criticalNodes) {

        //removing our criticalNodes
        ArrayList<Node> ourNodes = arrayToArrayList(world.getMyNodes());
        for (int i = 0; i < criticalNodes.size(); i++) {
            Node criticalNode = criticalNodes.get(i);
            if (ourNodes.contains(criticalNode)) {
                criticalNodes.remove(criticalNode);
                i--;
            }
        }

        criticalNodes.sort((o1, o2) -> {
            Mahdi.NodeBFSOutput o1BFSOutput = Mahdi.GetRouteToNodeGroup(world, o1, arrayToArrayList(world.getMap().getNodes()), borders, true);
            Mahdi.NodeBFSOutput o2BFSOutput = Mahdi.GetRouteToNodeGroup(world, o2, arrayToArrayList(world.getMap().getNodes()), borders, true);
            return o1BFSOutput.totalDistance - o2BFSOutput.totalDistance;
        });
    }

    public static <E> ArrayList<E> arrayToArrayList(E[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static int getEnemyId(World world) {
        return 1 - world.getMyID();
    }

}

