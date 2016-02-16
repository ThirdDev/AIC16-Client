package client.MST.ahmadalli;

import client.World;
import client.model.Node;
import client.MST.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by me on 11/02/2016.
 */
public class Ahmadalli {
    public static ArrayList<Node> getEnemyNeighbors(Node node, boolean emptyNeighbors) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node neighbor : node.getNeighbours()) {
            if (neighbor.getOwner() != node.getOwner() || (emptyNeighbors && neighbor.getOwner() == -1)) {
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
        int armyCount = node.getArmyCount();
        if (armyCount <= constants.c3)
            return 0;
        if (armyCount <= constants.c4)
            return 1;
        return 2;
    }

    public static boolean attackWeakestNearEnemy(World world, Node source, ArrayList<Node> alreadySentForce) {
        int attackedCount = 0;

        for (Node ownerless : getOwnerlessNeighbors(source)) {
            if (!alreadySentForce.contains(ownerless)) {
                int army = source.getArmyCount() - 1; //constants.countOfArmyToAttackToOwnerlessNeighbors;
                Ahmadalli.log("method: ahmadalli.attackWeakestNearEnemy - section: ownerless - from:" + source.getIndex() +
                        " - to: " + ownerless.getIndex() + " - army: " + army);
                world.moveArmy(source, ownerless, army);
                attackedCount++;
                alreadySentForce.add(ownerless);

                return true;
            }
        }

        Node weakest = null;

        for (Node neighbor : getEnemyNeighbors(source, true)) {
            if (weakest == null || neighbor.getArmyCount() > weakest.getArmyCount()) {
                weakest = neighbor;
            }
        }

        if (weakest != null && weakest.getArmyCount() <= getNodeState(source)) {
            int army = (int) ((double) (source.getArmyCount() - attackedCount) * constants.c1);
            Ahmadalli.log("method: ahmadalli.attackWeakestNearEnemy - section: weakest - from:" + source.getIndex() +
                    " - to: " + weakest.getIndex() + " - army: " + army);
            world.moveArmy(source, weakest, army);
            return true;
        }

        return false;
    }

    public static void moveRandomlyToFriendNeighbors(World world, Node source) {
        ArrayList<Node> friendlyNeighbors = getFriendlyNeighbors(source, true);
        if (friendlyNeighbors.size() > 0) {
            int army = (int) (source.getArmyCount() * constants.c2);
            Node randomFriendlyNeighbor = friendlyNeighbors.get((int) (friendlyNeighbors.size() * Math.random()));
            Ahmadalli.log("method: ahmadalli.moveRandomlyToFriendNeighbors - section:  - from:" + source.getIndex() +
                    " - to: " + randomFriendlyNeighbor.getIndex() + " - army: " + army);
            world.moveArmy(source, randomFriendlyNeighbor, army);
        }
    }

    public static void layer1Move(World world, Node source, ArrayList<Node> alreadySentForce) {
        if (!attackWeakestNearEnemy(world, source, alreadySentForce))
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

        Collections.sort(nodes,new Comparator<Node>()
        {
            @Override
            public int compare(Node a,Node b)
            {
                return Integer.signum( getOwnerlessNeighbors(a).size() - getOwnerlessNeighbors(b).size());
            }
        });

        return nodes;
    }

    public static void logException(Exception e) {
        log(e.getMessage());
    }

    public static void log(String text) {
        System.out.println(text);
    }

}

