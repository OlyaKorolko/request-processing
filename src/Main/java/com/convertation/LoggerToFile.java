package com.convertation;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerToFile {
    private static Logger LOGGER;
    private static final SimpleFormatter formatter = new SimpleFormatter();

    LoggerToFile(File fileName, String className) throws CustomException {
        LOGGER = Logger.getLogger(className);
        LOGGER.setLevel(Level.FINE);
        try {
            FileHandler filehandler = new FileHandler(fileName.getName(), true);
            filehandler.setLevel(Level.FINE);
            filehandler.setFormatter(formatter);
            LOGGER.addHandler(filehandler);
            LOGGER.fine("REQUEST HANDLING START" + "\n");
        } catch (IOException ex) {
            throw new CustomException("Log file is corrupted.", ex);
        }
    }

    public void writeRequestDataToLogFile(Types requestType, String requestInput, int result) {
        switch (requestType) {
            case SHORT_NAME: {
                LOGGER.fine("Find a company by short name | short name: " +
                        requestInput + ", companies found: " + result + "\n");
                break;
            }
            case BRANCH_OF_WORK: {
                LOGGER.fine("Find companies by branch of work | branch: " +
                        requestInput + ", companies found: " + result + "\n");
                break;
            }
            case TYPE_OF_WORK: {
                LOGGER.fine("Find companies by type of work | type: " +
                        requestInput + ", companies found: " + result + "\n");
                break;
            }
            case DATE: {
                String[] in = requestInput.split(" ");
                LOGGER.fine("Find companies by foundation date | date: " +
                        in[0] + in[1] + ", companies found: " + result + "\n");
                break;
            }
            case EMPLOYEES: {
                String[] in = requestInput.subSequence(1, requestInput.length() - 1).toString().split(", ");
                LOGGER.fine("Find companies by employees number | number: " + "[" + Integer.parseInt(in[0]) +
                        "," + Integer.parseInt(in[1]) + "]" + ", companies found: " + result + "\n");
                break;
            }
            case EXIT: {
                LOGGER.fine(requestInput + "\n");
            }
            default: {
                LOGGER.fine("Invalid request.");
            }
        }
    }

    public void writeExceptionToLogFile(String s) {
        LOGGER.severe(s + "\n");
    }
}
