package com.example.gif;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * @author �� sir 2014��12��16��
 *
 * This ImageView add supports to GIF ,
 * It extends ImageView , So,GIFImageView support all abilities of ImageView
	 * And what's more ?  see the public methods in this class
 */
public class GifImageView extends ImageView{

	/**
	 *  Class to play gif
	 */
	private Movie mMovie;
	
	/**
	 * Gif start playing time
	 * used to locating the index of bitmap
	 */
	private long mStartTime;
	
	private Matrix mDrawMatrix;
	private Matrix mMatrix;
	
	public GifImageView(Context context) {
		super(context);
	}

	public GifImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GifImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie == null) {
			// src is classical imageView;
			super.onDraw(canvas);
		} else {
			
//			int superScrollX = getScrollX();
//			int superScrollY = getScrollY();
//			int superRight = getRight();
//			int superLeft = getLeft();
//			int superBottom = getBottom();
//			int superTop = getTop();
//			canvas.clipRect(superScrollX + mSuperPaddingLeft, 
//					superScrollY + mSuperPaddingTop, 
//					superScrollX + superRight - superLeft - mSuperPaddingRight, 
//					superScrollY + superBottom - superTop - mSuperPaddingBottom);
			int saveCount = canvas.getSaveCount();
			if (mDrawMatrix != null && !mDrawMatrix.isIdentity()) {
				canvas.concat(mDrawMatrix);
			}
			
			if (isSelected()) {
				playMovie(canvas);
				invalidate();
			} else {
				mMovie.setTime(0);
				mMovie.draw(canvas, 0, 0);
			}
			
			canvas.restoreToCount(saveCount);
		}
	}
	
	
	/**
	 * play the gif
	 * @param canvas
	 * @return
	 */
	private boolean playMovie(Canvas canvas) {
		long now = SystemClock.uptimeMillis();
		
		if (mStartTime == 0) {
			mStartTime = now;
		}
		
		int duration = mMovie.duration();
		if (duration == 0) {
			duration = 1000;
		}
		int relTime = (int) ((now - mStartTime) % duration);
		mMovie.setTime(relTime);
		mMovie.draw(canvas, 0, 0);
		if ((now - mStartTime) >= duration) {
			mStartTime = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * Set gif with a inputStream  
	 * @param is
	 */
	public void setGif(InputStream is){
		mMovie = Movie.decodeStream(is);
		calculateMatrix();
	}
	
	/**
	 * Set gif with a ByteArray
	 * @param data
	 * @param offset
	 * @param length
	 */
	public void setGif(byte[] data, int offset, int length){
		mMovie = Movie.decodeByteArray(data, offset, length);
	}
	
	/**
	 * Set gif with a PathString
	 * @param pathName
	 */
	public void setGif(String pathName){
		mMovie = Movie.decodeFile(pathName);
	}
	
	/**
	 * Calculate the matrix to scale and center the movie in canvas.
	 * Rect of canvas  exclude padding
	 * 
	 * You can override this method to achieve effect  you want.
	 */
	private void calculateMatrix(){
		
		if (mMovie == null) {
			return;
		}
		
		int movieWidth = mMovie.width();// 实际像素
		int movieHeight = mMovie.height();
		
		int vWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		int vHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
		
		mDrawMatrix = new Matrix();
		float scale;
		
		scale = Math.min((float) vWidth / (float) movieWidth, (float) vHeight / (float) movieHeight);
		float dx = (int) ((vWidth - movieWidth * scale) * 0.5f + 0.5f);
		float dy = (int) ((vHeight - movieHeight * scale) * 0.5f + 0.5f);
		mDrawMatrix.setScale(scale, scale);
		mDrawMatrix.postTranslate(dx, dy);
	}
}
