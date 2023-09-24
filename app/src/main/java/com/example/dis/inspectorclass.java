package com.example.dis;

public class inspectorclass {

    private  String NAME;
    private  String CITY;
    private  String DESIGNATION;
    private  String PHONENUMBER;

    public inspectorclass() {
    }

    public inspectorclass(String NAME, String CITY, String DESIGNATION, String PHONENUMBER) {
        this.NAME = NAME;
        this.CITY = CITY;
        this.DESIGNATION = DESIGNATION;
        this.PHONENUMBER = PHONENUMBER;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public String getPHONENUMBER() {
        return PHONENUMBER;
    }

    public void setPHONENUMBER(String PHONENUMBER) {
        this.PHONENUMBER = PHONENUMBER;
    }


}
