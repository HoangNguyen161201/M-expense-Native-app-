package greenwich.edu.vn.ExpenseManageApp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import greenwich.edu.vn.ExpenseManageApp.MainActivity;
import greenwich.edu.vn.ExpenseManageApp.R;
import greenwich.edu.vn.ExpenseManageApp.expense.Expense;
import greenwich.edu.vn.ExpenseManageApp.expense.ExpenseAdapter;
import greenwich.edu.vn.ExpenseManageApp.expense.ExpenseService;
import greenwich.edu.vn.ExpenseManageApp.helper.DialogHelper;
import greenwich.edu.vn.ExpenseManageApp.helper.TimeDate;
import greenwich.edu.vn.ExpenseManageApp.helper.Validation;
import greenwich.edu.vn.ExpenseManageApp.trip.Trip;
import greenwich.edu.vn.ExpenseManageApp.trip.TripService;

public class detailTripFragment extends Fragment {

    TextView name, description, date, destination, isRisk, title, memberCount , predictedAmount;
    ImageView imageTrip;
    Button takePictureBtn, pictureFromFolderBtn;

    Uri uri = Uri.parse("");
    ImageView imageTripUpdate;

    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    private final int SELECT_IMAGE_CODE = 1;

    EditText updateName,
             updateDescription,
             updateDate,
             updateDestination,
             updateMemberCount,
             updatePredictedAmount;
    Switch updateIsRisk;
    Trip trip;
    Spinner spinner;

    ArrayList<Expense> listExpenses =  new ArrayList<Expense>();
    ExpenseAdapter expenseAdapter;

    // data to add new expense -------------------
    EditText timeExpensive,
             amountExpense,
             dateExpense,
             timeExpense,
             commentExpense,
             addressExpense;
    String typeExpense;
    int hour, minute;

    // button in add form expense
    Button cancelExpense, addExpense;

    public detailTripFragment() {
        // Required empty public constructor
    }

    public static detailTripFragment newInstance(String param1, String param2) {
        detailTripFragment fragment = new detailTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_trip, container, false);
        name = rootView.findViewById(R.id.nameDetail);
        description = rootView.findViewById(R.id.descriptionDetail);
        destination = rootView.findViewById(R.id.destinationDetail);
        date = rootView.findViewById(R.id.dateDetail);
        isRisk = rootView.findViewById(R.id.isRiskDetail);
        title = rootView.findViewById(R.id.detailTitle);
        imageTrip = rootView.findViewById(R.id.detailPicture);
        memberCount = rootView.findViewById(R.id.memberCountDetail);
        predictedAmount = rootView.findViewById(R.id.predictedAmountDetail);

        Bundle bundle = this.getArguments();
        int id = bundle.getInt("id");
        trip = TripService.getDetail(getContext(), id);

        updateDetail(trip);

        // show expenses
        RecyclerView listView = rootView.findViewById(R.id.listExpenses);
        expenseAdapter = new ExpenseAdapter(listExpenses);
        listView.setAdapter(expenseAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        rootView.findViewById(R.id.showExpenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(listExpenses.size() == 0) {
                   ArrayList<Expense> data = ExpenseService.getAllById(getContext(),id);
                   if(data.size() != 0) {
                       listExpenses.addAll(data);
                   }

               } else {
                   listExpenses.clear();
               }
                expenseAdapter.notifyDataSetChanged();
            }
        });

        // show form expense
        rootView.findViewById(R.id.addExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper dialogAddExpenseConfig = new DialogHelper(
                        getContext(),
                        getLayoutInflater(),
                        R.layout.dialog_add_expense
                );
                View viewAddExpense = dialogAddExpenseConfig.getView();
                Dialog DialogAddExpense = dialogAddExpenseConfig.getDialog();

                addressExpense = viewAddExpense.findViewById(R.id.addExpenseAddress);
                addressExpense.setText(MainActivity.textAddress);
                // open calendar
                TimeDate.openDate(viewAddExpense, R.id.addExpenseDate, getContext());

                // spinner
                spinner = viewAddExpense.findViewById(R.id.addExpenseType);
                ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getContext(), R.array.types, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                         typeExpense = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                DialogAddExpense.show();

                // button
                cancelExpense = viewAddExpense.findViewById(R.id.cancelAddExpense);
                cancelExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogAddExpense.dismiss();
                    }
                });
                viewAddExpense.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogAddExpense.dismiss();
                    }
                });

                addExpense = viewAddExpense.findViewById(R.id.addExpense);
                addExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        amountExpense = viewAddExpense.findViewById(R.id.addExpenseAmount);
                        dateExpense = viewAddExpense.findViewById(R.id.addExpenseDate);
                        timeExpense = viewAddExpense.findViewById(R.id.addExpenseTime);
                        commentExpense = viewAddExpense.findViewById(R.id.addExpenseAmount);
                        addressExpense = viewAddExpense.findViewById(R.id.addExpenseAddress);

                        ArrayList<EditText> editTexts = new ArrayList<>();
                        // check valid
                        editTexts.add(amountExpense);
                        editTexts.add(dateExpense);
                        editTexts.add(timeExpense);
                        Validation.checkEmpty(editTexts);
                        if( Validation.isError) {
                            return;
                        }
                        Validation.checkDate(dateExpense);
                        if( Validation.isError) {
                            return;
                        }
                        Validation.checkTime(timeExpense);
                        if( Validation.isError) {
                            return;
                        }

                        Expense expense = new Expense(0,typeExpense,dateExpense.getText().toString(),timeExpense.getText().toString(),commentExpense.getText().toString(), id, Integer.parseInt( amountExpense.getText().toString()), addressExpense.getText().toString());
                        boolean isSuccessfully = ExpenseService.insert(getContext(), expense);
                        if(isSuccessfully) {
                            Toast.makeText(getContext(), "add expense successfully", Toast.LENGTH_LONG).show();
                            ArrayList<Expense> data = ExpenseService.getAllById(getContext(),id);
                            if(data.size() != 0) {
                                listExpenses.clear();
                                listExpenses.addAll(data);
                            }

                            expenseAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getContext(), "add expense failed", Toast.LENGTH_LONG).show();
                        }

                        DialogAddExpense.dismiss();
                    }
                });

                // timePicker
                timeExpensive = viewAddExpense.findViewById(R.id.addExpenseTime);
                timeExpensive.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(b) {
                            // timePicker
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    calendar.setTimeZone(TimeZone.getDefault());
                                    SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
                                    String time = format.format(calendar.getTime());
                                    timeExpensive.setText(time);
                                }
                            }, hour, minute, false);
                            timePickerDialog.show();
                        }
                    }
                });
            }
        });



        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit:

                // dialog update config
                DialogHelper dialogUpdateTripConfig = new DialogHelper(
                        getContext(),
                        getLayoutInflater(),
                        R.layout.dialog_update_trip
                );
                View viewUpdateTrip = dialogUpdateTripConfig.getView();
                Dialog DialogUpdateTrip = dialogUpdateTripConfig.getDialog();


                takePictureBtn = viewUpdateTrip.findViewById(R.id.takePicture);
                pictureFromFolderBtn = viewUpdateTrip.findViewById(R.id.getPicture);
                imageTripUpdate = viewUpdateTrip.findViewById(R.id.imageTrip);

                // take picture by camera
                takePictureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(ActivityCompat.checkSelfPermission (viewUpdateTrip.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission, PERMISSION_CODE);
                            } else {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }
                    }
                });

                // select picture from device
                pictureFromFolderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent iGallery = new Intent();
                        iGallery.setType("image/*");
                        iGallery.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(iGallery, "Title"), SELECT_IMAGE_CODE);
                    }
                });

                updateName = viewUpdateTrip.findViewById(R.id.updateName);
                updateDescription = viewUpdateTrip.findViewById(R.id.updateDescription);
                updateDestination = viewUpdateTrip.findViewById(R.id.updateDestination);
                updateDate = viewUpdateTrip.findViewById(R.id.updateDate);
                updateIsRisk = viewUpdateTrip.findViewById(R.id.updateIsRisk);
                updateMemberCount = viewUpdateTrip.findViewById(R.id.updateMemberCount);
                updatePredictedAmount = viewUpdateTrip.findViewById(R.id.updatePredictedAmount);

                // openDate function is used so that when the user
                // clicks on the input, the calendar will be displayed for the user to choose
                TimeDate.openDate(viewUpdateTrip, R.id.updateDate, getContext());

                // paste old trip data into editTexts
                updateName.setText(trip.getName());
                updateDescription.setText(trip.getDescription());
                updateDestination.setText(trip.getDestination());
                updateDate.setText(trip.getDate());
                updateIsRisk.setChecked(trip.getIsRisk() == 1 ? true : false);
                updateMemberCount.setText("" + trip.getMemberCount());
                updatePredictedAmount.setText("" + trip.getPredictedAmount());

                // show dialog
                DialogUpdateTrip.show();

                viewUpdateTrip.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUpdateTrip.dismiss();
                    }
                });

                viewUpdateTrip.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUpdateTrip.dismiss();
                    }
                });

                // when you press confirm
                viewUpdateTrip.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<EditText> editTexts = new ArrayList<>();
                        // check valid
                        editTexts.add(updateName);
                        editTexts.add(updateDestination);
                        editTexts.add(updateDate);
                        Validation.checkEmpty(editTexts);
                        if( Validation.isError) {
                            return;
                        }
                        Validation.checkDate(updateDate);
                        if( Validation.isError) {
                            return;
                        }

                        // Update data to sqlite
                        Trip updateTrip = new Trip(updateName.getText().toString(),
                                updateDescription.getText().toString(),
                                updateDestination.getText().toString(),
                                updateDate.getText().toString(),
                                trip.getId(),
                                updateIsRisk.isChecked() ? 1 : 0, uri.toString(),
                                Integer.parseInt(updateMemberCount.getText().toString()),
                                Integer.parseInt(updatePredictedAmount.getText().toString())
                                );

                        boolean isUpdateSuccess = TripService.update(getContext(), updateTrip);

                        // Alert when update successfully
                        if(isUpdateSuccess) {
                            updateDetail(updateTrip);
                            Toast.makeText(getContext(), "Edit successfully", Toast.LENGTH_LONG).show();
                        }
                        DialogUpdateTrip.dismiss();
                    }
                });
                break;
            case R.id.delete:
                DialogHelper dialogConfirmDeleteConfig = new DialogHelper(
                        getContext(),
                        getLayoutInflater(),
                        R.layout.dialog_confirm_delete
                );
                View viewConfirmDelete = dialogConfirmDeleteConfig.getView();
                Dialog DialogConfirmDelete = dialogConfirmDeleteConfig.getDialog();
                DialogConfirmDelete.show();

                viewConfirmDelete.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogConfirmDelete.dismiss();
                    }
                });

                viewConfirmDelete.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogConfirmDelete.dismiss();
                    }
                });

                viewConfirmDelete.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isRemove = TripService.remove(getContext(), trip.getId());
                        if(isRemove) {
                            Toast.makeText(getContext(), "remove successfully", Toast.LENGTH_LONG).show();
                        }
                        DialogConfirmDelete.dismiss();
                        Navigation.findNavController(getView()).navigate(R.id.listTrips);
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camintent, CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == getActivity().RESULT_OK) {
            if(requestCode == 1) {
                uri = data.getData();
                imageTripUpdate.setImageURI(uri);
                imageTrip.setImageURI(uri);
            } else {
                imageTrip.setImageURI(uri);
                imageTripUpdate.setImageURI(uri);
            }
        }
    }

    private void updateDetail (Trip trip) {
        title.setText(trip.getName());
        name.setText(trip.getName());
        description.setText(trip.getDescription());
        destination.setText(trip.getDestination());
        date.setText(trip.getDate());
        isRisk.setText(trip.getIsRisk() == 0 ? "No": "Yes");
        predictedAmount.setText("$" + trip.getPredictedAmount());
        memberCount.setText("" + trip.getMemberCount());
        if(!TextUtils.isEmpty(trip.getPicture())) {
            imageTrip.setImageURI(Uri.parse(trip.getPicture()));
        }
    }
}