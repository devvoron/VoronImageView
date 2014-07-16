package org.voron.voronimageview;

import org.voron.voronimageview.utils.TouchManager;
import org.voron.voronimageview.utils.Vector2D;

import android.view.MotionEvent;

public class VoronIVUnbound extends AbstractVoronImageView{
	private TouchManager touchManager;
	private Vector2D position;
		
	private String touchModeFlag = "NONE";
	private boolean isRotationDone = false;
	
	private float currentAngle;

	@Override
	public void init() {
		touchManager = new TouchManager(2);
		position = new Vector2D();
		
		if(isRestoreInstanceState){			
			angleReset = -rotateAngle;	
			currentAngle = getRadiansFromDegrees(rotateAngle);
			//restoreState();
		}else{			
			scaleFactor = minScale;
	        doScale(scaleFactor);
	        //fitToView();
		}		
	}

	private void restoreState() {
		updateImageValues();		
		/*if(isViewPortret){
			minScale = minScalePortret;
		}else{
			minScale = minScaleLandscape;
		}*/		
		if(scaleFactor < minScale){
			float scale = minScale / scaleFactor;
			scaleFactor=minScale;
			doScale(scale);
		}else{			
			float deltax = Math.abs((imageViewWidth/2)-(imageViewWidthRetained/2));
			float deltay = Math.abs((imageViewHeight/2)-(imageViewHeightRetained/2));
			float scaleWidth = Math.round(imageBitmapWidth * scaleFactor);
	    	float scaleHeight = Math.round(imageBitmapHeight * scaleFactor);
			
			if(rotateAngle>45 && rotateAngle <=135){
				//x
				if(imageViewWidth>imageViewWidthRetained){
					if((x+deltax)>scaleHeight) deltax = scaleHeight-x;
					if(x<(imageViewWidth-deltax)) deltax = imageViewWidth-x;//deltax+(imageViewWidth-deltax)-x;
				}else if (imageViewWidth<imageViewWidthRetained) {
					deltax = -deltax;
				}
				//y
				if(imageViewHeight>imageViewHeightRetained){
					if(Math.abs(y)<deltay) deltay = Math.abs(y);
					if(Math.abs(y)==0) deltay = 0;
				}else if (imageViewHeight<imageViewHeightRetained) {
					deltay = -deltay;
				}
			}else if (rotateAngle>135 && rotateAngle<=225) {
				//x
				if(imageViewWidth>imageViewWidthRetained){
					if((x+deltax)>scaleWidth) deltax = scaleWidth-x;
					if(x<(imageViewWidth-deltax)) deltax = imageViewWidth-x;
				}else if (imageViewWidth<imageViewWidthRetained) {
					deltax = -deltax;
				}
				//y
				if(imageViewHeight>imageViewHeightRetained){
					if((y+deltay)>scaleHeight) deltay = scaleHeight-y;
					if(y<(imageViewHeight-deltay)) deltay = imageViewHeight-y;
				}else if (imageViewHeight<imageViewHeightRetained) {
					deltay = -deltay;
				}
			}else if (rotateAngle>225 && rotateAngle<=305) {
				//x
				if(imageViewWidth>imageViewWidthRetained){
					if((scaleHeight-Math.abs(x))<(imageViewWidth-deltax)) deltax = imageViewWidth-(scaleHeight-Math.abs(x));//deltax+(imageViewWidth-deltax)-(scaleHeight-x);
					if(Math.abs(x)==0) deltax = 0;
				}else if (imageViewWidth<imageViewWidthRetained) {
					deltax = -deltax;
				}else {
					deltax = 0;
				}	
				//y
				if(imageViewHeight>imageViewHeightRetained){
					if((y+deltay)>scaleWidth) deltay = scaleWidth-y;
					if(y<(imageViewHeight-deltay)) deltay = imageViewHeight-y;
				}else if (imageViewHeight<imageViewHeightRetained) {
					deltay = -deltay;
				}
			}else{	
				//x
				if(imageViewWidth>imageViewWidthRetained){
					if((scaleWidth-Math.abs(x))<(imageViewWidth-deltax)) deltax = imageViewWidth-(scaleWidth-Math.abs(x));//deltax+(imageViewWidth-deltax)-(scaleHeight-x);
					if(Math.abs(x)==0) deltax = 0;
				}else if (imageViewWidth<imageViewWidthRetained) {
					deltax = -deltax;
				}else {
					deltax = 0;
				}
				//y
				if(imageViewHeight>imageViewHeightRetained){
					if(Math.abs(y)<deltay) deltay = Math.abs(y);
					if(Math.abs(y)==0) deltay = 0;
				}else if (imageViewHeight<imageViewHeightRetained) {
					deltay = -deltay;
				}
			}
			myview.mImageMatrix.postTranslate(deltax, deltay);	
			updateImageValues();
		}		
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		try {
			touchManager.update(event);
			if(touchManager.getPressCount() == 1){
				position.add(touchManager.moveDelta(0));
			}
			if(touchManager.getPressCount() == 2){
				Vector2D current = touchManager.getVector(0, 1);
				Vector2D previous = touchManager.getPreviousVector(0, 1);
				if(Math.abs(Vector2D.getSignedAngleBetween(current, previous))>0.02){
					rotate(Vector2D.getSignedAngleBetween(current, previous));														
				}				
			}
		} catch (Throwable e) {
			// TODO: handle exception
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
	        start.set(event.getX(), event.getY());	           
	        mode = DRAG;
	        isClick = true;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			isClick = false;
			oldDist = spacing(event);
			if(oldDist >=mTouchSlop){
				midPoint(mid, event);
	            mode = ZOOM;
			}			
			break;
		case MotionEvent.ACTION_UP:			
			mode = NONE;			
			updateImageValues();
			if(isClick) //onMyClickListener();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = DRAG;
			break;
		case MotionEvent.ACTION_MOVE:
			isClick = false;
			if(mode==DRAG){				
	            float deltaX = event.getX() - start.x;
	            float deltaY =  event.getY() - start.y;            	            
	            start.x = event.getX();
	            start.y = event.getY();	            
	            doTranslate(deltaX,deltaY);	            
			}else if (mode == ZOOM) {				
				float newDist = spacing(event);
	            float scale = newDist / oldDist;	            	
	            doScale(preparedScale(scale));                 
	            //fitToView();	            
			}
		default:
			break;
		}		
		updateImageValues();
		if(isClick){
			return false;
		}else {
			return true;
		}
	}
	
	
	@Override
	public void doRotate(float angle) {
		rotate(getRadiansFromDegrees(angle));
	}
	private void rotate(float radian){
		currentAngle -= radian;
		rotateAngle = getDegreesFromRadians(currentAngle);
		rotateAngle = (rotateAngle+360)%360;						
		myview.mImageMatrix.postRotate(angleReset,mPivotX,mPivotY);
		myview.mImageMatrix.postRotate(rotateAngle,mPivotX,mPivotY);
		angleReset = -rotateAngle;
	}
	@Override
	public void doTranslate(float dx, float dy) {
		myview.mImageMatrix.postTranslate(dx, dy);		
	}
	@Override
	public void doScale(float scale) {
		myview.mImageMatrix.postScale(scale, scale, mid.x, mid.y);		
	}
	
	
	//UTILS
	private float getDegreesFromRadians(float angle) {
		return (float)(angle * 180.0 / Math.PI);
	}
	private float getRadiansFromDegrees(float angle){
		return (float)(angle*Math.PI/180);
	}
}
