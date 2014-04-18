import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/* Clarifications:
 * This code is all the work of Daniel Ackerman. I used no other code but
 * my own in the process of creating this solution. No one else, to the best of
 * my efforts, and to the extent of my knowledge, has viewed this work except
 * for myself, and my professors. I have collaborated with no one, received help
 * from no one, and talked to no one about this solution. All code here is the
 * individual work of Daniel Ackerman.
 * 
 * Copyright (c) <2013> <Daniel Ackerman>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above clarifications, copyright notice, and this permission
 * notice shall be included in all copies of the Software.
 *
 */

public class NodeShape extends TreeNode {
    final private static int radius = 17;
    final private static int xShift = 50;
    final private static int yShift = 50;

    /**
     * The center can EASILY be extracted from shape, but I want to finish the algorithms first
     */
    Point2D.Double center = null;
    Shape shape = null;
    private boolean black = true;

    public NodeShape(Person pVal, TreeNode pLkid, TreeNode pRkid) {
        super(pVal, pLkid, pRkid);
    }

    /**
     * Gives you the node that is contained in the point
     * 
     * @param arg0
     * @return
     */
    public NodeShape contains(Point2D.Double arg0) {
        if (shape != null) {
            if (shape.contains(arg0)) {
                return this;
            }
            else {
                if (getRkid() != null) {
                    NodeShape temp = ((NodeShape) getRkid()).contains(arg0);
                    if (temp != null) {
                        return temp;
                    }
                }
                if (getLkid() != null) {
                    NodeShape temp = ((NodeShape) getLkid()).contains(arg0);
                    if (temp != null) {
                        return temp;
                    }
                }
            }
        }
        return null;
    }

    public int totalNodesInTree() {
        int res = 1;
        if (this.getRkid() != null) {
            res += ((NodeShape) getRkid()).totalNodesInTree();
        }
        if (this.getLkid() != null) {
            res += ((NodeShape) getLkid()).totalNodesInTree();
        }
        return res;
    }

    /*
     * TopNode has its coordinates locked
     * 
     * The direction the node is being pointed at is critical to drawing it. It
     * has to be adjusted IFF it has kids in the opposite direction. Otherwise
     * we are ok drawing them in the normal fashion.
     * 
     * DrawRoot DrawKid requires direction of parent(boolean) If child is right
     * child, we need the absolute maximum count of nodes to the right. We shift
     * the drawing of this node that many 'shifts' in the direction the parent
     * is pointing to us.
     * 
     * 
     * 50 0 60 56 70
     * 
     * Because of 56 in the above tree we have to shift the drawing of 60 by one
     * width. Once we know the x coordinate of the 60, we infer the y coordinate
     * based on the depth of the node, and then directly draw it to the screen
     * like the root. Drawing the children follows the same process based on the
     * coordinates of the parents.
     * 
     * 50 will always and forever be drawn in the same location, because that
     * greately simplifies the algorithm.
     * 
     * To sum the process is, get coordinates of parent, get direction from
     * parent, find the number of steps from that parent you have to take and
     * then draw the node, recursively iterate for children.
     * 
     * Once we KNOW the coordinates of the node, it is much easier to draw the
     * children.
     */

    public void drawAbsolutely(Graphics2D gd, Point2D.Double initialPoint) {
        this.center = initialPoint;
        this.shape = new RoundRectangle2D.Double(initialPoint.x - radius, initialPoint.y
                - radius, radius * 2, radius * 2,10,10);
        int depth = 0; // when drawing absolutely we assume the depth at this node is 0
        if (this.getRkid() != null) {
            ((NodeShape) getRkid()).prepare(initialPoint, true, depth + 1);
        }
        if (this.getLkid() != null) {
            ((NodeShape) getLkid()).prepare(initialPoint, false, depth + 1);
        }
        draw(gd);
    }
    
    static int re = 0;

    private void draw(Graphics2D gd) {
        if (this.getRkid() != null) {
            NodeShape rightKid = ((NodeShape)getRkid());
            gd.draw(new Line2D.Double(this.center, rightKid.center));
            rightKid.draw(gd);
        }
        if (this.getLkid() != null) {
            NodeShape leftKid = ((NodeShape)getLkid());
            gd.draw(new Line2D.Double(this.center, leftKid.center));
            leftKid.draw(gd);
        }
        gd.setColor(gd.getBackground());
        gd.fill(shape);
        gd.setColor(getColor());
        gd.draw(shape);
        int iter = -8;
        /*
         *             if(i == 2) {
                gd.drawString("Age: " +s, (int)center.x-16, (int)center.y + iter);  
            } else {
                gd.drawString(s, (int)center.x-16, (int)center.y + iter);
            }
            iter+=6;
            i++;
         */
        String[] words = new String[4];
        int i = 0;
        for(String s: getVal().allFields().split(",")) {
            words[i++] = s;
        }
        gd.drawString("First: " + words[1], (int)center.x-14, (int)center.y-8);
        gd.drawString("Last: " + words[0], (int)center.x-14, (int)center.y-2);
        gd.drawString("Age: " + words[2], (int)center.x-14, (int)center.y+4);
        gd.drawString("State: " + words[3], (int)center.x-14, (int)center.y+10);
    }
    
    public void toggleColor() {
        black = !black;
    }

    private Color getColor() {
        if(black) {
            return Color.black;
        } else {
            return Color.red;
        }
    }

    /**
     * @param gd
     * @param initialPoint
     * @param depth
     * @return the center of the shape that is drawn
     */
    private void prepare(Double initialPoint, boolean rightOfParent, int depth) {

        double centerOfCircleYCoordinate = initialPoint.getY() + yShift;
        double centerOfCircleXCoordinate = 1;

        // to get the xShift, we completely ignore the nodes in the opposite
        // direction of the parent. We only worry about the the nodes
        // 'in-between' this node and the parent node, which is the total of
        // number of nodes in the tree towards the parent
        if (rightOfParent) {
            if (getLkid() != null) {
                centerOfCircleXCoordinate += ((NodeShape) getLkid())
                        .totalNodesInTree();
            }
        }
        else { // leftOfParent
            if (getRkid() != null) {
                centerOfCircleXCoordinate += ((NodeShape) getRkid())
                        .totalNodesInTree();
            }
            centerOfCircleXCoordinate *= -1;
        }
        
        centerOfCircleXCoordinate = centerOfCircleXCoordinate * xShift
                + initialPoint.getX();

        this.center = new Point2D.Double(centerOfCircleXCoordinate, centerOfCircleYCoordinate);
        this.shape = new RoundRectangle2D.Double(center.x - radius, center.y
                - radius, radius * 2, radius * 2,10,10);
        
        if (this.getRkid() != null) {
            ((NodeShape) getRkid()).prepare(center, true, depth + 1);
        }
        if (this.getLkid() != null) {
            ((NodeShape) getLkid()).prepare(center, false, depth + 1);
        }
    }

}
