package me.myapplication.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Aurelien on 29/04/2018.
 */

public class IncidentDBHelper extends SQLiteOpenHelper  {

    private static String DB_NAME = "database";
    private static IncidentDBHelper singleton;

    private SQLiteDatabase myDataBase;
    private final Context myContext;


    private IncidentDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /* singleton-related methods */

    public static void createSingleton(Context context){
        if(singleton != null)
            throw new IllegalStateException("The singleton is already created.");
        singleton = new IncidentDBHelper(context);
    }

    public static IncidentDBHelper getSingleton(){
        return singleton;
    }

    /* end */

    public void openDataBase() throws SQLException, IOException {
        //Open the database
        String myPath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }



    public void createDataBase() throws IOException {

        //By calling this method and empty database will be created into the default system path
        //of your application so we are gonna be able to overwrite that database with our database.
        this.getReadableDatabase();
        try {
            // Copy the database in assets to the application database.
            copyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database", e);
        }
    }

    private void copyDataBase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void initIncidents(){
        myDataBase.execSQL("DROP TABLE IF EXISTS incidents");
        myDataBase.execSQL("CREATE TABLE incidents (incidentID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "reporterId INTEGER,"+
                "locationId INTEGER,"+
                "typeId INETEGER,"+
                "importance INTEGER,"+
                "title VARCHAR(30),"+
                "description TEXT)"
        );

    }

    public void initLocations(){

        myDataBase.execSQL("CREATE TABLE locations (locationId INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name VARCHAR(30))");

        myDataBase.execSQL("INSERT INTO locations (name) VALUES('E130')");
        myDataBase.execSQL("INSERT INTO locations (name) VALUES('E131')");
        myDataBase.execSQL("INSERT INTO locations (name) VALUES('E132')");

        myDataBase.execSQL("INSERT INTO locations (name) VALUES('O108')");
        myDataBase.execSQL("INSERT INTO locations (name) VALUES('0109')");
        myDataBase.execSQL("INSERT INTO locations (name) VALUES('0110')");

        myDataBase.execSQL("INSERT INTO locations (name) VALUES('Parking')");
    }

    public void initTypes() {

        myDataBase.execSQL("CREATE TABLE types (typeId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(30))");

        myDataBase.execSQL("INSERT INTO types (name) VALUES('Fournitures')");
        myDataBase.execSQL("INSERT INTO types (name) VALUES('Matériel cassé')");
        myDataBase.execSQL("INSERT INTO types (name) VALUES('Autres')");
    }

    public void initTables() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(myContext.getAssets().open("scriptbd.sql")));
        StringBuilder strBuilder = new StringBuilder();
        String line;

        try{

            while((line = reader.readLine()) != null){
                if(!line.trim().startsWith("--"))
                    strBuilder.append(line);
            }
            for(String query : strBuilder.toString().split(";")){
                Logger.getAnonymousLogger().severe(query);

                if(!query.equals(""))
                    myDataBase.execSQL(query);

            }
            Logger.getAnonymousLogger().severe("end");
        }catch (IOException e){
            Logger.getAnonymousLogger().severe(e.toString());
        }

    }

    public List<String> getTypes(){

        List<String> types = new ArrayList<>(10);

        Cursor cursor = myDataBase.rawQuery("SELECT name from types", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int index = cursor.getColumnIndex("name");
            types.add(cursor.getString(index));
            cursor.moveToNext();
        }

        cursor.close();

        return types;
    }

    public List<String> getLocations(){

        List<String> types = new ArrayList<>(10);

        Cursor cursor = myDataBase.rawQuery("SELECT name from locations", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int index = cursor.getColumnIndex("name");
            types.add(cursor.getString(index));
            cursor.moveToNext();
        }

        cursor.close();

        return types;
    }

    public Cursor getIncidentCursor(int reporterId, int typeId, int importance, int rowNumber){

        String queryString = "SELECT * FROM incidents WHERE ";

        queryString += " reporterId ";
        queryString += (reporterId != -1 ? "="+reporterId : "<> -1");

        queryString += " AND typeId ";
        queryString += (typeId != -1 ? "="+typeId : "<> -1");

        queryString += " AND importance ";
        queryString += (importance != -1 ? "="+importance : "<> -1");

        queryString += " LIMIT " + rowNumber;

        Logger.getAnonymousLogger().warning(queryString);

        return myDataBase.rawQuery(queryString, null);
    }

    public Cursor getLastIncidentCursor(){
        Logger.getAnonymousLogger().log(Level.WARNING, "ok ?????");
        Cursor cursor = getIncidentCursor(-1,-1,-1,100);
        cursor.moveToLast();
        return cursor;
    }

    public void insertIncident(int reporterdID, int locationID, int typeID,
                            int importance, String title, String description){

        ContentValues contentValues = new ContentValues();
        contentValues.put("reporterId", reporterdID);
        contentValues.put("locationId", locationID);
        contentValues.put("typeId", typeID);
        contentValues.put("importance", importance);
        contentValues.put("title", title);
        contentValues.put("description", description);

        myDataBase.insert("incidents",null, contentValues);
    }

    public void logIncidents(){
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM incidents", null);
        String s = "";

        //prints the columns' names
        for(String name : cursor.getColumnNames()){
            s += " " + name;
        }
        Logger.getAnonymousLogger().warning(s);

        //print the columns
        s = "";
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            for (int i = 0; i < 7; i++) {
                s += " " + cursor.getString(i) + " ";
            }
            Logger.getAnonymousLogger().warning(s);

            cursor.moveToNext();
        }

        cursor.close();
    }

    public int getIncidentNumber(){
        Cursor cursor = myDataBase.rawQuery("SELECT incidentID FROM incidents", null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    public Cursor getUserCursor(int userId) throws NoRecordException {
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM users WHERE userId ="+userId,
                                            null);

        if(!cursor.moveToFirst())
            throw new NoRecordException("There is no user with and id of "+userId);
        return cursor;
    }

    public class NoRecordException extends SQLException {

        NoRecordException(String string){
            super(string);
        }

    }


}