package greenwich.edu.vn.ExpenseManageApp.trip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import greenwich.edu.vn.ExpenseManageApp.R;
import greenwich.edu.vn.ExpenseManageApp.fragments.detailTripFragment;

public class  TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>  {
    private List<Trip> list;
    private Context context;
    public TripAdapter(List<Trip> list, Context context, View view) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trip_list,parent,false);
        TripAdapter.ViewHolder viewHolder = new TripAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.ViewHolder holder, int position) {
        Trip item = list.get(position);
        holder.titleTrip.setText(item.getName());
        holder.dateTrip.setText(item.getDate());

        if(!TextUtils.isEmpty(item.getPicture())){
            holder.imageTrip.setImageURI(Uri.parse(item.getPicture()));
        }

        holder.showDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                Navigation.createNavigateOnClickListener(R.id.action_listTrips2_to_detailTripFragment, bundle).onClick(holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTrip;
        ImageView imageTrip;
        TextView dateTrip;
        Button showDetailBtn;
        public ViewHolder(View itemView) {
            super(itemView);

            titleTrip = itemView.findViewById(R.id.titleTrip);
            imageTrip = itemView.findViewById(R.id.imageTrip);
            dateTrip = itemView.findViewById(R.id.dateTrip);
            showDetailBtn = itemView.findViewById(R.id.showDetailBtn);
        }
    }
}
