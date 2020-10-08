package com.convertation;

import java.time.LocalDate;
import java.util.Objects;

public class CompanyStructure {
    private final String name;
    private final String shortName;
    private final LocalDate dateOfActualization;
    private final String address;
    private final LocalDate dateOfFoundation;
    private final int numberOfEmployees;
    private final String auditor;
    private final String phoneNumber;
    private final String email;
    private final String branchOfWork;
    private final String typeOfWork;
    private final String websiteAddress;

    public CompanyStructure(String name, String shortName, LocalDate dateOfActualization, String address,
                            LocalDate dateOfFoundation, int numberOfEmployees, String auditor, String phoneNumber,
                            String email, String branchOfWork, String typeOfWork, String websiteAddress) {
        this.name = name;
        this.shortName = shortName;
        this.dateOfActualization = dateOfActualization;
        this.address = address;
        this.dateOfFoundation = dateOfFoundation;
        this.numberOfEmployees = numberOfEmployees;
        this.auditor = auditor;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.branchOfWork = branchOfWork;
        this.typeOfWork = typeOfWork;
        this.websiteAddress = websiteAddress;
    }

    public String getShortName() {
        return shortName;
    }

    public String getShortNameToLowerCase() {
        return shortName.toLowerCase();
    }

    public LocalDate getDateOfActualization() {
        return dateOfActualization;
    }

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public String getBranchOfWork() {
        return branchOfWork;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyStructure that = (CompanyStructure) o;
        return numberOfEmployees == that.numberOfEmployees &&
                Objects.equals(name, that.name) &&
                Objects.equals(shortName, that.shortName) &&
                Objects.equals(dateOfActualization, that.dateOfActualization) &&
                Objects.equals(address, that.address) &&
                Objects.equals(dateOfFoundation, that.dateOfFoundation) &&
                Objects.equals(auditor, that.auditor) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(branchOfWork, that.branchOfWork) &&
                Objects.equals(typeOfWork, that.typeOfWork) &&
                Objects.equals(websiteAddress, that.websiteAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, shortName, dateOfActualization, address, dateOfFoundation, numberOfEmployees,
                auditor, phoneNumber, email, branchOfWork, typeOfWork, websiteAddress);
    }

    @Override
    public String toString() {
        return name + "," + shortName + "," + dateOfActualization + "," + address + "," + dateOfFoundation + "," +
                numberOfEmployees + "," + auditor + "," + phoneNumber + "," + email + "," + branchOfWork + "," +
                typeOfWork + "," + websiteAddress + "\n";
    }
}
