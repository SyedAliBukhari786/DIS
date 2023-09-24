package com.example.dis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class trackingactivty extends AppCompatActivity {

    ImageView imageView18;
    Button track;
    CardView trackingdetails;
    EditText Id;
    ProgressBar progressBar3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView date,number,status,namae,textView37;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackingactivty);

        imageView18 = (ImageView) findViewById(R.id.imageView18);
        trackingdetails = (CardView) findViewById(R.id.trackingdetails);
        track = (Button) findViewById(R.id.button7);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        Id = (EditText) findViewById(R.id.editTextText2);
        date=(TextView) findViewById(R.id.textView33) ;
     number=(TextView) findViewById(R.id.textView35) ;
     status=(TextView) findViewById(R.id.textView30) ;
      namae=(TextView) findViewById(R.id.textView34) ;
      textView37=(TextView) findViewById(R.id.textView37);

      number.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              Intent intent = new Intent(Intent.ACTION_DIAL);
              intent.setData(Uri.parse("tel:" + number.getText().toString()));

              // Check if the device supports the dial-up action
              PackageManager packageManager = getPackageManager();
              if (intent.resolveActivity(packageManager) != null) {
                  // Start the intent
                  startActivity(intent);
              } else {
                  // Handle the case where the dial-up action is not supported
                  Toast.makeText(trackingactivty.this, "Dial-up is not supported on this device.", Toast.LENGTH_SHORT).show();
              }



          }
      });





        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = Id.getText().toString().trim();
                if (!TextUtils.isEmpty(ID)) {

                    progressBar3.setVisibility(View.VISIBLE);

                    db.collection("Complain").document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                textView37.setText(textView37.getText()+" "+ID);
                                trackingdetails.setVisibility(View.VISIBLE);

                                String Status=documentSnapshot.getString("STATUS");
                                Date timestamp = documentSnapshot.getDate("Complain_Date");

                                // Format the date as desired
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                                String formattedDate = dateFormat.format(timestamp);
                                status.setText(Status);
                                date.setText(formattedDate);

                                if (Status.equals("UNCHECKED")){
                                    progressBar3.setVisibility(View.GONE);
                                    namae.setText("NULL");
                                    number.setText("NULL");




                                }
                                else {
                                    progressBar3.setVisibility(View.VISIBLE);
                                    String inspectorid=documentSnapshot.getString("inspector_id");
                                    db.collection("DRUG_INSPECTOR").document(inspectorid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                progressBar3.setVisibility(View.GONE);
                                                namae.setText(documentSnapshot.getString("NAME"));
                                                number.setText(documentSnapshot.getString("PHONENUMBER"));


                                            }else {

                                                progressBar3.setVisibility(View.GONE);
                                                namae.setText("NULL");
                                                number.setText("NULL");



                                            }

                                        }
                                    });









                                }





                            } else {
                                trackingdetails.setVisibility(View.GONE);
                                Toast.makeText(trackingactivty.this, "No Record found ", Toast.LENGTH_SHORT).show();
                                progressBar3.setVisibility(View.GONE);
                            }

                        }
                    });


                } else {

                    progressBar3.setVisibility(View.GONE);
                    Toast.makeText(trackingactivty.this, "ENTER TRACKING ID", Toast.LENGTH_SHORT).show();
                }


            }
        });
        imageView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackingdetails.setVisibility(View.GONE);
                namae.setText("");
                status.setText("");
                number.setText("");
                date.setText("");


            }
        });


    }
}