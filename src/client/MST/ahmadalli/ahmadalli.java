package client.MST.ahmadalli;

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

    public static Node[] getEnemyNeighbors(Node node)
    {
        ArrayList<Node> nodes= new ArrayList<>();
        for(Node neighbor:node.getNeighbours())
        {
            if(neighbor.getOwner()!=node.getOwner())
            {
                nodes.add(neighbor);
            }
        }
        if(nodes.size()!=0)
            return (Node[])nodes.toArray();
        return null;
    }

    public static Node[] getFriendlyNeighbors(Node node)
    {
        ArrayList<Node> nodes= new ArrayList<>();
        for(Node neighbor:node.getNeighbours())
        {
            if(neighbor.getOwner()==node.getOwner())
            {
                nodes.add(neighbor);
            }
        }

        return (Node[])nodes.toArray();
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
}

