package com.example.dis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class pharmacyactivity extends AppCompatActivity {
    EditText nofdrug, dform, rmarks;
    EditText regname, Contact, dsln, areaaentering;
    Button sumbit2, usecurrentloc, button5, submitt;
    ImageView cancelcurrentlocation;
    ProgressBar progressBar2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    TextView lattitudetextview, logitudetextview, addressdisplay2, cityyy, textaddress;


    // location
    FusedLocationProviderClient fusedLocationProviderClient;
    String City, latitude, longitude, addressssc, Locationss, noofdrug, dfoorm, rmaark, contaact, dslnn, regnaame;


    private static final int REQUEST_CODE = 100;


    ConstraintLayout testtlayout;
    ImageView cancelcarddd;

    Spinner spinner;

    ArrayAdapter<CharSequence> adapter;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacyactivity);
        areaaentering = (EditText) findViewById(R.id.editTextText);
        testtlayout = (ConstraintLayout) findViewById(R.id.testtlayout);
        submitt = (Button) findViewById(R.id.button);
        submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (City.equals("Select")) {

                    Toast.makeText(pharmacyactivity.this, "Select City", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(areaaentering.getText().toString().trim())) {
                    areaaentering.setError("Enter Area");
                } else {
                    addressssc = "Pakistan" + " " + City + " " + areaaentering.getText().toString().trim();

                    geocoder = new Geocoder(pharmacyactivity.this);

                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addressssc, 1);

                        if (addresses.size() > 0) {
                            double latitudeee = addresses.get(0).getLatitude();
                            double longitudeee = addresses.get(0).getLongitude();
                            Locationss = latitudeee + "," + longitudeee;
                            submitcomplain();

                        } else {
                            Toast.makeText(pharmacyactivity.this, "Enter Valid Area", Toast.LENGTH_SHORT).show();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        cancelcarddd = (ImageView) findViewById(R.id.imageView10);
        cancelcarddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testtlayout.setVisibility(View.GONE);
                usecurrentloc.setVisibility(View.VISIBLE);
            }
        });
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noofdrug = nofdrug.getText().toString().trim();
                dfoorm = dform.getText().toString().trim();
                rmaark = rmarks.getText().toString().trim();
                contaact = Contact.getText().toString().trim();
                dslnn = dsln.getText().toString().trim();
                regnaame = regname.getText().toString().trim();
                if (TextUtils.isEmpty(noofdrug) || TextUtils.isEmpty(regnaame) || TextUtils.isEmpty(dfoorm) || TextUtils.isEmpty(rmaark) || TextUtils.isEmpty(contaact) || TextUtils.isEmpty(dslnn)) {
                    Toast.makeText(pharmacyactivity.this, "Complete all credentials", Toast.LENGTH_SHORT).show();
                } else if (contaact.length() < 11) {
                    Contact.setError("Enter valid phone number");
                } else {
                    testtlayout.setVisibility(View.VISIBLE);
                    usecurrentloc.setVisibility(View.GONE);
                }
            }
        });


        spinner = findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.pakistan_cities_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        nofdrug = (EditText) findViewById(R.id.nofdrug);

        dform = (EditText) findViewById(R.id.dofdrug);
        rmarks = (EditText) findViewById(R.id.REmarks);
        regname = (EditText) findViewById(R.id.Regname);
        Contact = (EditText) findViewById(R.id.Contact);
        dsln = (EditText) findViewById(R.id.dsln);
        sumbit2 = (Button) findViewById(R.id.sumbit2);
        lattitudetextview = (TextView) findViewById(R.id.nooooo);
        logitudetextview = (TextView) findViewById(R.id.yesssss);
        addressdisplay2 = (TextView) findViewById(R.id.addressdisplay2);
        usecurrentloc = (Button) findViewById(R.id.usecurentloc);
        cityyy = (TextView) findViewById(R.id.okkkkkkkkk);
        textaddress = (TextView) findViewById(R.id.textaddress2);
        cancelcurrentlocation = (ImageView) findViewById(R.id.imageView9);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        cancelcurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelcurrentlocationfuntion();
            }
        });


        sumbit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noofdrug = nofdrug.getText().toString().trim();
                dfoorm = dform.getText().toString().trim();
                rmaark = rmarks.getText().toString().trim();
                contaact = Contact.getText().toString().trim();
                dslnn = dsln.getText().toString().trim();
                regnaame = regname.getText().toString().trim();


                if (TextUtils.isEmpty(noofdrug) || TextUtils.isEmpty(regnaame) || TextUtils.isEmpty(dfoorm) || TextUtils.isEmpty(rmaark) || TextUtils.isEmpty(contaact) || TextUtils.isEmpty(dslnn) || TextUtils.isEmpty(addressssc)) {

                    Toast.makeText(pharmacyactivity.this, "Complete all credentials", Toast.LENGTH_SHORT).show();
                } else if (contaact.length() < 11) {
                    Contact.setError("Enter valid phone number");
                } else {
                    submitcomplain();
                }

            }
        });

        usecurrentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                getLastLocation();
            }
        });


    }


    private void submitcomplain() {

        if (TextUtils.isEmpty(Locationss)) {
            Toast.makeText(this, "Enter Valid Area", Toast.LENGTH_SHORT).show();

        } else {
            progressBar2.setVisibility(View.VISIBLE);
            //  data baseee
            Date d = new Date();


            String input = noofdrug;
            String[] words = input.split("\\s+");

            StringBuilder result = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    String firstLetter = word.substring(0, 1).toUpperCase();
                    String remainingLetters = word.substring(1).toLowerCase();
                    result.append(firstLetter).append(remainingLetters).append(" ");
                }
            }

            String convertedText = result.toString().trim();


            Map<String, Object> complain = new HashMap<>();
            complain.put("TYPE", "PHARMACY");
            complain.put("Name_of_Drug", convertedText);
            complain.put("Dosage_Form", dfoorm);
            complain.put("Remarks", rmaark);
            complain.put("Contact", contaact);
            complain.put("Name", regnaame);
            complain.put("ID", dslnn);
            complain.put("STATUS", "UNCHECKED");
            complain.put("Location", Locationss);
            complain.put("Address", addressssc);
            complain.put("City", City);
            complain.put("inspector_id", "NULL");
            complain.put("Complain_Date", d);
            db.collection("Complain").add(complain).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        nofdrug.setText("");
                        dform.setText("");
                        rmarks.setText("");
                        regname.setText("");
                        Contact.setText("");
                        dsln.setText("");
                        testtlayout.setVisibility(View.GONE);
                        usecurrentloc.setVisibility(View.VISIBLE);
                        cancelcurrentlocationfuntion();


                        String documentId = task.getResult().getId();


// Encrypt the document ID


// Display the encrypted document ID in a dialog box
                        AlertDialog.Builder builder = new AlertDialog.Builder(pharmacyactivity.this);
                        builder.setTitle(" Tracking ID");
                        builder.setMessage("Copy Tracking ID: " + documentId);
                        builder.setPositiveButton("Copy", (dialog, which) -> {
                            // Copy the encrypted document ID to clipboard or handle it as needed
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("Encrypted Document ID", documentId);
                            clipboardManager.setPrimaryClip(clipData);
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> {
                            // Cancel button clicked
                            dialog.dismiss();
                        });
                        builder.show();

                        // Decrypt the encrypted document ID back to the original document ID


                        // Encrypt the document ID


                        // Display the encrypted document ID in a dialog box


                        // Decrypt the encrypted document ID back to the original document ID
                        //    String decryptedDocumentId = EncryptionUtil.decrypt(encryptedDocumentId);


                        Toast.makeText(pharmacyactivity.this, "Complain Submitted", Toast.LENGTH_SHORT).show();
                        Map<String, Object> counter = new HashMap<>();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        long compl = 1;
                        long year = cal.get(Calendar.YEAR);
                        long month = cal.get(Calendar.MONTH) + 1; // Add 1 to get the month in numbers (January = 1)
                        System.out.println("Year: " + year);
                        System.out.println("Month: " + month);
                        db.collection("Complain_Counter").whereEqualTo("Year", year).whereEqualTo("Month", month).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    counter.put("Year", year);
                                    counter.put("Month", month);
                                    counter.put("Complains", compl);
                                    db.collection("Complain_Counter").add(counter);
                                    progressBar2.setVisibility(View.GONE);

                                } else {

                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    long currentComplains = documentSnapshot.getLong("Complains");
                                    counter.put("Complains", currentComplains + compl);
                                    documentSnapshot.getReference().update(counter);
                                    progressBar2.setVisibility(View.GONE);

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startActivity(new Intent(pharmacyactivity.this, pharmacyactivity.class));
                            }
                        });


                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(pharmacyactivity.this, "Error check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    private void getLastLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {

                        Geocoder geocoder = new Geocoder(pharmacyactivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {

                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lattitudetextview.setText("" + addresses.get(0).getLatitude());
                            logitudetextview.setText("" + addresses.get(0).getLongitude());
                            addressdisplay2.setText("" + addresses.get(0).getAddressLine(0));
                            cityyy.setText("" + addresses.get(0).getLocality());


                            textaddress.setVisibility(View.VISIBLE);
                            addressdisplay2.setVisibility(View.VISIBLE);
                            cancelcurrentlocation.setVisibility(View.VISIBLE);
                            sumbit2.setVisibility(View.VISIBLE);
                            latitude = lattitudetextview.getText().toString().trim();
                            longitude = logitudetextview.getText().toString().trim();
                            Locationss = latitude + "," + longitude;
                            addressssc = addressdisplay2.getText().toString().trim();
                            City = cityyy.getText().toString().trim();
                            Toast.makeText(pharmacyactivity.this, Locationss, Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                            ///for city getlocalcityname  getcountryname
                        } catch (IOException e) {
                            e.printStackTrace();
                            progressBar2.setVisibility(View.GONE);

                        }


                    }
                }
            });
        } else {
            progressBar2.setVisibility(View.GONE);
            askpermission();
        }
    }

    private void askpermission() {
        ActivityCompat.requestPermissions(pharmacyactivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

    }

    private void cancelcurrentlocationfuntion() {
        cancelcurrentlocation.setVisibility(View.GONE);
        addressdisplay2.setText(null);
        addressdisplay2.setVisibility(View.GONE);
        textaddress.setVisibility(View.GONE);
        sumbit2.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(pharmacyactivity.this, "Please provide the Requested Permission", Toast.LENGTH_SHORT).show();


            }
        }
    }

}