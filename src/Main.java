public class Main {

    public static void main(String[] args) {
        String inputPath = args[0];
        String outputPath = args[1];

        long startingTime = System.currentTimeMillis();
        try {
            Utility.checkDirectories(inputPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Utility.processInputOutput(inputPath, outputPath);

        long endingTime = System.currentTimeMillis();
        System.out.println((endingTime - startingTime) / 1000.0 + "seconds");
    }
}
