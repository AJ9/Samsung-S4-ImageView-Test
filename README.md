# Samsung-S4-ImageView-Test
This Git Repo has been created to figure out why an ImageView will not load a picture from the camera or gallery when ran on a Samsung Galaxy S4, but the same code works on other Android devices.

Code snippets from the project:

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




