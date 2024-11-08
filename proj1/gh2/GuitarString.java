package gh2;

// TODO: uncomment the following import once you're ready to start this portion
// import deque.Deque;
// TODO: maybe more imports

import deque.ArrayDeque;
import deque.Deque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor
    private static final int _FIRST = 0;
    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {

        buffer = new ArrayDeque<Double>();
        int bufferSize = (int) Math.round(SR / frequency);
        for (int i = 0; i < bufferSize; i++) {
            buffer.addFirst(0.0);
        }

    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        int bufferSize = buffer.size();
        for (int i = 0; i < bufferSize; i++) {
            double value = Math.random() - 0.5;
            buffer.removeFirst();
            buffer.addLast(value);
        }

    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {

        double currentFirst = buffer.removeFirst();
        double mean = (currentFirst + buffer.get(0)) / 2;
        buffer.addLast(DECAY * mean);

    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        if (!buffer.isEmpty()) { return buffer.get(0); }
        return 0;
    }

    public int size() {
        return buffer.size();
    }

    public double get(int idx){
        return buffer.get(idx);
    }
}

