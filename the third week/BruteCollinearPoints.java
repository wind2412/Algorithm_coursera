
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {

	private LineSegment[] lines;
	private int count;
	
	public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
		if(points == null)	throw new NullPointerException();
		lines = new LineSegment[points.length];
		for(int i = 0; i < points.length; i ++){
			if(points[i] == null)
				throw new NullPointerException();
		}
		Arrays.sort(points);
		for(int i = 0; i < points.length-1; i ++){
			if(points[i].compareTo(points[i+1]) == 0)
				throw new IllegalArgumentException();
		}
		for(int k = 0; k < points.length; k ++){
			Comparator<Point> c = points[k].slopeOrder();
//			Arrays.sort(points, c);	//按照斜率排序	//但是这样已经改变了顺序了。comparator还是k=0的比较器，但是拍完顺序points[0]就不是原先那个数了。
			for(int i = 0; i < points.length; i ++ ){
				if(i == k)	continue;
				Point[] temp = new Point[4];
				temp[0] = points[k];
				temp[1] = points[i];
				int cnt = 2;
				for(int j = i+1; j < points.length; j ++){
					if(j == k)	continue;
					if(c.compare(points[i], points[j]) == 0){	//(4,50) (1,0) (1,1) (1,2)竟然放在了一起。因为整数原因！(50-0)/3和(50-1)/3,(50-2)/3==16...
						temp[cnt++] = points[j];
						if(cnt == 4){
							Arrays.sort(temp);
							if(count == lines.length)  resize(count*2);
							LineSegment ls = new LineSegment(temp[0], temp[3]);
							String s = ls.toString();
							int ret = 0;
//							for(int p = 0; p < count; p ++){	//判断重复。 
//								if(lines[p].toString().compareTo(s) == 0){
//									ret = 1;
//									break;
//								}
//							}
							if(temp[0].compareTo(points[k]) < 0){
								ret = 1;	//http://www.cnblogs.com/yilujuechen/articles/4856540.html 解耦合方法！

							}else{

							}
							if(ret == 0){
								lines[count++] = ls;
							}
							break;
						}
					}
				}
			}
		}
		
		
	}
	
	private void resize(int capacity){
		LineSegment[] temp = new LineSegment[capacity];
		for(int i = 0; i < count; i ++){	//这里是count，绝对不能是lines.length.因为如果是缩容的话，有可能溢出。
			temp[i] = lines[i];
		}
		lines = temp;
	}
	
	public int numberOfSegments() {       // the number of line segments
		return count;
	}
	
	public LineSegment[] segments() {               // the line segments
		resize(count);	//缩容 遍历的话就可以使用for(int i = 0; i < lines.length; i++)了。要不后边全是null。毕竟length相当于总容量，它不表示几个有效元素。
		return lines;
	}

	public static void main(String[] args) {
		
		Point[] all = new Point[11];
		all[0] = new Point(1,1);
		all[1] = new Point(3,40);
		all[2] = new Point(1,20);
		all[3] = new Point(0,20);
		all[4] = new Point(2,30);
		all[5] = new Point(4,50);
		all[6] = new Point(2,20);
		all[7] = new Point(1,2);
		all[8] = new Point(3,20);
		all[9] = new Point(1,3);
		all[10] = new Point(1,0);
		BruteCollinearPoints bcp = new BruteCollinearPoints(all);
		LineSegment[] ls = bcp.segments();
		for(int i = 0; i < ls.length; i ++){
			System.out.println(ls[i]);
		}
	}

}