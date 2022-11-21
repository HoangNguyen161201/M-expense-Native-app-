package greenwich.edu.vn.ExpenseManageApp.trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import greenwich.edu.vn.ExpenseManageApp.helper.DbHelper;

public class TripService {
    public static final String TABLE_NAME = "trips";
    public static final String COL_NAME_ID = "id";
    public static final String COL_NAME_NAME = "name";
    public static final String COL_NAME_DESCRIPTION = "description";
    public static final String COL_NAME_DESTINATION = "destination";
    public static final String COL_NAME_DATE = "date";
    public static final String COL_NAME_IS_RISK = "isRisk";
    public static final String COL_NAME_PICTURE = "picture";
    public static final String COL_NAME_MEMBER_COUNT = "memberCount";
    public static final String COL_NAME_PREDICTED_AMOUNT = "predictedAmount";

    public static final String SQL_CREATE_TABLE = "Create table " + TABLE_NAME +
            "(" + COL_NAME_ID+
            " integer primary key autoincrement," +
            "" + COL_NAME_NAME +
            " text, " + COL_NAME_DESCRIPTION +
            " text, " + COL_NAME_DESTINATION +
            " text, " + COL_NAME_DATE +
            " text, " + COL_NAME_IS_RISK +
            " integer, " + COL_NAME_PICTURE +
            " text, " + COL_NAME_MEMBER_COUNT +
            " integer, " + COL_NAME_PREDICTED_AMOUNT +
            " integer)";
    public static final String SQL_DROP_TABLE = "Drop table if exists " + TABLE_NAME;

    public static ArrayList<Trip> getAll(Context context) {
        ArrayList<Trip> list = new ArrayList<>();
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("select * from " + TABLE_NAME, null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            String description = cs.getString(2);
            String destination = cs.getString(3);
            String date = cs.getString(4);
            int isRisk = cs.getInt(5);
            String picture = cs.getString(6);
            int memberCount = cs.getInt(7);
            int predictedAmount = cs.getInt(8);
            Trip trip = new Trip(name, description, destination, date,id, isRisk, picture, memberCount, predictedAmount);
            list.add(trip);
            cs.moveToNext();
        }
        cs.close();
        db.close();
        return list;
    };

    public static Trip getDetail(Context context, int idTrip) {
        Trip detail = new Trip("","","","",2,1, "", 0, 0);
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("select * from " + TABLE_NAME +
                " where " + COL_NAME_ID +
                "= " + idTrip, null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            String description = cs.getString(2);
            String destination = cs.getString(3);
            String date = cs.getString(4);
            int isRisk = cs.getInt(5);
            String picture = cs.getString(6);
            int memberCount = cs.getInt(7);
            int predictedAmount = cs.getInt(8);
            detail = new Trip(name, description, destination, date,id, isRisk, picture, memberCount, predictedAmount);
            cs.moveToNext();

        }
        cs.close();
        db.close();
        return detail;
    };

    public static boolean insert(Context context, Trip trip) {
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME_NAME, trip.getName());
        values.put(COL_NAME_DESCRIPTION, trip.getDescription());
        values.put(COL_NAME_DESTINATION, trip.getDestination());
        values.put(COL_NAME_DATE, trip.getDate());
        values.put(COL_NAME_IS_RISK, trip.getIsRisk());
        values.put(COL_NAME_PICTURE, trip.getPicture());
        values.put(COL_NAME_MEMBER_COUNT, trip.getMemberCount());
        values.put(COL_NAME_PREDICTED_AMOUNT, trip.getPredictedAmount());
        long row = db.insert(TABLE_NAME,null,values);
        return row > 0;
    }

    public static boolean update (Context context, Trip trip) {
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME_NAME, trip.getName());
        values.put(COL_NAME_DESCRIPTION, trip.getDescription());
        values.put(COL_NAME_DESTINATION, trip.getDestination());
        values.put(COL_NAME_DATE, trip.getDate());
        values.put(COL_NAME_IS_RISK, trip.getIsRisk());
        values.put(COL_NAME_PICTURE, trip.getPicture());
        values.put(COL_NAME_MEMBER_COUNT, trip.getMemberCount());
        values.put(COL_NAME_PREDICTED_AMOUNT, trip.getPredictedAmount());
        int row = db.update(TABLE_NAME,values, "id=?", new String[]{trip.getId() + ""});
        return row > 0;
    }

    public static boolean remove (Context context, int id) {
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete(TABLE_NAME, "id=?", new String[]{id + ""});
        return row > 0;
    }

    public static boolean reset (Context context) {
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete(TABLE_NAME, null, null);
        return row > 0;
    }

    public static ArrayList<Trip> search(Context context, String nameSearch, String descriptionSearch, String destinationSearch, String dateSearch) {
        ArrayList<Trip> list = new ArrayList<>();
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("Select * from " + TABLE_NAME +
                " where" +
                " " + COL_NAME_NAME +
                " like " + "'%" + nameSearch + "%'" +
                " and " + COL_NAME_DESCRIPTION +
                " like" + "'%" + descriptionSearch + "%'" +
                " and " + COL_NAME_DATE+
                " like" + "'%" + dateSearch + "%'" + " and " + COL_NAME_DESTINATION +
                " like" + "'%" + destinationSearch +"%'" , null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            String description = cs.getString(2);
            String destination = cs.getString(3);
            String date = cs.getString(4);
            int isRisk = cs.getInt(5);
            String picture = cs.getString(6);
            int memberCount = cs.getInt(7);
            int predictedAmount = cs.getInt(8);
            Trip trip = new Trip(name, description, destination, date,id, isRisk, picture, memberCount, predictedAmount);
            list.add(trip);
            cs.moveToNext();
        }
        cs.close();
        db.close();
        return list;
    }
}
