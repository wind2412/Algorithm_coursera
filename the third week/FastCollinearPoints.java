

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

	private LineSegment[] lines;
	private Point[] temp;
	private int count;

	public FastCollinearPoints(Point[] points) { // finds all line segments
													// containing 4 points
		if (points == null)
			throw new NullPointerException();
		lines = new LineSegment[points.length];
		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				throw new NullPointerException();
		}
		Arrays.sort(points);
		Point[] points2 = new Point[points.length];
		for(int i = 0; i < points.length-1; i ++){
			points2[i] = points[i];
			if(points[i].compareTo(points[i+1]) == 0)
				throw new IllegalArgumentException();
		}
		points2[points.length-1] = points[points.length-1];
		for (int k = 0; k < points2.length; k++) {	//如果用一开始的points.length 那么后边的排序。。k整个乱了。。。
			Point poi = points2[k]; // 用句柄保存，一旦数组被排序就丢了！！内存地址索引到的已经是别的位置了！这种情况用clone吧。//这种想法是错的！！就是引用！会跟着走！！
			Comparator<Point> c = poi.slopeOrder();
			Arrays.sort(points, c); // 按照斜率排序
									// //但是这样已经改变了顺序了。comparator还是k=0的比较器，但是拍完顺序points[0]就不是原先那个数了。
			int cnt = 0;
			for (int i = 0; i < points.length; i++) {
				if (i != points.length - 1) { // 最后一次不能做。因为[i + 1] =
											// [length]越界。但是最后一次必须保留。因为如果最后一个元素正好凑成了4+个，由于c.compare(
											// , )==0,不会else.
											// 然后循环结束，整个temp被舍弃，重新新的k++大循环.
					if (points[i].compareTo(poi) == 0)
						continue;
					if (cnt == 0) {
						temp = new Point[4];
						temp[0] = /* points[k] */poi; // 注意points[k]已经被排序改变了！！
						temp[1] = points[i];
						cnt += 2;
					}
					if (points[i + 1].compareTo(poi) == 0) { // 设成i+1最好！与后一个比较，如果i+1不符合，那么过了这一轮i++之后这个不符合的还可以重用做temp[1].
						continue;
					}
				}
				if (i != points.length - 1
						&& c.compare(points[i], points[i + 1]) == 0) {
					if (cnt == temp.length)
						resize(temp, cnt, cnt + 1);
					temp[cnt++] = points[i + 1];
				} else {
					// 不符合共线：1.已经多余4个点了 2.少于4个 舍弃
					if (cnt < 4) {
						cnt = 0; // 归零
						continue; // 这次必须要全都遍历完了。因为不知道有几个在一条线上
					}
					Arrays.sort(temp);	
//					for (int l = 0; l < cnt; l++) {
//						System.out.print(temp[l] + " ");
//					}
//					System.out.println(
//							cnt + " " + temp[0].slopeTo(temp[cnt - 1]));

					if (count == lines.length)
						resize(lines, count, count * 2);
					LineSegment ls = new LineSegment(temp[0], temp[cnt - 1]);
					String s1 = ls.toString();
					int ret = 0;
//					for (int p = 0; p < count; p++) { // 判断重复。 重点！
//						String s2 = lines[p].toString();
//						if (s2.compareTo(s1) == 0) { // 只要判断toString()是否相等。
//							ret = 1;
//							break;
//						}
						if(temp[0].compareTo(poi) < 0){
							ret = 1;	//http://www.cnblogs.com/yilujuechen/articles/4856540.html 解耦合方法！

						}else{

						}
//					}
					if (ret == 0) {
						lines[count++] = ls;
					}
					cnt = 0; // 归零
				}

				// for(int j = /*i+1*/0; j < points.length; j ++){
				// //j=i+1，是类似选择排序的遍历。画出来是倒三角形，这样的遍历不完整。如果有5个，可能j=i之前的检查不到。。。
				// if(points[j].compareTo(poi)==0 || j == i) continue;
				// //加上了j==i.就可以把temp[0]==temp[1]的可能性去除掉了。
				// if(c.compare(points[i], points[j]) == 0){ //(4,50) (1,0)
				// (1,1) (1,2)竟然放在了一起。因为整数原因！(50-0)/3和(50-1)/3,(50-2)/3==16...
				// if(cnt == temp.length) resize(temp, cnt, cnt+1);
				// temp[cnt++] = points[j];
				//
				// }
				// }
			}
		}

	}

	private void resize(Object[] array, int size, int capacity) {
		Object[] o = null;
		if (array instanceof LineSegment[]) {
			o = new LineSegment[capacity];
			for (int i = 0; i < size; i++) { // 这里是count，绝对不能是lines.length.因为如果是缩容的话，有可能溢出。
				o[i] = array[i];
			}
			lines = (LineSegment[]) o;
		} else if (array instanceof Point[]) {
			o = new Point[capacity];
			for (int i = 0; i < size; i++) { // 这里是count，绝对不能是lines.length.因为如果是缩容的话，有可能溢出。
				o[i] = array[i];
			}
			temp = (Point[]) o;
		}
	}

	public int numberOfSegments() { // the number of line segments
		return count;
	}

	public LineSegment[] segments() { // the line segments
		resize(lines, count, count); // 缩容 遍历的话就可以使用for(int i = 0; i <
										// lines.length;
										// i++)了。要不后边全是null。毕竟length相当于总容量，它不表示几个有效元素。
		return lines;
	}

	public static void main(String[] args) {

		Point[] all = new Point[11];
		all[0] = new Point(1, 1);
		all[1] = new Point(3, 40);
		all[3] = new Point(0, 20);
		all[4] = new Point(2, 30);
		all[5] = new Point(4, 50);
		all[6] = new Point(2, 20);
		all[7] = new Point(5, 60);
		all[8] = new Point(3, 20);
		all[2] = new Point(1, 20);
		all[9] = new Point(1, 3);
		all[10] = new Point(1, 0);
		FastCollinearPoints bcp = new FastCollinearPoints(all);
		LineSegment[] ls = bcp.segments();
		for (int i = 0; i < ls.length; i++) {
	//		System.out.println(ls[i]);
		}
		
		
		
	}

}
