package com.convertation;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) {
            File logFile = Processor.createFile(args[0]);
            Processor processor = null;

            try {
                processor = new Processor(scan, logFile);
            } catch (IOException ex) {
                System.out.println("Log file is corrupted.");
            }

            if (processor != null) {
                try (BufferedReader in = new BufferedReader(
                        new FileReader(Processor.createFile(args[1])))) {
                    processor.setHolders(processor.deserializeToListOfStructureHolders(in));
                } catch (IOException e) {
                    processException("Input file is corrupted. Program is terminated");
                } catch (ArrayIndexOutOfBoundsException e) {
                    processException("Input file is empty. Program is terminated");
                } catch (NumberFormatException | DateTimeParseException ex) {
                    processException("Obligatory fields are empty. Program is terminated");
                }

                if (processor.getHolders() != null) {
                    try (BufferedWriter out = new BufferedWriter(
                            new FileWriter(Processor.createFile(args[2])))) {
                        showMenu();
                        processor.processRequest(processor.getHolders(), scan, out);
                    } catch (IOException ex) {
                        processException("Output file is corrupted. Program is terminated");
                    } catch (InputMismatchException ex) {
                        processException("Request is invalid. Program is terminated");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
}