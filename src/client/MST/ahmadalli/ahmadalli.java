package client.MST.ahmadalli;

import client.World;
import client.model.Node;
import client.MST.constants;
import java.util.ArrayList;

/**
 * Created by me on 11/02/2016.
 */
public class Ahmadalli {
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

    public static void layer1Move(World world, Node source){
        Node weakest = null;

        for (Node neighbor : getEnemyNeighbors(source,true)) {
            if (weakest == null || neighbor.getArmyCount() > weakest.getArmyCount()) {
                weakest = neighbor;
            }
        }

        if (weakest != null && weakest.getArmyCount() <= ahmadalli.getNodeState(source)) {
            world.moveArmy(source, weakest, (int) ((double)source.getArmyCount() * constants.c1));
        } else {
            ArrayList<Node> friendlyNeighbors = ahmadalli.getFriendlyNeighbors(source, true);
            if (friendlyNeighbors.size() > 0) {
                world.moveArmy(source, friendlyNeighbors.get((int) (friendlyNeighbors.size() * Math.random())),
                        (int) (source.getArmyCount() * constants.c2));
            }
        }
    }

    public static boolean isBorderNode(Node node)
    {
        try {
            int ownerId=node.getOwner();
            for(Node neighborNode:node.getNeighbours())
            {
                if(neighborNode.getOwner()!=ownerId)
                    return true;
            }
        }
        catch (Exception e) {}
        return false;

    }

    public static ArrayList<Node> getBorderNodes(World world)
    {
        ArrayList<Node> nodes=new ArrayList<>();
        for(Node node:world.getMyNodes()){
            if(isBorderNode(node))
            {
                nodes.add(node);
            }
        }
        return nodes;
    }
}

