package client.MST.mahdi;

import client.MST.ahmadalli.Ahmadalli;
import client.MST.constants;
import client.World;
import client.model.Node;

import java.util.*;

/**
 * Created by me on 11/02/2016.
 */
public class Mahdi {

    private static class AttackData {
        Node source;
        Node dest;
        int count;
    }

    public static class NodeBFSData {
        Node node;
        Node parent;
        int distance;

        NodeBFSData(Node _node, Node _parent, int _distance) {
            node = _node;
            parent = _parent;
            distance = _distance;
        }
    }

    public static class NodeBFSOutput {
        Node target;
        Node nextInPath;
        int totalDistance;

        NodeBFSOutput(Node _target, Node _nextInPath, int _totalDistance) {
            target = _target;
            nextInPath = _nextInPath;
            totalDistance = _totalDistance;
        }
    }

    static ArrayList<AttackData> attacks;

    static int minimumRecommendedForceInBorders = 10;

    public static int getMinimumRecommendedForceInBorders() {
        return minimumRecommendedForceInBorders;
    }

    public static void increaseMinimumRecommendedForceInBorders() {
        minimumRecommendedForceInBorders++;
    }

    public static ArrayList<Node> getWeakBorderNodes(ArrayList<Node> borderNodes) {
        if (getMinimumRecommendedForceInBorders() > constants.minimumRecommendedForceInBordersFailSafe) {
            return new ArrayList<>();
        }

        ArrayList<Node> output = new ArrayList<>();

        int minRecom = getMinimumRecommendedForceInBorders();

        for (Node node : borderNodes) {
            if (node.getArmyCount() < minRecom)
                output.add(node);
        }

        if (output.size() == 0) {
            increaseMinimumRecommendedForceInBorders();
            return getWeakBorderNodes(borderNodes);
        }

        return output;
    }

    public static Map<Node, NodeBFSOutput> FindNearestEnemyDistance(World world, ArrayList<Node> nodes) {
        Map<Node, NodeBFSOutput> output = new HashMap<>();
        for (Node i : nodes) {
            output.put(i, Mahdi.GetRouteToNearestEnemy(world, i));
        }
        return output;
    }

    public static void taneLash(World world, ArrayList<Node> untouchedNodes, ArrayList<Node> borderNodes, Map<Node, NodeBFSOutput> nearestEnemyDistance) {
        for (Node node : untouchedNodes) {
            if (borderNodes.contains(node))
                continue;
            if (node.getOwner() == world.getMyID()) {

                double bestVal = Integer.MAX_VALUE;
                Node bestNode = null;
                ArrayList<Node> bestPath = null;
                for (Node b : borderNodes) {
                    if (nearestEnemyDistance.get(b) == null)
                        continue;
                    ArrayList<Node> path = GetPath(world, node, b);
                    double val = path.size() * constants.taneLashC1
                            + nearestEnemyDistance.get(b).totalDistance * constants.taneLashC2;

                    if ((val < bestVal) ||
                            ((val == bestVal) && (bestNode != null) &&
                                    (nearestEnemyDistance.get(b).totalDistance < nearestEnemyDistance.get(bestNode).totalDistance))) {
                        bestVal = val;
                        bestPath = path;
                        bestNode = b;
                    }
                }

                Ahmadalli.log("method: Mahdi.taneLash - from:" + node.getIndex() +
                        " - to: " + bestPath.get(0).getIndex() + " - army: " + node.getArmyCount());
                Mahdi.Movement(node, bestPath.get(0), node.getArmyCount());
            }
        }
    }

    public static void taneLashOld(World world, ArrayList<Node> untouchedNodes, Map<Node, Integer> minDistanceToBorder) {
        Ahmadalli.log("method: Mahdi.taneLashOld - untochedNodes.size() = " + untouchedNodes.size());
        for (Node node : untouchedNodes) {
            if (node.getOwner() == world.getMyID()) {
                int nearerNeighborCount = 0;
                for (Node neighbour : node.getNeighbours()) {
                    if (neighbour.getOwner() == world.getMyID())
                        if (minDistanceToBorder.get(neighbour) < minDistanceToBorder.get(node)) {
                            nearerNeighborCount++;
                        }
                }

                if (nearerNeighborCount == 0)
                    continue;

                int curForces = node.getArmyCount();
                int moveCount = node.getArmyCount() / nearerNeighborCount;

                Ahmadalli.log("method: Mahdi.taneLashOld - node #" + node.getIndex() + " minDistanceToBorder = " + minDistanceToBorder.get(node));
                for (Node neighbour : node.getNeighbours()) {
                    if (neighbour.getOwner() == world.getMyID())
                        if (minDistanceToBorder.get(neighbour) < minDistanceToBorder.get(node)) {
                            int army = Math.min(moveCount, curForces);
                            Ahmadalli.log("method: Mahdi.taneLashOld - from:" + node.getIndex() +
                                    " - to: " + neighbour.getIndex() + " - army: " + army);
                            Mahdi.Movement(node, neighbour, army);
                            curForces -= moveCount;
                            if (curForces < 0)
                                break;
                        }
                }
            }
        }
    }

    public static ArrayList<Node> getOnlyEnemyNeighbors(Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node neighbor : node.getNeighbours()) {
            if (neighbor.getOwner() != node.getOwner() && (neighbor.getOwner() != -1)) {
                nodes.add(neighbor);
            }
        }

        return nodes;
    }

    public static void GoGrabOwnerlessNodes(Node source) {
        //Ahmadalli.log("a " + source.getIndex());
        if (IsMovingSrc(source))
            return;
        //Ahmadalli.log("b");
        ArrayList<Node> ownerlessNeighbors = Ahmadalli.getOwnerlessNeighbors(source);

        int count = 0;
        for (Node ownerless : ownerlessNeighbors) {
            if (!IsMovingDest(ownerless)) {
                count++;
            }
        }
        //Ahmadalli.log("c " + count);


        if (count == 0)
            return;

        double factor = constants.factorOfSendingToNewNodeWhenCurrentMightBeInDanger;
        if (getOnlyEnemyNeighbors(source).size() == 0)
            factor = constants.factorOfSendingToNewNodeWhenCurrentIsSafe2;

        //Ahmadalli.log("d " + factor + " " + getOnlyEnemyNeighbors(source).size());

        for (Node ownerless : ownerlessNeighbors) {
            if (!IsMovingDest(ownerless)) {
                Ahmadalli.log("method: Mahdi.GoGrabOwnerlessNodes - from:" + source.getIndex() +
                        " - to: " + ownerless.getIndex() + " - army: " + (int) (source.getArmyCount() * factor));
                Movement(source, ownerless, (int) (source.getArmyCount() * factor));
            }
        }
    }

    public static void InitMovements() {
        attacks = new ArrayList<>();
    }

    public static boolean IsMovingSrc(Node n) {
        for (AttackData d : attacks)
            if (d.source == n)
                return true;
        return false;
    }

    public static boolean IsMovingDest(Node n) {
        for (AttackData d : attacks)
            if (d.dest == n)
                return true;
        return false;
    }

    public static int GetArmyCountAfterMovements(Node n) {
        int count = n.getArmyCount();

        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).dest == n) {
                count += attacks.get(i).count;
            } else if (attacks.get(i).source == n) {
                count -= attacks.get(i).count;
            }

        return count;
    }

    public static void CancelMovementSrc(Node n) {
        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).source == n) {
                attacks.remove(attacks.get(i));
            }
    }

    public static void CancelMovementDest(Node n) {
        for (int i = 0; i < attacks.size(); i++)
            if (attacks.get(i).dest == n) {
                attacks.remove(attacks.get(i));
            }
    }

    public static boolean Movement(Node src, Node dest, int count) {
        if (count <= 0) {
            Ahmadalli.log("Invalid Movement Command from " + src.getIndex() + " to " + dest.getIndex() + " with count of " + count);
            return false;
        }

        if (IsMovingSrc(src))
            return false;

        AttackData data = new AttackData();
        data.source = src;
        data.dest = dest;
        data.count = count;

        attacks.add(data);

        return true;
    }

    public static void ApplyMovements(World world) {
        for (AttackData d : attacks)
            world.moveArmy(d.source, d.dest, d.count);
    }

    public static void Escape(Node n) {
        ArrayList<Node> neighbors = Ahmadalli.getFriendlyNeighbors(n, false);

        //This poor node is alone :(
        if (neighbors.size() == 0) {
            //Attack anyway to somewhere :).
            ArrayList<Node> enemies = Ahmadalli.getEnemyNeighbors(n, false);
            if (enemies.size() == 0)
                return;

            Node weakest = enemies.get(0);
            for (int i = 1; i < enemies.size(); i++) {
                if (enemies.get(i).getArmyCount() < weakest.getArmyCount())
                    weakest = enemies.get(i);
            }

            //If we're already moving somewhere ownerless, this attack will be ignored;
            // since that movement is registered before this.
            Movement(n, weakest, n.getArmyCount());
            Ahmadalli.log("method: Mahdi.Escape (Poor Alone Node) - from:" + n.getIndex() +
                    " - to: " + weakest.getIndex() + " - army: " + n.getArmyCount());
            return;
        }

        Node smallestNeighbor = neighbors.get(0);
        int smallestVal = GetArmyCountAfterMovements(smallestNeighbor);
        for (int i = 1; i < neighbors.size(); i++) {
            if (GetArmyCountAfterMovements(neighbors.get(i)) < smallestVal) {
                smallestNeighbor = neighbors.get(i);
                smallestVal = GetArmyCountAfterMovements(smallestNeighbor);
            }
        }

        //TODO: If we're gonna escape, should we escape to an 'ownerless' node or not? Currently, we're not doing that.

        if (Mahdi.IsMovingSrc(n))
            Mahdi.CancelMovementSrc(n);
        if (Mahdi.IsMovingDest(n))
            Mahdi.CancelMovementDest(n);

        Ahmadalli.log("method: Mahdi.Escape (Escape) - from:" + n.getIndex() +
                " - to: " + smallestNeighbor.getIndex() + " - army: " + n.getArmyCount());
        Movement(n, smallestNeighbor, n.getArmyCount());
    }

    public static NodeBFSOutput GetRouteToNearestEnemy(World world, Node source) {
        Map<Node, NodeBFSData> data = new HashMap<>();
        for (Node i : world.getFreeNodes())
            data.put(i, new NodeBFSData(i, null, Integer.MAX_VALUE));
        for (Node i : world.getOpponentNodes())
            data.put(i, new NodeBFSData(i, null, Integer.MAX_VALUE));
        for (Node i : Ahmadalli.getBorderNodes(world))
            if (i != source)
                data.put(i, new NodeBFSData(i, null, Integer.MAX_VALUE));

        data.put(source, new NodeBFSData(source, null, 0));

        Queue<Node> Q = new LinkedList<>();

        Q.add(source);

        Queue<Node> AttackCandidates = new LinkedList<>();

        while (Q.size() != 0) {
            Node current = Q.poll();

            ArrayList<Node> neighbors = new ArrayList<>(Arrays.asList(current.getNeighbours()));
            Collections.sort(neighbors, new Comparator<Node>() {
                @Override
                public int compare(Node a, Node b) {
                    return Integer.signum(-(a.getArmyCount() - b.getArmyCount()));
                }
            });
            for (Node neighbor : neighbors) {
                if (neighbor.getOwner() != world.getMyID()) {
                    if (data.get(neighbor).distance == Integer.MAX_VALUE) {
                        data.get(neighbor).distance = data.get(current).distance + 1;
                        data.get(neighbor).parent = current;
                        if (neighbor.getOwner() == -1) {
                            Q.add(neighbor);
                        } else {
                            AttackCandidates.add(neighbor);
                        }
                    }
                }
            }
        }
        //AttackCandidates is already sorted by minimum distance.
        int counter = 0;
        while (AttackCandidates.size() != 0) {
            Node target = AttackCandidates.poll();
            Node n = target;
            while (data.get(n).parent != source) {
                n = data.get(n).parent;
                counter++;
                if (counter > constants.GetRouteEndlessLoopThreshold) {
                    Ahmadalli.log("FATAL ERROR in Mahdi.GetRouteToNearestEnemy : EndlessLoopThreshold reached.");
                    return null;
                }
            }

            if (!IsMovingDest(n))
                return new NodeBFSOutput(target, n, data.get(target).distance);
        }
        return null;
    }

    public static void MarzbananBePish(World world, Node node) {
        NodeBFSOutput route = Mahdi.GetRouteToNearestEnemy(world, node);
        try {
            if (route != null) {
                if (route.totalDistance <= constants.EnemySoCloseDistance) {
                    if (route.target.getArmyCount() <= Ahmadalli.getNodeState(node)) {
                        Ahmadalli.log("method: Mahdi.MarzbananBePish (Escape) - from:" + node.getIndex() +
                                " - to: " + route.nextInPath.getIndex() + " - army: " + (int) (node.getArmyCount() * constants.c1));
                        Mahdi.Movement(node, route.nextInPath, (int) (node.getArmyCount() * constants.c1));
                    } else {
                        Mahdi.Escape(node);
                    }
                } else { //There's no enemy in distance == 1 of this node, so this node is safe.
                    Mahdi.Movement(node, route.nextInPath, (int) (node.getArmyCount() * constants.factorOfSendingToNewNodeWhenCurrentIsSafe));
                }

            } else { //Fall back on previous method.
                PreviousMarzbananAlgorithm(world, node);
            }
        } catch (Exception ex) {
            Ahmadalli.log("EXCEPTION IN MarzbananBePish.");
            PreviousMarzbananAlgorithm(world, node);
        }
    }

    public static void PreviousMarzbananAlgorithm(World world, Node node) {
        Mahdi.GoGrabOwnerlessNodes(node);
        if (Ahmadalli.attackWeakestNearEnemy(world, node) == -1)
            Mahdi.Escape(node);
    }

    public static ArrayList<Node> GetPath(World world, Node src, Node dest) {
        Map<Node, NodeBFSData> data = new HashMap<>();
        for (Node i : world.getFreeNodes())
            data.put(i, new NodeBFSData(i, null, Integer.MAX_VALUE));
        for (Node i : world.getOpponentNodes())
            data.put(i, new NodeBFSData(i, null, Integer.MAX_VALUE));
        for (Node i : world.getMyNodes())
            if (i != src)
                data.put(i, new NodeBFSData(i, null, Integer.MAX_VALUE));

        data.put(src, new NodeBFSData(src, null, 0));

        Queue<Node> Q = new LinkedList<>();

        Q.add(src);

        while (Q.size() != 0) {
            Node current = Q.poll();

            if (current == dest)
                break;

            for (Node neighbor : current.getNeighbours()) {
                if (data.get(neighbor).distance == Integer.MAX_VALUE) {
                    data.get(neighbor).distance = data.get(current).distance + 1;
                    data.get(neighbor).parent = current;
                    Q.add(neighbor);
                }
            }
        }

        ArrayList<Node> path = new ArrayList<>();
        Node cur = dest;
        while (cur != src) {
            path.add(cur);
            cur = data.get(cur).parent;
        }
        Collections.reverse(path);
        return path;
    }
}
