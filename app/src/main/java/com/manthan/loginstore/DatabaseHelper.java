package com.manthan.loginstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manthan on 1/31/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int Database_Version =1;
    private static final String Database_Name = "CredentialsDB";

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE Credentials ( " +
                "pid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "id TEXT, "+"pwd TEXT)";

        String CREATE_FOLDERS_TABLE = "CREATE TABLE Folders ( " +
                "pid INTEGER PRIMARY KEY AUTOINCREMENT, " + "folder TEXT, "+
                "title TEXT, "+
                "id TEXT, "+"pwd TEXT)";

        // create credentials table
        db.execSQL(CREATE_CREDENTIALS_TABLE);
        //create folders table
        db.execSQL(CREATE_FOLDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS Credentials");
        db.execSQL("DROP TABLE IF EXISTS Folders");
        // create fresh credentials table
        this.onCreate(db);
    }

    //Credentials Table Name
    private static final String TABLE_CREDENTIALS = "Credentials";

    // Books Table Columns names
    private static final String KEY_PID = "pid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "pwd";

    private static final String[] COLUMNS = {KEY_PID,KEY_TITLE,KEY_ID,KEY_PASSWORD};

    public void addCredential(CredentialsTable credential){
        Log.d("addCredential", credential.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, credential.getTitle()); // get title
        values.put(KEY_ID, credential.getId()); // get author
        values.put(KEY_PASSWORD,credential.getPwd()); //get password

        // 3. insert
        db.insert(TABLE_CREDENTIALS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public CredentialsTable getCredentials(String title){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_CREDENTIALS, // a. table
                        COLUMNS, // b. column names
                        " title = ?", // c. selections
                        new String[] { String.valueOf(title) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        CredentialsTable credential = new CredentialsTable();
        credential.setPid(Integer.parseInt(cursor.getString(0)));
        credential.setTitle(cursor.getString(1));
        credential.setId(cursor.getString(2));
        credential.setPwd(cursor.getString(3));

        //log
        Log.d("getBook(" + title + ")", credential.toString());

        // 5. return book
        return credential;
    }

    public List<CredentialsTable> getAllCredentials() {
        List<CredentialsTable> credentialsList = new ArrayList<CredentialsTable>();

        // 1. build the query
        String query = "SELECT  title FROM " + TABLE_CREDENTIALS;
Log.d("here","stage1 reached");
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        CredentialsTable credentials = null;
        if (cursor.moveToFirst()) {
            do {
                credentials = new CredentialsTable();
                Log.d("here",cursor.getString(0));
               // credentials.setPid(Integer.parseInt(cursor.getString(0)));
                credentials.setTitle(cursor.getString(0));
               // credentials.setId(cursor.getString(2));
               // credentials.setPwd(cursor.getString(3));

                // Add book to books
                credentialsList.add(credentials);
            } while (cursor.moveToNext());
        }

        //Log.d("getAllBooks()", credentialsList.toString());

        // return books
        return credentialsList;
    }

    public int updateCredential(CredentialsTable crdential, CredentialsTable new_credential) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", new_credential.getTitle()); // get title
        values.put("id", new_credential.getId()); // get author
        values.put("pwd", new_credential.getPwd()); //get password

        // 3. updating row
        int i = db.update(TABLE_CREDENTIALS, //table
                values, // column/value
                KEY_TITLE+" = ?", // selections
                new String[] { String.valueOf(crdential.getTitle()) }); //selection args

        // 4. close
        db.close();

        return i;

    }


    public void deleteCredential(CredentialsTable credential) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_CREDENTIALS, //table name
                KEY_TITLE+" = ?",  // selections
                new String[] { String.valueOf(credential.getTitle()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteCredential", credential.toString());

    }

    //Folders Table Name
    private static final String TABLE_FOLDERS = "Folders";

    // Folders Table Columns names
    private static final String KEY_FPID = "pid";
    private static final String KEY_FFolder = "folder";
    private static final String KEY_FTITLE = "title";
    private static final String KEY_FID = "id";
    private static final String KEY_FPASSWORD = "pwd";
    private static final String No_TITLE="no_title";

    private static final String[] FCOLUMNS = {KEY_FPID,KEY_FFolder,KEY_FTITLE,KEY_FID,KEY_FPASSWORD};

    public void addFolderCredential(FoldersTable foldersT){
       // Log.d("addCredential", foldersT.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_FFolder,foldersT.getFolder()); //get Folder
        if(foldersT.getTitle()!=null) {
//            List<FoldersTable> folderTList = getAllFolderCredentials(foldersT.getFolder());
//            if(folderTList.size()==1&&folderTList.get(0).getTitle().isEmpty())
//            {
//                updateFolderCredential(foldersT,foldersT);
//                db.close();
//                return;
//            }

            values.put(KEY_FTITLE, foldersT.getTitle()); // get title
            values.put(KEY_FID, foldersT.getId()); // get author
            values.put(KEY_FPASSWORD, foldersT.getPwd()); //get password
        }
        else{
            values.put(KEY_FTITLE,No_TITLE);
        }


        // 3. insert
        db.insert(TABLE_FOLDERS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
Log.d("addFOlder()","added");
        // 4. close
        db.close();
    }

    public FoldersTable getFolderCredentials(String folder, String title){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_FOLDERS, // a. table
                        FCOLUMNS, // b. column names
                        "folder =? AND title = ?", // c. selections
                        new String[] { String.valueOf(folder),String.valueOf(title) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        FoldersTable folderT = new FoldersTable();
        folderT.setPid(Integer.parseInt(cursor.getString(0)));
        folderT.setFolder(cursor.getString(1));
        folderT.setTitle(cursor.getString(2));
        folderT.setId(cursor.getString(3));
        folderT.setPwd(cursor.getString(4));

        //log
        Log.d("getBook(" + title + ")", folderT.toString());

        // 5. return book
        return folderT;
    }

    public List<FoldersTable> getAllFolderCredentials(String folder) {
        List<FoldersTable> folderTList = new ArrayList<FoldersTable>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_FOLDERS +" WHERE folder = \'"+folder+"\'";
        Log.d(query,"stage1 reached");
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        FoldersTable folderT = null;
        if (cursor.moveToFirst()) {
            do {
                Log.d("inside get all folders ",cursor.getString(1) );
                folderT = new FoldersTable();
                if(!(cursor.getString(2).equals(null)||cursor.getString(2).isEmpty())) {
                    Log.d("here", cursor.getString(2));

                        folderT.setTitle(cursor.getString(2));

                        folderTList.add(folderT);

                }
            } while (cursor.moveToNext());
        }

        //Log.d("getAllBooks()", credentialsList.toString());

        // return books
        return folderTList;
    }

    public List<FoldersTable> getAllFolder() {
        List<FoldersTable> folderTList = new ArrayList<FoldersTable>();

        // 1. build the query
        String query = "SELECT DISTINCT folder FROM " + TABLE_FOLDERS;
        Log.d("getting folders","all folders");

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        FoldersTable folderT = null;
        if (cursor.moveToFirst()) {
            do {
                folderT = new FoldersTable();
                // credentials.setPid(Integer.parseInt(cursor.getString(0)));
                folderT.setFolder(cursor.getString(0));
                // credentials.setId(cursor.getString(2));
                // credentials.setPwd(cursor.getString(3));
Log.d("getAllFolder","reached here inside loop");
                // Add book to books
                folderTList.add(folderT);
            } while (cursor.moveToNext());
        }

        Log.d("getAllFOlder()", folderTList.toString());

        // return books
        return folderTList;
    }


    public int updateFolderCredential(FoldersTable folderT,FoldersTable newFolderT) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
       // values.put("folder",folderT.getFolder());  //get folder
        values.put("title", newFolderT.getTitle()); // get title
        values.put("id", newFolderT.getId()); // get author
        values.put("pwd", newFolderT.getPwd()); //get password

        // 3. updating row
        int i = db.update(TABLE_FOLDERS, //table
                values, // column/value
               KEY_FFolder+" =? AND "+ KEY_FTITLE+" = ?", // selections
                new String[] { String.valueOf(folderT.getFolder()),String.valueOf(folderT.getTitle()) }); //selection args

        // 4. close
        db.close();

        return i;

    }


    public void deleteFolderCredential(FoldersTable folderT) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_FOLDERS, //table name
                KEY_FFolder+" =? AND "+ KEY_FTITLE+" = ?",  // selections
                new String[] {String.valueOf(folderT.getFolder()), String.valueOf(folderT.getTitle()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteCredential", folderT.toString());

    }


    public void deleteEntireFolder(String folderName) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_FOLDERS, //table name
                KEY_FFolder+" =?",  // selections
                new String[] {folderName }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteCredential", folderName);
    }
}
