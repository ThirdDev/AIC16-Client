package client;

import client.MST.ahmadalli.Ahmadalli;
import client.MST.amirhosein.Amirhosein;
import client.MST.mahdi.Mahdi;
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
    Ahmadalli ahmadalli = new Ahmadalli();
    Amirhosein amirhosein = new Amirhosein();
    Mahdi mahdi = new Mahdi();

    public void doTurn(World world) {
        // fill this method, we've presented a stupid AI for example!

        try {

        }
        catch (Exception ex) {
            layer1Move(world);
        }
    }

    private void layer1Move(World world) {
        try {

            Node[] myNodes = world.getMyNodes();
            for (Node source : myNodes) {
                ahmadalli.layer1Move(world, source);
            }

        } catch (Exception e) {
            layer0Move(world);
        }
    }

    private void layer0Move(World world) {
        Node[] myNodes = world.getMyNodes();
        for (Node source : myNodes) {
            // get neighbours
            Node[] neighbours = source.getNeighbours();
            if (neighbours.length > 0) {
                // select a random neighbour
                Node destination = neighbours[(int) (neighbours.length * Math.random())];
                // move half of the node's army to the neighbor node
                world.moveArmy(source, destination, source.getArmyCount() / 2);
            }
        }
    }

}
