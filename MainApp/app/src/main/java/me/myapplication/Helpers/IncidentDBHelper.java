package me.myapplication.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
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

    public Cursor geUsersLogin(){
        return myDataBase.rawQuery("SELECT emailAddress, password, userId from users", null);
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

        List<String> locations = new ArrayList<>(10);

        Cursor cursor = myDataBase.rawQuery("SELECT name from locations", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int index = cursor.getColumnIndex("name");
            locations.add(cursor.getString(index));
            cursor.moveToNext();
        }

        cursor.close();

        return locations;
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
        String url = cursor.getString(cursor.getColumnIndexOrThrow("urlPhoto"));
        String sdate = cursor.getString(cursor.getColumnIndexOrThrow("declarationDate"));

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = dateFormat.parse(sdate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return new Incident(incidentId,reporterId,
                locationId,
                typeId,
                Importance.URGENT,
                title,
                desc,url,date);
    }

    public List<PlanningIncident> getDayPlanningIncident(int id, Date date){

        List<PlanningIncident> planningIncident = new ArrayList<>();

        String queryString = "SELECT i.incidentId, i.title, i.importance, a.startDate, a.endDate, t.name AS typeName, l.name AS lieuName" +
                " FROM assignations AS a, incidents AS i, locations AS l, types AS t" +
                " WHERE l.locationId=i.locationId AND i.incidentId=a.incidentId AND t.typeId = i.typeId AND ";

        queryString += "a.userId=";
        queryString += id;

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(date);
        String endDate = startDate.substring(0,10)+" 23:59:59";

        queryString +=" AND a.startDate>=";
        queryString += startDate;

         queryString +=" AND a.endDate<='";
        queryString += endDate+"'";

        Cursor cursor= myDataBase.rawQuery(queryString, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            planningIncident.add(createPlanningIncident(cursor));
            cursor.moveToNext();
        }

        return planningIncident;
    }


    public Cursor getIncidentMonth(int userId, int month, int year){
        Calendar aCalendar = new GregorianCalendar(year,month-1,1);

        Date firstDay = aCalendar.getTime();
        aCalendar.set(Calendar.DATE,     aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDay = aCalendar.getTime();

        String queryString = "SELECT i.incidentId, i.title, i.importance, a.startDate, a.endDate, t.name AS typeName, l.name AS lieuName" +
                " FROM assignations AS a, incidents AS i, locations AS l, types AS t" +
                " WHERE l.locationId=i.locationId AND i.incidentId=a.incidentId AND t.typeId = i.typeId AND ";

        queryString += "a.userId=";
        queryString += userId;

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(firstDay);
        String endDate = format.format(lastDay)+" 23:59:59";

        queryString +=" AND a.startDate>=";
        queryString += startDate;

        queryString +=" AND a.endDate<='";
        queryString += endDate+"'";

        Log.i("queryPlanning",queryString);

        Cursor cursor = myDataBase.rawQuery(queryString,null);
        Log.i("infos",""+month+",,,"+year+"----"+cursor.getCount());

        return cursor;
    }


    private PlanningIncident createPlanningIncident(Cursor cursor){

        int incidentId = cursor.getInt(cursor.getColumnIndex("incidentId"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String lieuName = cursor.getString(cursor.getColumnIndex("lieuName"));
        String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
        int importance = cursor.getInt(cursor.getColumnIndex("importance"));
        String startDateS = cursor.getString(cursor.getColumnIndex("startDate"));
        String endDateS = cursor.getString(cursor.getColumnIndex("endDate"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();

        try {
            startDate = dateFormat.parse(startDateS);
            endDate = dateFormat.parse(endDateS);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return new PlanningIncident(incidentId,title, lieuName, typeName, startDate, endDate, importance);
    }

    public Cursor getCommentaires(int incidentId) {

        String queryString = "SELECT c.*, u.username FROM comments AS c, users AS u WHERE u.userId=c.userId AND ";

        queryString += "incidentId=";
        //queryString += (incidentId != -1 ? "="+incidentId : "<> -1");
        queryString += incidentId;

        Logger.getAnonymousLogger().warning(queryString);

        Cursor cursor = myDataBase.rawQuery(queryString, null);
        Log.i("n",""+cursor.getCount());

        return cursor;
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

    public Cursor getTechnicien(){
        Cursor cursor=myDataBase.rawQuery("SELECT username, userId from users WHERE roles='TECHNICIEN'",null);

        return cursor;
    }


    public boolean getTechoStatus(int userid){

        Date current = Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentString = format.format(current);

        String queryString = "SELECT * FROM assignations WHERE userId=";

        queryString +=userid;

        queryString +=" AND startDate<='";
        queryString +=currentString;

        queryString +="' AND endDate>='";
        queryString +=currentString+"'";

        Cursor cursor = myDataBase.rawQuery(queryString,null);

        return cursor.getCount()>0;
    }

    public int getTechoDayCount(int userid){

        Date current= Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String currentString = format.format(current);

        String queryString = "SELECT COUNT(*) AS Nb FROM assignations WHERE userId=";

        queryString +=userid;

        queryString +=" AND startDate>='";
        queryString +=currentString+" 00:00:00";

        queryString +="' AND endDate<='";
        queryString +=currentString+" 23:59:59'";

        Cursor cursor = myDataBase.rawQuery(queryString,null);
        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex("Nb"));
    }

    public int getTechoWeekCount(int userid){

        Calendar c = GregorianCalendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.add(Calendar.DAY_OF_WEEK, 1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate;
        String endDate;
        startDate = df.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 6);
        endDate = df.format(c.getTime());

        String queryString = "SELECT COUNT(*) AS Nb FROM assignations WHERE userId=";

        queryString +=userid;

        queryString +=" AND startDate>='";
        queryString +=startDate+" 00:00:00";

        queryString +="' AND endDate<='";
        queryString +=endDate+" 23:59:59'";

        Log.i("EEEE",queryString);

        Cursor cursor = myDataBase.rawQuery(queryString,null);
        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex("Nb"));
    }

    public long getTechoWeekSum(int userid){

        Calendar c = GregorianCalendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.add(Calendar.DAY_OF_WEEK, 1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate;
        String endDate;
        startDate = df.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 6);
        endDate = df.format(c.getTime());
        String queryString = "SELECT startDate, endDate FROM assignations WHERE userId=";

        queryString +=userid;

        queryString += " AND startDate>='";
        queryString +=startDate+" 00:00:00'";

        queryString +=" AND endDate<='";
        queryString +=endDate+" 23:59:59'";

        Cursor cursor = myDataBase.rawQuery(queryString,null);
        cursor.moveToFirst();
        long sum=0;

        while (!cursor.isAfterLast()) {
            sum += differenceDate(cursor);
            cursor.moveToNext();
        }

        return sum;
    }

    public long getTechoDaySum(int userid){

        Date current= Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String currentString = format.format(current);

        String queryString = "SELECT startDate, endDate FROM assignations WHERE userId=";

        queryString +=userid;

        queryString += " AND startDate>='";
        queryString +=currentString+" 00:00:00'";

        queryString +=" AND endDate<='";
        queryString +=currentString+" 23:59:59'";

        Cursor cursor = myDataBase.rawQuery(queryString,null);
        cursor.moveToFirst();
        long sum=0;

        while (!cursor.isAfterLast()) {
            sum += differenceDate(cursor);
            cursor.moveToNext();
        }

        return sum;
    }

    private long differenceDate(Cursor cursor){
        String startDateS = cursor.getString(cursor.getColumnIndex("startDate"));
        String endDateS = cursor.getString(cursor.getColumnIndex("endDate"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();

        try {
            startDate = dateFormat.parse(startDateS);
            endDate = dateFormat.parse(endDateS);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        long difference = Math.abs(endDate.getTime()-startDate.getTime());

        return difference;
    }

    public boolean isUserSubscribed(int userId, int incidentId){

        String queryString = "SELECT * FROM subscriptions WHERE ";

        queryString += " userId = "+userId;

        queryString += " AND incidentId = "+incidentId;

        Logger.getAnonymousLogger().warning(queryString);

        return myDataBase.rawQuery(queryString, null).getCount() > 0;
    }

    public void updateStatus(int incidentId, int statusId){
        String queryString = "UPDATE incidents SET status";
        queryString += "= "+statusId;
        queryString += " WHERE incidentId = "+incidentId;
        myDataBase.execSQL(queryString);
    }

    public Cursor getLastIncidentCursor(){
        Logger.getAnonymousLogger().log(Level.WARNING, "ok ?????");
        Cursor cursor = getIncidentCursor(-1,-1,-1,100);
        cursor.moveToLast();
        return cursor;
    }

    public void insertIncident(int reporterdID, int locationID, int typeID,
                            int importance, String title, String description, String imgUrl, int status, Date date){

        ContentValues contentValues = new ContentValues();
        contentValues.put("reporterId", reporterdID);
        contentValues.put("locationId", locationID);
        contentValues.put("typeId", typeID);
        contentValues.put("importance", importance);
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("urlPhoto",imgUrl);
        contentValues.put("status", status);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        contentValues.put("declarationDate", format.format(date));

        myDataBase.insert("incidents",null, contentValues);
    }

    public void insertComm(int userId,int incidentId,Date date,String content){

        ContentValues contentValues = new ContentValues();
        contentValues.put("userId",userId);
        contentValues.put("incidentId",incidentId);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        contentValues.put("date",format.format(date));
        contentValues.put("comment",content);

        myDataBase.insert("comments",null,contentValues);
    }


    public void insertSub(int userId,int incidentId){
        ContentValues contentValues = new ContentValues();
        contentValues.put("incidentId",incidentId);
        contentValues.put("userId",userId);

        myDataBase.insert("subscriptions",null,contentValues);
    }

    public void logComms(){ logTable("comments");}

    public void logIncidents(){
        logTable("incidents");
    }


    public void logUsers(){
        logTable("users");
    }


    public void logTable(String tableName){

        Cursor cursor = myDataBase.rawQuery("SELECT * FROM "+tableName, null);
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
            for (int i = 0; i < cursor.getColumnCount(); i++) {
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