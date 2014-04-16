import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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

public class NodeShape extends Ellipse2D.Double implements Shape {
    
    public NodeShape(double x, double y, double width, double height) {
        super(x-width/2,y-height/2,width,height);
    }
    
    @Override
    public boolean contains(Point2D arg0) {
        return super.contains(arg0);
    }
    
    /*TODO Shape
     * 
     * Need to know where to draw child nodes
     * 
     */

}
