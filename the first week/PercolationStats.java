import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int numOfOpened;

    public PercolationStats(int n, int trials) { // perform trials independent
                                                 // experiments on an n-by-n
                                                 // grid
        // System.out.println(n+" "+trials);
        results = new double[trials];
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        for (int i = 0; i < trials; i++) {
            Percolation test = new Percolation(n);
            numOfOpened = 0;
            while (true) {
                int x, y;
                do {
                    x = StdRandom.uniform(1, n + 1);
                    y = StdRandom.uniform(1, n + 1); // 可能会重复多次。。。。随机数也会重复多次的。。。。
                } while (test.isOpen(x, y));
                test.open(x, y);
                numOfOpened ++;
                if (numOfOpened >= n && test.percolates()) { // numOfOpened < n
                                                             // 是非常好的优化。
                    results[i] = ((double) (numOfOpened)) / (n * n);
                    // System.out.println(results[i]);
                    // results[i] = (double)(test.getNumOfOpened())/(n*n);
                    break;
                }
            }

        }
    }

    public double mean() { // sample mean of percolation threshold
        // double sum = 0.0;
        // for(double result : results){
        // sum += result;
        // }
        // return sum/results.length;
        return StdStats.mean(results);
    }

    public double stddev() { // sample standard deviation of percolation
                             // threshold
        // double sum = 0.0;
        // double u = mean();
        // for(double result : results){
        // double num = result - u ;
        // sum += Math.pow(num, 2.0);
        // }
        // sum /= (results.length - 1);
        // return Math.sqrt(sum);
        return StdStats.stddevp(results);
    }

    public double confidenceLo() { // low endpoint of 95% confidence interval
        double u = mean();
        double o = stddev();
        return u - 1.96 * o / Math.sqrt(results.length);
    }

    public double confidenceHi() { // high endpoint of 95% confidence interval
        double u = mean();
        double o = stddev();
        return u + 1.96 * o / Math.sqrt(results.length);
    }

    public static void main(String[] args) { // test client (described below)
        PercolationStats per = new PercolationStats(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        // System.out.println(args[0]+" "+args[1]);
        System.out.println(per.mean());
        System.out.println(per.stddev());
        System.out.println(per.confidenceLo());
        System.out.println(per.confidenceHi());
    }
}
