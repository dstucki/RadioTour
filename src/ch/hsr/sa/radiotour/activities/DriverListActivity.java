package ch.hsr.sa.radiotour.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.DriverListAdapter;
import ch.hsr.sa.radiotour.domain.BicycleRider;

public class DriverListActivity extends ListActivity {
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

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), "You Clicked an Item",
						Toast.LENGTH_SHORT).show();
			}
		});
		setListAdapter(new DriverListAdapter(this, R.layout.list_item,
				R.id.getName, RIDERS));
	}

	static final ArrayList<BicycleRider> RIDERS = new ArrayList<BicycleRider>();

}
