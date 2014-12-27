package com.example.gif;

import com.example.powerimageviewtest.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;

public class MainActivity extends Activity {

	GifImageView mGifview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGifview = (GifImageView) findViewById(R.id.image_gif);
		
		mGifview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setSelected(! v.isSelected());
				mGifview.setGif(getResources().openRawResource(R.drawable.anim));
				
			}
		});
	}

}
