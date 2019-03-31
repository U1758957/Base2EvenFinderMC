import buffer.Buffer;
import finders.Finder;

public class Main {

    private static Buffer buffer = new Buffer();

    /**
     * The main method to get everything going and output results
     *
     * @param args first argument is time to run, 2nd argument is threads to run, or can specify time only, or nothing
     */
    public static void main(String[] args) {

        int timeToRun;
        int threadCount;
        long totalProcessed = 0;

        switch (args.length) {
            case 1:
                timeToRun = Integer.parseInt(args[0]);
                threadCount = Runtime.getRuntime().availableProcessors();
                break;
            case 2:
                timeToRun = Integer.parseInt(args[0]);
                threadCount = Integer.parseInt(args[1]);
                break;
            default:
                timeToRun = 60;
                threadCount = Runtime.getRuntime().availableProcessors();
                break;
        }

        String plural = timeToRun != 1 ? "seconds" : "second";
        System.out.println("Running for " + timeToRun + " " + plural + System.lineSeparator());

        Finder[] finders = new Finder[threadCount];

        for (int x = 0; x < threadCount; x++) {
            finders[x] = new Finder(x, threadCount);
        }

        try {
            for (Finder finder : finders) {
                finder.t.start();
            }
            Thread.sleep(timeToRun * 1000);
            Buffer.setFinished();
            for (Finder finder : finders) {
                finder.t.join();
                totalProcessed += finder.getProcessed();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!Buffer.isBufferEmpty()) {
            try {
                System.out.println("Found: 2^" + buffer.getFromBuffer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.lineSeparator() + "Total powers of two processed: " + totalProcessed);

    }
}
