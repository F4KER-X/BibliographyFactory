import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws FileInvalidException {
        System.out.println("Welcome to BibilographyFactory!\n");
        final int NUMBER_OF_FILES = 10;
        final String INPUT_EXT = ".bib";
        final String OUTPUT_EXT = ".json";
        final String INPUT_FILE_NAME = "Latex";
        final String[] OUTPUT_FILE_NAME = {"IEEE", "ACM", "NJ"};
        Scanner[] scanArray = new Scanner[NUMBER_OF_FILES];
        PrintWriter[] pwArray = new PrintWriter[NUMBER_OF_FILES * 3];

        //create directory
        File outDir = new File("files/");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
//        //open files
//        openFiles(scanArray, NUMBER_OF_FILES, INPUT_FILE_NAME, INPUT_EXT);
//        //create files
//        createFiles(pwArray, scanArray, NUMBER_OF_FILES, OUTPUT_FILE_NAME, OUTPUT_EXT);

        try {
            String[] test = new String[0];
            int counter = 1;
            Scanner scan = new Scanner(new FileInputStream("Latex1.bib"));
//            String fileToString = fileToString(scan);
//            String[] filetoStringArray = fileToString.split("@ARTICLE");
//            for (String s : filetoStringArray) {
//                test = s.split(",");
//                //System.out.println(counter + test[0]);
//                counter++;
//            }
//            System.out.println(filetoStringArray[4]);
            while (scan.hasNextLine()){
                System.out.println(scan.nextLine());
            }
            // System.out.println(fileToString);


           // int counter = 0;
            int articleNumber = 0;

            //String line = scan.nextLine();
//            while (scan.hasNextLine()) {
//
//                String line = scan.nextLine();
//                System.out.println(line);
//                StringTokenizer str = new StringTokenizer(scan, "$");
//
//                if (!line.isEmpty()) {
//                    //int articleNumber = scan.nextInt();
//                    String[] line1 = line.split("=");
//
////                    if (scan.nextLine().endsWith()("")) {
////                        articleNumber = scan.nextInt();
////                        continue;
////
////                    }
//                    if (line1[1].length() <= 3)
//                        throw new FileInvalidException();
//
//                    else if (line1[0].equals("author")) {
//                        counter++;
//                        System.out.println(line1[1].length());
//                    }
//
//                }
//
//
//            }
            //System.out.println(counter);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void deleteFiles(File dirPath) {
        File[] fileList = dirPath.listFiles();
        for (File f : fileList) {
            f.delete();
        }
    }

    public static void processFilesForValidation(Scanner[] scanArray) {


    }

    private static void closeAllScanner(int scanCounter, Scanner[] scanArray) {
        for (int i = 0; i < scanCounter; i++) {
            scanArray[i].close();
        }
    }

    private static void closeAllPrintWriter(int writeCounter, PrintWriter[] pwArray) {
        for (int i = 0; i < writeCounter; i++) {
            pwArray[i].close();
        }
    }

    public static void openFiles(Scanner[] scanArray, int NUMBER_OF_FILES, String inputFileName, String INPUT_EXT) {
        int scanCounter = 0;
        while (scanCounter < NUMBER_OF_FILES) {
            try {
                scanArray[scanCounter] = new Scanner(new FileInputStream(inputFileName + (scanCounter + 1) + INPUT_EXT));
            } catch (FileNotFoundException e) {
                System.out.println("Could not open input file " + inputFileName + scanCounter + 1 + INPUT_EXT + " for reading.\nPlease check if file exists! Program will terminate after closing any opened files.");
                closeAllScanner(scanCounter, scanArray);
                System.exit(0);
            }
            scanCounter++;
        }

    }

    public static void createFiles(PrintWriter[] pwArray, Scanner[] scanArray, int NUMBER_OF_FILES, String[] outputFileName, String OUTPUT_TXT) {
        int writeCounter = 0;
        while (writeCounter < (NUMBER_OF_FILES * 3)) {
            for (int i = 1; i <= 10; i++) {
                try {
                    pwArray[writeCounter] = new PrintWriter(new FileOutputStream("files/" + outputFileName[writeCounter % 3] + i + OUTPUT_TXT));
                } catch (FileNotFoundException e) {
                    System.out.println("Could not creat output file " + outputFileName[writeCounter % 3] + i + OUTPUT_TXT + "\nPlease check for problems! Program will terminate after closing all files and deleting previous ones.");
                    File fileToDelete = new File("files/");
                    //close pw objects.
                    closeAllPrintWriter(writeCounter, pwArray);
                    //close scanner objects.
                    closeAllScanner(NUMBER_OF_FILES, scanArray);
                    //delete created files.
                    deleteFiles(fileToDelete);


                    System.exit(0);
                }
            }
            writeCounter++;
        }
    }

    public static String fileToString(Scanner scan) {
        String fileAsString = "";
        while (scan.hasNextLine()) {
            fileAsString += scan.nextLine();
        }

        //check
        scan.close();
        return fileAsString;
    }
}