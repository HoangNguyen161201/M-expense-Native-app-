package greenwich.edu.vn.ExpenseManageApp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import greenwich.edu.vn.ExpenseManageApp.R;
import greenwich.edu.vn.ExpenseManageApp.helper.DialogHelper;
import greenwich.edu.vn.ExpenseManageApp.helper.TimeDate;
import greenwich.edu.vn.ExpenseManageApp.helper.Validation;
import greenwich.edu.vn.ExpenseManageApp.trip.Trip;
import greenwich.edu.vn.ExpenseManageApp.trip.TripService;

public class AddTripsFragment extends Fragment {
    // element and another to add new trip
    Button addButton;
    ImageView imageView;
    EditText name,
             description,
             destination,
             date,
             memberCount,
             predictedAmount;
    int isRisk = 0;

    // element and another to take picture
    Uri uri = Uri.parse("");
    Button takePictureBtn, pictureFromFolderBtn;
    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    private final int SELECT_IMAGE_CODE = 1;

    public AddTripsFragment() {}

    public static AddTripsFragment newInstance(String param1, String param2) {
        AddTripsFragment fragment = new AddTripsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_trips, container, false);
        addButton = rootView.findViewById(R.id.addButton);
        name = rootView.findViewById(R.id.addName);
        description = rootView.findViewById(R.id.addDescription);
        destination = rootView.findViewById(R.id.addDestination);
        date = rootView.findViewById(R.id.addDate);
        memberCount = rootView.findViewById(R.id.addMemberCount);
        predictedAmount = rootView.findViewById(R.id.addPredictedAmount);
        Switch switchIsRisk = rootView.findViewById(R.id.addIsRisk);

        switchIsRisk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRisk =  b ? 1: 0;
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<EditText> editTexts = new ArrayList<>();
                // check valid
                editTexts.add(name);
                editTexts.add(destination);
                editTexts.add(date);
                Validation.checkEmpty(editTexts);
                if( Validation.isError) {
                    return;
                }
                Validation.checkDate(date);
                if( Validation.isError) {
                    return;
                }

                DialogHelper dialogConfirmConfig = new DialogHelper(
                                                                getContext(),
                                                                getLayoutInflater(),
                                                                R.layout.dialog_confirm_add
                                                             );
                View viewConfirm = dialogConfirmConfig.getView();
                Dialog DialogConfirm = dialogConfirmConfig.getDialog();
                DialogConfirm.show();

                TextView nameConfirm = viewConfirm.findViewById(R.id.confirmName);
                TextView descriptionConfirm = viewConfirm.findViewById(R.id.confirmDescription);
                TextView destinationConfirm = viewConfirm.findViewById(R.id.confirmDestination);
                TextView dateConfirm = viewConfirm.findViewById(R.id.confirmDate);
                TextView isRiskConfirm = viewConfirm.findViewById(R.id.confirmIsRisk);
                TextView memberCountConfirm = viewConfirm.findViewById(R.id.confirmMemberCount);
                TextView predictedAmountConfirm = viewConfirm.findViewById(R.id.confirmPredictedAmount);
                Button confirmButton = viewConfirm.findViewById(R.id.confirm);
                Button editButton = viewConfirm.findViewById(R.id.edit);

                nameConfirm.setText(name.getText().toString());
                descriptionConfirm.setText(description.getText().toString());
                destinationConfirm.setText(destination.getText().toString());
                dateConfirm.setText(date.getText().toString());
                isRiskConfirm.setText(isRisk == 1 ? "Yes": "No");
                memberCountConfirm.setText(memberCount.getText().toString());
                predictedAmountConfirm.setText(predictedAmount.getText().toString());

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogConfirm.dismiss();
                    }
                });
                viewConfirm.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogConfirm.dismiss();
                    }
                });


                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Trip trip = new Trip(
                                name.getText().toString(),
                                description.getText().toString(),
                                destination.getText().toString(),
                                date.getText().toString(),
                                0,
                                isRisk,
                                uri.toString(),
                                Integer.parseInt(memberCount.getText().toString()),
                                Integer.parseInt(predictedAmount.getText().toString())
                        );
                        boolean isAddSuccess = TripService.insert(getContext(), trip);
                        if(isAddSuccess) {
                            Toast.makeText(getContext(), "Add successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Add failed", Toast.LENGTH_LONG).show();
                        }
                        DialogConfirm.dismiss();
                    }
                });
            }
        });

        // open calendar to add date
        TimeDate.openDate(rootView, R.id.addDate, getContext());

        // take picture
        takePictureBtn = rootView.findViewById(R.id.takePicture);
        pictureFromFolderBtn = rootView.findViewById(R.id.getPicture);
        imageView = rootView.findViewById(R.id.imageTrip);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ActivityCompat.checkSelfPermission (rootView.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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

        pictureFromFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent();
                iGallery.setType("image/*");
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iGallery, "Title"), SELECT_IMAGE_CODE);
            }
        });

        return rootView;
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
                imageView.setImageURI(uri);
            } else {
                imageView.setImageURI(uri);
            }
        }
    }
}