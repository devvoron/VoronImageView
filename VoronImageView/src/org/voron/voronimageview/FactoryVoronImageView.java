package org.voron.voronimageview;

public class FactoryVoronImageView {
	public IVoronImageView getVoronImageView(int type){		
		if (type==90) {
			return new VoronIV90R();
		}else if (type==1) {
			return new VoronIVNoR();
		}else if (type==0) {
			return new VoronIVUnbound();
		}
		return null;
	}
}
