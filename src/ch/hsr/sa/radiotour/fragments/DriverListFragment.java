package ch.hsr.sa.radiotour.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.BicycleRider;

public class DriverListFragment extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayAdapter<BicycleRider> adapter = new ArrayAdapter<BicycleRider>(
				getActivity(), android.R.layout.simple_list_item_1,
				((RadioTour) getActivity().getApplication()).getRiders());
		setListAdapter(adapter);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getActivity(), "On long click listener",
						Toast.LENGTH_LONG).show();
				return true;
			}
		});
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
