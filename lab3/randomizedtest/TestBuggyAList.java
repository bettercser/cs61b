package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for(int i = 0; i < N; i++){
            int operationNumber = StdRandom.uniform(0,4);
            if (operationNumber == 0){
                int randVal  = StdRandom.uniform(0,N);
                L.addLast(randVal);
                B.addLast(randVal);


            }else if (operationNumber == 1){
                int size = L.size();
                int sizeB = B.size();


            }else if (L.size() > 0){
                if (operationNumber == 2){
                    int Last = L.getLast();
                    int BLast = B.getLast();


                }else if(operationNumber == 3){
                    int Last = L.getLast();
                    L.removeLast();
                    int BLast = B.getLast();
                    B.removeLast();
                   
                   
                }
            }
        }
    }
}
