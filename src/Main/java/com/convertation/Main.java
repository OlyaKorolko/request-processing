package com.convertation;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length >= 3) {
            try (Scanner scan = new Scanner(System.in);
                 BufferedReader in = new BufferedReader(new FileReader(Processor.createFile(args[1])));
                 BufferedWriter out = new BufferedWriter(new FileWriter(Processor.createFile(args[2])))) {
                File logFile = Processor.createFile(args[0]);
                Processor processor = new Processor(logFile);
                processor.setHolders(processor.deserializeToListOfCompanyStructures(in));

                if (processor.getHolders().size() != 0) {
                    showMenu();
                    int input;
                    do {
                        System.out.println("Enter the number of request: ");
                        input = scan.nextInt();
                        processRequest(scan, out, processor, input);
                    } while (input != 6);
                }
                else {
                    processException("Input file is empty.");
                }
            } catch (CustomException | IOException | InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        } else {
            processException("INVALID INPUT");
        }
    }

    public static void showMenu() {
        System.out.println("Here are all the available requests: ");
        System.out.println("1. Find a company by short name");
        System.out.println("2. Find companies by branch of work");
        System.out.println("3. Find companies by type of work");
        System.out.println("4. Find companies by foundation date (between DATE1 && DATE2) [YYYY-MM-DD]");
        System.out.println("5. Find companies by employees number (between NUM1 && NUM2)");
        System.out.println();
    }

    public static void processException(String s) {
        System.out.println(s);
        Processor.logger.writeExceptionToLogFile(s);
    }

    public static void processRequest(Scanner scan, BufferedWriter out, Processor processor, int requestNum)
            throws InputMismatchException, IOException, CustomException {
        RequestName rName;
        try {
            rName = RequestName.values()[requestNum - 1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new CustomException("INVALID REQUEST");
        }
        switch (rName) {
            case SHORT_NAME: {
                System.out.println("Enter a short name: ");
                scan.nextLine();
                String input = scan.nextLine().toLowerCase();
                CompanyStructure s = processor.searchByShortName(input);
                Processor.logger.writeRequestDataToLogFile(RequestName.SHORT_NAME, input, s != null ? 1 : 0);
                if (s != null) {
                    out.write(s.toString());
                }
                out.write("\n");
            }
            break;
            case BRANCH_OF_WORK: {
                System.out.println("Branch of work: ");
                scan.nextLine();
                String branchOfWork = scan.nextLine();
                List<CompanyStructure> listOfSh = processor.searchByBranchOrTypeOfWork(branchOfWork, true);
                Processor.logger.writeRequestDataToLogFile(RequestName.BRANCH_OF_WORK, branchOfWork, listOfSh.size());
                for (CompanyStructure s : listOfSh) {
                    out.write(s.toString());
                }
                out.write("\n");
                break;
            }
            case TYPE_OF_WORK: {
                System.out.println("Type of work: ");
                scan.nextLine();
                String typeOfWork = scan.nextLine();
                List<CompanyStructure> listOfSh = processor.searchByBranchOrTypeOfWork(typeOfWork, false);
                Processor.logger.writeRequestDataToLogFile(RequestName.TYPE_OF_WORK, typeOfWork, listOfSh.size());
                for (CompanyStructure s : listOfSh) {
                    out.write(s.toString());
                }
                out.write("\n");
                break;
            }
            case DATE: {
                System.out.println("Enter the time boundaries to be searched between:");
                String[] dateIn = {scan.next(), scan.next()};
                try {
                    List<CompanyStructure> listOfSh = processor.searchByDate(
                            LocalDate.parse(dateIn[0]), LocalDate.parse(dateIn[1]));
                    Processor.logger.writeRequestDataToLogFile(
                            RequestName.DATE, Arrays.toString(dateIn), listOfSh.size());
                    for (CompanyStructure s : listOfSh) {
                        out.write(s.toString());
                    }
                    out.write("\n");
                } catch (DateTimeParseException ex) {
                    throw new CustomException("INVALID DATE FORMAT");
                }
                break;
            }
            case EMPLOYEES: {
                System.out.println("Enter the lower and upper bounds of number of employees: ");
                String[] emIn = {scan.next(), scan.next()};
                try {
                    List<CompanyStructure> listOfSh = processor.searchByEmployees(
                            Integer.parseInt(emIn[0]), Integer.parseInt(emIn[1]));
                    Processor.logger.writeRequestDataToLogFile(
                            RequestName.EMPLOYEES, Arrays.toString(emIn), listOfSh.size());
                    for (CompanyStructure s : listOfSh) {
                        out.write(s.toString());
                    }
                    out.write("\n");
                } catch (NumberFormatException ex) {
                    throw new CustomException("INVALID NUMBER INPUT");
                }
                break;
            }
            case EXIT: {
                Processor.logger.writeRequestDataToLogFile(RequestName.EXIT,
                        "REQUEST HANDLING FINISHED SUCCESSFULLY.", 0);
                break;
            }
            default: {
                processException("INVALID REQUEST");
                break;
            }
        }
    }
}
