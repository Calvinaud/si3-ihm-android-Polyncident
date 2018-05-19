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
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Models.Importance;
import me.myapplication.Models.Incident;
import me.myapplication.Models.PlanningIncident;

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

    public Incident getIncidentById(int incidentId){

        Cursor cursor=myDataBase.rawQuery("SELECT * FROM incidents WHERE incidentId="+incidentId, null);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String desc = cursor.getString(cursor.getColumnIndex("description"));
        //Importance importance; A FAIRE
        String urgence = cursor.getString(cursor.getColumnIndex("importance")); //temp

       //TODO: Gérer les importances
        Importance importance;

        int reporterId = cursor.getInt(cursor.getColumnIndexOrThrow("reporterId"));
        int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("locationId"));
        int typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeId"));

        return new Incident(reporterId,
                locationId,
                typeId,
                Importance.URGENT,
                title,
                desc);
    }

    public List<PlanningIncident> getDayPlanningIncident(int userId, Date date){

        List<PlanningIncident> planningIncident=new ArrayList<>();

        String queryString = "SELECT i.title, i.importance, a.startDate, a.endDate, t.name AS typeName, l.name AS lieuName" +
                "FROM assignations AS a, incidents AS i, locations AS l, types AS t" +
                "WHERE l.locationId=i.locationId AND i.incidentId=a.incidentId AND i.typeId=t.typeId AND";

        queryString += " userId=";
        queryString += userId;

        queryString += "AND startDate=";
        queryString += date;

        Cursor cursor= myDataBase.rawQuery(queryString, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            planningIncident.add(createPlanningIncident(cursor));
            cursor.moveToNext();
        }

        return planningIncident;
    }

    private PlanningIncident createPlanningIncident(Cursor cursor){

        String title = cursor.getString(cursor.getColumnIndex("title"));
        String lieuName = cursor.getString(cursor.getColumnIndex("lieuName"));
        String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
        int importance = cursor.getInt(cursor.getColumnIndex("importance"));
        String startDateS = cursor.getString(cursor.getColumnIndex("startDate"));
        String endDateS = cursor.getString(cursor.getColumnIndex("endDate"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date startDate = new Date();
        Date endDate = new Date();

        try {
            startDate = dateFormat.parse(startDateS);
            endDate = dateFormat.parse(endDateS);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return new PlanningIncident(title, lieuName, typeName, startDate, endDate, importance);
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