package greenwich.edu.vn.ExpenseManageApp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import greenwich.edu.vn.ExpenseManageApp.R;
import greenwich.edu.vn.ExpenseManageApp.trip.Trip;
import greenwich.edu.vn.ExpenseManageApp.trip.TripAdapter;
import greenwich.edu.vn.ExpenseManageApp.trip.TripService;

public class ListTrips extends Fragment {
    TripAdapter adapter;
    ArrayList<Trip> listTrips = new ArrayList<>();

    // search
    SearchView searchTrip;
    EditText searchName, searchDescription, searchDestination, searchDate;
    Button cancelBtn, confirmBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.search) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View viewDialogConfirm = inflater.inflate(R.layout.dialog_search, null);
            builder.setView(viewDialogConfirm);
            Dialog dialog = builder.create();
            dialog.show();

            searchName = viewDialogConfirm.findViewById(R.id.searchName);
            searchDescription = viewDialogConfirm.findViewById(R.id.searchDescription);
            searchDestination = viewDialogConfirm.findViewById(R.id.searchDestination);
            searchDate = viewDialogConfirm.findViewById(R.id.searchDate);

            cancelBtn = viewDialogConfirm.findViewById(R.id.cancleSearch);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            confirmBtn = viewDialogConfirm.findViewById(R.id.confirmSearch);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Trip> data = TripService.search(getContext(), searchName.getText().toString(),searchDescription.getText().toString(), searchDestination.getText().toString(), searchDate.getText().toString());
                    listTrips.clear();
                    listTrips.addAll(data);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

        }
        if(id == R.id.clearSearch) {
            ArrayList<Trip> data = TripService.getAll(getContext());
            listTrips.clear();
            listTrips.addAll(data);
            adapter.notifyDataSetChanged();

            Toast.makeText(getContext(), "Clear search successfully", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_trips, container, false);

        ArrayList<Trip> data = TripService.getAll(getContext());
        if(data.size() != 0) {
            listTrips = data;
        }

        RecyclerView listView = rootView.findViewById(R.id.listTrips);
        adapter = new TripAdapter(listTrips, getActivity(), rootView);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        // search trip
        searchTrip = rootView.findViewById(R.id.searchTrip);
        searchTrip.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Trip> data = TripService.search(getContext(), newText,"", "", "");
                listTrips.clear();
                listTrips.addAll(data);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        return rootView;
    }
}