package greenwich.edu.vn.ExpenseManageApp.helper;

import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean isError = false;
    public static void checkEmpty (ArrayList<EditText> editTexts) {
        int number = 0;
        for (EditText editText : editTexts) {
            if(editText.getText().toString().isEmpty()) {
                editText.setError("Please, can't be left blank!");
                number += 1;
            }
        }
        if (number == 0) {
            isError = false;
        } else {
            isError = true;
        }

    }

    public static void checkDate (EditText date) {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date.getText().toString());
            isError = false;

        } catch (ParseException e) {
            date.setError("Format error");
            isError = true;
        }
    }

    public static void checkTime (EditText date) {
        DateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setLenient(false);
        try {
            sdf.parse(date.getText().toString());
            isError = false;

        } catch (ParseException e) {
            date.setError("Format error");
            isError = true;
        }
    }
}
