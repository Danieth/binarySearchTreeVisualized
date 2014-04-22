import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BstObjShape {
    public NodeShape root = null;
    private AtomicBoolean needsPreparation = new AtomicBoolean(true);
    private int nodeCount = 0;

    public BstObjShape() {
    }

    public BstObjShape(NodeShape root) {
        this.root = root;
    }

    public void insert() {
        nodeCount++;
        needsPreparation.compareAndSet(false, true);
    }

    public void delete() {
        nodeCount--;
        needsPreparation.compareAndSet(false, true);
    }

    public void resetNodeCount() {
        nodeCount = 0;
    }

    public void draw(Graphics2D gd) {
        gd.setFont(new Font("Georgia", 0, 6));
        gd.drawString("BST Root", -12, -62);
        gd.drawRect(-10, -60, 20, 20);
        if (root == null) {
            gd.drawLine(-10, -60, 10, -40);
        } else {
            if (needsPreparation.get()) {
                root.prepareDrawFromThisNode();
                needsPreparation.compareAndSet(true, false);
            }
            gd.drawLine(0, -50, 0, 0);
            root.draw(gd);
        }
    }

    public void buildRandomTree(PersonGenerator ps, double percent, double decrement) {
        buildRandomTree(ps, percent, decrement, false);
    }

    public void buildRandomTree(PersonGenerator ps, double percent, double decrement, boolean full) {
        ps.reset();
        insert();
        if (full) {
            root = generateNodesFull(ps, percent, decrement);
        } else {
            root = generateNodes(ps, percent, decrement);
        }
    }

    private NodeShape generateNodes(PersonGenerator ps, double percent, double decrement) {
        if (Math.random() <= percent) {
            percent -= decrement;
            return new NodeShape(ps
                    .generateRandomPerson(), generateNodes(ps, percent, decrement), generateNodes(ps, percent, decrement));
        } else {
            return null;
        }
    }

    private NodeShape generateNodesFull(PersonGenerator ps, double percent, double decrement) {
        NodeShape one = null;
        NodeShape two = null;
        if (Math.random() <= percent) {
            percent -= decrement;
            one = generateNodesFull(ps, percent, decrement);
            two = generateNodesFull(ps, percent, decrement);
        }
        return new NodeShape(ps.generateRandomPerson(), one, two);
    }

    public int numberOfNodes() {
        return nodeCount;
    }
}
