package org.voron.voronimageviewdemo;

import org.voron.voronimageview.EventObject;

public interface IListener {
	public void onSelectViewCommand(String doCommand);
	public void onUpdateMatrixValue(EventObject e);
}
