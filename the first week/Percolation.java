import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // 不要忘了并查集全是一维。所以必须把二维空间变成一维的空间。
    private WeightedQuickUnionUF top; // 并查集内部的数组是表示他们联通关系，不表示是否开闭。
    private WeightedQuickUnionUF compare; //(对照回流模型)
    private int length; // 从1开始计数的实际长度
    private boolean[] id; // 如果从0开始不好协调。因此也从1开始吧。

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException("argument error.");
        length = n;
        top = new WeightedQuickUnionUF(n * n + 2); // 首位是虚拟的注水口，设置排水口!!!!要不最后一排如果联通跟注水口并查还是与出水口并查就是个问题。
        compare = new WeightedQuickUnionUF(n * n + 1);
        id = new boolean[n * n + 1];
    }

    private void verify(int i, int j) { // 人手动输入的
        if (i < 1 || i > length || j < 1 || j > length)
            throw new IndexOutOfBoundsException("out of bounds.");
    }

    public void open(int i, int j) { // open site (row i, column j) if it is not
                                     // open already
        // System.out.println(i + " " + j);
        // if (isOpen(i, j)) //那边调用了这边就不要调用了。
        // return;
        id[(i - 1) * length + j] = true;
        if (i != 1 && isOpen(i - 1, j)){
            top.union((i - 2) * length + j, (i - 1) * length + j); // 上边可以连接就并查上边
            compare.union((i - 2) * length + j, (i - 1) * length + j);
        }
        if (i != length && isOpen(i + 1, j)){
            top.union((i - 1) * length + j, (i) * length + j); // 下边
            compare.union((i - 1) * length + j, (i) * length + j);
        }
        if (j != 1 && isOpen(i, j - 1)){
            top.union((i - 1) * length + j - 1, (i - 1) * length + j); // 左边
            compare.union((i - 1) * length + j - 1, (i - 1) * length + j);
        } 
        if (j != length && isOpen(i, j + 1)){
            top.union((i - 1) * length + j + 1, (i - 1) * length + j); // 右边
            compare.union((i - 1) * length + j + 1, (i - 1) * length + j);
        }
        if (i == 1){
            top.union(0, (i - 1) * length + j); // 第一行与入水口对齐!!别忘了
            compare.union(0, (i - 1) * length + j);
        }
        if (i == length)
            top.union((i - 1) * length + j, length * length + 1);// 不能这么简单合并。有可能最后一行的祖先就不是top了。这样isFull()会失败
        //源代码有漏洞。如果大小相等的话，右参数会合并到左参数上。因为源码判断用的是<号而不是<=！！
    }

    public boolean isOpen(int i, int j) { // is site (row i, column j) open?
        verify(i, j);
        return id[(i - 1) * length + j];
    }

    public boolean isFull(int i, int j) { // is site (row i, column j) full?
        verify(i, j);
        if (isOpen(i, j)) // 非常好的优化。
            return compare.find((i - 1) * length + j) == compare.find(0);
        return false;
    }

    public boolean percolates() { // does the system percolate?
        // for (int i = 1; i <= length; i++) {
        // if (isFull(length, i)) { // 从最后一行第一个开始
        // return true;
        // }
        // }
        return top.find(length * length + 1) == top.find(0);
        // return false;
    }

    public static void main(String[] args) { // test client (optional)
        Percolation percolation = new Percolation(4);
        System.out.println(percolation.percolates());
        percolation.open(1, 1);
        percolation.open(2, 1);
        System.out.println(percolation.percolates());
        percolation.open(3, 1);
        percolation.open(3, 2);
        System.out.println(percolation.isFull(3, 2));
        percolation.open(4, 1);
        System.out.println(percolation.percolates());
    }
}
