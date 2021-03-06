package client;

import client.MST.ahmadalli.Ahmadalli;
import client.MST.amirhosein.Amirhosein;
import client.MST.mahdi.Mahdi;
import client.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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

    ArrayList<Node> ourNodes = new ArrayList<>();
    ArrayList<Node> opponentNodes = new ArrayList<>();
    ArrayList<Node> criticalBorder = new ArrayList<>();

    public void doTurn(World world) {
        // fill this method, we've presented a stupid AI for example!
        Ahmadalli.log("--------");
        Ahmadalli.log("Cycle #" + Integer.toString(world.getTurnNumber()));
        Ahmadalli.log("We currently have " + world.getMyNodes().length + " nodes.");


        if (world.getTurnNumber() == 0) {
            Mahdi.CalculateCriticalNodes(world, ourNodes, criticalBorder, opponentNodes);
        }

        //try {
        Mahdi.InitMovements();

        ArrayList<ArrayList<Node>> clusters = Mahdi.GetOurClusters(world);
        Ahmadalli.log("AI: Mahdi.GetOurClusters finished, found " + clusters.size() + " clusters.");

        ArrayList<Node> borderNodes = Ahmadalli.getBorderNodes(world);
        Ahmadalli.log("AI: Ahmadalli.getBorderNodes finished.");

        ArrayList<Node> worthwhileBorderNodes = Mahdi.getWorthwhileBorderNodes(world, borderNodes);
        Ahmadalli.log("AI: Mahdi.getWorthwhileBorderNodes finished.");

        ArrayList<Node> untouchedNodes = new ArrayList<>(Arrays.asList(world.getMyNodes())); //Amirhosein.crave(world, worthwhileBorderNodes);
        //Ahmadalli.log("AI: Amirhossein.crave finished.");

        Map<Node, Integer> minDistanceToBorder = Amirhosein.findDis(world, borderNodes);
        Ahmadalli.log("AI: Amirhossein.findDis finished.");

        Map<Node, Mahdi.NodeBFSOutput> nearestEnemyDistance = Mahdi.FindNearestEnemyDistance(world, borderNodes);
        Ahmadalli.log("AI: FindNearestEnemyDistance for borderNodes (count = " + borderNodes.size() + ") finished.");

        for (Node node : borderNodes) {
            Mahdi.MarzbananBePish(world, node, clusters);
        }
        Ahmadalli.log("AI: Mahdi.GoGrabOwnerlessNodes and Ahmadalli.attackWeakestNearEnemy called for all border nodes.");

        try {
            Mahdi.taneLash(world, untouchedNodes, borderNodes, nearestEnemyDistance);

        } catch (Exception ex) {
            Ahmadalli.log("EXCEPTION in taneLash. Calling taneLashOld...");
            Mahdi.taneLashOld(world, untouchedNodes, minDistanceToBorder);
        }
        Ahmadalli.log("AI: Mahdi.taneLash finished.");

        Mahdi.ModafeaneHaram(world);
        Ahmadalli.log("AI: Mahdi.ModafeaneHaram finished.");
/*        } catch (Exception ex) {
            Ahmadalli.log("EXCEPTION @ AI.java. " + ex.getMessage());
            layer1Move(world);
        }*/

        int armyCount = 0;
        for (Node n : world.getMyNodes())
            armyCount += n.getArmyCount();

        ArrayList<Node> crit = new ArrayList<>(criticalBorder);
        Ahmadalli.log("Ultimate try");
        Ahmadalli.getCriticalNotFriendlyNodesSorted(world, borderNodes, crit);
        for (Node n : crit) {
            Ahmadalli.log("crave for " + n.getIndex() + " ... " + (int)(armyCount / (2 * criticalBorder.size())));
            Amirhosein.crave(world, n, (int)(armyCount / (2 * criticalBorder.size())), borderNodes, criticalBorder);
        }


        Mahdi.ApplyMovements(world);
        Ahmadalli.log(world.getTurnTimePassed() + "ms", 0);
    }

    private void layer1Move(World world) {
        try {
            Node[] myNodes = world.getMyNodes();
            for (Node source : myNodes) {
                Ahmadalli.layer1Move(world, source);
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
                int army = source.getArmyCount() / 2;
                Ahmadalli.log("method: AI.layer0Move - section:  - from:" + source.getIndex() +
                        " - to: " + destination.getIndex() + " - army: " + army);
                world.moveArmy(source, destination, army);
            }
        }
    }

}
