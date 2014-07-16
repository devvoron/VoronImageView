package org.voron.voronimageview;

import org.voron.voronimageview.utils.TouchManager;
import org.voron.voronimageview.utils.Vector2D;

import android.view.MotionEvent;

public class VoronIV90R extends AbstractVoronImageView{
	private TouchManager touchManager;
	private Vector2D position;
		
	private String touchModeFlag = "NONE";
	private boolean isRotationDone = false;

	@Override
	public void init() {
		touchManager = new TouchManager(2);
		position = new Vector2D();
		
		if(isRestoreInstanceState){			
			angleReset = -rotateAngle;			
			restoreState();
		}else{			
			scaleFactor = minScale;
	        doScale(scaleFactor);
	        fitToView();
		}
	}
	
	private void restoreState() {		
		updateImageValues();		
		if(isViewPortret){
			minScale = minScalePortret;
		}else{
			minScale = minScaleLandscape;
		}
		if(scaleFactor < minScale){
			float scale = minScale / scaleFactor;
			scaleFactor=minScale;
			doScale(scale);
		}else{			
			float deltax = Math.abs((imageViewWidth/2)-(imageViewWidthRetained/2));
			float deltay = Math.abs((imageViewHeight/2)-(imageViewHeightRetained/2));
			float scaleWidth = Math.round(imageBitmapWidth * scaleFactor);
	    	float scaleHeight = Math.round(imageBitmapHeight * scaleFactor);
			
			if(rotateAngle==90){
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
			}else if (rotateAngle==180) {
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
			}else if (rotateAngle==270) {
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
					if((scaleWidth-Math.abs(x))<(imageViewWidth-deltax)) deltax = imageViewWidth-(scaleWidth-Math.abs(x));
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
			fitToView();
		}		
	}
	
	
	//MOTION EVENTS
	public boolean onTouch(MotionEvent event){				
		try {
			touchManager.update(event);
			if(touchManager.getPressCount() == 1){
				position.add(touchManager.moveDelta(0));
			}
			if(touchManager.getPressCount() == 2){
				Vector2D current = touchManager.getVector(0, 1);
				Vector2D previous = touchManager.getPreviousVector(0, 1);
				if(Math.abs(Vector2D.getSignedAngleBetween(current, previous))>0.06){					
					if(!isRotationDone){
						isRotationDone = true;
						touchModeFlag = "ROTATE";							
						if(Vector2D.getSignedAngleBetween(current, previous)>0){
							doRotate(-90);
						}else{
							doRotate(90);
						}						
					}						
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
			if(touchModeFlag.contains("NONE") && !isRotationDone && oldDist >=mTouchSlop*2){
				midPoint(mid, event);
	            mode = ZOOM;
	            touchModeFlag = "ZOOM";
			}			
			break;
		case MotionEvent.ACTION_UP:			
			mode = NONE;
			touchModeFlag = "NONE";
			isRotationDone = false;
			updateImageValues();
			if(isClick) //doOnClick();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			touchModeFlag = "NONE";
			mode = NONE;
			isRotationDone = false;
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
				if(!isRotationDone && oldDist >=mTouchSlop){
	            	float scale = newDist / oldDist;	            	
	             	doScale(preparedScale(scale));                 
	             	fitToView();
	            }
			}
			break;
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
	public void doTranslate(float deltaX, float deltaY) {
		updateImageValues();
	    float scaleWidth = Math.round(imageBitmapWidth * scaleFactor);
		float scaleHeight = Math.round(imageBitmapHeight * scaleFactor);
	    
	    if(rotateAngle==90){
	    	//x
	    	if(x + deltaX >scaleHeight){
	    		deltaX = 0;
	    	}else if (x + deltaX < imageViewWidth) {
	    		deltaX = 0;
			}
	    	//y
	    	if(y + deltaY > 0){
	        	deltaY = 0;
	        	  
	        }else if (y + deltaY < -scaleWidth+imageViewHeight) {
	        	deltaY = 0;
			}
	    }else if (rotateAngle==180) {
	    	//x
	    	if(x + deltaX >scaleWidth){
	    		deltaX = 0;
	    	}else if (x + deltaX < imageViewWidth) {
	    		deltaX = 0;
			}
	    	//y
	    	if(y + deltaY >scaleHeight){
	    		deltaY = 0;
	    	}else if (y + deltaY<imageViewHeight) {
	    		deltaY = 0;
			}
		}else if (rotateAngle==270) {
			//x
			if(x + deltaX > 0){
	            deltaX = -x;
	        }else if(x + deltaX < -scaleHeight + imageViewWidth){
	        	deltaX = 0;
	        }
			//y
	    	if(y + deltaY >scaleWidth){
	    		deltaY = 0;
	    	}else if (y + deltaY<imageViewHeight) {
	    		deltaY = 0;
			}
		}else {	            		             
	        if(y + deltaY > 0){
	        	deltaY = -y;
	        	  
	        }else if(y + deltaY < -scaleHeight+imageViewHeight){        	  
	            deltaY = (y*-1-scaleHeight+imageViewHeight);
	        }
	        if(x + deltaX > 0){
	            deltaX = -x;
	        }else if(x + deltaX < -scaleWidth + imageViewWidth){
	        	deltaX = -(x+scaleWidth - imageViewWidth);
	        }
		}            
	    myview.mImageMatrix.postTranslate(deltaX, deltaY);		
	}

	@Override
	public void doScale(float scale) {
		myview.mImageMatrix.postScale(scale, scale, mid.x, mid.y);
		fitToView();
	}

	@Override
	public void doRotate(float angle) {
		rotateAngle = rotateAngle + angle;
		rotateAngle = (rotateAngle+360)%360;
		myview.mImageMatrix.postRotate(angleReset,mPivotX,mPivotY);						
		myview.mImageMatrix.postRotate(rotateAngle,mPivotX,mPivotY);
		angleReset = -rotateAngle;						
		if(rotateAngle==90 || rotateAngle==270){
			isViewPortret = !isViewPortret;
			minScale = minScalePortret;
			if(scaleFactor < minScalePortret){
				float scale = minScalePortret / scaleFactor;
				scaleFactor=minScalePortret;
				doScale(scale);
			}
		}else {
			isViewPortret = !isViewPortret;
			minScale = minScaleLandscape;
		}
		fitToView();		
	}
	
	//UTILS
	private void fitToView() {
		updateImageValues();
        float xCorection = x;
        float yCorection = y;        
	    float scaleWidth = Math.round(imageBitmapWidth * scaleFactor);
		float scaleHeight = Math.round(imageBitmapHeight * scaleFactor);
		float deltaY=0;
		float deltaX=0;
		if(rotateAngle==90){
			//x
	    	if(xCorection >scaleHeight){
	    		deltaX = -(xCorection-scaleHeight);
	    	}else if (xCorection < imageViewWidth) {
	    		deltaX = imageViewWidth-xCorection;
			}
	    	//y
	    	if(yCorection > 0){
	        	deltaY = -yCorection;
	        	  
	        }else if (yCorection < -scaleWidth+imageViewHeight) {
	        	deltaY = (yCorection*-1)-(scaleWidth-imageViewHeight);
			}
		}else if (rotateAngle==180) {
			//x
	    	if(xCorection >scaleWidth){
	    		deltaX = -(xCorection-scaleWidth);
	    	}else if (xCorection < imageViewWidth) {
	    		deltaX = imageViewWidth-xCorection;
			}
	    	//y
	    	if(yCorection >scaleHeight){
	    		deltaY = -(yCorection-scaleHeight);
	    	}else if (yCorection<imageViewHeight) {
	    		deltaY = imageViewHeight-(yCorection);
			}
		}else if (rotateAngle==270) {
			//x
			if(xCorection > 0){
	   		 deltaX = -xCorection;
	        }else if(xCorection < -scaleHeight + imageViewWidth){
	            deltaX = (xCorection*-1)-(scaleHeight - imageViewWidth);
	        }
			//y
			if(yCorection >scaleWidth){
	    		deltaY = -(yCorection-scaleWidth);
	    	}else if (yCorection<imageViewHeight) {
	    		deltaY = imageViewHeight-(yCorection);
			}
		}else{
			//x
			if(xCorection > 0){
	   		 deltaX = -xCorection;
	        }else if(xCorection < -scaleWidth + imageViewWidth){
	            deltaX = (xCorection*-1)-(scaleWidth - imageViewWidth);
	        }			
			//y
			if(yCorection > 0){
	        	deltaY = -yCorection;
	        	  
	        }else if (yCorection < -scaleHeight+imageViewHeight) {
	        	deltaY = (yCorection*-1)-(scaleHeight-imageViewHeight);
			}
		}    	 
		myview.mImageMatrix.postTranslate(deltaX, deltaY);
		
	}
}
