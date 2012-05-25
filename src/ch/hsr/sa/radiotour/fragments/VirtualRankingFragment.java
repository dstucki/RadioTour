package ch.hsr.sa.radiotour.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.VirtualRankingAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.sorting.RiderSortStrategy;

public class VirtualRankingFragment extends ListFragment {
	private final HashMap<Integer, RiderSortStrategy> map = new HashMap<Integer, RiderSortStrategy>();
	private TextView latestSort;
	private VirtualRankingAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initializeResourceStrategyMap();

		setSortableHeaderview();
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				if (position > 0) {
					((RadioTourActivity) getActivity()).showRiderDialog(
							(RiderStageConnection) parent.getAdapter().getItem(
									position), adapter);
				}
			}

		});
		adapter = new VirtualRankingAdapter(getActivity(),
				R.layout.picklist_item, R.id.startNr1,
				new ArrayList<RiderStageConnection>(((RadioTour) getActivity()
						.getApplication()).getRiderPerStage().values()));
		setListAdapter(adapter);

	}

	private void initializeResourceStrategyMap() {
		map.put(R.id.startnummer, new RiderSortStrategy.SortByStartNr());
		map.put(R.id.fahrername, new RiderSortStrategy.SortByName());
		map.put(R.id.team, new RiderSortStrategy.SortByTeam());
		map.put(R.id.virtualRank, new RiderSortStrategy.SortByOfficialRank());
		map.put(R.id.timeBoni, new RiderSortStrategy.SortByTimeBoni());
		map.put(R.id.timeOfficial, new RiderSortStrategy.SortByOfficialTime());
		map.put(R.id.handicapOfficial,
				new RiderSortStrategy.SortByOfficialHandicap());
		map.put(R.id.virtualDeficit,
				new RiderSortStrategy.SortByVirtualDeficit());
		map.put(R.id.land_image, new RiderSortStrategy.SortByCountry());
		map.put(R.id.land_text, new RiderSortStrategy.SortByCountry());
	}

	private void setSortableHeaderview() {

		View v = getActivity().getLayoutInflater().inflate(
				R.layout.virtual_ranking_item, null);
		latestSort = (TextView) v.findViewById(R.id.startnummer);
		latestSort.setTypeface(Typeface.DEFAULT_BOLD);
		OnClickListener listener = getClickListener();

		for (int i : map.keySet()) {
			try {
				v.findViewById(i).setOnClickListener(listener);
			} catch (NullPointerException e) {
				Log.d(getClass().getSimpleName(), e.getMessage()
						+ " occured, because of missing rider information");
			}
		}

		ImageView image = (ImageView) v.findViewById(R.id.land_image);
		image.setImageDrawable(null);
		((TextView) v.findViewById(R.id.land_text)).setText(R.string.land);

		getListView().addHeaderView(v);

	}

	private OnClickListener getClickListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				latestSort.setTypeface(Typeface.DEFAULT);
				((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
				latestSort = (TextView) v;
				RiderSortStrategy strategy = map.get(v.getId());
				try {
					adapter.sort(strategy);
				} catch (NullPointerException e) {
					Log.d(getClass().getSimpleName(), e.getMessage()
							+ " occured");
				}
				strategy.switchAscending();

			}
		};
	}
}