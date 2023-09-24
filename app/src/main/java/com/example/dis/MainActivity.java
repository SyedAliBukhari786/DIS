
package com.example.dis;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dis.BuildConfig;
import com.example.dis.R;
import com.example.dis.inspectoractivity;
import com.example.dis.patientactivity;
import com.example.dis.pharmacyactivity;
import com.example.dis.trackingactivty;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class MainActivity extends AppCompatActivity {

    private CardView patient, pharmacybtn, inspectors, tracking;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);



        // Initialize Firebase Remote Config
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // Adjust this interval as needed
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        // Define the keys for remote config values
        String newVersionCodeKey = "new_version_code";

        // Fetch remote config values
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            int newVersionCode = (int) mFirebaseRemoteConfig.getLong(newVersionCodeKey);
                            int currentVersionCode = BuildConfig.VERSION_CODE; // Assuming you have version code in your app's build.gradle

                            // Check if a new version is available
                            if (newVersionCode > currentVersionCode) {
                                showUpdateDialog();
                            }
                        }
                    }
                });

        // Rest of your code for initializing buttons and click listeners
        patient = findViewById(R.id.button2);
        pharmacybtn = findViewById(R.id.phharmacybtn);
        inspectors = findViewById(R.id.cardView7);
        tracking = findViewById(R.id.cardView8);

        // Set click listeners for buttons
        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, trackingactivty.class));
            }
        });

        inspectors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, inspectoractivity.class));
            }
        });

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, patientactivity.class));
            }
        });

        pharmacybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, pharmacyactivity.class));
            }
        });
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Required");
        builder.setMessage("A new version of the app is available. Please update to the latest version.");
        builder.setPositiveButton("Update", (dialog, which) -> {
            // Open the Play Store for your app
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?id=drap.dsr.dis"));
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle cancel action if needed
        });
        builder.setCancelable(false);
        builder.show();
    }
}
