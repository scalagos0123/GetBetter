package com.dlsu.getbetter.getbetter.objects;

/**
 * Created by mikedayupay on 27/01/2016.
 */
public class Patient {

    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthdate;
    private String age;
    private String gender;
    private String civilStatus;


    public Patient(long id, String firstName, String middleName, String lastName,
                   String birthdate, String age, String gender, String civilStatus) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.age = age;
        this.gender = gender;
        this.civilStatus = civilStatus;
    }

    public Patient(String firstName, String middleName, String lastName, String birthdate, String gender, String civilStatus) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.civilStatus = civilStatus;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCivilStatus() {
        return civilStatus;
    }
}
