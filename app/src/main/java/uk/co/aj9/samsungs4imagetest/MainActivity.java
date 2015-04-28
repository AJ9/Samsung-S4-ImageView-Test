package uk.co.aj9.samsungs4imagetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    //Views

    Button btn;
    ImageView imageView;

    //Request codes

    final int GALLERY = 0;
    final int CAMERA = 1;

    //Context
    Context context;

    //Uri used when picking from the camera
    Uri imageURI;

    final String IMAGEFOLDER = "S4Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //Set the views

        btn = (Button) findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.imageView);

        //Set the listener where a click builds a listener and offers the gallery or camera picker

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        //Launching the gallery
                                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(i, GALLERY);

                                        break;

                                    case 1:
                                        //Specify a camera intent
                                        Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");

                                        File cameraFolder;

                                        //Check to see if there is an SD card mounted
                                        if (android.os.Environment.getExternalStorageState().equals
                                                (android.os.Environment.MEDIA_MOUNTED))
                                            cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),
                                                    IMAGEFOLDER);
                                        else
                                            cameraFolder = MainActivity.this.getCacheDir();
                                        if (!cameraFolder.exists())
                                            cameraFolder.mkdirs();

                                        //Appending timestamp to "picture_"
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                                        String timeStamp = dateFormat.format(new Date());
                                        String imageFileName = "picture_" + timeStamp + ".jpg";

                                        File photo = new File(Environment.getExternalStorageDirectory(),
                                                IMAGEFOLDER + imageFileName);
                                        getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

                                        //Setting a global variable to be used in the OnActivityResult
                                        imageURI = Uri.fromFile(photo);

                                        startActivityForResult(getCameraImage, CAMERA);

                                        break;
                                    default:
                                        break;
                                }
                            }
                        });

                builder.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            switch (requestCode) {
                case GALLERY:
                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);

                    break;
                case CAMERA:

                    imageView.setImageURI(imageURI);
                    break;
            }

        }

    }

}
