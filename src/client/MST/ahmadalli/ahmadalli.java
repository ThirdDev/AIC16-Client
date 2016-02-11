package client.MST.ahmadalli;

import client.World;
import client.model.Node;
import client.MST.constants;
import java.util.ArrayList;

/**
 * Created by me on 11/02/2016.
 */
public class ahmadalli {
    public static void test()
    {
        System.out.println("It's a test");
    }

    public static ArrayList<Node> getEnemyNeighbors(Node node, boolean emptyNeighbors)
    {
        ArrayList<Node> nodes= new ArrayList<>();
        for(Node neighbor:node.getNeighbours())
        {
            if(neighbor.getOwner()!=node.getOwner()|| (emptyNeighbors&&neighbor.getOwner()==-1))
            {
                nodes.add(neighbor);
            }
        }

       return nodes;
    }

    public static ArrayList<Node> getFriendlyNeighbors(Node node, boolean emptyNeighbors)
    {
        ArrayList<Node> nodes= new ArrayList<>();
        for(Node neighbor:node.getNeighbours())
        {
            if(neighbor.getOwner()==node.getOwner()||(emptyNeighbors&&neighbor.getOwner()==-1))
            {
                nodes.add(neighbor);
            }
        }

        return nodes;
    }

    public static int getNodeState(Node node)
    {
        int armyCount = node.getArmyCount();
        if(armyCount <= constants.c3)
            return 0;
        if(armyCount <= constants.c4)
            return 1;

        return 2;
    }

    public static void layer1Move(World world, Node source, Node[] neighbours){
        int myID = world.getMyID();

        Node weakest = neighbours[0];

        for (Node neighbor : neighbours) {
            //will make sure that if there's an enemy node, the weakest will be the enemy
            if (weakest.getOwner() == myID && neighbor.getOwner() != myID && neighbor.getOwner() != -1) {
                weakest = neighbor;
            }
            if (neighbor.getOwner() != myID && neighbor.getArmyCount() > weakest.getArmyCount()) {
                weakest = neighbor;
            }
        }

        if (weakest.getOwner() != myID && weakest.getArmyCount() <= ahmadalli.getNodeState(source)) {
            System.out.println("attack node " + source.getIndex() + " to weakest: " + weakest.getIndex());
            world.moveArmy(source, weakest, (int) (source.getArmyCount() * constants.c1));
        } else {
            ArrayList<Node> friendlyNeighbors = ahmadalli.getFriendlyNeighbors(source, true);
            if (friendlyNeighbors.size() > 0) {
                world.moveArmy(source, friendlyNeighbors.get((int) (friendlyNeighbors.size() * Math.random())),
                        (int) (source.getArmyCount() * constants.c2));
            }
        }
    }
}

