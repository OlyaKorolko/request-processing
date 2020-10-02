package com.convertation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Processor {
    private static final boolean BRANCH = true;
    private static final boolean TYPE = false;
    private static final int SHORT_NAME = 1;
    private static final int BRANCH_OF_WORK = 2;
    private static final int TYPE_OF_WORK = 3;
    private static final int DATE = 4;
    private static final int EMPLOYEES = 5;
    private static final int EXIT = 6;

    public static LoggerToFile logger;
    private Scanner scan;
    private List<StructureHolder> holders;

    Processor(Scanner scan, File logFile) throws IOException {
        this.scan = scan;
        holders = null;
        logger = new LoggerToFile(logFile);
    }

    public List<StructureHolder> getHolders() {
        return holders;
    }

    public void setHolders(List<StructureHolder> holders) {
        this.holders = holders;
    }

    public static File createFile(String fileName) {
        return new File(System.getProperty("user.dir") + "\\src\\" + fileName);
    }

    public void processRequest(List<StructureHolder> holders, Scanner scan, BufferedWriter out)
            throws InputMismatchException {
        Hashtable<String, StructureHolder> hashTableOfShortName = new Hashtable<>();
        int message;

        do {
            System.out.println();
            System.out.println("Request number: ");
            message = scan.nextInt();

            switch (message) {
                case SHORT_NAME: {
                    putIntoHashTable(holders, hashTableOfShortName);
                    String input = inputToShortName();
                    try {
                        StructureHolder s = searchByShortName(input, hashTableOfShortName);
                        logger.writeRequestDataToLogFile(SHORT_NAME, input, s != null ? 1 : 0);
                        if (s != null) {
                            out.write(s.toString());
                        }
                        out.write("\n");
                    } catch (IOException ex) {
                        System.out.println("No field found according to your request");
                    }
                    break;
                }
                case BRANCH_OF_WORK: {
                    System.out.println("Branch of work: ");
                    scan.nextLine();
                    String branchOfWork = scan.nextLine();
                    try {
                        List<StructureHolder> listOfSh = searchByBranchOrTypeOfWork(holders, branchOfWork, BRANCH);
                        logger.writeRequestDataToLogFile(BRANCH_OF_WORK, branchOfWork, listOfSh.size());
                        for (StructureHolder s : listOfSh) {
                            out.write(s.toString());
                        }
                        out.write("\n");
                    } catch (IOException ex) {
                        System.out.println("No field found according to your request");
                    }
                    break;
                }
                case TYPE_OF_WORK: {
                    System.out.println("Type of work: ");
                    scan.nextLine();
                    String typeOfWork = scan.nextLine();
                    try {
                        List<StructureHolder> listOfSh = searchByBranchOrTypeOfWork(holders, typeOfWork, TYPE);
                        logger.writeRequestDataToLogFile(TYPE_OF_WORK, typeOfWork, listOfSh.size());
                        for (StructureHolder s : listOfSh) {
                            out.write(s.toString());
                        }
                        out.write("\n");
                    } catch (IOException ex) {
                        System.out.println("No field found according to your request");
                    }
                    break;
                }
                case DATE: {
                    System.out.println("Enter the time boundaries to be searched between:");
                    String[] dateIn = {scan.next(), scan.next()};
                    try {
                        List<StructureHolder> listOfSh =
                                searchByDate(holders, LocalDate.parse(dateIn[0]), LocalDate.parse(dateIn[1]));
                        logger.writeRequestDataToLogFile(DATE, Arrays.toString(dateIn), listOfSh.size());
                        for (StructureHolder s : listOfSh) {
                            out.write(s.toString());
                        }
                        out.write("\n");
                    } catch (IOException ex) {
                        System.out.println("No field found according to your request");
                    } catch (DateTimeParseException ex) {
                        System.out.println("Incorrect date format");
                    }
                    break;
                }
                case EMPLOYEES: {
                    System.out.println("Enter the lower and upper bounds of number of employees: ");
                    String[] emIn = {scan.next(), scan.next()};
                    try {
                        List<StructureHolder> listOfSh =
                                searchByEmployees(holders, Long.parseLong(emIn[0]), Long.parseLong(emIn[1]));
                        logger.writeRequestDataToLogFile(EMPLOYEES, Arrays.toString(emIn), listOfSh.size());
                        for (StructureHolder s : listOfSh) {
                            out.write(s.toString());
                        }
                        out.write("\n");
                    } catch (IOException ex) {
                        System.out.println("No field found according to your request");
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input.");
                    }
                    break;
                }
                case EXIT: {
                    logger.writeRequestDataToLogFile(EXIT, "REQUEST HANDLING FINISHED SUCCESSFULLY.", 0);
                    break;
                }
                default: {
                    System.out.println("Invalid request.");
                    break;
                }
            }
        } while (message != EXIT);
    }

    public List<StructureHolder> deserializeToListOfStructureHolders(BufferedReader in)
            throws ArrayIndexOutOfBoundsException {
        List<StructureHolder> sh = in.lines().map(line -> {
            String[] x = setPattern().split(line);
            return new StructureHolder(x[0], x[1], LocalDate.parse(x[2]), x[3], LocalDate.parse(x[4]),
                    Long.parseLong(x[5]), x[6], x[7], x[8], x[9], x[10], x[11]);
        }).collect(Collectors.toList());
        sh.removeIf(s -> s.getShortName().equals("") || s.getBranchOfWork().equals("") || s.getTypeOfWork().equals(""));
        return sh;
    }

    private Pattern setPattern() {
        return Pattern.compile(",");
    }

    private String inputToShortName() {
        System.out.println("Enter a short name: ");
        scan.nextLine();
        return scan.nextLine().toLowerCase();
    }

    private void putIntoHashTable(List<StructureHolder> holders, Hashtable<String, StructureHolder> shortName) {
        for (StructureHolder s : holders) {
            shortName.put(s.getShortName().toLowerCase(), s);
        }
    }

    private StructureHolder searchByShortName(String shortName, Hashtable<String, StructureHolder> table) {
        return table.get(shortName);
    }

    private List<StructureHolder> searchByBranchOrTypeOfWork(List<StructureHolder> holders,
                                                             String branchType, boolean choice) {
        List<StructureHolder> list = new ArrayList<>();
        if (choice) {
            for (StructureHolder holder : holders) {
                if (holder.getBranchOfWork().equalsIgnoreCase(branchType)) {
                    list.add(holder);
                }
            }
        } else {
            for (StructureHolder holder : holders) {
                if (holder.getTypeOfWork().equalsIgnoreCase(branchType)) {
                    list.add(holder);
                }
            }
        }
        return list;
    }

    private List<StructureHolder> searchByDate(List<StructureHolder> holders, LocalDate d1, LocalDate d2)
            throws DateTimeParseException {
        List<StructureHolder> list = new ArrayList<>();
        for (StructureHolder sh : holders) {
            if (sh.getDateOfActualization().isAfter(d1) && sh.getDateOfActualization().isBefore(d2))
                list.add(sh);
        }
        return list;
    }

    private List<StructureHolder> searchByEmployees(List<StructureHolder> holders, long lower, long upper) {
        List<StructureHolder> list = new ArrayList<>();
        for (StructureHolder sh : holders) {
            if (sh.getNumberOfEmployees() >= lower && sh.getNumberOfEmployees() <= upper)
                list.add(sh);
        }
        return list;
    }
}
