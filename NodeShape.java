import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
    final private static int maxRadius = 30;
    final private static int xShift = 50;
    final private static int yShift = 50;
    final private static double CONSTANT_OF_CENTER = 1.177793508745657;
    
    Point2D.Double center = null;
    Shape shape = null;
    private boolean black = true;
    private boolean selected = false;
    private int size = radius;
    private long timeSelected;

    public NodeShape(Person pVal, TreeNode pLkid, TreeNode pRkid) {
        super(pVal, pLkid, pRkid);
    }

    /**
     * Gives you the node that is contained in the point
     * 
     * @param point2d
     * @return
     */
    public NodeShape contains(Point2D point2d) {
        if (shape != null) {
            if (shape.contains(point2d)) {
                return this;
            }
            else {
                if (getRkid() != null) {
                    NodeShape temp = ((NodeShape) getRkid()).contains(point2d);
                    if (temp != null) {
                        return temp;
                    }
                }
                if (getLkid() != null) {
                    NodeShape temp = ((NodeShape) getLkid()).contains(point2d);
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

    public void prepareDrawFromThisNode() {
        this.center = new Point2D.Double();
        this.shape = new RoundRectangle2D.Double(center.x - size, center.y
                - size, size * 2, size * 2,30,14);
        if (this.getRkid() != null) {
            ((NodeShape) getRkid()).prepare(center, true);
        }
        if (this.getLkid() != null) {
            ((NodeShape) getLkid()).prepare(center, false);
        }
    }
    
    public void draw(Graphics2D gd) {
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
        if (selected) {
            if(size != maxRadius) {
                size += (int)((System.currentTimeMillis()-timeSelected)/200);
                size = Math.min(size, maxRadius);
                shape = new RoundRectangle2D.Double(center.x - size, center.y
                        - size, size * 2, size * 2,30,18);
            }
        } else if (size != radius) {
            size -= (int)((System.currentTimeMillis()-timeSelected)/50);
            size = Math.max(size, radius); 
            shape = new RoundRectangle2D.Double(center.x - size, center.y
                    - size, size * 2, size * 2,30,18);
        }
        gd.fill(shape);
        gd.setColor(getColor());
        gd.draw(shape);
        String[] words = new String[4];
        int i = 0;
        for(String s: getVal().allFields().split(",")) {
            words[i++] = s;
        }
        // centers the text for each node
        gd.setColor(Color.black);
        String first = "First: " + words[0];
        String last = "Last: " + words[1];
        String age = "Age: " + words[2];
        String state = "State: " + words[3];
        int max = (int) (max(first.length(), last.length(), age.length(), state.length()) / (CONSTANT_OF_CENTER));
        gd.drawString(first, (int)center.x-max, (int)center.y-7);
        gd.drawString(last, (int)center.x-max, (int)center.y-1);
        gd.drawString(age, (int)center.x-max, (int)center.y+5);
        gd.drawString(state, (int)center.x-max, (int)center.y+11);
    }

    private int max(int... ints) {
        if (ints.length < 1) {
            return Integer.MAX_VALUE;
        }
        int max = ints[0];
        for (int i = 1; i < ints.length; i++) {
            if (ints[i] > max) {
                max = ints[i];
            }
        }
        return max;
    }
    
    public void select() {
        timeSelected = System.currentTimeMillis();
        selected = true;
        black = false;
    }
    public void unselect() {
        timeSelected = System.currentTimeMillis();
        selected = false;
        black = true;
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
     */
    private void prepare(Double initialPoint, boolean rightOfParent) {
        double centerOfCircleYCoordinate = initialPoint.getY() + yShift;
        double centerOfCircleXCoordinate = 1;

        if (rightOfParent) {
            if (getLkid() != null) {
                centerOfCircleXCoordinate += ((NodeShape) getLkid())
                        .totalNodesInTree();
            }
        }
        else {
            if (getRkid() != null) {
                centerOfCircleXCoordinate += ((NodeShape) getRkid())
                        .totalNodesInTree();
            }
            centerOfCircleXCoordinate *= -1;
        }
        
        centerOfCircleXCoordinate = centerOfCircleXCoordinate * xShift
                + initialPoint.getX();

        this.center = new Point2D.Double(centerOfCircleXCoordinate, centerOfCircleYCoordinate);
        this.shape = new RoundRectangle2D.Double(center.x - size, center.y
                - size, size * 2, size * 2,30,18);
        
        if (this.getRkid() != null) {
            ((NodeShape) getRkid()).prepare(center, true);
        }
        if (this.getLkid() != null) {
            ((NodeShape) getLkid()).prepare(center, false);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (black ? 1231 : 1237);
        result = prime * result + ((center == null) ? 0 : center.hashCode());
        result = prime * result + (selected ? 1231 : 1237);
        result = prime * result + ((shape == null) ? 0 : shape.hashCode());
        result = prime * result + size;
        result = prime * result + (int) (timeSelected ^ (timeSelected >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeShape other = (NodeShape) obj;
        if (black != other.black)
            return false;
        if (center == null) {
            if (other.center != null)
                return false;
        }
        else if (!center.equals(other.center))
            return false;
        if (selected != other.selected)
            return false;
        if (shape == null) {
            if (other.shape != null)
                return false;
        }
        else if (!shape.equals(other.shape))
            return false;
        if (size != other.size)
            return false;
        if (timeSelected != other.timeSelected)
            return false;
        return true;
    }
}
