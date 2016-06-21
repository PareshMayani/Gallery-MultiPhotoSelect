package com.technotalkative.multiphotoselect;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
public class MultiPhotoSelectActivity extends AppCompatActivity {

	private ArrayList<String> imageUrls;
	private ImageAdapter imageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);

		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");
		
		this.imageUrls = new ArrayList<String>();
		
		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(imagecursor.getString(dataColumnIndex));
			
			System.out.println("=====> Array path => "+imageUrls.get(i));
		}
		

		imageAdapter = new ImageAdapter(this, imageUrls);
		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(imageAdapter);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void btnChoosePhotosClick(View v){
		
		ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
		Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: "+selectedItems.size(), Toast.LENGTH_SHORT).show();
		Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
	}

}