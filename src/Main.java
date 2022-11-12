import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to BibilographyFactory!\n");
        final int NUMBER_OF_FILES = 10;
        final String INPUT_EXT = ".bib";
        final String OUTPUT_EXT = ".json";
        final String INPUT_FILE_NAME = "Latex";
        final String[] OUTPUT_FILE_NAME = {"IEEE", "ACM", "NJ"};
        Scanner[] scanArray = new Scanner[NUMBER_OF_FILES];
        PrintWriter[][] pwArray = {new PrintWriter[NUMBER_OF_FILES], new PrintWriter[NUMBER_OF_FILES], new PrintWriter[NUMBER_OF_FILES],};


        //open files
        readFiles(scanArray, NUMBER_OF_FILES, INPUT_FILE_NAME, INPUT_EXT);
        //create files
        createFiles(pwArray, NUMBER_OF_FILES, OUTPUT_FILE_NAME, OUTPUT_EXT, scanArray);

        processFilesForValidation(scanArray, pwArray, OUTPUT_FILE_NAME, OUTPUT_EXT);

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter file name: ");

        String fileName = scan.nextLine();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String x = br.readLine();
            while (x !=null) {
                System.out.println(x);
                x = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, another attempt");
            fileName = scan.nextLine();
            try {
                br = new BufferedReader(new FileReader(fileName));
                String x = br.readLine();
                while (x != (null)) {
                    System.out.println(x);
                    x = br.readLine();
                }
                br.close();
            } catch (FileNotFoundException j) {
                System.out.println("oops");
            } catch (IOException j) {

            }

        } catch (IOException e) {

        }
    }


    private static void deleteFiles(String filesName) {

        File f = new File(filesName);
        f.delete();
    }


    public static void processFilesForValidation(Scanner[] scanArray, PrintWriter[][] pw, String[] OUTPUT_FILE_NAME, String ext) {
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

        //get file by file
        for (int k = 0; k < scanArray.length; k++) {
            try {
                //Scanner sc = new Scanner(new FileInputStream("Latex1.bib"));
                //file to String
                String fileToString = fileToString(scanArray[k]);
                //split based on articles
                String[] fileToStringArray = fileToString.trim().split("@ARTICLE");

                acmRecords = new String[fileToStringArray.length - 1];
                ieeeRecords = new String[fileToStringArray.length - 1];
                njRecords = new String[fileToStringArray.length - 1];

                int counter = 0;
                //article by article (remove first because always empty)
                for (int j = 1; j < fileToStringArray.length; j++) {

                    String[] singleArticle = fileToStringArray[j].split(",");
                    for (int i = 0; i < singleArticle.length; i++) {
                        String[] info = singleArticle[i].split("=");
                        if (info.length >= 2) {
                            String filed = info[0].trim();
                            if (info[1].trim().substring(1, info[1].length() - 1).isEmpty()) {
                                error = info[0].trim();

                                //filesToDelete = new String[3];
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
                    //System.out.println(ieee);
                    ieeeRecords[counter] = ieee;

                    String acm = acmFormat(counter + 1, author, title, journal, volume, number, year, pages, doi);
                    acmRecords[counter] = acm;

                    String nj = njFormat(author, title, journal, volume, pages, year);
                    njRecords[counter] = nj;


                    counter++;


                }

                writeToFile(pw[0][k], ieeeRecords);
                writeToFile(pw[1][k], acmRecords);
                writeToFile(pw[2][k], njRecords);


            } catch (FileInvalidException e) {
                System.out.println("error location " + error);
                for (String s : OUTPUT_FILE_NAME) {
                    deleteFiles(s + (k + 1) + ext);
                }
                for (PrintWriter[] p : pw) {
                    p[k].close();
                }
                //deleteFiles(filesToDelete);


            }
        }

    }

    //no changes
    private static void closeAllScanner(Scanner[] scanArray) throws NullPointerException {
        for (Scanner sc : scanArray) {
            sc.close();
        }
    }

    //no changes
    private static void closeAllPrintWriter(PrintWriter[][] pwArray) throws NullPointerException {
        for (int j = 0; j < pwArray.length; j++) {
            for (int i = 0; i < pwArray[j].length; i++) {
                pwArray[j][i].close();
            }
        }
    }


    //no changes
    public static void readFiles(Scanner[] scanArray, int NUMBER_OF_FILES, String inputFileName, String INPUT_EXT) {
        for (int i = 1; i <= NUMBER_OF_FILES; i++) {
            String fileName = inputFileName + i + INPUT_EXT;
            try {
                scanArray[i - 1] = new Scanner(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                System.out.println("Could not open input file " + fileName + " for reading.\nPlease check if file exists! Program will terminate after closing any opened files.");
                try {
                    closeAllScanner(scanArray);
                } catch (NullPointerException j) {
                    //this could happen if not all scanners were created and trying to close empty scanner object will through null exception
                    //means all scanners are closed

                    System.exit(0);
                }
            }
        }

    }

    public static void createFiles(PrintWriter[][] pwArray, int numberOfFiles, String[] outputName, String
            outputEXT, Scanner[] scanArray) {
        String fileName = "";
        String[] createdFilesNames = new String[numberOfFiles * outputName.length];
        int counter = 0;
        try {
            for (int j = 0; j < pwArray.length; j++) {
                for (int i = 1; i <= numberOfFiles; i++) {
                    fileName = outputName[j] + i + outputEXT;
                    pwArray[j][i - 1] = new PrintWriter(new FileOutputStream(fileName));
                    createdFilesNames[counter] = fileName;
                    counter++;

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not creat output file " + fileName + "\nPlease check for problems! Program will terminate after closing all files and deleting previous ones.");
            try {
                closeAllPrintWriter(pwArray);

            } catch (NullPointerException j) {
                //this means all PrintWriter are closed
                //now close scanner
                closeAllScanner(scanArray);
                try {
                    for (String s : createdFilesNames)
                        deleteFiles(s);

                } catch (NullPointerException t) {
                    //this mean that all files are deleted but for empty array objects
                    //this will always happen when there is an error creating an objects even if we created 29 files the 30 files will through null ecrption
                    System.exit(0);

                }


            }
        }

    }

    //no changes
    private static String fileToString(Scanner scan) {
        String fileAsString = "";
        while (scan.hasNextLine()) {
            String tempLine = scan.nextLine();
            //check for empty lines in file
            if (!tempLine.isEmpty())
                fileAsString += tempLine;
        }
        //closing scanner
        scan.close();
        return fileAsString;
    }

    //no changes
    private static String ieeeFormat(String author, String title, String journal, String volume, String
            number, String pages, String month, String year) {
        author = author.replace(" and", ",");
        return author + "." + " \"" + title + "\", " + journal + ", vol. " + volume + ", no. " + number + ", p. " + pages + ", " + month + " " + year + ".\n";
    }

    //no changes
    private static String acmFormat(int counter, String author, String title, String journal, String
            volume, String number, String year, String pages, String doi) {
        author = author.split("and")[0] + "et al.";
        return "[" + counter + "] " + author + " " + year + ". " + title + ". " + journal + ". " + volume + ", " + number + " (" + year + "), " + pages + ". DOI:https://doi.org/" + doi + ".\n";

    }


    //no changes
    private static String njFormat(String author, String title, String journal, String volume, String pages, String
            year) {
        author = author.replace("and", "&");
        return author + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").\n";

    }


    private static void writeToFile(PrintWriter pw, String[] content) {
        for (String s : content) {
            pw.write(s + "\n");
        }
        pw.close();
    }


}