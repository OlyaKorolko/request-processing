package com.convertation;

public enum RequestName {

    SHORT_NAME (1),
    BRANCH_OF_WORK (2),
    TYPE_OF_WORK (3),
    DATE (4),
    EMPLOYEES (5),
    EXIT (6),
    DEFAULT(7);

    public int number;

    RequestName(int number) {
        this.number = number;
    }

    public static RequestName castFromIntToEnum(int value) {
        for (RequestName name : values()) {
            if (name.number == value) {
                return name;
            }
        }
        return RequestName.DEFAULT;
    }
}
