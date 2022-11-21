package greenwich.edu.vn.ExpenseManageApp.expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import greenwich.edu.vn.ExpenseManageApp.helper.DbHelper;

public class ExpenseService {
    public static final String TABLE_NAME = "expense";
    public static final String COL_NAME_ID = "id";
    public static final String COL_NAME_TYPE = "type";
    public static final String COL_NAME_TIME = "time";
    public static final String COL_NAME_DATE = "date";
    public static final String COL_NAME_AMOUNT = "amount";
    public static final String COL_NAME_COMMENT = "additionalComments";
    public static final String COL_NAME_ADDRESS = "address";
    public static final String COL_NAME_TRIP_ID = "tripId";


    public static final String SQL_CREATE_TABLE = "Create table " + TABLE_NAME +
            " (id integer primary key autoincrement," +
            COL_NAME_TYPE +
            " text not null," +
            COL_NAME_DATE +
            " text not null," +
            COL_NAME_TIME +
            " text not null," +
            COL_NAME_AMOUNT +
            " integer not null," +
            COL_NAME_COMMENT +
            " text," +
            COL_NAME_ADDRESS +
            " text," +
            COL_NAME_TRIP_ID+
            " integer NOT NULL," +
            "FOREIGN KEY (tripId) REFERENCES trips (id)" +
            " ON DELETE CASCADE)";
    public static final String SQL_DROP_TABLE = "Drop table if exists " + TABLE_NAME;

    public static ArrayList<Expense> getAllById(Context context, int idTrip) {
        ArrayList<Expense> list = new ArrayList<>();
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("select * from expense where tripId = " + idTrip, null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String type = cs.getString(1);
            String date = cs.getString(2);
            String time = cs.getString(3);
            int amount = cs.getInt(4);
            String additionalComments = cs.getString(5);
            String address = cs.getString(6);
            int tripId = cs.getInt(7);

            Expense expense = new Expense(id, type,date,time,additionalComments,tripId, amount, address);
            list.add(expense);
            cs.moveToNext();
        }
        cs.close();
        db.close();
        return list;
    };

    public static ArrayList<Expense> getAll(Context context) {
        ArrayList<Expense> list = new ArrayList<>();
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("select * from expense", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String type = cs.getString(1);
            String date = cs.getString(2);
            String time = cs.getString(3);
            int amount = cs.getInt(4);
            String additionalComments = cs.getString(5);
            String address = cs.getString(6);
            int tripId = cs.getInt(7);

            Expense expense = new Expense(id, type,date,time,additionalComments,tripId, amount, address);
            list.add(expense);
            cs.moveToNext();
        }
        cs.close();
        db.close();
        return list;
    };

    public static Expense getDetail(Context context, int idExpense) {
        Expense detail = new Expense(0,"","","","",2,1, "");
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("select * from " + TABLE_NAME +
                " where id= " + idExpense, null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String type = cs.getString(1);
            String date = cs.getString(2);
            String time = cs.getString(3);
            int amount = cs.getInt(4);
            String additionalComments = cs.getString(5);
            String address = cs.getString(6);
            int tripId = cs.getInt(7);

            detail = new Expense(id, type, date, time, additionalComments, tripId, amount, address);
            cs.moveToNext();
        }
        cs.close();
        db.close();
        return detail;
    };

    public static boolean insert(Context context, Expense expense) {
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME_TYPE, expense.getType());
        values.put(COL_NAME_DATE, expense.getDate());
        values.put(COL_NAME_TIME, expense.getTime());
        values.put(COL_NAME_AMOUNT, expense.getAmount());
        values.put(COL_NAME_COMMENT, expense.getAdditionalComments());
        values.put(COL_NAME_ADDRESS, expense.getAddress());
        values.put(COL_NAME_TRIP_ID, expense.getTripId());
        long row = db.insert(TABLE_NAME,null,values);
        return row > 0;
    }
}
