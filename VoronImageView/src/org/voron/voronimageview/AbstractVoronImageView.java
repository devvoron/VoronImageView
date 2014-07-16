package org.voron.voronimageview;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View.OnClickListener;

public abstract class AbstractVoronImageView implements IVoronImageView{
	
	//listener
	protected OnMatrixChangeListener matrixListener;
	protected EventObject eventObject;
	
	//image&view properties
	protected VoronImageView myview;
	protected int imageViewWidth, imageViewHeight;
	protected int imageViewWidthRetained, imageViewHeightRetained;
	protected int imageBitmapWidth, imageBitmapHeight;
	protected int mPivotX, mPivotY;
	protected float minScale;
	protected float minScaleLandscape;
	protected float minScalePortret;
	protected float maxScale;
	
	//matrix properties
	protected float x;
	protected float y;
	protected float scaleFactor;
	protected float rotateAngle;	
	protected boolean isViewPortret = false;
	protected boolean isRestoreInstanceState = false;
	protected float[] m;
	protected PointF start = new PointF();
	protected PointF mid = new PointF();
	protected float oldDist = 1f;	
	protected float angleReset = 0;
	
	/* Drag threshold */
	protected int mTouchSlop;	
	protected static final int NONE = 0;
	protected static final int DRAG = 1;
	protected static final int ZOOM = 2;
	protected static final int ROTATION = 3;
	protected int mode = NONE;
	protected boolean isClick = false;

	@Override
	public void setImageView(VoronImageView v) {
		this.myview = v;
	}
	@Override
	public void removeImageView() {
		this.myview = null;		
	}

	@Override
	public void setParam(int imageViewWidth, int imageViewHeight, int imageViewWidthRetained, int imageViewHeightRetained,
			int imageBitmapWidth, int imageBitmapHeight,
			float minScale, float minScaleLandscape, float minScalePortret, float maxScale,
			boolean isPortretView, boolean isRestoreState,
			float rotAngle, float scaleF, int mTouchSlop) {
		// TODO Auto-generated method stub
		this.imageViewWidth = imageViewWidth;
		this.imageViewHeight = imageViewHeight;
		this.imageViewWidthRetained = imageViewWidthRetained;
		this.imageViewHeightRetained = imageViewHeightRetained;
		this.imageBitmapWidth = imageBitmapWidth;
		this.imageBitmapHeight = imageBitmapHeight;
		this.mPivotX = imageViewWidth/2;
		this.mPivotY = imageViewHeight/2;
		this.minScale = minScale;
		this.minScaleLandscape = minScaleLandscape;
		this.minScalePortret = minScalePortret;
		this.maxScale = maxScale;
		this.rotateAngle = rotAngle;
		this.scaleFactor = scaleF;
		this.mTouchSlop = mTouchSlop;
		this.isViewPortret = isPortretView;
		this.isRestoreInstanceState = isRestoreState;
		init();
	}
	
	public void setOnMatrixChangeListener(OnMatrixChangeListener listener){
		matrixListener = listener;
		eventObject = new EventObject();
	}
	
	//COMMANDS
	@Override
	public void doTranslateRight(float dx) {
		if(dx > 0.0) doTranslate(dx, 0);		
	}
	@Override
	public void doTranslateLeft(float dx) {
		if(dx > 0.0) doTranslate(-dx, 0);	
		if(dx < 0.0) doTranslate(dx, 0);
	}
	@Override
	public void doTranslateUp(float dy) {
		if(dy < 0.0) doTranslate(0, dy);
		if(dy > 0.0) doTranslate(0, -dy);
	}
	@Override
	public void doTranslateDown(float dy) {
		if(dy > 0.0) doTranslate(0, dy);		
	}
	@Override
	public void doZoomIn(float scale) {		
		if(scale >=0)doScale(preparedScale(1+(scale*0.1f)));		
	}
	@Override
	public void doZoomOut(float scale) {
		if(scale >=0)doScale(preparedScale(1-(scale*0.1f)));		
	}
	@Override
	public void doRotateLeft() {
		doRotate(-90);		
	}
	@Override
	public void doRotateRight() {
		doRotate(90);		
	}
	public void doRotateBy(float degree){
		doRotate(degree);
	}

	//GETTERS
	@Override
	public float getScaleFactor() {
		return this.scaleFactor;
	}
	@Override
	public float getRotateAngel() {
		return this.rotateAngle;
	}
	@Override
	public boolean isViewPortret() {
		return this.isViewPortret;
	}	
	@Override
	public float getX() {
		return this.x;
	}
	@Override
	public float getY() {
		return this.y;
	}
	
	public boolean isClick(){
		return this.isClick;
	}
	//abstract
	public abstract void init();
	public abstract boolean onTouch(MotionEvent event);
	public abstract void doTranslate(float dx, float dy);
	public abstract void doScale(float scale);
	public void doRotate(float angle){};
	
	//UTILS
	public void notifyMatrixChange(){
		if(matrixListener != null){
			updateImageValues();
			eventObject.setX(x);
			eventObject.setY(y);
			eventObject.setRotateAngle(rotateAngle);
			eventObject.setScaleFactor(scaleFactor);
			matrixListener.onEvent(eventObject);
		}		
	}
	protected void updateImageValues() {
		m = new float[9];
		myview.mImageMatrix.getValues(m);
		this.x = m[Matrix.MTRANS_X];
        this.y = m[Matrix.MTRANS_Y];		
	}
	protected float spacing(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
	    float y = event.getY(0) - event.getY(1);
	    return FloatMath.sqrt(x * x + y * y);
	}
	protected void midPoint(PointF point, MotionEvent event){
		float x = event.getX(0) + event.getX(1);
	    float y = event.getY(0) + event.getY(1);
	    point.set(x / 2, y / 2);
	}
	protected float preparedScale(float scale){		
		float origScale = scaleFactor;
     	scaleFactor *= scale;                 	
     	if(scaleFactor >= maxScale){
     		scaleFactor = maxScale;
       	 scale = maxScale / origScale;
        }else if(scaleFactor <= minScale){
        	scaleFactor = minScale;
       	 scale = minScale / origScale;
        }
     	return scale;
	}
}
