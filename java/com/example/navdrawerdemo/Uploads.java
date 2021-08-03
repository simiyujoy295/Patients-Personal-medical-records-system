package com.example.navdrawerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Uploads extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;


    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private EditText mdescriptionEditText;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        drawerLayout = findViewById(R.id.drawer_layout);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.nameEditText);
        mdescriptionEditText = findViewById(R.id.descriptionEditText);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Uploads.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK

                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);

        }

    }

    private String getFileExtension(Uri uri) {

        ContentResolver cR = getContentResolver();

        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadFile() {

        if (mImageUri != null) {

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()

                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override

                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {

                                @Override

                                public void run() {

                                    mProgressBar.setProgress(0);

                                }

                            }, 500);

                            Toast.makeText(Uploads.this, "Upload successful", Toast.LENGTH_LONG).show();

                           /* Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),

                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            String uploadId = mDatabaseRef.push().getKey();

                            mDatabaseRef.child(uploadId).setValue(upload);*/

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                            while (!urlTask.isSuccessful());

                            Uri downloadUrl = urlTask.getResult();

                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString());

                            String uploadId = mDatabaseRef.push().getKey();

                            mDatabaseRef.child(uploadId).setValue(upload);

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Uploads.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override

                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                            mProgressBar.setProgress((int) progress);

                        }

                    });

        } else {

            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();

        }

    }

    private void openImagesActivity() {

        Intent intent = new Intent(this, ImagesActivity.class);

        startActivity(intent);

    }


public  void ClickMenu(View view){

    MainActivity.openDrawer(drawerLayout);
}
    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickProfile(View view){
        MainActivity.redirectActivity(this,Profile.class);
    }
    public void ClickDashboard(View view){
        MainActivity.redirectActivity(this, MainActivity.class);
    }
    public void ClickNewAppointment(View view){
        MainActivity.redirectActivity(this, NewAppointment.class);

    }

    public void ClickUploads(View view){
       recreate();
    }
    public void ClickNotes(View view){
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

    public EditText getMdescriptionEditText() {
        return mdescriptionEditText;
    }

    public void setMdescriptionEditText(EditText mdescriptionEditText) {
        this.mdescriptionEditText = mdescriptionEditText;
    }
}

