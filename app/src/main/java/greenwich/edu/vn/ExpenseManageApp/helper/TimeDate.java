package greenwich.edu.vn.ExpenseManageApp.helper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import greenwich.edu.vn.ExpenseManageApp.R;

public class TimeDate {
    public static void openDate (View view, int id, Context context) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        EditText date = view.findViewById(id);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            String dateString = dayOfMonth + "/" + month + "/" + year;
                            date.setText(dateString);
                            date.setError(null);
                        }
                    }, year, month, dayOfMonth);
                    datePickerDialog.show();
                }
            }
        });
    }
}
