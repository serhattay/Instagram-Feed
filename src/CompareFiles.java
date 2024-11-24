import java.io.*;

public class CompareFiles {
    public static void main(String[] args) {
        String file1Path = "output/output.txt"; // Path to the resulting output file
        String file2Path = "output/otype1_large.txt"; // Path to the correct output file

        try {
            boolean areFilesIdentical = compareFiles(file1Path, file2Path);
            if (areFilesIdentical) {
                System.out.println("The files are identical.");
            } else {
                System.out.println("The files are different.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the files:");
            e.printStackTrace();
        }
    }

    private static boolean compareFiles(String file1Path, String file2Path) throws IOException {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2Path))) {

            String line1, line2;
            int lineNumber = 0;

            while ((line1 = reader1.readLine()) != null) {
                lineNumber++;
                line2 = reader2.readLine();

                if (line2 == null || !line1.equals(line2)) {
                    System.out.printf("Files differ at line %d:%n", lineNumber);
                    System.out.printf("File 1: %s%n", line1);
                    System.out.printf("File 2: %s%n", line2 != null ? line2 : "EOF");
                    return false;
                }
            }

            // Check if file2 has extra lines
            if (reader2.readLine() != null) {
                System.out.println("File 2 has extra lines.");
                return false;
            }
        }

        return true;
    }
}