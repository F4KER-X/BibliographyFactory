import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to BibilographyFactory!\n");
        Scanner scan = null;
        PrintWriter pw = null;
        final int NUMBER_OF_FILES = 10;
        final String INPUT_TXT = ".bib";
        final String OUTPUT_TXT = ".json";
        final String inputFileName = "Latex";
        final String[] outputFileName = {"IEEE", "ACM", "NJ"};
        Scanner[] scanArray = new Scanner[NUMBER_OF_FILES];
        PrintWriter[] pwArray = new PrintWriter[NUMBER_OF_FILES * 3];
        int scanCounter = 0;
        int writeCounter = 0;

        File outDir = new File("files/");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        while (scanCounter < NUMBER_OF_FILES) {

            try {
                scanArray[scanCounter] = new Scanner(new FileInputStream(inputFileName + scanCounter + 1 + INPUT_TXT));
            } catch (FileNotFoundException e) {
                System.out.println("Could not open input file " + inputFileName + scanCounter + 1 + INPUT_TXT + " for reading.\nPlease check if file exists! Program will terminate after closing any opened files.");
                closeAllScanner(scanCounter, scanArray);
                System.exit(0);
            }
            scanCounter++;
        }


        while (writeCounter < (NUMBER_OF_FILES * 3)) {
            for (int i = 1; i <= 10; i++) {
                try {
                    pwArray[writeCounter] = new PrintWriter(new FileOutputStream("files/" + outputFileName[writeCounter % 3] + i + OUTPUT_TXT));
                } catch (FileNotFoundException e) {
                    System.out.println("Could not creat output file " + outputFileName[writeCounter % 3] + i + OUTPUT_TXT + "\nPlease check for problems! Program will terminate after closing all files and deleting previous ones.");
                    File fileToDelete = new File("files/");
                    deleteFiles(fileToDelete);
                    closeAllPrintWriter(writeCounter, pwArray);
                    System.exit(0);
                }
            }
            writeCounter++;
        }
    }


    public static void deleteFiles(File dirPath) {
        File[] fileList = dirPath.listFiles();
        for (File f : fileList) {
            f.delete();
        }
    }

    public static void processFilesForValidation(Scanner scan, PrintWriter pw) {


    }

    public static void closeAllScanner(int scanCounter, Scanner[] scanArray) {
        for (int i = 0; i < scanCounter; i++) {
            scanArray[i].close();
        }
    }

    public static void closeAllPrintWriter(int writeCounter, PrintWriter[] pwArray) {
        for (int i = 0; i < writeCounter; i++) {
            pwArray[i].close();
        }
    }
}