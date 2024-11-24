public class Main {

    public static void main(String[] args) {
        String inputPath = "input/type1_large.txt";
        String outputPath = "my_outputs/output.txt";

        long startingTime = System.currentTimeMillis();
        try {
            Utility.checkDirectories(inputPath, outputPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Utility.processInputOutput(inputPath, outputPath);

        long endingTime = System.currentTimeMillis();
        System.out.println((endingTime - startingTime) / 1000.0 + "seconds");
    }
}
