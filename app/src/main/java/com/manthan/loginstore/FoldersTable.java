package com.manthan.loginstore;

/**
 * Created by Manthan on 1/31/2015.
 */
public class FoldersTable {

    private int pid;
    private String folder;
    private String title;
    private String id;
    private String pwd;

    public FoldersTable(){}

    public FoldersTable(String folder, String title, String id, String pwd)
    {
        super();
        this.folder= folder;
        this.title=title;
        this.id=id;
        this.pwd=pwd;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "Credentials [pid=" + pid +", folder="+ folder +", title=" + title + ", id=" + id + ", pwd=" + pwd
                + "]";
    }
}
