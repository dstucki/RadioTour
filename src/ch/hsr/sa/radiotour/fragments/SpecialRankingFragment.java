package ch.hsr.sa.radiotour.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.SpecialRankingListAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Judgement;
import ch.hsr.sa.radiotour.domain.SpecialPointHolder;
import ch.hsr.sa.radiotour.domain.SpecialRanking;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

public class SpecialRankingFragment extends Fragment {
	private ArrayAdapter<SpecialRanking> adapterForSpecialRankingSpinner;
	private ArrayAdapter<Judgement> adapterForJudgementSpinner;
	private View v;
	private SpecialRanking actualSpecialRanking;
	private Judgement actualJudgement;
	private RuntimeExceptionDao<SpecialRanking, Integer> specialDatabaseDao;
	private RuntimeExceptionDao<Judgement, Integer> judgementDatabaseDao;
	private Spinner judgementSpinner;
	private RadioTourActivity activity;
	private RadioTour application;

	private final Comparator<SpecialPointHolder> comparator = new Comparator<SpecialPointHolder>() {

		@Override
		public int compare(SpecialPointHolder holder,
				SpecialPointHolder anotherHolder) {
			if (anotherHolder.getPointBoni() - holder.getPointBoni() != 0) {
				return anotherHolder.getPointBoni() - holder.getPointBoni();
			}
			if (anotherHolder.getTimeBoni() - holder.getTimeBoni() != 0) {
				return anotherHolder.getTimeBoni() - holder.getTimeBoni();
			}
			return anotherHolder.getRider().getStartNr()
					- holder.getRider().getStartNr();
		}
	};

	private final OnItemSelectedListener listener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			actualSpecialRanking = adapterForSpecialRankingSpinner
					.getItem(position);
			clearTextFields();
			fillJudgements();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			clearTextFields();
		}
	};

	public LinearLayout clearTextFields() {
		LinearLayout llparent = (LinearLayout) v
				.findViewById(R.id.llayout_driver_set);
		llparent.removeAllViews();
		return llparent;
	}

	private final OnItemSelectedListener listener2 = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			actualJudgement = adapterForJudgementSpinner.getItem(position);
			fillJudgementInfo();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			clearTextFields();
		}
	};

	private void fillJudgementInfo() {
		if (v == null) {
			return;
		}
		LinearLayout llparent = clearTextFields();
		for (int i = 0; i < actualJudgement.getNrOfWinningDrivers(); i++) {
			LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater()
					.inflate(R.layout.driverset_judgement, null);
			((TextView) ll.findViewById(R.id.txt_rank_in_words))
					.setText((i + 1) + ".");
			((EditText) ll.findViewById(R.id.edtxt_for_number_insert))
					.setText(actualJudgement.getWinningRiders().get(i) + "");
			llparent.addView(ll);
		}
	}

	private void saveJudgement() throws NullPointerException {
		ArrayList<Integer> tempArray = new ArrayList<Integer>();
		LinearLayout llparent = (LinearLayout) v
				.findViewById(R.id.llayout_driver_set);
		Set<Integer> tempSet = new HashSet<Integer>();

		for (int i = 0; i < actualJudgement.getNrOfWinningDrivers(); i++) {
			LinearLayout ll = (LinearLayout) llparent.getChildAt(i);
			((TextView) ll.findViewById(R.id.txt_rank_error_show)).setText("");
			int temp = 0;
			try {
				temp = Integer.valueOf(((EditText) ll
						.findViewById(R.id.edtxt_for_number_insert)).getText()
						.toString());
			} catch (Exception e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
				((TextView) ll.findViewById(R.id.txt_rank_error_show))
						.setText(getResources().getString(
								R.string.lb_invalidnumber));
			}
			if (temp != 0 && tempSet.contains(temp)) {
				((TextView) ll.findViewById(R.id.txt_rank_error_show))
						.setText(getResources().getString(
								R.string.lb_doubleassing));
				return;
			}
			tempSet.add(temp);
			tempArray.add(temp);

		}
		actualJudgement.setWinningRiders(tempArray);
		judgementDatabaseDao.update(actualJudgement);
		setVirtualRanking();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.special_ranking_fragment,
				container, false);
		final Spinner spinner = (Spinner) view
				.findViewById(R.id.spinner_special_ranking);

		judgementSpinner = (Spinner) view.findViewById(R.id.spinner_judgement);

		activity = ((RadioTourActivity) getActivity());

		application = (RadioTour) activity.getApplication();

		specialDatabaseDao = DatabaseHelper.getHelper(activity)
				.getSpecialRankingDao();
		judgementDatabaseDao = DatabaseHelper.getHelper(activity)
				.getJudgementDao();

		Button addSpecialRanking = (Button) view
				.findViewById(R.id.button_add_new_special_ranking);
		addSpecialRanking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.showSpecialRankingDialog(SpecialRankingFragment.this,
						null);
			}
		});
		Button editSpecialRanking = (Button) view
				.findViewById(R.id.button_edit_special_ranking);
		editSpecialRanking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.showSpecialRankingDialog(SpecialRankingFragment.this,
						(SpecialRanking) spinner.getSelectedItem());
			}
		});
		Button saveJudgement = (Button) view
				.findViewById(R.id.button_save_judgement);
		saveJudgement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					saveJudgement();
				} catch (NullPointerException e) {
					Log.e(getClass().getSimpleName(),
							e.getMessage() == null ? "NullPointer in Saving Judgemnent"
									: e.getMessage());
				}
			}
		});

		Button deleteRankingButton = (Button) view
				.findViewById(R.id.button_delete_special_ranking);
		deleteRankingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SpecialRanking temp = (SpecialRanking) spinner
						.getSelectedItem();
				adapterForJudgementSpinner.clear();
				judgementDatabaseDao.delete(getJudgement(temp));
				adapterForSpecialRankingSpinner.remove(temp);
				specialDatabaseDao.delete(temp);
			}
		});
		Button deleteJudgementButton = (Button) view
				.findViewById(R.id.button_delete_judgement);
		deleteJudgementButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Judgement temp = (Judgement) judgementSpinner.getSelectedItem();
				adapterForJudgementSpinner.remove(temp);
				judgementDatabaseDao.delete(temp);
				fillJudgements();
			}
		});

		view.findViewById(R.id.button_add_new_judgement).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						actualJudgement = new Judgement(getResources()
								.getString(R.string.lb_newjudgement), 12.5,
								application.getActualSelectedStage());
						if (actualSpecialRanking == null) {
							actualSpecialRanking = (SpecialRanking) spinner
									.getSelectedItem();
							if (actualSpecialRanking == null) {
								return;
							}
						}
						actualJudgement.setRanking(actualSpecialRanking);
						activity.showTextViewDialog(
								SpecialRankingFragment.this, actualJudgement);
					}
				});

		adapterForSpecialRankingSpinner = new ArrayAdapter<SpecialRanking>(
				getActivity(), android.R.layout.simple_spinner_item);
		adapterForSpecialRankingSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterForSpecialRankingSpinner
				.addAll(specialDatabaseDao.queryForAll());

		adapterForJudgementSpinner = new ArrayAdapter<Judgement>(getActivity(),
				android.R.layout.simple_spinner_item);
		adapterForJudgementSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		judgementSpinner.setAdapter(adapterForJudgementSpinner);
		spinner.setAdapter(adapterForSpecialRankingSpinner);
		spinner.setOnItemSelectedListener(listener);
		judgementSpinner.setOnItemSelectedListener(listener2);
		v = view;
		return view;
	}

	public void setSpecialRankingFromDialog(SpecialRanking ranking) {
		specialDatabaseDao.createOrUpdate(ranking);
		adapterForSpecialRankingSpinner.clear();
		adapterForSpecialRankingSpinner
				.addAll(specialDatabaseDao.queryForAll());
	}

	private List<Judgement> getJudgement(SpecialRanking ranking) {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("specialranking", ranking);
		map.put("etappe", ((RadioTour) getActivity().getApplication())
				.getActualSelectedStage());
		return judgementDatabaseDao.queryForFieldValues(map);
	}

	private void fillJudgements() {
		adapterForJudgementSpinner.clear();
		adapterForJudgementSpinner.addAll(getJudgement(actualSpecialRanking));
		judgementSpinner.setSelection(adapterForJudgementSpinner.getCount());
		setVirtualRanking();
	}

	private void setVirtualRanking() {
		ListView lv = (ListView) v
				.findViewById(R.id.listview_place_for_special_ranking);

		HashMap<Integer, SpecialPointHolder> map = new HashMap<Integer, SpecialPointHolder>();
		for (Judgement temp : judgementDatabaseDao.queryForEq("specialranking",
				actualSpecialRanking)) {

			for (int j = 0; j < temp.getNrOfWinningDrivers(); j++) {
				int tempRidernr = temp.getWinningRiders().get(j);
				if (tempRidernr == 0) {
					continue;
				}
				if (!map.containsKey(tempRidernr)) {
					SpecialPointHolder tempPointHolder = new SpecialPointHolder();
					tempPointHolder.setRider(application.getRider(tempRidernr));
					map.put(tempRidernr, tempPointHolder);
				}
				if (temp.isPointBoni()) {
					Integer pointBoni = temp.getPointBonis().get(j);
					Log.i(getClass().getSimpleName(), pointBoni + "");
					map.get(tempRidernr).addPointBoni(pointBoni);
				}
				if (temp.isTimeBoni()) {
					Integer timeBoni = temp.getTimeBonis().get(j);
					Log.i(getClass().getSimpleName(), timeBoni + "");

					map.get(tempRidernr).addTimeBoni(timeBoni);
				}

			}
		}
		if (lv.getHeaderViewsCount() == 0) {
			lv.addHeaderView(getActivity().getLayoutInflater().inflate(
					R.layout.textview_ranking_special_ranking, null));
		}
		ArrayList<SpecialPointHolder> temp = new ArrayList<SpecialPointHolder>(
				map.values());
		Collections.sort(temp, comparator);
		lv.setAdapter(new SpecialRankingListAdapter(getActivity(), temp));

	}

	public void nameChangedJudgement(Judgement judgement) {
		judgementDatabaseDao.create(judgement);
		adapterForJudgementSpinner.add(judgement);
		int postion = adapterForJudgementSpinner.getPosition(judgement);
		fillJudgements();
		judgementSpinner.setSelection(postion);
	}

}
