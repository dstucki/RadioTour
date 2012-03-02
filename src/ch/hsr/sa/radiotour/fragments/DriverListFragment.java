package ch.hsr.sa.radiotour.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.BicycleRider;

public class DriverListFragment extends ListFragment{
	private static final ArrayList<BicycleRider> RIDERS = new ArrayList<BicycleRider>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RIDERS.add(new BicycleRider(1,"Florian Bentele", "Team SUI", "TSU",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(2,"Florian Bentele2", "Team SUI1", "TSU2",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(3,"Florian Bentele3", "Team SUI2", "TSU3",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(4,"Florian Bentele4", "Team SUI3", "TSU4",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(5,"Florian Bentele5", "Team SUI4", "TSU5",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(6,"Florian Bentele6", "Team SUI5", "TSU6",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(7,"Florian Bentele7", "Team SUI6", "TSU7",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));
		RIDERS.add(new BicycleRider(8,"Florian Bentele8", "Team SUI7", "TSU8",
				"SUI", new Date()));

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//				"Linux", "OS/2" };
		ArrayAdapter<BicycleRider> adapter = new ArrayAdapter<BicycleRider>(getActivity(),
				android.R.layout.simple_list_item_1, RIDERS);
		setListAdapter(adapter);
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		BicycleRider item = ((BicycleRider) getListAdapter().getItem(position));
		DriverDetailFragment fragment = (DriverDetailFragment) getFragmentManager()
				.findFragmentById(R.id.detailFragment);
		if (fragment != null && fragment.isInLayout()) {
			fragment.setDriver(item);
		} else {
			Intent intent = new Intent(getActivity().getApplicationContext(),
		RadioTourActivity.class);
			intent.putExtra("value", item.getName());
			startActivity(intent);

		}

	}
}
