package org.voron.voronimageview;

import android.view.MotionEvent;
import android.view.View.OnClickListener;

public interface IVoronImageView {
	
	//ini
		public void setImageView(VoronImageView v);	
		public void removeImageView();
		public void setParam(int imageViewWidth, int imageViewHeight, int imageViewWidthRetained, int imageViewHeightRetained,
				int imageBitmapWidth, int imageBitmapHeight,
				float minScale, float minScaleLandscape, float minScalePortret, float maxScale,
				boolean isPortretView,boolean isRestoreState,
				float rotAngle, float scaleF, int mTouchSlop);
		public boolean onTouch(MotionEvent event);
		public void setOnMatrixChangeListener(OnMatrixChangeListener listener);
		//commands
		public void doTranslateRight(float dx);
		public void doTranslateLeft(float dx);
		public void doTranslateUp(float dy);
		public void doTranslateDown(float dy);
		public void doZoomIn(float scale);
		public void doZoomOut(float scale);
		public void doRotateLeft();
		public void doRotateRight();
		public void doRotateBy(float degree);
		public void notifyMatrixChange();
		//geters
		public float getX();
		public float getY();
		public float getScaleFactor();
		public float getRotateAngel();
		public boolean isViewPortret();
		public boolean isClick();

}
