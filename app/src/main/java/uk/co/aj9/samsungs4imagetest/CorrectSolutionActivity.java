package uk.co.aj9.samsungs4imagetest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CorrectSolutionActivity extends ActionBarActivity {
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
                                        if (Environment.getExternalStorageState().equals
                                                (Environment.MEDIA_MOUNTED))
                                            cameraFolder = new File(Environment.getExternalStorageDirectory(),
                                                    IMAGEFOLDER);
                                        else
                                            cameraFolder = CorrectSolutionActivity.this.getCacheDir();
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
                    Bitmap bitmap = createScaledBitmap(getImagePath(data, getApplicationContext()), imageView.getWidth(), imageView.getHeight());
                    imageView.setImageBitmap(bitmap);
                    break;
                case CAMERA:
                    String path = imageURI.getPath();
                    Bitmap bitmapCamera = createScaledBitmap(path, imageView.getWidth(), imageView.getHeight());
                    imageView.setImageBitmap(bitmapCamera);
                    break;
            }

        }

    }

    // Function to get image path from ImagePicker
    public static String getImagePath(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }


    public Bitmap createScaledBitmap(String pathName, int width, int height) {
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opt);
        opt.inSampleSize = calculateBmpSampleSize(opt, width, height);
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, opt);
    }

    public int calculateBmpSampleSize(BitmapFactory.Options opt, int width, int height) {
        final int outHeight = opt.outHeight;
        final int outWidth = opt.outWidth;
        int sampleSize = 1;
        if (outHeight > height || outWidth > width) {
            final int heightRatio = Math.round((float) outHeight / (float) height);
            final int widthRatio = Math.round((float) outWidth / (float) width);
            sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return sampleSize;
    }
    /*
        * Function to determine the dimensions of the target device display
        */
    public DisplayMetrics getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mainActivity) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id == R.id.picassoActivity){
            Intent intent = new Intent(this, PicassoActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id == R.id.bitmapActivity){
            Intent intent = new Intent(this, BitmapActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id == R.id.correctSolutionActivitiy){
            Intent intent = new Intent(this, CorrectSolutionActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
