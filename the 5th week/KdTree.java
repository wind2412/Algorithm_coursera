

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

	private Node root;
	private double MinDistance;
	private Point2D min;
	private int cnt = 0;
	
	private class Node{
		private Point2D p; // the point
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree
		private boolean isVertical;
		private RectHV rect;	//to get the div of a line. prevent from out of range.
//		private Point2D lbxy;	//the vertex of the line which is the left/bottom of the two
//		private Point2D rtxy;	//the vertex of the line which is the right/top of the two
		
		//lbxy & rtxy the two only have too little information to draw a line. Must use a RectHV !!
		public Node(Point2D p, boolean isVertical, RectHV rect){
			this.p = p;
			this.rect = rect;
			this.isVertical = isVertical;
			this.lb = null;
			this.rt = null;
		}
	}
	
	public int size(){
		return cnt;
	}
	
	public boolean isEmpty(){
		return size() == 0;
	}
	
	private boolean contains(Node x, Point2D p){
		if(x == null)	return false;
		if(p.equals(x.p))	return true;
		if(compare(p, x.p, x.isVertical) < 0)	return contains(x.lb, p);	//千万别忘了这个也要归类！！！....
		else return contains(x.rt, p);
	}
	
	public boolean contains(Point2D p){
		return contains(root, p);
	}
	
	private Node insert(Node x, Point2D p, boolean isVertical, RectHV rect){
		if(x == null)	{
			cnt ++;
			return new Node(p, isVertical, rect);
		}
		if(x.p.equals(p))	return x;
		RectHV r = x.rect;
		if(compare(p, x.p, isVertical) >= 0){	//大于等于0调了两个小时。。。。。==
			if(isVertical){	//如果此时是竖直的，那么insert的下一个一定是水平的。
				if(x.rt != null)	x.rt = insert(x.rt, p, !isVertical, x.rt.rect);	//加上的这4句话漂亮！！因为创建insert之后每个结点都有rect，还要创建干毛？？
				//所以有个诀窍了！每次想要递归new的时候就把new对象缓存进每个结点中去，从而减少递归new次数！
				else x.rt = insert(x.rt, p, !isVertical, new RectHV(x.p.x(), r.ymin(), r.xmax(), r.ymax()));
			}else{	//反之是竖直的。
				if(x.rt != null)	x.rt = insert(x.rt, p, !isVertical, x.rt.rect);
				else x.rt = insert(x.rt, p, !isVertical, new RectHV(r.xmin(), x.p.y(), r.xmax(), r.ymax()));
			}
		}
		else if(compare(p, x.p, isVertical) < 0){
			if(isVertical){
				if(x.lb != null)	x.lb = insert(x.lb, p, !isVertical, x.lb.rect);
				else x.lb = insert(x.lb, p, !isVertical, new RectHV(r.xmin(), r.ymin(), x.p.x(), r.ymax()));
			}else{
				if(x.lb != null)	x.lb = insert(x.lb, p, !isVertical, x.lb.rect);
				else x.lb = insert(x.lb, p, !isVertical, new RectHV(r.xmin(), r.ymin(), r.xmax(), x.p.y()));
			}
		}
//		else{	//由于compare()中的 只x相等 返回了1， 所以=0的情况只有两个点x，y完全相等。因此什么也不做。
//			
//		}
		return x;
	}
	
	public void insert(Point2D p) {
		if(p == null)	throw new NullPointerException();
		root = insert(root, p, true, new RectHV(0, 0, 1, 1));
	}

	private void draw(Node x){
		if(x != null){
			
			draw(x.lb);	//Recursion
			
			StdDraw.setPenRadius(0.002);
			if(x.isVertical){
	            StdDraw.setPenColor(StdDraw.RED);
	            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
			}else{
	            StdDraw.setPenColor(StdDraw.BLUE);
	            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
			}
			
			StdDraw.setPenRadius(0.01);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.point(x.p.x(), x.p.y());
			
			draw(x.rt);	//Recursion
		}
	}
	
	public void draw() {
		draw(root);
		StdDraw.show();
	}

	private void range(Node x, Queue<Point2D> q, RectHV rect){	//非常有道理！！好好重看一遍！！！
		if(x != null /*&& rect.contains(x.p)*/){	//can't judge at the first time. if the root is not contained to the rect, It'll go wrong.
			if(!rect.intersects(x.rect))	return;		//根本不相交，那么子节点也肯定没有。这个需要好好考虑。//相交可以作为一个判断条件。
			boolean compareToLo = (x.isVertical && x.p.x() > rect.xmin() || !x.isVertical && x.p.y() > rect.ymin() );
			boolean compareToHi = (x.isVertical && x.p.x() <= rect.xmax() || !x.isVertical && x.p.y() <= rect.ymax());
			if(compareToLo)	range(x.lb, q, rect);
			if(rect.contains(x.p))	q.enqueue(x.p);	
			if(compareToHi)	range(x.rt, q, rect);
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> q = new Queue<>();
		range(root, q, rect);
		return q;
	}

	private void nearest(Node x, Point2D query){
		if(x == null)	return;		
		if(x.rect.distanceSquaredTo(query) >= MinDistance) return;	//这句也非常重要！可以简化很多步骤。
		double currentDistance = x.p.distanceSquaredTo(query);
		if(MinDistance > currentDistance){
			MinDistance = currentDistance;
			min = x.p;
		}
		if(compare(query, x.p, x.isVertical) < 0){	//把某一坐标相等的状况设成if 和 else if的其中一个就可以了。
			nearest(x.lb, query);		//这个不能只写一个！！因为kd树的话是可能向左向右全会接近，
										//和标准二叉树的【一定】会接近是不同的！
			nearest(x.rt, query);		//因此要左右都写！！
		}
		else if(compare(query, x.p, x.isVertical) >= 0){
			nearest(x.rt, query);
			nearest(x.lb, query);
		}
		return;
	}
	
	public Point2D nearest(Point2D query) {
		MinDistance = Double.MAX_VALUE;
		nearest(root, query);
		return min;
	}

	private double compare(Point2D p1, Point2D p2, boolean isVertical){
		if(isVertical)	return p1.x() - p2.x();
		return p1.y() - p2.y();
	}
	
	private void inorderTraversal(Node x, StringBuilder sb){
		if(x != null){
			inorderTraversal(x.lb, sb);
			sb.append(x.p);
			inorderTraversal(x.rt, sb);
		}
	}
	
	private String inorderTraversal(){
		StringBuilder sb = new StringBuilder();
		inorderTraversal(root, sb);
		return sb.toString();
	}
	
//	@Override
//	public String toString() {
//		return inorderTraversal();
//	}
	
	public static void main(String[] args) {
//		String filename = "kdtree/circle10000.txt";
//        In in = new In(filename);
//
//        StdDraw.enableDoubleBuffering();
//
//        // initialize the two data structures with point from standard input
//        KdTree k = new KdTree();
//        KdTreeOthers ko = new KdTreeOthers();
//        KdTreeSagmentationFault kdsf = new KdTreeSagmentationFault();
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            k.insert(p);
//            ko.insert(p);
////            StdOut.print(k.toString().equals(ko.toString()));StdOut.println(" "+p);
//            kdsf.insert(p);
////            StdOut.println(k);
////            StdOut.println(ko);
//        }
////        String k1 = k.inorderTraversal();
////        String k2 = ko.inorderTraversal();
////        String k3 = kdsf.inorderTraversal();
//		k.draw();
//		StdDraw.show();
//		StdDraw.pause(5000);
////		StdDraw.clear();
//		ko.draw();
////		StdDraw.pause(3000);
////		kdsf.draw();
//		StdDraw.show();
////		Point2D min = k.nearest(new Point2D(0.2, 1));
////		Point2D min1 = k.nearest(new Point2D(0.1, 1));
////		Point2D min2 = ko.nearest(new Point2D(0.1, 1));
////		StdDraw.setPenRadius(0.03);
////		StdDraw.point(min.x(), min.y());
////		StdOut.println(min1);
////		StdOut.println(min2);
////		k.inorderTraversal();
////		for(Point2D p : k.range(new RectHV(0, 0.5, 1, 1))){
////			StdOut.println(p);
////		}
	}

}
