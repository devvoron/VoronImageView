package org.voron.voronimageview;

import android.view.MotionEvent;

public class VoronIVNoR extends AbstractVoronImageView{	
	
	@Override
	public void init() {		
		if(isRestoreInstanceState){				
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
			myview.mImageMatrix.postTranslate(deltax, deltay);	
			updateImageValues();
			fitToView();	
		}		
	}	
	
	//MOTION EVENTS
	public boolean onTouch(MotionEvent event){
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
	            fitToView();	            
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
	    myview.mImageMatrix.postTranslate(deltaX, deltaY);		
	}

	@Override
	public void doScale(float scale) {
		myview.mImageMatrix.postScale(scale, scale, mid.x, mid.y);	
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
		myview.mImageMatrix.postTranslate(deltaX, deltaY);		
	}
}
