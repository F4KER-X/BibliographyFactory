import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
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
        //open files
        openFiles(scanArray, NUMBER_OF_FILES, INPUT_FILE_NAME, INPUT_EXT);
        //create files
        createFiles(pwArray, scanArray, NUMBER_OF_FILES, OUTPUT_FILE_NAME, OUTPUT_EXT);

        processFilesForValidation(scanArray);


    }


    private static void deleteFiles(File dirPath) {
        File[] fileList = dirPath.listFiles();
        for (File f : fileList) {
            f.delete();
        }
    }

    public static void processFilesForValidation(Scanner[] scanArray) {

        //for (int t = 0; t < scanArray.length; t++) {
        String error = "";
        String author = "";
        String journal = "";
        String year = "";
        String title = "";
        String volume = "";
        String pages = "";
        String number = "";
        String doi = "";
        String month = "";
        String[] acmRecords;
        String[] ieeeRecords;
        String[] njRecords;


        try {
            Scanner scan = new Scanner(new FileInputStream("Latex1.bib"));
            String fileToString = fileToString(scan);
            String[] fileToStringArray = fileToString.trim().split("@ARTICLE");

            acmRecords = new String[fileToStringArray.length-1];
            ieeeRecords = new String[fileToStringArray.length-1];
            njRecords = new String[fileToStringArray.length-1];

            int counter = 0;
            for (int j = 1; j < fileToStringArray.length; j++) {

                String[] singleArticle = fileToStringArray[j].split(",");
                for (int i = 0; i < singleArticle.length; i++) {
                    String[] info = singleArticle[i].split("=");
                    if (info.length >= 2) {
                        String filed = info[0].trim();
                        if (info[1].trim().substring(1, info[1].length() - 1).isEmpty()) {
                            error = info[0].trim();
                            throw new FileInvalidException();
                        }
                        switch (filed) {
                            case "author":
                                author = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "journal":
                                journal = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "volume":
                                volume = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "year":
                                year = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "number":
                                number = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "title":
                                title = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "pages":
                                pages = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "doi":
                                doi = info[1].substring(1, info[1].length() - 1).trim();
                                break;
                            case "month":
                                month = info[1].substring(1, info[1].length() - 1).trim();
                                break;


                        }

                    }
                }

                String ieee = ieeeFormat(author, title, journal, volume, number, pages, month, year);
                ieeeRecords[counter] = ieee;
                //System.out.println("IEEE: " + ieee);
                //System.out.println();
                String acm = acmFormat(counter + 1, author, title, journal, volume, number, year, pages, doi);
                acmRecords[counter] = acm;
                // System.out.println("ACM: " + acm);
                //System.out.println();
                String nj = njFormat(author, title, journal, volume, pages, year);
                njRecords[counter] = nj;
                //System.out.println("NJ: " + nj);
                //System.out.println();

               // System.out.println("----Ended one article---");


                counter++;

            }
            System.out.println("IEEE:");
            for (String i : ieeeRecords) {
                System.out.println(i);
            }
            System.out.println("ACM");
            for (String a : acmRecords) {
                System.out.println(a);
            }
            System.out.println("NJ:");
            for (String n : njRecords) {
                System.out.println(n);
            }

            //System.out.println("ended file " + (t + 1));
            System.out.println();


        } catch (FileInvalidException e) {
            // System.out.println(e.getMessage() + "file " + t);
            System.out.println("error location " + error);
            ieeeRecords = new String[0];
            njRecords = new String[0];
            acmRecords = new String[0];
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


    }
    //}


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

    private static String fileToString(Scanner scan) {
        String fileAsString = "";
        while (scan.hasNextLine()) {
            String tempLine = scan.nextLine();
            if (!tempLine.isEmpty())
                fileAsString += tempLine;
        }

        //check
        scan.close();
        return fileAsString;
    }

    private static String ieeeFormat(String author, String title, String journal, String volume, String number, String pages, String month, String year) {

        author = author.replace(" and", ",");

        return author + "." + " \"" + title + "\", " + journal + ", vol. " + volume + ", no. " + number + ", p. " + pages + ", " + month + " " + year + ".\n";
    }

    private static String acmFormat(int counter, String author, String title, String journal, String volume, String number, String year, String pages, String doi) {
        author = author.split("and")[0] + "et al.";

        return "[" + counter + "] " + author + " " + year + ". " + title + ". " + journal + ". " + volume + ", " + number + " (" + year + "), " + pages + ". DOI:https://doi.org/" + doi + ".\n";

    }

    private static String njFormat(String author, String title, String journal, String volume, String pages, String year) {

        author = author.replace("and", "&");

        return author + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").\n";

    }


}