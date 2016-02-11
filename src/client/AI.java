package client;

import client.MST.ahmadalli.ahmadalli;
import client.MST.constants;
import client.model.Node;

/**
 * AI class.
 * You should fill body of the method {@link #doTurn}.
 * Do not change name or modifiers of the methods or fields
 * and do not add constructor for this class.
 * You can add as many methods or fields as you want!
 * Use world parameter to access and modify game's
 * world!
 * See World interface for more details.
 */
public class AI {
    public void doTurn(World world) {
        int myID= world.getMyID();

        // fill this method, we've presented a stupid AI for example!
        Node[] myNodes = world.getMyNodes();
        for (Node source : myNodes) {
            // get neighbours
            Node[] neighbours = source.getNeighbours();
            if (neighbours.length > 0) {

                Node weakest = neighbours[0];

                for(Node neighbor: ahmadalli.getEnemyNeighbors(source))
                {
                    //will make sure that if there's an enemy node, the weakest will be the enemy
                    if(weakest.getOwner()==myID&&neighbor.getOwner()!=myID)
                    {
                        weakest=neighbor;
                    }
                    if(neighbor.getOwner()!=myID&& neighbor.getArmyCount()>weakest.getArmyCount())
                    {
                        weakest=neighbor;
                    }
                }

                if(weakest.getOwner() != myID &&  weakest.getArmyCount() <= ahmadalli.getNodeState(source)) {
                    world.moveArmy(source, weakest,(int)(source.getArmyCount() * constants.c1));
                }
                else if(weakest.getOwner() == myID){
                    world.moveArmy(source, neighbours[(int)(neighbours.length * Math.random())],
                            (int)(source.getArmyCount() * constants.c2));
                }
            }
        }

    }

}
