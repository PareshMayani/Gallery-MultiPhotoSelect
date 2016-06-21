package com.technotalkative.multiphotoselect;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
public class MultiPhotoSelectActivity extends AppCompatActivity {

	private ImageAdapter imageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_multi_photo_select);

		ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
		imageAdapter = new ImageAdapter(this, imageUrls);

		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(imageAdapter);
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

	public void btnChoosePhotosClick(View v){
		
		ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
		Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: "+selectedItems.size(), Toast.LENGTH_SHORT).show();
		Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
	}

}