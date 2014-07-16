package org.voron.voronimageview;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View.BaseSavedState;

public class VoronSaveState extends BaseSavedState{	
	
float scaleFactor;
float rotateAngel;
float[] matrixValue;

int imageViewWidth;
int imageViewHeight;

boolean isViewPortret;

public VoronSaveState(Parcelable superState) {
	super(superState);
}

public VoronSaveState(Parcel source) {
	super(source);		
	scaleFactor = source.readFloat();
	rotateAngel = source.readFloat();		
	matrixValue = source.createFloatArray();
	imageViewWidth = source.readInt();
	imageViewHeight = source.readInt();
	isViewPortret = (Boolean)source.readValue(null);
}

@Override
public void writeToParcel(Parcel dest, int flags) {
	super.writeToParcel(dest, flags);		
	dest.writeFloat(scaleFactor);
	dest.writeFloat(rotateAngel);
	dest.writeFloatArray(matrixValue);
	dest.writeInt(imageViewWidth);
	dest.writeInt(imageViewHeight);
	dest.writeValue(isViewPortret);
}

public static final Parcelable.Creator<VoronSaveState> CREATOR = new Parcelable.Creator<VoronSaveState>() {

	@Override
	public VoronSaveState createFromParcel(Parcel source) {
		return new VoronSaveState(source);
	}
	@Override
	public VoronSaveState[] newArray(int size) {
		return new VoronSaveState[size];
	}
};	

}
