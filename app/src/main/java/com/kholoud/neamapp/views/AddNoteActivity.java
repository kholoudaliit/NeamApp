package com.kholoud.neamapp.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.util.StringUtil;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.kholoud.neamapp.R;
import com.kholoud.neamapp.data.NeamNote;
import com.kholoud.neamapp.viewsmodels.NeamNotesVM;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class AddNoteActivity extends AppCompatActivity {
    @BindView(R.id.add_image)
    ImageButton addImage;
    @BindView(R.id.note_image)
    ImageView noteImage;
    @BindView(R.id.add_note)
    Button addNote;
    @BindView(R.id.delete_note)
    Button deleteNote;
    @BindView(R.id.note_content_add)
    EditText noteContent;
    final RxPermissions rxPermissions = new RxPermissions(this);
    String imageURI = "";
    private NeamNotesVM mNoteViewModel;
    AdView mAdView;
    NeamNote selectedNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        //check Extra and get info
        Intent starterActivity = getIntent();
        if (starterActivity.hasExtra("note")) {
            selectedNote = (NeamNote) getIntent().getSerializableExtra("note");
            setTitle(R.string.edit_label);
            addImage.setImageBitmap(BitmapFactory.decodeFile(selectedNote.getNeamNoteImage()));
            noteContent.setText(selectedNote.getDescription());
            addNote.setText(getString(R.string.edit));
            deleteNote.setVisibility(View.VISIBLE);
        }
        //initalize Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //Ads stuff
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //VM initialization
        mNoteViewModel = new ViewModelProviders().of(this).get(NeamNotesVM.class);

        //Click Listener
        addImage.setOnClickListener(v -> selectImage(AddNoteActivity.this));
        addNote.setOnClickListener(v -> addNote());
        deleteNote.setOnClickListener(v -> {
            deleteNote(selectedNote);
        });

    }


    public void deleteNote(NeamNote note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(getString(R.string.confirmation_title));
        builder.setIcon(R.drawable.ic_empty_list);
        builder.setPositiveButton(getString(R.string.yes), (dialog, id) -> {
            mNoteViewModel.delete(note);
            dialog.dismiss();
            finish();
        });
        builder.setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();

    }

    //Add New Note
    public void addNote() {

        if (getIntent().hasExtra("note")) {
            //EDIT NOTE
            selectedNote.setDescription(noteContent.getText().toString());
            selectedNote.setNeamNoteImage(imageURI);
            mNoteViewModel.update(selectedNote);
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.note_updated_sucessfully), Snackbar.LENGTH_LONG).show();
            finish();
            return;
        } else {
                // ADD NOTE
            if (TextUtils.isEmpty(noteContent.getText())) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.note_validation_content), Snackbar.LENGTH_LONG).show();
                return;
            }
            NeamNote note = new NeamNote();
            note.setDescription(noteContent.getText().toString());
            Date currentTime = Calendar.getInstance().getTime();
            final Calendar cal = Calendar.getInstance();
            cal.setTime(currentTime);
            note.setCreated_at(cal);
            note.setNeamNoteImage(imageURI);
            mNoteViewModel.insert(note);
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.added_note_msg), Snackbar.LENGTH_LONG).show();
            finish();
        }

    }

    // Ask the user to choose the image picker options
    private void selectImage(Context context) {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_gallery), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.builder_title));

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {
                    // Ask the permission
                    rxPermissions
                            .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(granted -> {
                                if (granted) { // Always true pre-M
                                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, 0);
                                } else {

                                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.permission_denied), Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            });

                } else if (options[item].equals(getString(R.string.choose_gallery))) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Handle the Picker result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        noteImage.setImageBitmap(selectedImage);

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(getApplicationContext(), selectedImage);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        imageURI = finalFile.getAbsolutePath();

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        noteImage.setImageURI(null);
                        noteImage.setImageURI(selectedImage);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                File imgFile = new File(picturePath);
                                imageURI = imgFile.getAbsolutePath();
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


    //Helper methods to get URI
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "NeamNote" + Calendar.getInstance().getTimeInMillis(), null);
        return Uri.parse(path);
    }

    //getRealPathFromURI Method
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

}
