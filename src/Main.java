import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to BibilographyFactory!\n");
        final Scanner scan = new Scanner(System.in);
        final int NUMBER_OF_FILES = 10;
        final String INPUT_EXT = ".bib";
        final String OUTPUT_EXT = ".json";
        final String INPUT_FILE_NAME = "Latex";
        final String[] OUTPUT_FILE_NAME = {"IEEE", "ACM", "NJ"};


        //open files
        Scanner[] scArray = readFiles(NUMBER_OF_FILES, INPUT_FILE_NAME, INPUT_EXT);
        //create files
        PrintWriter[][] pwArray = createFiles(NUMBER_OF_FILES, OUTPUT_FILE_NAME, OUTPUT_EXT, scArray);
        //process files
        processFilesForValidation(scArray, pwArray, OUTPUT_FILE_NAME, OUTPUT_EXT, INPUT_FILE_NAME, INPUT_EXT);
        //user input
        userProcessing(scan);

        System.out.println("Goodbye! Hope you enjoyed creating the needed files using BibilographyFactory");


    }


    /**
     * Takes user input to return the info from files.
     *
     * @param scan User Input.
     */
    public static void userProcessing(Scanner scan) {
        System.out.print("Please enter the name of the files that you need to review: ");
        String fileName = scan.nextLine();
        BufferedReader br = null;

        try {
            //initialize BufferReader object.
            br = new BufferedReader(new FileReader(fileName));
            //read first line
            String x = br.readLine();
            //check not at the end of the file.
            while (x != null) {
                System.out.println(x);
                x = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("\nCouldn't open input file. File does not exist; possibly it could not be created!");
            System.out.println("However, you will be allowed another chance to enter another file name");
            System.out.print("\nPlease enter the name of the files that you need to review: ");
            fileName = scan.nextLine();
            try {
                br = new BufferedReader(new FileReader(fileName));
                String x = br.readLine();
                while (x != null) {
                    System.out.println(x);
                    x = br.readLine();
                }
                br.close();
            } catch (FileNotFoundException j) {
                System.out.println("\nCouldn't open input file again! Either file does not exit or could not be created.");
                System.out.println("Sorry! I am unable to display your desired files! Program will exit!");
                System.exit(1);

            } catch (IOException j) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //delete file
    private static void deleteFile(String fileName) {

        //create a file object
        File f = new File(fileName);
        //delete the file.
        //this could return a false value if the file is still being used (stream in open)
        f.delete();

    }


    /**
     * Transfer and writes info into the right files.
     *
     * @param scanArray        Array of File to read
     * @param pw               Array of created files
     * @param OUTPUT_FILE_NAME Array of list of output files
     * @param ext              Output Extension
     * @param inputName        Input file name
     * @param inputEXT         Input Extension
     */
    public static void processFilesForValidation(Scanner[] scanArray, PrintWriter[][] pw, String[]
            OUTPUT_FILE_NAME, String ext, String inputName, String inputEXT) {
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
        int invalidFiles = 0;

        String[] acmRecords;
        String[] ieeeRecords;
        String[] njRecords;

        //get file by file
        for (int k = 0; k < scanArray.length; k++) {
            try {
                //file to String
                String fileToString = fileToString(scanArray[k]);
                //split on @
                StringTokenizer tk = new StringTokenizer(fileToString, "@");
                acmRecords = new String[tk.countTokens()];
                ieeeRecords = new String[tk.countTokens()];
                njRecords = new String[tk.countTokens()];

                int counter = 0;
                //loop over the tokens
                while (tk.hasMoreTokens()) {
                    //split on ,
                    String[] singleArticle = tk.nextToken().split(",");
                    //loop the on the array after we split on ,
                    for (int i = 1; i < singleArticle.length; i++) {
                        //split again on =
                        String[] info = singleArticle[i].split("=");
                        //just making sure that we have 2 inputs in the array not 1 to avoid any error
                        if (info.length >= 2) {
                            String field = info[0].trim();
                            if (info[1].trim().substring(1, info[1].length() - 1).isEmpty()) {
                                invalidFiles++;
                                error = info[0].trim();

                                throw new FileInvalidException("Error: Detected Empty Field!\n" + "============================" + "\nProblem detected with input file: " + inputName + (k + 1) + inputEXT + "\nFile is Invalid: Field \"" + error + "\"" + "is Empty. Processing stopped at this point. Other empty fields may be present as well!\n");
                            }
                            switch (field) {
                                case "author" -> author = info[1].substring(1, info[1].length() - 1).trim();
                                case "journal" -> journal = info[1].substring(1, info[1].length() - 1).trim();
                                case "volume" -> volume = info[1].substring(1, info[1].length() - 1).trim();
                                case "year" -> year = info[1].substring(1, info[1].length() - 1).trim();
                                case "number" -> number = info[1].substring(1, info[1].length() - 1).trim();
                                case "title" -> title = info[1].substring(1, info[1].length() - 1).trim();
                                case "pages" -> pages = info[1].substring(1, info[1].length() - 1).trim();
                                case "doi" -> doi = info[1].substring(1, info[1].length() - 1).trim();
                                case "month" -> month = info[1].substring(1, info[1].length() - 1).trim();
                            }

                        }
                    }
                    ieeeRecords[counter] = ieeeFormat(author, title, journal, volume, number, pages, month, year);
                    acmRecords[counter] = acmFormat(counter + 1, author, title, journal, volume, number, year, pages, doi);
                    njRecords[counter] = njFormat(author, title, journal, volume, pages, year);

                    counter++;
                }

                writeToFile(pw[0][k], ieeeRecords);
                writeToFile(pw[1][k], acmRecords);
                writeToFile(pw[2][k], njRecords);

                //close files after adding the records
                for (PrintWriter[] p : pw) {
                    p[k].close();
                }


            } catch (FileInvalidException e) {
                System.out.println(e.getMessage());

                for (PrintWriter[] p : pw) {
                    p[k].close();
                }
                for (String s : OUTPUT_FILE_NAME) {
                    deleteFile(s + (k + 1) + ext);
                }


            }

        }

        System.out.println("A total of " + invalidFiles + " files were invalid, and could not be processed. All other " + (scanArray.length - invalidFiles) + " \"Valid\" files have been created.\n");

    }

    //closing all scanner streams.
    //takes in a scanner array
    //throws NullPointerException when looping over the entire array if not all scanner were added.
    private static void closeAllScanner(Scanner[] scanArray) throws NullPointerException {
        for (Scanner sc : scanArray) {
            sc.close();
        }
    }

    //closing all PrintWriter streams
    //takes in a 2d array
    private static void closeAllPrintWriter(PrintWriter[][] pwArray) throws NullPointerException {
        for (PrintWriter[] printWriters : pwArray) {
            for (PrintWriter printWriter : printWriters) {
                printWriter.close();
            }
        }
    }

    /**
     * Read the provided files.
     *
     * @param NUMBER_OF_FILES Number of files to read.
     * @param inputFileName   Input file name.
     * @param inputEXT        Input Extension.
     * @return Array of Scanner Object.
     */
    public static Scanner[] readFiles(int NUMBER_OF_FILES, String inputFileName, String inputEXT) {
        //loop over the number of files we are trying to open (10 in our case)
        Scanner[] scanArray = new Scanner[NUMBER_OF_FILES];
        for (int i = 1; i <= NUMBER_OF_FILES; i++) {
            String fileName = inputFileName + i + inputEXT;
            try {
                scanArray[i - 1] = new Scanner(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                System.out.println("Could not open input file " + fileName + " for reading.\nPlease check if file exists! Program will terminate after closing any opened files.");
                try {
                    //close all scanners if there is an exception.
                    closeAllScanner(scanArray);
                } catch (NullPointerException j) {
                    //this could happen if not all scanners were created and trying to close empty scanner object will through null exception
                    //means all scanners are closed
                    System.exit(1);
                }
            }
        }
        return scanArray;

    }

    /**
     * Create files.
     *
     * @param numberOfFiles Number of read files
     * @param outputName    Array of output files
     * @param outputEXT     Output Extension.
     * @param scanArray     Array of open files to read from.
     * @return 2D array of PrintWriter objects.
     */
    public static PrintWriter[][] createFiles(int numberOfFiles, String[] outputName, String
            outputEXT, Scanner[] scanArray) {
        //create the array
        PrintWriter[][] pwArray = new PrintWriter[outputName.length][];
        for (int i = 0; i < outputName.length; i++) {
            pwArray[i] = new PrintWriter[numberOfFiles];
        }

        String fileName = "";
        //to save created files name in order to delete
        String[] createdFilesNames = new String[numberOfFiles * outputName.length];
        int counter = 0;
        try {
            //loop to create the files.
            for (int j = 0; j < pwArray.length; j++) {
                for (int i = 1; i <= numberOfFiles; i++) {
                    fileName = outputName[j] + i + outputEXT;
                    //add PrintWriter object to the array
                    pwArray[j][i - 1] = new PrintWriter(new FileOutputStream(fileName));
                    //add the name of the list.
                    createdFilesNames[counter] = fileName;
                    counter++;

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not creat output file " + fileName + "\nPlease check for problems! Program will terminate after closing all files and deleting previous ones.");
            try {
                //close all PrintWriters
                closeAllPrintWriter(pwArray);

            } catch (NullPointerException j) {
                //this means all PrintWriter are closed
                //now close scanner
                closeAllScanner(scanArray);
                try {
                    for (String s : createdFilesNames)
                        deleteFile(s);

                } catch (NullPointerException t) {
                    //this mean that all files are deleted but for empty array objects
                    //this will always happen when there is an error creating an objects even if we created 29 files the 30 files will through null exception
                    //exit
                    System.exit(1);

                }


            }
        }
        return pwArray;
    }

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

    private static String ieeeFormat(String author, String title, String journal, String volume, String
            number, String pages, String month, String year) {
        author = author.replace(" and", ",");
        return author + "." + " \"" + title + "\", " + journal + ", vol. " + volume + ", no. " + number + ", p. " + pages + ", " + month + " " + year + ".\n";
    }

    private static String acmFormat(int counter, String author, String title, String journal, String
            volume, String number, String year, String pages, String doi) {
        author = author.split("and")[0] + "et al.";
        return "[" + counter + "] " + author + " " + year + ". " + title + ". " + journal + ". " + volume + ", " + number + " (" + year + "), " + pages + ". DOI:https://doi.org/" + doi + ".\n";

    }


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