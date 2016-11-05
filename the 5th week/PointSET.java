
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
	
	private Set<Point2D> rbtree;
	
	public PointSET() { // construct an empty set of points
		rbtree = new TreeSet<>();
	}
	
	public boolean isEmpty() { // is the set empty?
		return rbtree.isEmpty();
	}
	
	public int size() { // number of points in the set
		return rbtree.size();
	}
	
	public void insert(Point2D p) { // add the point to the set (if it is not
									// already in the set)
		if(p != null)	rbtree.add(p);
	}
	
	public boolean contains(Point2D p) { // does the set contain point p?
		return rbtree.contains(p);
	}
	
	public void draw() { // draw all points to standard draw
		for(Point2D p : rbtree){
			StdDraw.point(p.x(), p.y());
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside
													// the rectangle
		Set<Point2D> set = new TreeSet<>();
		for(Point2D p : rbtree){
			if(rect.contains(p)){
				set.add(p);
			}
		}
		return set;
	}
	
	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to
										// point p; null if the set is empty
		if(rbtree.isEmpty())	return null;
		double minDistance = Double.MAX_VALUE;
		Point2D min = null;
		for(Point2D point : rbtree){
			double currentDistance = p.distanceTo(point);
			if(currentDistance < minDistance) {
				minDistance = currentDistance;
				min = point;
			}
		}
		return min;
	}
	
	private Set<Point2D> get(){
		return this.rbtree;
	}

	public static void main(String[] args) { // unit testing of the methods
												// (optional)
		String filename = "kdtree/input10K.txt";
//        In in = new In(filename);

//        StdDraw.enableDoubleBuffering();

        // initialize the two data structures with point from standard input
//        PointSET brute = new PointSET();
//        KdTree kdtree = new KdTree();
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//            brute.insert(p);
//        }

 //       StdDraw.clear();
 //       StdDraw.setPenColor(StdDraw.BLACK);
 //       StdDraw.setPenRadius(0.01);
 //       brute.draw();
 //       StdDraw.show();
        
        
 //       StdOut.println(brute.size());
	}

}
