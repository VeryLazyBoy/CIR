package com.cir.models;

public class Alias {

    public Alias(String longName, String shortName) {
        this.longName = longName;
        this.shortName = shortName;
    }
    String longName;
    /**
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }
    /**
     * @param longName the longName to set
     */
    public void setLongName(String fullName) {
        this.longName = fullName;
    }
    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }
    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    String shortName;

}
