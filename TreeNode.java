/**
 * TreeNode is a class that stores a Person as the node's value. It also
 * contains references to a right and left TreeNode.
 *
 * @author Richard Harrah
 * @version 04132014
 */
public class TreeNode {

    private Person val;
    private TreeNode lKid;
    private TreeNode rKid;

    /**
     * Create a node from the given parameters.
     * <p/>
     * pre: you need to create a TreeNode and tVal is not null
     * <p/>
     * post: you have a new TreeNode
     *
     * @param tVal  Person to store in this TreeNode
     * @param tLkid reference to left child TreeNode
     * @param tRkid reference to right child TreeNode
     */
    public TreeNode(Person tVal, TreeNode tLkid, TreeNode tRkid) {
        this.val = tVal;
        this.lKid = tLkid;
        this.rKid = tRkid;
    }

    /**
     * Get the left kid.
     * <p/>
     * pre: you need the left kid of this TreeNode
     * <p/>
     * post: you have the left kid of this TreeNode
     *
     * @return left child TreeNode
     */
    public TreeNode getLkid() {
        return lKid;
    }

    /**
     * Set the lKid to the given parameter.
     * <p/>
     * pre: you need to change the left kid of this TreeNode
     * <p/>
     * post: you have changed the left kid of this TreeNode
     *
     * @param leftKid new left child TreeNode
     */
    public void setLkid(TreeNode leftKid) {
        this.lKid = leftKid;
    }

    /**
     * Get the right kid.
     * <p/>
     * pre: you need the right kid of this TreeNode
     * <p/>
     * post: you have the right kid of this TreeNode
     *
     * @return right child TreeNode
     */
    public TreeNode getRkid() {
        return rKid;
    }

    /**
     * Set the rKid to the given parameter.
     * <p/>
     * pre: you need to change the right kid of this TreeNode
     * <p/>
     * post: you have changed the right kid of this TreeNode
     *
     * @param rightKid new right child TreeNode
     */
    public void setRkid(TreeNode rightKid) {
        this.rKid = rightKid;
    }

    /**
     * Get the val of this node.
     * <p/>
     * pre: you need the value of this TreeNode
     * <p/>
     * post: you have the value of this TreeNode
     *
     * @return value of this TreeNode
     */
    public Person getVal() {
        return val;
    }

    /**
     * Set the val to the given parameter.
     * <p/>
     * pre: you need to change the value of this TreeNode and val is not null
     * <p/>
     * post: you have changed the value of this TreeNode
     *
     * @param value new value for TreeNode
     */
    public void setVal(Person value) {
        this.val = value;
    }

}
