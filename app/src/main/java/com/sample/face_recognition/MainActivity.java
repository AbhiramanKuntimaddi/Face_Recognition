package com.sample.face_recognition;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity {

    private  float x1,x2,y1,y2;
    private static final int CAMERA = 2;
    private  static final int GALLERY = 1;
    private static final String IMAGE_DIRECTORY = "/PetPals/Profile_Pictures";
    ImageView imageView;
    RectF rectF;
    Canvas canvas;

    Paint rectPaint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//imageView_ref
        imageView = findViewById(R.id.imageview_face);


//Face Detection Case
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x1=x2=y1=y2=0;
                imageView = findViewById(R.id.imageview_face);
                final Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.tom);
                imageView.setImageBitmap(myBitmap);

                rectPaint.setStrokeWidth(5);
                rectPaint.setColor(Color.WHITE);
                rectPaint.setStyle(Paint.Style.STROKE);

                final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(), Bitmap.Config.RGB_565);
                canvas  = new Canvas(tempBitmap);
                canvas.drawBitmap(myBitmap,0,0,null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if(!faceDetector.isOperational())
                {
                    Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);

                for(int i=0;i<sparseArray.size();i++)
                {
                    Face face = sparseArray.valueAt(i);
                    x1=face.getPosition().x;
                    y1 =face.getPosition().y;
                    x2 = x1+face.getWidth();
                    y2=y1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);
                }

                if(x2 == 0 && y2 == 0) {
                    Snackbar.make(view, "No Face Detected", Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(view, "Face Detected", Snackbar.LENGTH_LONG).show();
                }
                imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
            }
        });
//No Face Detection Case
        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x1=x2=y1=y2=0;
                imageView = findViewById(R.id.imageview_face);
                final Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.test);
                imageView.setImageBitmap(myBitmap);

                rectPaint.setStrokeWidth(5);
                rectPaint.setColor(Color.WHITE);
                rectPaint.setStyle(Paint.Style.STROKE);

                final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(), Bitmap.Config.RGB_565);
                canvas  = new Canvas(tempBitmap);
                canvas.drawBitmap(myBitmap,0,0,null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if(!faceDetector.isOperational())
                {
                    Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);

                for(int i=0;i<sparseArray.size();i++)
                {
                    Face face = sparseArray.valueAt(i);
                    x1=face.getPosition().x;
                    y1 =face.getPosition().y;
                    x2 = x1+face.getWidth();
                    y2=y1+face.getHeight();
                    rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);

                }

                if(x2 == 0 && y2 == 0) {
                    Snackbar.make(view, "No Face Detected", Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(view, "Face Detected", Snackbar.LENGTH_LONG).show();
                }
                imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
            }
        });
//Image from Camera or Gallery
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicDiag();
            }
        });
    }


    private void showPicDiag(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                    Face_Recognition(bitmap);

                   /* imageView.setImageBitmap(bitmap);

                    rectPaint.setStrokeWidth(5);
                    rectPaint.setColor(Color.WHITE);
                    rectPaint.setStyle(Paint.Style.STROKE);

                    final Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
                    canvas  = new Canvas(tempBitmap);
                    canvas.drawBitmap(bitmap,0,0,null);

                    FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .setMode(FaceDetector.FAST_MODE)
                            .build();
                    if(!faceDetector.isOperational())
                    {
                        Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Face> sparseArray = faceDetector.detect(frame);
                    x1=x2=y1=y2=0;
                    for(int i=0;i<sparseArray.size();i++)
                    {
                        Face face = sparseArray.valueAt(i);
                        x1=face.getPosition().x;
                        y1 =face.getPosition().y;
                        x2 = x1+face.getWidth();
                        y2=y1+face.getHeight();

                    }

                    if(x2 == 0 && y2 == 0) {
                        Log.d("GALLERY_IMAGE_FACE_RECG", "No Face Detected");
                       Toast.makeText(MainActivity.this,"No Face Detected",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"Face Detected",Toast.LENGTH_SHORT).show();
                        String path = saveImage(bitmap);
                        Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_LONG).show();
                    }*/
                  //  imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap image = (Bitmap) data.getExtras().get("data");

            Face_Recognition(image);
            /*imageView.setImageBitmap(image);
            saveImage(image);
            imageView.setImageBitmap(image);

            rectPaint.setStrokeWidth(5);
            rectPaint.setColor(Color.WHITE);
            rectPaint.setStyle(Paint.Style.STROKE);

            final Bitmap tempBitmap = Bitmap.createBitmap(image.getWidth(),image.getHeight(), Bitmap.Config.RGB_565);
            canvas  = new Canvas(tempBitmap);
            canvas.drawBitmap(image,0,0,null);

            FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .setMode(FaceDetector.FAST_MODE)
                    .build();
            if(!faceDetector.isOperational())
            {
                Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
                return;
            }
            Frame frame = new Frame.Builder().setBitmap(image).build();
            SparseArray<Face> sparseArray = faceDetector.detect(frame);
            x1=x2=y1=y2=0;
            for(int i=0;i<sparseArray.size();i++)
            {
                Face face = sparseArray.valueAt(i);
                x1=face.getPosition().x;
                y1 =face.getPosition().y;
                x2 = x1+face.getWidth();
                y2=y1+face.getHeight();

            }

            if(x2 == 0 && y2 == 0) {
                Log.d("CAMERA_IMAGE_FACE_RECG", "No Face Detected");
                Toast.makeText(MainActivity.this,"No Face Detected",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"Face Detected",Toast.LENGTH_SHORT).show();
                String path = saveImage(image);
                Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();*/
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() +"_PetPals_Profile_pic_temp"+".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void Face_Recognition(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);

       /* final Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
        canvas  = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);*/

        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if(!faceDetector.isOperational())
        {
            Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);
        x1=x2=y1=y2=0;
        for(int i=0;i<sparseArray.size();i++)
        {
            Face face = sparseArray.valueAt(i);
            x1=face.getPosition().x;
            y1 =face.getPosition().y;
            x2 = x1+face.getWidth();
            y2=y1+face.getHeight();

        }

        if(x2 == 0 && y2 == 0) {
            Toast.makeText(MainActivity.this,"No Face Detected",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"Face Detected",Toast.LENGTH_SHORT).show();
            String path = saveImage(bitmap);
            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_LONG).show();
        }

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
        if (id == R.id.action_settings) {
            if(imageView ==null){
                //Do nothing
            }
            else{
                imageView.setImageResource(0);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
