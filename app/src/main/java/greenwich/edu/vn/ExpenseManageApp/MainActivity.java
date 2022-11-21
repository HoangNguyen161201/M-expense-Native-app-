package greenwich.edu.vn.ExpenseManageApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import greenwich.edu.vn.ExpenseManageApp.fragments.AddTripsFragment;
import greenwich.edu.vn.ExpenseManageApp.fragments.ContactFragment;
import greenwich.edu.vn.ExpenseManageApp.fragments.HomeFragment;
import greenwich.edu.vn.ExpenseManageApp.fragments.ListTrips;

public class MainActivity extends AppCompatActivity {
    public static String textAddress = "";

    // For GPS.
    protected final int REFRESH_TIME = 15000;
    protected final int REFRESH_DISTANCE = 500;
    protected final int REQUEST_CODE_PERMISSIONS_GPS = 105;

    // Below is an array of required permissions used for processing.
    protected final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    BottomNavigationView bottomMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.listTrips, R.id.addTripsFragment, R.id.contactFragment
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        startGPS();
    }

    // Check if the permission is authorized by the user
    private boolean  allPermissionGranted_GPS() {
        for(String permission : REQUIRED_PERMISSIONS)
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    protected void startGPS() {
        // If tracking the location has not been authorized by the user, it will continue
        if(!allPermissionGranted_GPS()) {
            // Whether the user allows it or not, it will also function onRequestPermissionsResult
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS_GPS);
            return;
        }

        locationListener = getLocation();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // After 15 seconds, the location will be updated again
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME, REFRESH_DISTANCE, locationListener);
    }

    protected LocationListener getLocation() {
        return new LocationListener() {
            @Override
            // When the position is updated this function will be called again
            public void onLocationChanged(@NonNull Location location) {
                String error = "Can not find detailed information";

                String address = error;
                String country = error;
                String city = error;
                String postalCode = error;

                double latitude = location.getLatitude();
                double longitude = location .getLongitude();

                // Set more information
                try {
                    // Geocoder helps get a list of addresses based on location
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    // Returns a list of addresses but contains only the closest one
                    List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);

                    if(addresses.size() > 0) {
                        address = addresses.get(0).getAddressLine(0);
                        country = addresses.get(0).getCountryName();
                        city = addresses.get(0).getLocality();
                        postalCode = addresses.get(0).getPostalCode();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textAddress = address + ", " + country + ", " + city + ", " + postalCode;
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check if the requestCode is the same as the GPS permission code
        // if true, users just need to start GPS without having to turn off the device
        if(requestCode == REQUEST_CODE_PERMISSIONS_GPS) {
            if(allPermissionGranted_GPS()){
                startGPS();
                return;
            }

            Toast.makeText(this, "GPS Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
        }
    }
}