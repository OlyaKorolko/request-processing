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
        try (Scanner scan = new Scanner(System.in)) {
            if (args.length >= 3) {
                File logFile = Processor.createFile(args[0]);
                Processor processor = new Processor(logFile);

                try (BufferedReader in = new BufferedReader(
                        new FileReader(Processor.createFile(args[1])))) {
                    processor.setHolders(processor.deserializeToListOfCompanyStructures(in));
                } catch (IOException e) {
                    processException("Input file is corrupted. Program is terminated");
                } catch (NumberFormatException | DateTimeParseException ex) {
                    processException("Obligatory fields are empty. Program is terminated");
                }

                if (processor.getHolders().size() != 0) {
                    try (BufferedWriter out = new BufferedWriter(
                            new FileWriter(Processor.createFile(args[2])))) {
                        showMenu();
                        int input;
                        do {
                            System.out.println("Enter the number of request: ");
                            input = scan.nextInt();
                            processRequest(scan, out, processor, RequestName.values()[input - 1]);
                        } while (RequestName.values()[input - 1] != RequestName.EXIT);
                    } catch (IOException ex) {
                        processException("Output file is corrupted. Program is terminated");
                    } catch (InputMismatchException | NumberFormatException ex) {
                        processException("Request is invalid. Program is terminated");
                    } catch (DateTimeParseException ex) {
                        processException("Incorrect date format");
                    }
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
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

    public static void processRequest(Scanner scan, BufferedWriter out, Processor processor, RequestName requestName)
            throws IOException {
        switch (requestName) {
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
                List<CompanyStructure> listOfSh = processor.searchByDate(
                        LocalDate.parse(dateIn[0]), LocalDate.parse(dateIn[1]));
                Processor.logger.writeRequestDataToLogFile(
                        RequestName.DATE, Arrays.toString(dateIn), listOfSh.size());
                for (CompanyStructure s : listOfSh) {
                    out.write(s.toString());
                }
                out.write("\n");
                break;
            }
            case EMPLOYEES: {
                System.out.println("Enter the lower and upper bounds of number of employees: ");
                String[] emIn = {scan.next(), scan.next()};
                List<CompanyStructure> listOfSh = processor.searchByEmployees(
                        Integer.parseInt(emIn[0]), Integer.parseInt(emIn[1]));
                Processor.logger.writeRequestDataToLogFile(
                        RequestName.EMPLOYEES, Arrays.toString(emIn), listOfSh.size());
                for (CompanyStructure s : listOfSh) {
                    out.write(s.toString());
                }
                out.write("\n");

                break;
            }
            case EXIT: {
                Processor.logger.writeRequestDataToLogFile(RequestName.EXIT,
                        "REQUEST HANDLING FINISHED SUCCESSFULLY.", 0);
                break;
            }
            default: {
                System.out.println("Invalid request.");
                break;
            }
        }
    }
}
