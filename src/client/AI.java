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
        // fill this method, we've presented a stupid AI for example!
        Node[] myNodes = world.getMyNodes();
        for (Node source : myNodes) {
            // get neighbours
            Node[] neighbours = source.getNeighbours();
            if (neighbours.length > 0) {
                try {
                    ahmadalli.layer1Move(world,source, neighbours);
                }
                catch(Exception e) {
                    moveRandomly(world,source,neighbours);
                }
            }
        }

    }

    private void moveRandomly(World world, Node source, Node[] neighbours)
    {
        // select a random neighbour
        Node destination = neighbours[(int) (neighbours.length * Math.random())];
        // move half of the node's army to the neighbor node
        world.moveArmy(source, destination, source.getArmyCount()/2);
    }
}
