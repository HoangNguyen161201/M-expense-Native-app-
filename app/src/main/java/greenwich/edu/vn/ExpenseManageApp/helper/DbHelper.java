package greenwich.edu.vn.ExpenseManageApp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import greenwich.edu.vn.ExpenseManageApp.expense.Expense;
import greenwich.edu.vn.ExpenseManageApp.expense.ExpenseService;
import greenwich.edu.vn.ExpenseManageApp.trip.Trip;
import greenwich.edu.vn.ExpenseManageApp.trip.TripService;

public class DbHelper extends SQLiteOpenHelper {
    public  DbHelper (Context context) {
        super(context, "ManageTrips", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTrip = TripService.SQL_CREATE_TABLE;
        db.execSQL(sqlCreateTrip);
        String sqlCreateExpense = ExpenseService.SQL_CREATE_TABLE;
        db.execSQL(sqlCreateExpense);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TripService.SQL_DROP_TABLE);
        db.execSQL(ExpenseService.SQL_DROP_TABLE);
        onCreate(db);
    }

    public static void backupData(Context context) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // get all trips from Sqlite
        ArrayList<Trip> trips = TripService.getAll(context);
        // get all expense from Sqlite
        ArrayList<Expense> expenses = ExpenseService.getAll(context);
        // check length of trip list
        if(trips.size() > 0 ){
            database.collection("trips")
                    .get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            //Clear all trips in FireStore
                            for(QueryDocumentSnapshot document : task.getResult()){
                                document.getReference().delete();
                            }

                            //backup data to FireStore
                            for(Trip trip : trips){
                                database.collection("trips").add(trip);
                            }
                        } else {
                            Toast.makeText(context, "Backup failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        // check length of expense list
        if(expenses.size() > 0 ){
            database.collection("expenses")
                    .get()
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            //Clear data expense in firebase
                            for(QueryDocumentSnapshot document : task.getResult()){
                                document.getReference().delete();
                            }

                            //backup data to FireStore
                            for(Expense expense : expenses){
                                database.collection("expenses").add(expense);
                            }
                        } else {
                            Toast.makeText(context, "Backup failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        // alert backup successfully
        Toast.makeText(context, "Backup data successfully!", Toast.LENGTH_SHORT).show();
    }
}
