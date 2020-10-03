package com.convertation;

import java.io.BufferedReader;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Processor {
    public static LoggerToFile logger;
    private Scanner scan;
    private Map<String, CompanyStructure> holders;

    Processor(Scanner scan, File logFile) throws CustomException {
        this.scan = scan;
        holders = new HashMap<>();
        logger = new LoggerToFile(logFile, Processor.class.getName());
    }

    public void setHolders(Map<String, CompanyStructure> holders) {
        this.holders = holders;
    }

    public Map<String, CompanyStructure> getHolders() {
        return holders;
    }

    public static File createFile(String fileName) {
        return new File(System.getProperty("user.dir") + "\\src\\" + fileName);
    }

    public Map<String, CompanyStructure> deserializeToListOfCompanyStructures(BufferedReader in)
            throws ArrayIndexOutOfBoundsException {
        return in.lines().map(line -> {
            String[] x = setPattern().split(line);
            return new CompanyStructure(x[0], x[1], LocalDate.parse(x[2]), x[3], LocalDate.parse(x[4]),
                    Integer.parseInt(x[5]), x[6], x[7], x[8], x[9], x[10], x[11]);
        })
                .filter(s -> !s.getShortName().isEmpty() || !s.getBranchOfWork().isEmpty() || !s.getTypeOfWork().isEmpty())
                .collect(Collectors.toMap(CompanyStructure::getShortNameToLowerCase, Function.identity()));
    }

    private Pattern setPattern() {
        return Pattern.compile(",");
    }

    CompanyStructure searchByShortName(String shortName) {
        return holders.get(shortName);
    }

    List<CompanyStructure> searchByBranchOrTypeOfWork(String branchType, boolean choice) {
        if (choice) {
            return holders
                    .values()
                    .stream()
                    .filter(sh -> sh.getBranchOfWork().equals(branchType))
                    .collect(Collectors.toList());
        } else {
            return holders
                    .values()
                    .stream()
                    .filter(sh -> sh.getTypeOfWork().equals(branchType))
                    .collect(Collectors.toList());
        }
    }

    List<CompanyStructure> searchByDate(LocalDate d1, LocalDate d2)
            throws DateTimeParseException {
        return holders
                .values()
                .stream()
                .filter(sh -> sh.getDateOfActualization().isAfter(d1) && sh.getDateOfActualization().isBefore(d2))
                .collect(Collectors.toList());
    }

    List<CompanyStructure> searchByEmployees(Integer lower, Integer upper) {
        return holders
                .values()
                .stream()
                .filter(sh -> sh.getNumberOfEmployees() >= lower && sh.getNumberOfEmployees() <= upper)
                .collect(Collectors.toList());
    }
}
