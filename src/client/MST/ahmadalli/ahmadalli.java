package client.MST.ahmadalli;

import client.model.Node;

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

        return (Node[])nodes.toArray();
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
}

