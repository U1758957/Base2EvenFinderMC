package buffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A buffer class that uses locks and conditions to control accessing of multiple threads to store their findings
 */
public class Buffer {

    private static boolean bufferInUse = false;
    private static boolean finished = false;
    private static List<Integer> buffer = new ArrayList<>();
    private static int bufferSize = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition bufferUsage = lock.newCondition();

    /**
     * Set the flag that the program is to finish
     */
    public static void setFinished() {
        Buffer.finished = true;
    }

    /**
     * See if the program is finished
     *
     * @return the state of the program
     */
    public static boolean isFinished() {
        return finished;
    }

    /**
     * Is the buffer empty?
     *
     * @return true if the size of the buffer is < 1
     */
    public static boolean isBufferEmpty() {
        return bufferSize < 1;
    }

    /**
     * Add a power to the buffer
     *
     * @param x the power to add
     * @throws InterruptedException if a thread is interrupted while waiting
     */
    public void addToBuffer(int x) throws InterruptedException {
        lock.lock();
        try {
            if (bufferInUse) bufferUsage.await();
            bufferInUse = true;
            buffer.add(x);
            Collections.sort(buffer);
            bufferSize++;
        } finally {
            bufferInUse = false;
            bufferUsage.signal();
            lock.unlock();
        }
    }

    /**
     * Get a power from the buffer
     *
     * @return a power from the buffer
     * @throws InterruptedException if a thread is interrupted while waiting
     */
    public int getFromBuffer() throws InterruptedException {
        lock.lock();
        try {
            if (bufferInUse || (bufferSize < 1 && !finished)) bufferUsage.await();
            bufferInUse = true;
            bufferSize--;
            return buffer.get(bufferSize);
        } finally {
            bufferInUse = false;
            bufferUsage.signal();
            lock.unlock();
        }
    }
}
