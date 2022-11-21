package greenwich.edu.vn.ExpenseManageApp.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import greenwich.edu.vn.ExpenseManageApp.R;

public class DialogHelper {
    private AlertDialog.Builder builder;
    private View view;

    public DialogHelper(Context context, LayoutInflater inflater, int id) {
            builder = new AlertDialog.Builder(context);
            view = inflater.inflate(id, null);
            builder.setView(view);
    }

    public Dialog getDialog() {
        return builder.create();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
        builder.setView(view);
    }
}
