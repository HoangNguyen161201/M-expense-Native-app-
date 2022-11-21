package greenwich.edu.vn.ExpenseManageApp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import greenwich.edu.vn.ExpenseManageApp.R;
import greenwich.edu.vn.ExpenseManageApp.expense.Expense;
import greenwich.edu.vn.ExpenseManageApp.expense.ExpenseService;
import greenwich.edu.vn.ExpenseManageApp.helper.DbHelper;
import greenwich.edu.vn.ExpenseManageApp.trip.Trip;
import greenwich.edu.vn.ExpenseManageApp.trip.TripService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    LinearLayout reset, backup , addNew, list, about;
    BottomNavigationView bottomNavigationView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        addNew = rootView.findViewById(R.id.homeAdd);
        about = rootView.findViewById(R.id.homeAbout);
        backup = rootView.findViewById(R.id.homeBackup);
        reset = rootView.findViewById(R.id.homeReset);
        list = rootView.findViewById(R.id.homeList);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.addTripsFragment);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.listTrips);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.contactFragment);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean successReset = TripService.reset(getContext());
                if(successReset) {
                    Toast.makeText(getContext(), "Reset successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Reset failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper.backupData(getContext());            }
        });

        return rootView;
    }
}