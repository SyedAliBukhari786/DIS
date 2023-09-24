package com.example.dis;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class patientactivity extends AppCompatActivity {
    private EditText nameofDrug, dosageForm, remarks;
    private EditText fullname, contact, cnic, areaEntering, otpEditText;
    private Button submit, useCurrentLoc, submitButton, verifyButton;
    private ImageView cancelCurrentLocation;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView latitudeTextView, longitudeTextView, addressDisplay, cityTextView, textAddress;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String city, latitude, longitude, address, Locations, drugName, dosage, remark, patientContact, cnicNumber, patientFullName;
    private String verificationId; // To store the verification ID for OTP verification
    private static final int REQUEST_CODE = 100;

    private ConstraintLayout testLayout, otpLayout;
    private ScrollView mainView;
    private Spinner spinner;
    private Geocoder geocoder;
    private ArrayAdapter<CharSequence> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientactivity);

        testLayout = findViewById(R.id.testtlayout22);
        otpLayout = findViewById(R.id.otplayout);
        mainView = findViewById(R.id.scrollView2);

        areaEntering = findViewById(R.id.editTextText22);
        spinner = findViewById(R.id.spinner22);
        adapter = ArrayAdapter.createFromResource(this, R.array.pakistan_cities_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        submitButton = findViewById(R.id.button22);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city.equals("Select")) {
                    Toast.makeText(patientactivity.this, "Select City", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(areaEntering.getText().toString().trim())) {
                    areaEntering.setError("Enter Area");
                } else {
                    address = "Pakistan" + " " + city + " " + areaEntering.getText().toString().trim();
                    geocoder = new Geocoder(patientactivity.this);

                    try {
                        List<Address> addresses = geocoder.getFromLocationName(address, 1);

                        if (addresses.size() > 0) {
                            double latitudeee = addresses.get(0).getLatitude();
                            double longitudeee = addresses.get(0).getLongitude();
                            Locations = latitudeee + "," + longitudeee;
                            submitComplaint();
                        } else {
                            Toast.makeText(patientactivity.this, "Enter a Valid Area", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        nameofDrug = findViewById(R.id.nameofdrug);
        dosageForm = findViewById(R.id.dosageform);
        remarks = findViewById(R.id.remarks);
        fullname = findViewById(R.id.Fullname);
        contact = findViewById(R.id.contact);
        cnic = findViewById(R.id.cnic);
        submit = findViewById(R.id.sumbit);
        latitudeTextView = findViewById(R.id.nooooo2);
        longitudeTextView = findViewById(R.id.yesssss2);
        addressDisplay = findViewById(R.id.addressdisplay);
        useCurrentLoc = findViewById(R.id.button3);
        cityTextView = findViewById(R.id.okkkkkkkkk2);
        textAddress = findViewById(R.id.textaddress);
        cancelCurrentLocation = findViewById(R.id.imageView5);
        progressBar = findViewById(R.id.progressBar);
        otpEditText = findViewById(R.id.editTextText3); // OTP input field
        verifyButton = findViewById(R.id.button8); // Verify button

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        cancelCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCurrentLocationFunction();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drugName = nameofDrug.getText().toString().trim();
                dosage = dosageForm.getText().toString().trim();
                remark = remarks.getText().toString().trim();
                patientContact = contact.getText().toString().trim();
                cnicNumber = cnic.getText().toString().trim();
                patientFullName = fullname.getText().toString().trim();

                if (TextUtils.isEmpty(drugName) || TextUtils.isEmpty(patientFullName) || TextUtils.isEmpty(dosage) || TextUtils.isEmpty(remark) || TextUtils.isEmpty(patientContact) || TextUtils.isEmpty(cnicNumber) || TextUtils.isEmpty(address)) {
                    Toast.makeText(patientactivity.this, "Complete all credentials", Toast.LENGTH_SHORT).show();
                } else if (patientContact.length() < 11) {
                    contact.setError("Enter a valid phone number");
                } else if (cnicNumber.length() != 13) {
                    cnic.setError("Enter a valid CNIC");
                } else {
                    //sendOtp();
                    submitComplaint();
                }
            }
        });

        useCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getLastLocation();
            }
        });

        // Verify button click listener
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredOtp = otpEditText.getText().toString().trim(); // Get OTP from EditText
                //verifyOtpWithFirebase(enteredOtp);
            }
        });
    }

  /*  private void verifyOtpWithFirebase(String enteredOtp) {
        // Get the verification ID from your previous Firebase Phone Authentication process.
        // You should have stored this verification ID when sending the OTP.
        // Replace "YOUR_VERIFICATION_ID" with your actual verification ID
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enteredOtp);

        // Sign in with the PhoneAuthCredential.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // The OTP verification is successful.
                            submitComplaint(); // Call your submitComplaint method
                        } else {
                            // The OTP verification has failed.
                            // Display an error message or handle incorrect OTP
                            Toast.makeText(patientactivity.this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
*/
   /* private void sendOtp() {
        // Send OTP logic here
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                patientContact,
                60, // Timeout duration
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // OTP verification is completed
                        mainView.setVisibility(View.GONE); // Hide the main view
                        otpLayout.setVisibility(View.VISIBLE); // Show the OTP input layout
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Handle OTP verification failure
                        Toast.makeText(patientactivity.this, "Verification failed."+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s; // Store the verification ID for later use
                    }
                }
        );
    }*/

    private void submitComplaint() {
        // Database logic here
      //  otpLayout.setVisibility(View.GONE);
        //mainView.setVisibility(View.VISIBLE);
        Date currentDate = new Date();
        progressBar.setVisibility(View.VISIBLE);

        String[] words = drugName.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String remainingLetters = word.substring(1).toLowerCase();
                result.append(firstLetter).append(remainingLetters).append(" ");
            }
        }

        String convertedText = result.toString().trim();

        Map<String, Object> complaint = new HashMap<>();
        complaint.put("TYPE", "PATIENT");
        complaint.put("Name_of_Drug", convertedText);
        complaint.put("Dosage_Form", dosage);
        complaint.put("Remarks", remark);
        complaint.put("Contact", patientContact);
        complaint.put("STATUS", "UNCHECKED");
        complaint.put("Name", patientFullName);
        complaint.put("ID", cnicNumber);
        complaint.put("Location", Locations);
        complaint.put("Address", address);
        complaint.put("City", city);
        complaint.put("inspector_id", "NULL");
        complaint.put("Complain_Date", currentDate);

        db.collection("Complain").add(complaint)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            nameofDrug.setText("");
                            dosageForm.setText("");
                            remarks.setText("");
                            fullname.setText("");
                            contact.setText("");
                            cnic.setText("");

                            testLayout.setVisibility(View.GONE);
                            useCurrentLoc.setVisibility(View.VISIBLE);
                            cancelCurrentLocationFunction();

                            String documentId = task.getResult().getId();

                            AlertDialog.Builder builder = new AlertDialog.Builder(patientactivity.this);
                            builder.setTitle("Tracking ID");
                            builder.setMessage("Copy Tracking ID: " + documentId);
                            builder.setPositiveButton("Copy", (dialog, which) -> {
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Encrypted Document ID", documentId);
                                clipboardManager.setPrimaryClip(clipData);
                            });
                            builder.setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            });
                            builder.show();

                            Toast.makeText(patientactivity.this, "Complaint Submitted", Toast.LENGTH_SHORT).show();

                            // Update the complaint counter
                            Map<String, Object> counter = new HashMap<>();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(currentDate);
                            long complaints = 1;
                            long year = cal.get(Calendar.YEAR);
                            long month = cal.get(Calendar.MONTH) + 1;
                            db.collection("Complain_Counter")
                                    .whereEqualTo("Year", year)
                                    .whereEqualTo("Month", month)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (queryDocumentSnapshots.isEmpty()) {
                                                counter.put("Year", year);
                                                counter.put("Month", month);
                                                counter.put("Complains", complaints);
                                                db.collection("Complain_Counter").add(counter);
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                long currentComplains = queryDocumentSnapshots.getDocuments().get(0).getLong("Complains");
                                                counter.put("Complains", currentComplains + complaints);
                                                queryDocumentSnapshots.getDocuments().get(0).getReference().update(counter);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            startActivity(new Intent(patientactivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(patientactivity.this, "Error: Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(patientactivity.this, Locale.getDefault());

                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitudeTextView.setText("" + addresses.get(0).getLatitude());
                            longitudeTextView.setText("" + addresses.get(0).getLongitude());
                            addressDisplay.setText("" + addresses.get(0).getAddressLine(0));
                            cityTextView.setText("" + addresses.get(0).getLocality());
                            textAddress.setVisibility(View.VISIBLE);
                            addressDisplay.setVisibility(View.VISIBLE);
                            cancelCurrentLocation.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            latitude = latitudeTextView.getText().toString().trim();
                            longitude = longitudeTextView.getText().toString().trim();
                             Locations= latitude + "," + longitude;
                            address = addressDisplay.getText().toString().trim();
                            city = cityTextView.getText().toString().trim();
                         //   Toast.makeText(patien.this, location, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } catch (IOException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(patientactivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void cancelCurrentLocationFunction() {
        cancelCurrentLocation.setVisibility(View.GONE);
        addressDisplay.setText(null);
        addressDisplay.setVisibility(View.GONE);
        textAddress.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(patientactivity.this, "Please provide the Requested Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
