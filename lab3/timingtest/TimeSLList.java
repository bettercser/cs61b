package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static final int M = 10000;
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        SLList<Integer> integerSLList = new SLList<>();
        int start = 1000;
        while(true){

            for(int i = 0; i < start; i++){
                integerSLList.addLast(i);
            }
            Stopwatch stopwatch = new Stopwatch();
            for(int i = 0; i < M; i++){
                integerSLList.getLast();
            }
            double time = stopwatch.elapsedTime();
            Ns.addLast(start);
            times.addLast(time);
            opCounts.addLast(M);
            start *= 2;
            if(start > 128000) break;
        }
        printTimingTable(Ns, times, opCounts);
    }

}
