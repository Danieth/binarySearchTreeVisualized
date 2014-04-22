package me.danieth.bstvisualized;

/**
 * me.danieth.bstvisualized.TreeNode is a class that stores a me.danieth.bstvisualized.Person as the node's value. It also
 * contains references to a right and left me.danieth.bstvisualized.TreeNode.
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
     * pre: you need to create a me.danieth.bstvisualized.TreeNode and tVal is not null
     * <p/>
     * post: you have a new me.danieth.bstvisualized.TreeNode
     *
     * @param tVal  me.danieth.bstvisualized.Person to store in this me.danieth.bstvisualized.TreeNode
     * @param tLkid reference to left child me.danieth.bstvisualized.TreeNode
     * @param tRkid reference to right child me.danieth.bstvisualized.TreeNode
     */
    public TreeNode(Person tVal, TreeNode tLkid, TreeNode tRkid) {
        this.val = tVal;
        this.lKid = tLkid;
        this.rKid = tRkid;
    }

    /**
     * Get the left kid.
     * <p/>
     * pre: you need the left kid of this me.danieth.bstvisualized.TreeNode
     * <p/>
     * post: you have the left kid of this me.danieth.bstvisualized.TreeNode
     *
     * @return left child me.danieth.bstvisualized.TreeNode
     */
    public TreeNode getLkid() {
        return lKid;
    }

    /**
     * Set the lKid to the given parameter.
     * <p/>
     * pre: you need to change the left kid of this me.danieth.bstvisualized.TreeNode
     * <p/>
     * post: you have changed the left kid of this me.danieth.bstvisualized.TreeNode
     *
     * @param leftKid new left child me.danieth.bstvisualized.TreeNode
     */
    public void setLkid(TreeNode leftKid) {
        this.lKid = leftKid;
    }

    /**
     * Get the right kid.
     * <p/>
     * pre: you need the right kid of this me.danieth.bstvisualized.TreeNode
     * <p/>
     * post: you have the right kid of this me.danieth.bstvisualized.TreeNode
     *
     * @return right child me.danieth.bstvisualized.TreeNode
     */
    public TreeNode getRkid() {
        return rKid;
    }

    /**
     * Set the rKid to the given parameter.
     * <p/>
     * pre: you need to change the right kid of this me.danieth.bstvisualized.TreeNode
     * <p/>
     * post: you have changed the right kid of this me.danieth.bstvisualized.TreeNode
     *
     * @param rightKid new right child me.danieth.bstvisualized.TreeNode
     */
    public void setRkid(TreeNode rightKid) {
        this.rKid = rightKid;
    }

    /**
     * Get the val of this node.
     * <p/>
     * pre: you need the value of this me.danieth.bstvisualized.TreeNode
     * <p/>
     * post: you have the value of this me.danieth.bstvisualized.TreeNode
     *
     * @return value of this me.danieth.bstvisualized.TreeNode
     */
    public Person getVal() {
        return val;
    }

    /**
     * Set the val to the given parameter.
     * <p/>
     * pre: you need to change the value of this me.danieth.bstvisualized.TreeNode and val is not null
     * <p/>
     * post: you have changed the value of this me.danieth.bstvisualized.TreeNode
     *
     * @param value new value for me.danieth.bstvisualized.TreeNode
     */
    public void setVal(Person value) {
        this.val = value;
    }

}
