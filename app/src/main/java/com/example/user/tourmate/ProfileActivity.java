package com.example.user.tourmate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {
    private EditText nameEt,emailEt,ageEt,addressEt;
    private TextView nameTv,emailTv,ageTv,addressTv;
    private Button addbtn;
    private String name,email,age,address;
    private DatabaseReference mainDB;
    private FirebaseAuth mAuth;
    private String userId;
    private ImageView profileImage,showProfileImage;
    private String profileImageUrlLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profileImageFromCameraIvId);
        nameEt = findViewById(R.id.profileNameEt);
        emailEt = findViewById(R.id.profileEmailEt);
        ageEt = findViewById(R.id.profileAgeEt);
        addressEt = findViewById(R.id.profileAddressEt);
        addbtn = findViewById(R.id.profileAddBtn);

        nameTv = findViewById(R.id.profileNameTv);
        emailTv = findViewById(R.id.profileEmailTv);
        ageTv = findViewById(R.id.profileAgeTv);
        addressTv = findViewById(R.id.profileAddressTv);
        showProfileImage = findViewById(R.id.profileImageIdIv);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mainDB = FirebaseDatabase.getInstance().getReference();


        mainDB.child("Users").child(userId).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    ProfileClass profileClass = dataSnapshot.getValue(ProfileClass.class);
                    Picasso.get().load(profileClass.getProfileImage()).into(showProfileImage);
                    nameTv.setText(profileClass.getName());
                    emailTv.setText(profileClass.getEmail());
                    ageTv.setText(profileClass.getAge());
                    addressTv.setText(profileClass.getAddress());


                }
                else {
                    Toast.makeText(ProfileActivity.this, "No data exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString();
                email = emailEt.getText().toString();
                age = ageEt.getText().toString();
                address = addressEt.getText().toString();
                final ProgressDialog progressDialog1 = new ProgressDialog(ProfileActivity.this);
                progressDialog1.setMessage("Please wait...");
                progressDialog1.show();
                progressDialog1.setCanceledOnTouchOutside(false);
                DatabaseReference newEvenRef = mainDB.child("Users").child(userId).child("Profile");
                ProfileClass profileClass = new ProfileClass(profileImageUrlLink,name,email,age,address);
                newEvenRef.setValue(profileClass).addOnSuccessListener(new OnSuccessListener <Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog1.dismiss();
                    }
                });
                Intent intent  = new Intent(ProfileActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1  && resultCode == Activity.RESULT_OK) {

            try{
                if (requestCode == 1 && resultCode == Activity.RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] uri = byteArrayOutputStream.toByteArray();

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures").child("photos_"+System.currentTimeMillis());
                    storageReference.putBytes(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                 profileImageUrlLink = uri.toString();
                                }
                            });
                        }
                    });

                    Toast.makeText(this, "Image Added", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }
}
