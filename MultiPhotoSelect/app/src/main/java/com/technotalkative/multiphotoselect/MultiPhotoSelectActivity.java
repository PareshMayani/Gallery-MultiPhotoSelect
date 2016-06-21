package com.technotalkative.multiphotoselect;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
public class MultiPhotoSelectActivity extends AppCompatActivity {

	private ImageAdapter imageAdapter;
	private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_multi_photo_select);

		populateImagesFromGallery();
	}

	public void btnChoosePhotosClick(View v){
		
		ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
		Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: "+selectedItems.size(), Toast.LENGTH_SHORT).show();
		Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
	}

	private void populateImagesFromGallery() {
		if (!mayRequestGalleryImages()) {
			return;
		}

		ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
		initializeRecyclerView(imageUrls);
	}

	private boolean mayRequestGalleryImages() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}

		if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
			//promptStoragePermission();
			showPermissionRationaleSnackBar();
		} else {
			requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
		}

		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == REQUEST_FOR_STORAGE_PERMISSION) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				populateImagesFromGallery();
			} else if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
				showPermissionRationaleSnackBar();
			}
		}
	}

	private ArrayList<String> loadPhotosFromNativeGallery() {
		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");

		ArrayList<String> imageUrls = new ArrayList<String>();

		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(imagecursor.getString(dataColumnIndex));

			System.out.println("=====> Array path => "+imageUrls.get(i));
		}

		return imageUrls;
	}

	private void initializeRecyclerView(ArrayList<String> imageUrls) {
		imageAdapter = new ImageAdapter(this, imageUrls);

		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(imageAdapter);
	}

	private void showPermissionRationaleSnackBar() {
		Snackbar.make(findViewById(R.id.button1), getString(R.string.permission_rationale),
				Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Request the permission
				ActivityCompat.requestPermissions(MultiPhotoSelectActivity.this,
						new String[]{READ_EXTERNAL_STORAGE},
						REQUEST_FOR_STORAGE_PERMISSION);
			}
		}).show();

	}
}