package com.manthan.loginstore;

/**
 * Created by Manthan on 1/28/2015.
 */
public class ListElements {

    private String name ="";
    boolean isFolder;

    public boolean isFolder() {
        return isFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }
}
