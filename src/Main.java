public class Main {

    public static void main(String[] args) {
        String inputPath = args[0];
        String outputPath = args[1];

        long startingTime = System.currentTimeMillis();
        try {
            // If the input directory is valid
            Utility.checkDirectories(inputPath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Reads the input file and writes the required outputs line by line to the output file
        Utility.processInputOutput(inputPath, outputPath);

        long endingTime = System.currentTimeMillis();

        // Printing the total run time of the program
        System.out.println((endingTime - startingTime) / 1000.0 + "seconds");
    }
}
