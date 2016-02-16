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

    public static ArrayList<Node> crave (World world , ArrayList<Node> borderNodes)
    {

        int nextTurnVals[] = new int[200];
        for(int j = 0 ; j <world.getMyNodes().length;j++)
        {
            Node u = world.getMyNodes()[j];
            nextTurnVals[u.getIndex()] = u.getArmyCount();
        }

        int mark[] = new int[200];
        for(int j = 0 ; j <world.getMyNodes().length;j++)
        {
            mark[j] = 0;
        }
        int moved[] = new int[200];
        for(int j = 0 ; j <world.getMyNodes().length;j++)
        {
            moved[j] = 0;
        }
        Queue <Node> q = new LinkedList<Node>();
        for (int i = 0 ; i < borderNodes.size(); i++)
        {
            q.clear();
            boolean flag = false;
            Node chief = borderNodes.get(i);
            q.add(chief);
            mark[chief.getIndex()] = 1;
            int dis[] = new int[150];
            for(int j = 0 ; j < 150; j++)
            {
                dis[j] = 0;
            }
            while(q.size()>0 && !flag)
            {
                Node u = q.poll();
                mark[u.getIndex()] = 1;
                for(int j = 0 ; j < u.getNeighbours().length; j++ )
                {
                    Node neighbour = u.getNeighbours()[j];
                    if(neighbour.getOwner() == world.getMyID() &&  dis[neighbour.getIndex()] == 0)
                    {
                        q.add(neighbour);
                        mark[neighbour.getIndex()] = 1;
                        dis[neighbour.getIndex()] = dis[u.getIndex()] + 1;
                        if(!Ahmadalli.isBorderNode(neighbour)) //TODO: Leave a Minimum of one
                        {
                            if( nextTurnVals[neighbour.getIndex()]> constants.minimumNumberOfUnitsLeftInEachNode)
                            {
                                nextTurnVals[neighbour.getIndex()] =constants.minimumNumberOfUnitsLeftInEachNode;
                                nextTurnVals[u.getIndex()] += neighbour.getArmyCount()-constants.minimumNumberOfUnitsLeftInEachNode;
                                if(moved[neighbour.getIndex()] == 0)
                                {
                                    moved[neighbour.getIndex()] = 1;
                                    world.moveArmy(neighbour, u, neighbour.getArmyCount() - 1);
                                    flag = true;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            if( nextTurnVals[neighbour.getIndex()]> Mahdi.getMinimumRecommendedForceInBorders())
                            {
                                nextTurnVals[neighbour.getIndex()] = Mahdi.getMinimumRecommendedForceInBorders();
                                nextTurnVals[u.getIndex()] += neighbour.getArmyCount()-Mahdi.getMinimumRecommendedForceInBorders();
                                if(moved[neighbour.getIndex()] == 0)
                                {
                                    moved[neighbour.getIndex()] = 1;
                                    world.moveArmy(neighbour, u, neighbour.getArmyCount() - Mahdi.getMinimumRecommendedForceInBorders());
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList <Node> ret = new ArrayList<Node>();
        Graph map = world.getMap();
        for(int i = 0 ; i< 150 ; i++)
        {
            if(mark[i] == 0)
            {
                ret.add(map.getNode(i));
            }
        }
        return ret;

    }

    public static Map<Node,Integer> findDis (World world , ArrayList<Node> borderNodes)
    {
        int dis[] = new int[150];
        for(int j = 0 ; j < 150; j++)
        {
            dis[j] = Integer.MAX_VALUE;
        }

        Queue <Node> q = new LinkedList<Node>();
        for (int i = 0 ; i < borderNodes.size(); i++)
        {
            q.clear();
            boolean flag = false;
            Node chief = borderNodes.get(i);
            q.add(chief);
            while(q.size()>0)
            {
                Node u = q.poll();
                int mark[] = new int[150];
                for(int j = 0 ; j < 150; j++)
                {
                    mark[j] = 0;
                }
                mark[u.getIndex()]=1;
                for(int j = 0 ; j < u.getNeighbours().length; j++ )
                {
                    Node neighbour = u.getNeighbours()[j];
                    if(neighbour.getOwner() == world.getMyID() &&  mark[neighbour.getIndex()] == 0)
                    {
                        q.add(neighbour);
                        mark[neighbour.getIndex()] =1;
                        dis[neighbour.getIndex()] = Math.min( dis[neighbour.getIndex()] , (dis[u.getIndex()] + 1) );
                    }
                }
            }
        }
        Map <Node ,  Integer > ret = new HashMap<>();
        Graph map = world.getMap();
        for(Node node:map.getNodes())
        {
            ret.put(node, dis[node.getIndex()]);
        }
        return ret;
    }
}
