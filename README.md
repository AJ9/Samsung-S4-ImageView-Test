# Samsung-S4-ImageView-Test
This Git Repo has been created to figure out why an ImageView will not load a picture from the camera or gallery when ran on a Samsung Galaxy S4, but the same code works on other Android devices.

#Solved

Turns out any images taken from the Samsung S4 camera are too large to be handled by the device. 

##Correct solution: 

```Java
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
```

#Problem

Initial code:

### Launching the camera / gallery
```Java
    //Launch the gallery
    
    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(i, GALLERY);
    
    //Launch the camera
    Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");
    File cameraFolder;

    //Check to see if there is an SD card mounted
    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),IMAGEFOLDER);
    else
        cameraFolder = MainActivity.this.getCacheDir();
    if (!cameraFolder.exists())
        cameraFolder.mkdirs();

    //Appending timestamp to "picture_"
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    String timeStamp = dateFormat.format(new Date());
    String imageFileName = "picture_" + timeStamp + ".jpg";

    File photo = new File(Environment.getExternalStorageDirectory(), IMAGEFOLDER + imageFileName);
    getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
    
    //Setting a global variable to be used in the OnActivityResult
    imageURI = Uri.fromFile(photo);
   
    startActivityForResult(getCameraImage, CAMERA);
```    
### Handling the response in onActivityResult
```Java
    switch (requestCode){
    
        case GALLERY: 
            Uri selectedImage=data.getData(); 
            imageView.setImageURI(selectedImage); 
            break; 
    
        case CAMERA: 
            imageView.setImageURI(imageURI); 
            break;
    }
```
##Results

When testing this project on a Samsung Galaxy S4 running Android 4.2.2 the ImageView turns blank when being set by a URI.

##S4

S4 Before                  |  S4 After
:-------------------------:|:-------------------------:
![S4 Before](http://i.imgur.com/oNWVpJY.png "S4 Before")  |  ![S4 After](http://i.imgur.com/HiiU2j1.png "S4 After")

However when tested using GenyMotion 2.4.0 on a device running 4.4.4, the code works:

## GenyMotion 2.4.0

GenyMotion 2.4.0/4.4.4 Before                  |  GenyMotion 2.4.0/4.4.4 After
:-------------------------:|:-------------------------:
![GenyMotion 2.4/0/4.4.4 Before](http://i.imgur.com/EJJQlqG.png "S4 Before")  |  ![GenyMotion 2.4.0/4.4.4 After](http://i.imgur.com/KDUUjgl.png "S4 After")

#Solutions tried:

These have all failed.

##SetImageURI()

```Java

imageView.setImageURI(imageURI);
```

##Picasso

```Java

Picasso.with(context)
    .load(imageURI)
    .into(imageView);

```

##Bitmap Factory

```Java
try {
    Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageURI));
    imageView.setImageBitmap(bitmap);
} catch (FileNotFoundException e) {
    e.printStackTrace();
}
```
But the bitmap does correctly load in the debugger:
![Debugger Bitmap](http://i.imgur.com/TSXBkcj.png "Bitmap loads in the debugger") 

[Full Size Image](http://i.imgur.com/Ls8Q0qh.jpg "Bitmap loads in the debugger")



