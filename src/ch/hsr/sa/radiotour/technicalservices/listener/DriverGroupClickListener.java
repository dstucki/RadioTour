package ch.hsr.sa.radiotour.technicalservices.listener;

import java.util.Observable;

import android.view.View;
import android.view.View.OnClickListener;

public class DriverGroupClickListener extends Observable implements
		OnClickListener {

	@Override
	public void onClick(View v) {
		this.setChanged();
		this.notifyObservers(v);
	}

}
