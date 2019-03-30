public class Main {

    /**
     * The main method to get everything going and output results
     *
     * @param args first argument is time to run, 2nd argument is threads to run, or can specify time only, or nothing
     */
    public static void main(String[] args) {

        int timeToRun;
        int threadCount;

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
    }

}
