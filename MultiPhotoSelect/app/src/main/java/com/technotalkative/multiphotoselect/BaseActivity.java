package com.technotalkative.multiphotoselect;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Paresh Mayani (@pareshmayani)
 */
public abstract class BaseActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
}
