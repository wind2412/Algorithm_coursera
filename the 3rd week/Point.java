

import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
    	if(this.compareTo(that) == 0){
    		return Double.NEGATIVE_INFINITY;
    	}
    	else if(this.x == that.x){
    		return Double.POSITIVE_INFINITY;
    	}
    	else if(this.y == that.y){
    		return +0.0;	//java中+0.0是大于-0.0的。
    	}
    	else {
    		return ((double)(that.y - this.y))/(that.x - this.x);	//千万是浮点数啊！调bug调了好长时间。。。
    	}
    	
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
    	int temp = this.y - that.y;
    	return temp == 0 ? this.x - that.x : temp;
    	
    }

    private class ComparatorOfSlope implements Comparator<Point>{

		@Override
		public int compare(Point o1, Point o2) {
			return ((Double)slopeTo(o1)).compareTo(slopeTo(o2));	//返回int真是难办。。。直接就返回Double的默认方法了。
		}
    	
    }
    
    
    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
    	return new ComparatorOfSlope();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    	Point p1 = new Point(3, 6);
    	Point p2 = new Point(3, 7);
    	Point p3 = new Point(4, 6);
    	Point p4 = new Point(4, 7);
    	Point p5 = new Point(5, 6);
    	Point p6 = new Point(5, 7);
    	System.out.println(p1.compareTo(p3));
    	System.out.println(p1.compareTo(p2));	//无论是比较x还是比较y的优先级，这两个sysout全返回-1. 但是由于放入集合之后是两两比较，因此可以找到真正的顺序。
    	
    	Point[] points = new Point[6];
    	points[0] = p1;
    	points[1] = p2;
    	points[2] = p3;
    	points[3] = p4;
    	points[4] = p5;
    	points[5] = p6;
    	Arrays.sort(points, p1.slopeOrder());
    	for(int i = 0; i < points.length; i ++){
    		System.out.println(points[i]);
    	}
    	
    	Point pp1 = new Point(1,0);
    	Point pp2 = new Point(3,20);
    	Point pp3 = new Point(2,20);
    	Point pp4 = new Point(1,20);
    	System.out.println(pp1.slopeOrder().compare(pp2, pp3));
    	
    }
}
