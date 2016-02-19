package client.MST.amirhosein;

import client.MST.ahmadalli.Ahmadalli;
import client.MST.constants;
import client.MST.mahdi.Mahdi;
import client.model.*;
import client.*;


import java.util.*;

/**
 * Created by me on 11/02/2016.
 */

public class Amirhosein
{

    public static boolean isIt (Node node , ArrayList<Node> nodeList)
    {
        if(node != null)
            for (int i = 0; i < nodeList.size(); i++)
            {
                if(node.getIndex() == nodeList.get(i).getIndex())
                {
                    return true;
                }
            }
        return false;
    }
    public static ArrayList<Node> crave(World world, Node src, int craveMeter, ArrayList <Node> borderNodes, ArrayList<Node> criticalNodes)
    {
        int mapSize = world.getMap().getNodes().length;
        int[] mark = new int[mapSize];

        for (int i  = 0 ; i < mapSize ; i++)
        {
            mark[i] = 0;
        }

        Node[] par = new Node[mapSize];

        Queue <Node> q = new LinkedList<>();
        q.add(src);
        mark[src.getIndex()] = 1;

        while(q.size()>0 || craveMeter>0)
        {
            Node u = q.poll();
            if(u == null)
                break;
            if(!isIt(u,criticalNodes) || !isIt(u,borderNodes) )
            {
                Node[] uNeighbours = u.getNeighbours();
                for (int i = 0; i < uNeighbours.length; i++)
                {
                    if (mark[uNeighbours[i].getIndex()] == 0)
                    {
                        if (isIt(u,borderNodes) && isIt(uNeighbours[i] , borderNodes))
                        {
                            continue;
                        }
                        mark[uNeighbours[i].getIndex()] = 1;
                        par[uNeighbours[i].getIndex()] = u;
                        if(uNeighbours[i].getOwner() != world.getMyID())
                        {
                            craveMeter -= uNeighbours[i].getArmyCount();
                            if (Mahdi.IsMovingSrc(uNeighbours[i]))
                                Mahdi.CancelMovementSrc(uNeighbours[i]);
                            Mahdi.Movement(uNeighbours[i], u, uNeighbours[i].getArmyCount());
                            Ahmadalli.log("crave movement from " + uNeighbours[i].getIndex() + " to " + u.getIndex() + " count " + uNeighbours[i].getArmyCount());
                        }
                        q.add(uNeighbours[i]);
                    }
                }
            }
        }


        ArrayList<Node> ret = new ArrayList<Node>();
        Graph map = world.getMap();
        for (int i = 0; i < map.getNodes().length; i++)
        {
            if (mark[i] == 0)
            {
                ret.add(map.getNode(i));
            }
        }
        return ret;

    }

    public static Map<Node, Integer> findDis(World world, ArrayList<Node> borderNodes)
    {
        int dis[] = new int[150];
        for (int j = 0; j < 150; j++)
        {
            dis[j] = Integer.MAX_VALUE;
        }

        Queue<Node> q = new LinkedList<Node>();
        for (int i = 0; i < borderNodes.size(); i++)
        {
            q.clear();
            boolean flag = false;
            Node chief = borderNodes.get(i);
            q.add(chief);

            int mark[] = new int[150];
            for (int j = 0; j < 150; j++)
            {
                mark[j] = 0;
            }
            dis[chief.getIndex()] = 0;

            while (q.size() > 0)
            {
                Node u = q.poll();

                mark[u.getIndex()] = 1;
                for (int j = 0; j < u.getNeighbours().length; j++)
                {
                    Node neighbour = u.getNeighbours()[j];
                    if (neighbour.getOwner() == world.getMyID() && mark[neighbour.getIndex()] == 0)
                    {
                        q.add(neighbour);
                        mark[neighbour.getIndex()] = 1;
                        dis[neighbour.getIndex()] = Math.min(dis[neighbour.getIndex()], (dis[u.getIndex()] + 1));
                    }
                }
            }
        }
        Map<Node, Integer> ret = new HashMap<>();
        Graph map = world.getMap();
        for (Node node : map.getNodes())
        {
            if (node.getOwner() == world.getMyID())
                ret.put(node, dis[node.getIndex()]);
        }
        return ret;
    }


}

