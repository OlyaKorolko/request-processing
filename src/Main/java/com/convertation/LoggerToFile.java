package com.convertation;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerToFile {
    private final static Logger LOGGER = Logger.getLogger(Processor.class.getName());
    private static final SimpleFormatter formatter = new SimpleFormatter();

    LoggerToFile(File fileName) throws IOException {
        LOGGER.setLevel(Level.FINE);
        FileHandler filehandler = new FileHandler(fileName.getName(), true);
        filehandler.setLevel(Level.FINE);
        filehandler.setFormatter(formatter);
        LOGGER.addHandler(filehandler);
        LOGGER.fine("REQUEST HANDLING START" + "\n");
    }

    public void writeRequestDataToLogFile(int requestNum, String input, int result) {
        switch (requestNum) {
            case 1: {
                LOGGER.fine("Find a company by short name | short name: " +
                        input + ", companies found: " + result + "\n");
                break;
            }
            case 2: {
                LOGGER.fine("Find companies by branch of work | branch: " +
                        input + ", companies found: " + result + "\n");
                break;
            }
            case 3: {
                LOGGER.fine("Find companies by type of work | type: " +
                        input + ", companies found: " + result + "\n");
                break;
            }
            case 4: {
                String[] in = input.split(" ");
                LOGGER.fine("Find companies by foundation date | date: " +
                        in[0] + in[1] + ", companies found: " + result + "\n");
                break;
            }
            case 5: {
                String[] in = input.subSequence(1, input.length() - 1).toString().split(", ");
                LOGGER.fine("Find companies by employees number | number: " + "[" + Long.parseLong(in[0]) +
                        "," + Long.parseLong(in[1]) + "]" + ", companies found: " + result + "\n");
                break;
            }
            case 6: {
                LOGGER.fine(input + "\n");
            }
        }
    }

    public void writeExceptionToLogFile(String s) {
        LOGGER.warning(s + "\n");
    }
}
