package com.example.navdrawerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    EditText name_et,age_et,gender_et;
    Button button_save;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Upload1 upload;
    String name ;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dahboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        name_et = findViewById(R.id.editText_name);
        age_et = findViewById(R.id.editText_age);
        gender_et = findViewById(R.id.editText_gender);
        upload = new Upload1();
        button_save = findViewById(R.id.button_save);
        recyclerView = findViewById(R.id.recyclerview_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = firebaseDatabase.getInstance().getReference().child("Data");


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upload.setName(name_et.getText().toString());
                upload.setAge(age_et.getText().toString());
                upload.setGender(gender_et.getText().toString());

                String id = databaseReference.push().getKey();
                databaseReference.child(id).setValue(upload);
                Toast.makeText(Dashboard.this, "Data saved", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Upload1> options =
                new FirebaseRecyclerOptions.Builder<Upload1>()
                        .setQuery(databaseReference,Upload1.class)
                        .build();


        FirebaseRecyclerAdapter<Upload1,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Upload1, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Upload1 model) {
                        holder.setData(getApplicationContext(),model.getName(),model.getAge(),model.getGender());

                        holder.setOnClickListener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemlongClick(View view, int position) {

                                name = getItem(position).getName();

                                showDeleteDataDialog(name);
                            }
                        });


                    }


                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row,parent,false);

                        return new ViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void showDeleteDataDialog(String name){
        AlertDialog .Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this Data?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Query query = databaseReference.orderByChild("name").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Dashboard.this, "Data deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public  void ClickMenu(View view){

        MainActivity.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickProfile(View view){
        MainActivity.redirectActivity(this, Profile.class);
    }
    public void ClickDashboard(View view){
        recreate();
    }
    public void ClickNewAppointment(View view){
        MainActivity.redirectActivity(this, NewAppointment.class);
    }

    public void ClickNotes(View view) {
        MainActivity.redirectActivity(this, Notes.class);
    }
        public void ClickLogout(View view){
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}