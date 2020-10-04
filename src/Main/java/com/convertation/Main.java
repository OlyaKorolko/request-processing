package com.convertation;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    if (args.length >= 3) {
      try (Scanner scan = new Scanner(System.in);
           BufferedReader in = new BufferedReader(new FileReader(Processor.createFile(args[1])));
           BufferedWriter out = new BufferedWriter(new FileWriter(Processor.createFile(args[2])))) {
        File logFile = Processor.createFile(args[0]);
        Processor processor = new Processor(logFile);
        Map<String, CompanyStructure> companies = processor.deserializeToListOfCompanyStructures(in);
        processor.setHolders(companies);

        if (processor.getHolders().size() != 0) {
          showMenu();
          int input;
          do {
            System.out.println("Enter the number of request: ");
            input = scan.nextInt();
            processRequest(scan, out, processor, input);
          } while (input != 6);
        }
      } catch (CustomException | IOException e) {
        System.out.println(e.getMessage());
      }
    } else {
      System.out.println("Invalid input.");
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

  public static void processRequest(Scanner scan, BufferedWriter out, Processor processor, int type)
      throws InputMismatchException, IOException, CustomException {
    switch (type) {
      case 1: {
        System.out.println("Enter a short name: ");
        scan.nextLine();
        String input = scan.nextLine().toLowerCase();
          CompanyStructure s = processor.searchByShortName(input);
          Processor.logger.writeRequestDataToLogFile(1, input, s != null ? 1 : 0);
          if (s != null) {
            out.write(s.toString());
          }
          out.write("\n");
        break;
      }
      case 2: {
        System.out.println("Branch of work: ");
        scan.nextLine();
        String branchOfWork = scan.nextLine();
          List<CompanyStructure> listOfSh = processor.searchByBranchOrTypeOfWork(branchOfWork, true);
          Processor.logger.writeRequestDataToLogFile(2, branchOfWork, listOfSh.size());
          for (CompanyStructure s : listOfSh) {
            out.write(s.toString());
          }
          out.write("\n");
        break;
      }
      case 3: {
        System.out.println("Type of work: ");
        scan.nextLine();
        String typeOfWork = scan.nextLine();
          List<CompanyStructure> listOfSh = processor.searchByBranchOrTypeOfWork(typeOfWork, false);
          Processor.logger.writeRequestDataToLogFile(3, typeOfWork, listOfSh.size());
          for (CompanyStructure s : listOfSh) {
            out.write(s.toString());
          }
          out.write("\n");
        break;
      }
      case 4: {
        System.out.println("Enter the time boundaries to be searched between:");
        String[] dateIn = {scan.next(), scan.next()};
        try {
          List<CompanyStructure> listOfSh = processor.searchByDate(
              LocalDate.parse(dateIn[0]), LocalDate.parse(dateIn[1]));
          Processor.logger.writeRequestDataToLogFile(4, Arrays.toString(dateIn), listOfSh.size());
          for (CompanyStructure s : listOfSh) {
            out.write(s.toString());
          }
          out.write("\n");
        } catch (DateTimeParseException ex) {
          throw new CustomException("Incorrect date format.");
        }
        break;
      }
      case 5: {
        System.out.println("Enter the lower and upper bounds of number of employees: ");
        String[] emIn = {scan.next(), scan.next()};
        try {
          List<CompanyStructure> listOfSh = processor.searchByEmployees(
              Integer.parseInt(emIn[0]), Integer.parseInt(emIn[1]));
          Processor.logger.writeRequestDataToLogFile(
              5, Arrays.toString(emIn), listOfSh.size());
          for (CompanyStructure s : listOfSh) {
            out.write(s.toString());
          }
          out.write("\n");
        } catch (NumberFormatException ex) {
          throw new CustomException("Invalid input.");
        }
        break;
      }
      case 6: {
        Processor.logger.writeRequestDataToLogFile(6,
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
