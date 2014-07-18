package org.voron.voronimageview.utils;

import android.content.Context;

public class ImageDetails {
	
	private int widthView = 400;
	private int heightView = 395;
	private float viewRatio;
	private int dencityScreen;
	private long memmory = 160000000;
	
	private int imageWidth;
	private int imageHeight;
	private float imageRatio;
	private int sampleSize = 1;
	private boolean isRotation = false;
	private boolean isControlMode = false;
	private float minScale = 1.0f;
	private float minScaleRot = 1.f;
	private float maxScale = 1.0f;
	private String samleSizeAttr = "large";
	private int widthPadding = 0;
	
	public String getSamleSizeAttr() {
		return samleSizeAttr;
	}

	public void setSamleSizeAttr(String samleSizeAttr) {
		this.samleSizeAttr = samleSizeAttr;
	}

	public void setScreenInfo(int width, int dencity){
		this.widthView = width;
		this.dencityScreen = dencity;
	}
	
	public void setImageDetails(int width, int height){
		if(width >= height) imageRatio = (float)width/(float)height;
		if(width < height) imageRatio = (float)height/(float)width;
		
		if(widthView >= heightView) viewRatio = (float)widthView/(float)heightView;
		if(widthView < heightView) viewRatio = (float)heightView/(float)widthView;
		//samlesize
		if(samleSizeAttr.contains("small")){
			if(width >= height){
				while (width/(sampleSize*2)>(widthView - widthPadding)){
					sampleSize*=2;
				}
				isRotation = false;
			}else {
				while (height/(sampleSize*2)>(widthView - widthPadding)){
					sampleSize*=2;
				}
				isRotation = true;
			}
		}else if (samleSizeAttr.contains("middle")) {
			if(width >= height){
				while (width/(sampleSize*2)>(widthView - widthPadding)*2){
					sampleSize*=2;
				}
				isRotation = false;
			}else {
				while (height/(sampleSize*2)>(widthView - widthPadding)*2){
					sampleSize*=2;
				}
				isRotation = true;
			}
		}else if (samleSizeAttr.contains("large")) {
			sampleSize = 1;
		}
		if(width >= height){
			minScale = (float)(widthView - widthPadding)/(float)(width/sampleSize);	
			if(imageRatio>viewRatio)minScale = minScale * imageRatio/viewRatio;
			minScaleRot = (float)(widthView - widthPadding)/(float)(height/sampleSize);	
			if(imageRatio>viewRatio)minScaleRot = minScaleRot * imageRatio/viewRatio;
		}else{
			minScale = (float)(widthView - widthPadding)/(float)(height/sampleSize);
			if(imageRatio>viewRatio)minScale = minScale * imageRatio/viewRatio;//NB! not tested
			minScaleRot = (float)(widthView - widthPadding)/(float)(width/sampleSize);	
			if(imageRatio>viewRatio)minScaleRot = minScaleRot * imageRatio/viewRatio;
		}
		
	}
	
	public void setImageViewDim(int widthView, int heightView){
		this.widthView = widthView;
		this.heightView = heightView;
	}
	
	public int getHeightView() {
		return heightView;
	}

	public void setHeightView(int heightView) {
		this.heightView = heightView;
	}
	
	public int getwidthView() {
		return widthView;
	}
	public void setwidthView(int widthView) {
		this.widthView = widthView;
	}
	public int getDencityScreen() {
		return dencityScreen;
	}
	public void setDencityScreen(int dencityScreen) {
		this.dencityScreen = dencityScreen;
	}
	public long getMemmory() {
		return memmory;
	}
	public void setMemmory(long memmory) {
		this.memmory = memmory;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public float getImageRatio() {
		return imageRatio;
	}
	public void setImageRatio(float imageRatio) {
		this.imageRatio = imageRatio;
	}
	public int getSampleSize() {
		return sampleSize;
	}
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	public boolean isRotation() {
		return isRotation;
	}
	public void setRotation(boolean isRotation) {
		this.isRotation = isRotation;
	}
	public boolean isControlMode() {
		return isControlMode;
	}
	public void setControlMode(boolean isControlMode) {
		this.isControlMode = isControlMode;
	}
	public float getMinScale() {
		return minScale;
	}
	public void setMinScale(float minScale) {
		this.minScale = minScale;
	}
	
	public float getMinScaleRot() {
		return minScaleRot;
	}

	public void setMinScaleRot(float minScaleRot) {
		this.minScaleRot = minScaleRot;
	}

	public float getMaxScale() {
		return maxScale;
	}
	public void setMaxScale(float maxScale) {
		this.maxScale = maxScale;
	}
	public int getImageViewWidth(){
		return widthView - widthPadding;
	}
	public float getImageViewHeight(){		
		//return (widthView - widthPadding)/imageRatio;
		return 395;
	}
	//util
	public float convertDpToPixel(float dp){	    
	    float px = dp * (dencityScreen/160f);
	    return px;
	}
	
	
}
