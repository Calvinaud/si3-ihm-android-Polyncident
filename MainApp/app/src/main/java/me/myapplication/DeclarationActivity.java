package me.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;

public class DeclarationActivity extends Activity {

    //GUI components
    private Spinner typeSpinner;
    private Spinner locationSpinner;
    private SeekBar importanceSeekBar;
    private Button submitButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView typeLabel;
    private TextView locationLabel;

    private View[] collapsibleViews;
    private final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 3;

    private String mCurrentPhotoPath="";




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_declaration);

        initElementReferences();
        initElementListeners();

        fillTypes();
        fillDeclarations();
        Button btnGalery = findViewById(R.id.btnGalery);
        btnGalery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/* video/*");
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    Log.i("path",mCurrentPhotoPath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                startActivityForResult(intent,PICK_IMAGE);

            }
        });

        Button btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        Log.i("path",mCurrentPhotoPath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        Button btnVideo = findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }

            }
        });
    }

    private void initElementReferences(){
        this.locationSpinner = findViewById(R.id.declaration_location_spinner);
        this.typeSpinner = findViewById(R.id.declaration_type_spinner);
        this.importanceSeekBar = findViewById(R.id.declaration_importance_seekBar);
        this.submitButton = findViewById(R.id.declaration_submit_button);
        this.titleEditText = findViewById(R.id.declaration_title_input);
        this.descriptionEditText = findViewById(R.id.declaration_description_input);
        this.typeLabel = findViewById(R.id.declaration_type_label);
        this.locationLabel = findViewById(R.id.declaration_location_label);

        this.collapsibleViews = new View[]{locationLabel, typeLabel, locationSpinner,
                typeSpinner, titleEditText, importanceSeekBar};
    }

    private void initElementListeners(){
        this.importanceSeekBar.setMax(Importance.values().length);
        this.submitButton.setOnClickListener(new me.myapplication.DeclarationActivity.SubmissionListener());
        this.descriptionEditText.setOnFocusChangeListener(new me.myapplication.DeclarationActivity.DescriptionListener());
    }

    private void fillTypes(){
        ArrayAdapter<String> types = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item,
                IncidentDBHelper.getSingleton().getTypes());
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.typeSpinner.setAdapter(types);
    }

    private void fillDeclarations(){
        ArrayAdapter<String> locations = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item,
                IncidentDBHelper.getSingleton().getLocations());
        locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.locationSpinner.setAdapter(locations);
    }

    private class DescriptionListener implements EditText.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {

            if(hasFocus){
                for (View collapsibleView : collapsibleViews)
                    collapsibleView.setVisibility(View.GONE);
            }
            else{
                Logger.getAnonymousLogger().log(Level.WARNING, "lol");

                for (View collapsibleView : collapsibleViews)
                    collapsibleView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class SubmissionListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(titleEditText.getText().toString().equals("")
                    || descriptionEditText.getText().toString().equals(""))
                return;

            IncidentDBHelper.getSingleton()
                    .insertIncident(0, locationSpinner.getSelectedItemPosition()+1,
                            typeSpinner.getSelectedItemPosition()+1,importanceSeekBar.getProgress(),
                            titleEditText.getText().toString(), descriptionEditText.getText().toString(),
                            mCurrentPhotoPath, 0
                    );
            IncidentDBHelper.getSingleton().logIncidents();

            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView mImageView = findViewById(R.id.imageView4);
        VideoView mVideoView = findViewById(R.id.videoView);

        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedMediaUri);
                        mImageView.setImageBitmap(bm);
                        mImageView.setVisibility(View.VISIBLE);
                        mVideoView.setVisibility(View.INVISIBLE);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else  if (selectedMediaUri.toString().contains("video")) {
                    mVideoView.setVideoURI(selectedMediaUri);
                    mImageView.setVisibility(View.INVISIBLE);
                    mVideoView.setVisibility(View.VISIBLE);
                }

            }

            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Log.i("uhjk", "ujhnk");
                mImageView.setImageBitmap(imageBitmap);
                mImageView.setVisibility(View.VISIBLE);
                mVideoView.setVisibility(View.INVISIBLE);
            }

            else if (requestCode == REQUEST_VIDEO_CAPTURE){
                Uri videoUri = data.getData();
                mVideoView.setVideoURI(videoUri);
                mImageView.setVisibility(View.INVISIBLE);
                mVideoView.setVisibility(View.VISIBLE);

            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
