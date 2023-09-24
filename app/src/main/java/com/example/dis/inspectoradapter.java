package com.example.dis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

import java.util.jar.Attributes;

public class inspectoradapter extends FirestoreRecyclerAdapter<inspectorclass, inspectoradapter.inspectorholer> {


    public inspectoradapter(@NonNull FirestoreRecyclerOptions<inspectorclass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull inspectorholer holder, int position, @NonNull inspectorclass model) {
        holder.Name.setText(model.getNAME());
        holder.city.setText(model.getCITY());
        holder.designation.setText(model.getDESIGNATION());
        holder.phonenumber.setText(model.getPHONENUMBER());

        holder.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = holder.phonenumber.getText().toString();
                Context context = view.getContext();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Dialer app not found", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @NonNull
    @Override
    public inspectorholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.druginspectorlayout, parent, false);
        return new inspectorholer(v);
    }

    class inspectorholer extends RecyclerView.ViewHolder {
        TextView Name, city, designation, phonenumber;
       Button button6;


        public inspectorholer(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.textView2);
            city = itemView.findViewById(R.id.textView21);
            designation = itemView.findViewById(R.id.textView24);
            phonenumber = itemView.findViewById(R.id.textView23);
            button6=itemView.findViewById(R.id.button6);


        }
    }
}
