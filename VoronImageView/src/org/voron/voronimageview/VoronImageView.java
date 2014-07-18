package org.voron.voronimageview;

import org.voron.voronimageview.utils.ImageDetails;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class VoronImageView extends ImageView{
	public Matrix mImageMatrix;
	private IVoronImageView voronImageView;
	
	private int imageViewWidthRetained;
	private int imageViewHeightRetained;
	private int imageViewWidth;
	private int imageViewHeight;
	protected float scaleFactor;
	protected float rotateAngle = 0;
	protected boolean isRestoreState = false;
	protected boolean isPortretView = false;
	private float[] m;
	private int type;
	
	private OnClickListener onClickListener;
	

	public VoronImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public VoronImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VoronImageView, 0, 0);
		try {
			type = array.getInt(R.styleable.VoronImageView_rotation, 90);
		} finally {
			array.recycle();
		}
		init();
	}
	public VoronImageView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		mImageMatrix = new Matrix();
		FactoryVoronImageView vFactory = new FactoryVoronImageView();
		voronImageView = vFactory.getVoronImageView(type);
		setScaleType(ScaleType.MATRIX);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {		
		ImageDetails details = new ImageDetails();
		this.imageViewWidth = w;
		this.imageViewHeight = h;
		details.setImageViewDim(w, h);
		details.setImageDetails(getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight();
		int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		voronImageView.setImageView(this);
		voronImageView.setParam(w, h, imageViewWidthRetained, imageViewHeightRetained, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight(),
				details.getMinScale(), details.getMinScale(), details.getMinScaleRot(), details.getMaxScale(),
				isPortretView, isRestoreState, rotateAngle, scaleFactor, mTouchSlop);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();		
	}	

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		VoronSaveState vivss = new VoronSaveState(superState);
		vivss.rotateAngel = voronImageView.getRotateAngel();
		vivss.scaleFactor = voronImageView.getScaleFactor();		
		vivss.matrixValue = getMatrixValues();
		vivss.imageViewWidth = imageViewWidth;
		vivss.imageViewHeight = imageViewHeight;
		vivss.isViewPortret = voronImageView.isViewPortret();
		return vivss;
	}
	@Override
	protected void onRestoreInstanceState(Parcelable state) {		
		VoronSaveState vivss = (VoronSaveState)state;
		super.onRestoreInstanceState(vivss.getSuperState());
		isRestoreState = true;
		this.rotateAngle = vivss.rotateAngel;
		this.scaleFactor = vivss.scaleFactor;		
		this.m = vivss.matrixValue;
		mImageMatrix.setValues(m);
		this.imageViewWidthRetained = vivss.imageViewWidth;
		this.imageViewHeightRetained = vivss.imageViewHeight;	
		this.isPortretView = vivss.isViewPortret;
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		voronImageView.removeImageView();
	}
	
	public void setOnMatrixChangeListener(OnMatrixChangeListener listener){
		voronImageView.setOnMatrixChangeListener(listener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean myevent = voronImageView.onTouch(event);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();		
		if(!myevent){
			if(event.getAction()==MotionEvent.ACTION_UP && voronImageView.isClick()){
				if(onClickListener!=null)onClickListener.onClick(null);
				return false;//super.onTouchEvent(saveEvent);
			}else{
				return true;
			}			
		}else {
			return true;
		}
	}	
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
		onClickListener = l;
	}
	//COMMANDS
	public void doTranslateRight(float dx){
		voronImageView.doTranslateRight(dx);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doTranslateLeft(float dx){
		voronImageView.doTranslateLeft(dx);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doTranslateUp(float dy){
		voronImageView.doTranslateUp(dy);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doTranslateDown(float dy){
		voronImageView.doTranslateDown(dy);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doZoomIn(float scale){
		voronImageView.doZoomIn(scale);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doZoomOut(float scale){
		voronImageView.doZoomOut(scale);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doRotateLeft(){
		voronImageView.doRotateLeft();
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doRotateRight(){
		voronImageView.doRotateRight();
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	public void doRotateBy(float degree){
		voronImageView.doRotateBy(degree);
		setImageMatrix(mImageMatrix);
		voronImageView.notifyMatrixChange();
	}
	//UTILS
	public float[] getMatrixValues() {
		m = new float[9];
		mImageMatrix.getValues(m);
		return m;
	}
}
