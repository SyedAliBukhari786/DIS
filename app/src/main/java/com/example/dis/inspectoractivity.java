package com.example.dis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class inspectoractivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    Spinner spinner2;
    String city;
    RecyclerView recyclerView;
    ArrayAdapter<CharSequence> adapter;
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL, false);;
  private  inspectoradapter inspectoradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspectoractivity);
        recyclerView=(RecyclerView) findViewById(R.id.recc);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(this, R.array.pakistan_cities_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
                setupRecyclerview(city);
                inspectoradapter.startListening();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


    }

    private void setupRecyclerview(String city) {


            Query query = db.collection("DRUG_INSPECTOR").whereEqualTo("CITY",city);
            FirestoreRecyclerOptions<inspectorclass> options = new FirestoreRecyclerOptions.Builder<inspectorclass>().
                    setQuery(query, inspectorclass.class).build();
           inspectoradapter = new inspectoradapter(options);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(inspectoradapter);





    }
}