package com.convertation;

import java.io.IOException;

public class CustomException extends Exception {
    public CustomException(String errorMessage, IOException err) {
        super(errorMessage, err);
    }
    public CustomException(String errorMessage, ArrayIndexOutOfBoundsException err) {
        super(errorMessage, err);
    }
}
