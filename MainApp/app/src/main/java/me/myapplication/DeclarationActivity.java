package me.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.myapplication.Helpers.IncidentDBHelper;
import me.myapplication.Models.Importance;

public class DeclarationActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 3;
    private final int PICK_IMAGE = 1;

    private String mCurrentPhotoPath="";

    private int userId;

    //GUI components
    private Spinner typeSpinner;
    private TextView typeLabel;

    private Spinner locationSpinner;
    private TextView locationLabel;

    private SeekBar importanceSeekBar;
    private TextView importanceLabel;

    private EditText titleEditText;
    private EditText descriptionEditText;


    //media selection buttons
    private ImageButton btnGalery;
    private ImageButton btnCapture;
    private ImageButton btnVideo;
    private ImageButton[] mediaButtons;

    private ImageButton mediaSelectionButton;
    private ImageButton mediaDeletionButton;

    private VideoView videoView;
    private ImageView imageView;

    private Button submitButton;
    private byte[] image;

    //list of views
    private View[] collapsibleViews;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_declaration);

        initElementReferences();

        initElementListeners();

        setMediaSelectionButtonsVisibility(View.GONE);

        hideMediaViews();

        fillTypes();

        fillDeclarations();

        this.importanceSeekBar.setMax(Importance.values().length - 1);

        this.userId = getIntent().getIntExtra("userId", 0);

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
                Log.i("test",""+(photoFile!=null));
                startActivityForResult(intent,PICK_IMAGE);

            }
        });

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

        btnVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }

            }
        });

        mediaSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMediaSelectionButtonsVisibility(View.VISIBLE);
                mediaSelectionButton.setVisibility(View.GONE);
            }
        });

        mediaSelectionButton.setVisibility(View.VISIBLE);

        mediaDeletionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMediaViews();
                DeclarationActivity.this.mediaSelectionButton.setVisibility(View.VISIBLE);
            }
        });


        image = new byte[]{};
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
        this.importanceLabel = findViewById(R.id.declaration_importance_label);

        this.btnGalery = findViewById(R.id.btnGalery);
        this.btnCapture = findViewById(R.id.btnCapture);
        this.btnVideo = findViewById(R.id.btnVideo);
        this.mediaSelectionButton = findViewById(R.id.mediaSelectionButton);
        this.mediaDeletionButton = findViewById(R.id.deleteMediaButton);

        this.imageView = findViewById(R.id.imageView4);
        this.videoView = findViewById(R.id.videoView);

        this.collapsibleViews = new View[]{
                locationLabel, typeLabel, locationSpinner,
                typeSpinner, titleEditText, importanceSeekBar,
                importanceLabel, btnCapture, btnGalery, btnVideo
        };

        this.mediaButtons = new ImageButton[]{btnVideo, btnGalery, btnCapture};
    }

    private void initElementListeners(){
        this.submitButton.setOnClickListener(new me.myapplication.DeclarationActivity.SubmissionListener());
        this.descriptionEditText.setOnFocusChangeListener(new me.myapplication.DeclarationActivity.DescriptionListener());
        this.importanceSeekBar.setOnSeekBarChangeListener(new ImportanceBarListener());
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

    private void hideMediaViews(){

        this.videoView.setVisibility(View.GONE);
        this.imageView.setVisibility(View.GONE);
        this.mediaDeletionButton.setVisibility(View.GONE);

    }

    private void setMediaSelectionButtonsVisibility(int visibility){
        for(ImageButton imageButton : mediaButtons){
            imageButton.setVisibility(visibility);
        }
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
                || descriptionEditText.getText().toString().equals("")){
                Toast.makeText(view.getContext(),"Titre ou description vide",Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

// 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Confirmez-vous l'envoi ?")
                    .setTitle("Confirmation");
// 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.i("typeee",""+(typeSpinner.getSelectedItemPosition()+1));
                    IncidentDBHelper.getSingleton()
                            .insertIncident(DeclarationActivity.this.userId, locationSpinner.getSelectedItemPosition()+1,
                                    (typeSpinner.getSelectedItemPosition()+1),importanceSeekBar.getProgress()+1,
                                    titleEditText.getText().toString(), descriptionEditText.getText().toString(),
                                    image, 0, Calendar.getInstance().getTime()
                            );
                    IncidentDBHelper.getSingleton().logIncidents();

                    finish();
                }
            });
            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.show();

        }
    }

    private class ImportanceBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            DeclarationActivity.this.importanceLabel.setText(
                    Importance.getImportanceByValue(progress+1).getText()
            );
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
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

        hideMediaViews();

        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedMediaUri);
                        imageView.setImageBitmap(bm);
                        imageView.setVisibility(View.VISIBLE);
                        setMediaSelectionButtonsVisibility(View.GONE);
                        this.mediaDeletionButton.setVisibility(View.VISIBLE);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG,0,stream);
                        this.image = stream.toByteArray();

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else  if (selectedMediaUri.toString().contains("video")) {
                    videoView.setVideoURI(selectedMediaUri);
                    videoView.setVisibility(View.VISIBLE);
                    MediaController mc = new MediaController(this);
                    videoView.setMediaController(mc);
                    setMediaSelectionButtonsVisibility(View.GONE);
                    this.mediaDeletionButton.setVisibility(View.VISIBLE);

                }

            }

            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Log.i("uhjk", "ujhnk");
                imageView.setImageBitmap(imageBitmap);
                imageView.setVisibility(View.VISIBLE);
                this.mediaDeletionButton.setVisibility(View.VISIBLE);
                setMediaSelectionButtonsVisibility(View.GONE);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,0,stream);
                this.image = stream.toByteArray();

            }

            else if (requestCode == REQUEST_VIDEO_CAPTURE){
                Uri videoUri = data.getData();
                videoView.setVideoURI(videoUri);
                videoView.setVisibility(View.VISIBLE);
                setMediaSelectionButtonsVisibility(View.GONE);
                this.mediaDeletionButton.setVisibility(View.VISIBLE);


                MediaController mc = new MediaController(this);
                videoView.setMediaController(mc);

               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                FileInputStream fis;
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getContentResolver().query(videoUri,filePathColumn,null,null,null);
                String path="";
                if(cursor.moveToFirst()){
                    int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                    path = cursor.getString(columnIndex);
                }
                cursor.close();

                try {
                    fis = new FileInputStream(new File(path));
                    byte[] buf = new byte[1024];
                    int n;
                    while(-1 != (n=fis.read(buf))){
                        stream.write(buf,0,n);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.image=stream.toByteArray();
                Log.i("ok",""+image.length);*/


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

    public static class ConfirmationDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Confirmer l'envoi ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
