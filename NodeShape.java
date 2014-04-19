import java.awt.*;
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
    final private static int RADIUS = 17;
    private int size = RADIUS;
    final private static int MAX_RADIUS = 30;
    final private static int X_SHIFT = 30;
    final private static int Y_SHIFT = 50;
    private static final int GROW_RATE = 200;
    private static final int SHRINK_RATE = 50;
    private static final String FONT_NAME = "Georgia";
    
    private static final int MIN_FONT_SIZE = 4;
    private int fontSize = MIN_FONT_SIZE;
    private static final int MAX_FONT_SIZE = 5;
    private static final int MIN_Y_SCALE = 2;
    private int yScale = MIN_Y_SCALE;
    private static final int MAX_Y_SCALE = 6;
    private static final int MIN_DELTA_Y = 5;
    private int deltaY = MIN_DELTA_Y;
    private static final int MAX_DELTA_Y = 10;
    
    Point2D.Double center = null;
    Shape shape = null;
    private boolean black = true;
    private boolean selected = false;
    private long timeSelected;
    
    private long timeUntilUnselected = -1;

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
            } else {
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
                - size, size * 2, size * 2, 30, 14);
        if (this.getRkid() != null) {
            ((NodeShape) getRkid()).prepare(center, true);
        }
        if (this.getLkid() != null) {
            ((NodeShape) getLkid()).prepare(center, false);
        }
    }

    public void draw(Graphics2D gd) {
        if (this.getRkid() != null) {
            NodeShape rightKid = ((NodeShape) getRkid());
            if(rightKid.center != null) {
                gd.draw(new Line2D.Double(this.center, rightKid.center));
                rightKid.draw(gd);
            }
        }
        if (this.getLkid() != null) {
            NodeShape leftKid = ((NodeShape) getLkid());
            if(leftKid.center != null) {
                gd.draw(new Line2D.Double(this.center, leftKid.center));
                leftKid.draw(gd);
            }
        }
        gd.setColor(gd.getBackground());
        final long currentTime = System.currentTimeMillis() - timeSelected;
        if (selected) {
            if (size < MAX_RADIUS) {
                size += (int) ((currentTime) / GROW_RATE);
                size = Math.min(size, MAX_RADIUS);
                shape = new RoundRectangle2D.Double(center.x - size, center.y
                        - size, size * 2, size * 2, 30, 18);
            }
            if (fontSize < MAX_FONT_SIZE) {
                fontSize += (int) ((currentTime) / GROW_RATE);
                fontSize = Math.min(fontSize, MAX_FONT_SIZE);
            }
            if (yScale < MAX_Y_SCALE) {
                yScale += (int) ((currentTime) / GROW_RATE);
                yScale = Math.min(yScale, MAX_Y_SCALE);
            }
            if (deltaY < MAX_DELTA_Y) {
                deltaY += (int) ((currentTime) / GROW_RATE);
                deltaY = Math.min(deltaY, MAX_DELTA_Y);
            }
            if(timeUntilUnselected > 0 && (timeUntilUnselected-currentTime) < 0) {
                timeUntilUnselected = -1;
                unselect();
            }
        } else {
            if (size > RADIUS) {
                size -= (int) ((currentTime) / SHRINK_RATE);
                size = Math.max(size, RADIUS);
                shape = new RoundRectangle2D.Double(center.x - size, center.y
                        - size, size * 2, size * 2, 30, 18);
            }
            if (fontSize > MIN_FONT_SIZE) {
                fontSize -= (int) ((currentTime) / SHRINK_RATE);
                fontSize = Math.max(fontSize, MIN_FONT_SIZE);
            }
            if (yScale > MIN_Y_SCALE) {
                yScale -= (int) ((currentTime) / SHRINK_RATE);
                yScale = Math.max(yScale, MIN_Y_SCALE);
            }
            if (deltaY > MIN_DELTA_Y) {
                deltaY -= (int) ((currentTime) / SHRINK_RATE);
                deltaY = Math.max(deltaY, MIN_DELTA_Y);
            }
        }
        gd.fill(shape);
        gd.setColor(getColor());
        gd.draw(shape);
        String[] words;
        // centers the text for each node
        gd.setColor(Color.black);
        String first;
        String last;
        String age;
        String state;

        if (!selected || size < MAX_RADIUS) {
            words = getVal().allFields().split(",");
            first = "First: " + words[0];
            last = "Last: " + words[1];
            age = "Age: " + words[2];
            state = "State: " + words[3];
        } else {
            words = getVal().allLongFields().split(",");
            first = "First Name: " + words[0];
            last = "Last Name: " + words[1];
            age = "Age: " + words[2];
            state = "State: " + words[3];
        }

        gd.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));

        int alignX = size - 2;
        int alignY = fontSize + yScale;

        gd.drawString(first, (int) (center.x - alignX), (int) center.y - alignY);
        gd.drawString(last, (int) (center.x - alignX), (int) (center.y - alignY + deltaY));
        gd.drawString(age, (int) (center.x - alignX), (int) (center.y - alignY + (deltaY * 2)));
        gd.drawString(state, (int) (center.x - alignX), (int) center.y - alignY + (deltaY * 3));
    }

    public void select() {
        timeSelected = System.currentTimeMillis();
        selected = true;
        black = false;
        timeUntilUnselected=-1;
    }
    
    public void select(long i) {
        select();
        timeUntilUnselected = i;
    }

    public void unselect() {
        if(timeUntilUnselected < 0) {
            timeSelected = System.currentTimeMillis();
            selected = false;
            black = true;
        }
    }

    public void toggleColor() {
        black = !black;
    }

    private Color getColor() {
        if (black) {
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
        double centerOfCircleYCoordinate = initialPoint.getY() + Y_SHIFT;
        double centerOfCircleXCoordinate = 1;

        if (rightOfParent) {
            if (getLkid() != null) {
                centerOfCircleXCoordinate += ((NodeShape) getLkid())
                        .totalNodesInTree();
            }
            centerOfCircleXCoordinate *= -1;
        } else {
            if (getRkid() != null) {
                centerOfCircleXCoordinate += ((NodeShape) getRkid())
                        .totalNodesInTree();
            }
        }

        centerOfCircleXCoordinate = centerOfCircleXCoordinate * X_SHIFT
                + initialPoint.getX();

        this.center = new Point2D.Double(centerOfCircleXCoordinate, centerOfCircleYCoordinate);
        this.shape = new RoundRectangle2D.Double(center.x - size, center.y
                - size, size * 2, size * 2, 30, 18);

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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NodeShape other = (NodeShape) obj;
        if (black != other.black) {
            return false;
        }
        if (center == null) {
            if (other.center != null) {
                return false;
            }
        } else if (!center.equals(other.center)) {
            return false;
        }
        if (selected != other.selected) {
            return false;
        }
        if (shape == null) {
            if (other.shape != null) {
                return false;
            }
        } else if (!shape.equals(other.shape)) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        if (timeSelected != other.timeSelected) {
            return false;
        }
        return true;
    }
}
