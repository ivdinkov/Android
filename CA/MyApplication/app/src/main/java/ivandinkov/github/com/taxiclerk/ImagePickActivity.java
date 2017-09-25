package ivandinkov.github.com.taxiclerk;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class ImagePickActivity extends AppCompatActivity {
	
	private static final int REQUEST_CODE = 1;
	private Bitmap bitmap;
	private ImageView imageView;
	private Button btnPickFromDevice;
	private Button btnSaveToApp;
	private Button btnTakeNewPhoto;
	private String mCurrentPhotoPath;
	private Uri imageToUploadUri;
	private String imageFileName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_pick);
		
		imageView = (ImageView) findViewById(R.id.result);
		btnPickFromDevice = (Button) findViewById(R.id.pickFromDevice);
		btnSaveToApp = (Button) findViewById(R.id.saveToApp);
		btnTakeNewPhoto = (Button) findViewById(R.id.makePhoto);
		
		btnPickFromDevice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		btnSaveToApp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicture();
				
				
//				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//					startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//				}
			}
		});
		
//		btnTakeNewPhoto.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(ImagePickActivity.this,MakePhotoActivity.class));
//			}
//		});
	}
	
	private void takePicture() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		File f = new File(Environment.getExternalStorageDirectory(), createNewImageFile());
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		imageToUploadUri = Uri.fromFile(f);
		//startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	}
	
	private String createNewImageFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		imageFileName = "TC_" + timeStamp + "_.jpg";
		return imageFileName;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			imageView.setImageBitmap(imageBitmap);
		}
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		InputStream stream = null;
//
//
//
//
//
//		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
//			try {
//				// recyle unused bitmaps
//				if (bitmap != null) {
//					bitmap.recycle();
//				}
//				stream = getContentResolver().openInputStream(data.getData());
//				bitmap = BitmapFactory.decodeStream(stream);
//
//				imageView.setImageBitmap(bitmap);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} finally {
//				if (stream != null)
//					try {
//						stream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			}
//	}
