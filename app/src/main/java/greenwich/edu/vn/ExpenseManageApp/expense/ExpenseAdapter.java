package greenwich.edu.vn.ExpenseManageApp.expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import greenwich.edu.vn.ExpenseManageApp.R;

public class  ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> list;

    public ExpenseAdapter(List<Expense> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_expenses,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense item = list.get(position);
        holder.amountExpense.setText("$"+ item.getAmount());
        holder.typeExpense.setText(item.getType());
        holder.dateExpense.setText(item.getDate());
        holder.timeExpense.setText(item.getTime());
        holder.address.setText(item.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateExpense, timeExpense, address , typeExpense, amountExpense;
        public ViewHolder(View itemView) {
            super(itemView);
            dateExpense = itemView.findViewById(R.id.dateExpense);
            timeExpense = itemView.findViewById(R.id.timeExpense);
            typeExpense = itemView.findViewById(R.id.typeExpense);
            amountExpense = itemView.findViewById(R.id.amountExpense);
            address = itemView.findViewById(R.id.addressExpense);

        }
    }
}
