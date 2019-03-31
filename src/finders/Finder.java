package finders;

import buffer.Buffer;

import java.math.BigInteger;

public class Finder implements Runnable {

    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static Buffer buffer = new Buffer();
    public Thread t;
    private int initialValue;
    private int offset;
    private long processed = 0;

    /**
     * A constructor for the finders, defines the thread too
     *
     * @param initialValue the value to start, 2^initialValue
     * @param offset       the value to do next, 2^x (where x is initially initialValue, then increments by offset)
     */
    public Finder(int initialValue, int offset) {
        this.t = new Thread(this);
        this.initialValue = initialValue;
        this.offset = offset;
    }

    /**
     * Check if a (BigInteger) number is even
     *
     * @param n the number to check
     * @return if it is even (true) or not (false)
     */
    private boolean checkEven(BigInteger n) {
        BigInteger digit;
        while (n.compareTo(BigInteger.ZERO) > 0) {
            digit = n.mod(BigInteger.TEN);
            if (!(digit.mod(TWO).equals(BigInteger.ZERO))) return false;
            else n = n.divide(BigInteger.TEN);
        }
        return true;
    }

    /**
     * If a number was entirely even, this will be added to the buffer
     *
     * @param n the number to add to the buffer
     */
    private void setNewFinding(int n) {
        try {
            buffer.addToBuffer(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a new power of 2 and check if it is even, if so, add to the buffer
     *
     * @param n the power to generate
     */
    private void generateAndCheck(int n) {
        if (checkEven(TWO.pow(n))) setNewFinding(n);
    }

    /**
     * Get how many powers of 2 were processed by this worker
     *
     * @return the amount processed
     */
    public long getProcessed() {
        return processed;
    }

    @Override
    public void run() {
        int x = initialValue;
        while (!(Buffer.isFinished())) {
            generateAndCheck(x);
            x += offset;
            processed++;
        }
    }
}
