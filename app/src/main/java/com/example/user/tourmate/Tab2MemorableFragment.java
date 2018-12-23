package com.example.user.tourmate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.tourmate.forecast.List;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Tab2MemorableFragment extends Fragment {
    private  String eventIdImage;
    private ImageView memorableImageFromCameraBtn,memorableImageFromGalleryBtn;
    private EditText memorableCaption;
    private RecyclerView recyclerView;
    private Button uploadBtn;
    private static final int REQUEST_CODE_FOR_CAMERA = 1;
    private static final int REQUEST_CODE_FOR_GALLARY = 2;

    private DatabaseReference mainDB;
    private FirebaseAuth mAuth;
    private String userId;
    private String downLoadImageUrlLink;
    private String caption;
   private java.util.List<MemorablePlaceClass> memorableList;
    private MemorablePlaceAdapter memorablePlaceAdapter;


    @SuppressLint("ValidFragment")
    public Tab2MemorableFragment(String eventIdImage) {
        // Required empty public constructor
        this.eventIdImage = eventIdImage;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab2_memorable, container, false);
        Toast.makeText(getContext(), eventIdImage, Toast.LENGTH_SHORT).show();
        recyclerView = view.findViewById(R.id.showMemorableRecycleView);
        memorableImageFromCameraBtn = view.findViewById(R.id.memorableImageFromCameraIvId);
        memorableImageFromGalleryBtn = view.findViewById(R.id.memorableImageFromGalleryIvId);
        memorableCaption = view.findViewById(R.id.memorableCaptionEtId);
        uploadBtn = view.findViewById(R.id.uploadBtn);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mainDB = FirebaseDatabase.getInstance().getReference();

        memorableList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        memorablePlaceAdapter = new MemorablePlaceAdapter(getActivity(),memorableList);
        recyclerView.setAdapter(memorablePlaceAdapter);



        mainDB.child("Users").child(userId).child("Event").child(eventIdImage).child("MemorablePlace").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for(DataSnapshot child: dataSnapshot.getChildren()) {

                        MemorablePlaceClass memorablePlaceClass = child.getValue(MemorablePlaceClass.class);

                        memorableList.add(memorablePlaceClass);
                        memorablePlaceAdapter.notifyDataSetChanged();
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        memorableImageFromCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                caption = memorableCaption.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                DatabaseReference databaseReference =mainDB.child("Users").child(userId).child("Event").child(eventIdImage).child("MemorablePlace").push();
                MemorablePlaceClass memorablePlaceClass = new MemorablePlaceClass(downLoadImageUrlLink,caption);
                databaseReference.setValue(memorablePlaceClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        return  view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1  && resultCode == Activity.RESULT_OK) {

            try{
                if (requestCode == 1 && resultCode == Activity.RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] uri = byteArrayOutputStream.toByteArray();

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("MemorablePhotos").child("photos_"+System.currentTimeMillis());
                    storageReference.putBytes(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downLoadImageUrlLink = uri.toString();
                                }
                            });
                        }
                    });

                    Toast.makeText(getActivity(), "Picture added", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){

                Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }
}
