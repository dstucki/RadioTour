package ch.hsr.sa.radiotour.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class SpecialRakingFragment extends Fragment {
	private ArrayAdapter<SpecialRanking> adapterForSpecialRankingSpinner;
	private ArrayAdapter<Judgement> adapterForJudgementSpinner;
	private View v;
	private SpecialRanking actualSpecialRanking;
	private Judgement actualJudgement;
	private RuntimeExceptionDao<SpecialRanking, Integer> specialDatabaseDao;
	private RuntimeExceptionDao<Judgement, Integer> judgementDatabaseDao;

	private final Comparator<SpecialPointHolder> comparator = new Comparator<SpecialPointHolder>() {

		@Override
		public int compare(SpecialPointHolder object2,
				SpecialPointHolder object1) {
			if (object1.getPointBoni() - object2.getPointBoni() != 0) {
				return object1.getPointBoni() - object2.getPointBoni();
			}
			if (object1.getTimeBoni() - object2.getTimeBoni() != 0) {
				return object1.getTimeBoni() - object2.getTimeBoni();
			}
			return object1.getRider() - object2.getRider();
		}
	};
	private final OnItemSelectedListener listener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			actualSpecialRanking = adapterForSpecialRankingSpinner
					.getItem(position);
			LinearLayout llparent = (LinearLayout) v
					.findViewById(R.id.llayout_driver_set);
			llparent.removeAllViews();
			fillJudgements();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			LinearLayout llparent = (LinearLayout) v
					.findViewById(R.id.llayout_driver_set);
			llparent.removeAllViews();

		}
	};
	private final OnItemSelectedListener listener2 = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			actualJudgement = adapterForJudgementSpinner.getItem(position);
			fillJudgementInfo();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			LinearLayout llparent = (LinearLayout) v
					.findViewById(R.id.llayout_driver_set);
			llparent.removeAllViews();

		}
	};
	private Spinner secondSpinner;

	private void fillJudgementInfo() {
		if (v == null) {
			return;
		}
		LinearLayout llparent = (LinearLayout) v
				.findViewById(R.id.llayout_driver_set);
		llparent.removeAllViews();
		for (int i = 0; i < actualSpecialRanking.getNrOfWinningDrivers(); i++) {
			LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater()
					.inflate(R.layout.driverset_judgement, null);
			((TextView) ll.findViewById(R.id.txt_rank_in_words)).setText(i + 1
					+ ".");
			((EditText) ll.findViewById(R.id.edtxt_for_number_insert))
					.setText(actualJudgement.getWinningRiders()[i] + "");
			llparent.addView(ll);
		}
	}

	private void saveJudgement() throws NullPointerException {
		int[] tempArray = new int[actualSpecialRanking.getNrOfWinningDrivers()];
		LinearLayout llparent = (LinearLayout) v
				.findViewById(R.id.llayout_driver_set);
		Set<Integer> tempSet = new HashSet<Integer>();

		for (int i = 0; i < actualSpecialRanking.getNrOfWinningDrivers(); i++) {
			LinearLayout ll = (LinearLayout) llparent.getChildAt(i);
			((TextView) ll.findViewById(R.id.txt_rank_error_show)).setText("");
			int temp = 0;
			try {
				temp = Integer.valueOf(((EditText) ll
						.findViewById(R.id.edtxt_for_number_insert)).getText()
						.toString());
			} catch (Exception e) {
				((TextView) ll.findViewById(R.id.txt_rank_error_show))
						.setText("Ungültige Nummer, Speicherung abgebrochen");
			}
			Log.d(getClass().getSimpleName(), "before duplicate");
			if (temp != 0 && tempSet.contains(temp)) {
				Log.d(getClass().getSimpleName(), "Duplicate badibadi");
				((TextView) ll.findViewById(R.id.txt_rank_error_show))
						.setText("Mehrfachzuweisung, Speicherung abgebrochen");
				return;
			}
			tempSet.add(temp);
			tempArray[i] = temp;

		}
		actualJudgement.setWinningRiders(tempArray);
		judgementDatabaseDao.update(actualJudgement);
		setVirtualRanking();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.special_ranking_fragment,
				container, false);
		final Spinner spinner = (Spinner) view
				.findViewById(R.id.spinner_special_ranking);

		secondSpinner = (Spinner) view.findViewById(R.id.spinner_judgement);

		specialDatabaseDao = ((RadioTourActivity) getActivity()).getHelper()
				.getSpecialRankingDao();
		judgementDatabaseDao = ((RadioTourActivity) getActivity()).getHelper()
				.getJudgementDao();

		Button addSpecialRanking = (Button) view
				.findViewById(R.id.button_add_new_special_ranking);
		addSpecialRanking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((RadioTourActivity) getActivity()).showSpecialRankingDialog(
						SpecialRakingFragment.this, null);
			}
		});
		Button editSpecialRanking = (Button) view
				.findViewById(R.id.button_edit_special_ranking);
		editSpecialRanking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((RadioTourActivity) getActivity()).showSpecialRankingDialog(
						SpecialRakingFragment.this,
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
					Log.e(getClass().getSimpleName(), "");
				}
			}

		});

		Button deleButton = (Button) view
				.findViewById(R.id.button_delete_special_ranking);
		deleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SpecialRanking temp = (SpecialRanking) spinner
						.getSelectedItem();
				try {
					for (Judgement judgement : getJudgement(temp)) {
						adapterForJudgementSpinner.remove(judgement);
						judgementDatabaseDao.delete(judgement);
					}
					adapterForSpecialRankingSpinner.remove(temp);
					specialDatabaseDao.delete(temp);

				} catch (SQLException e) {
					Log.e(getClass().getSimpleName(), e.getMessage());
				}

			}
		});
		Button deleButton2 = (Button) view
				.findViewById(R.id.button_delete_judgement);
		deleButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Judgement temp = (Judgement) secondSpinner.getSelectedItem();
				adapterForJudgementSpinner.remove(temp);
				judgementDatabaseDao.delete(temp);
				fillJudgements();
			}
		});

		view.findViewById(R.id.button_add_new_judgement).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						actualJudgement = new Judgement("Neue Wertung", 12.5,
								((RadioTour) getActivity().getApplication())
										.getActualSelectedStage());
						if (actualSpecialRanking == null) {
							actualSpecialRanking = (SpecialRanking) spinner
									.getSelectedItem();
						}
						actualJudgement.setRanking(actualSpecialRanking);
						((RadioTourActivity) getActivity()).showTextViewDialog(
								SpecialRakingFragment.this, actualJudgement);

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

		secondSpinner.setAdapter(adapterForJudgementSpinner);
		spinner.setAdapter(adapterForSpecialRankingSpinner);
		spinner.setOnItemSelectedListener(listener);
		secondSpinner.setOnItemSelectedListener(listener2);
		v = view;
		return view;
	}

	public void setSpecialRankingFromDialog(SpecialRanking ranking) {
		specialDatabaseDao.createOrUpdate(ranking);
		adapterForSpecialRankingSpinner.clear();
		adapterForSpecialRankingSpinner
				.addAll(specialDatabaseDao.queryForAll());
	}

	public List<Judgement> getJudgement(SpecialRanking ranking)
			throws SQLException {
		QueryBuilder<Judgement, Integer> queryBuilder = judgementDatabaseDao
				.queryBuilder();
		queryBuilder
				.where()
				.eq("specialranking", ranking)
				.and()
				.eq("etappe",
						((RadioTour) getActivity().getApplication())
								.getActualSelectedStage());
		return judgementDatabaseDao.query(queryBuilder.prepare());
	}

	public void fillJudgements() {
		adapterForJudgementSpinner.clear();
		try {
			adapterForJudgementSpinner
					.addAll(getJudgement(actualSpecialRanking));
			secondSpinner.setSelection(adapterForJudgementSpinner.getCount());
		} catch (SQLException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
		setVirtualRanking();
	}

	private void setVirtualRanking() {
		ListView lv = (ListView) v
				.findViewById(R.id.listview_place_for_special_ranking);

		HashMap<Integer, SpecialPointHolder> map = new HashMap<Integer, SpecialPointHolder>();
		for (int i = 0; i < adapterForJudgementSpinner.getCount(); i++) {
			Judgement temp = adapterForJudgementSpinner.getItem(i);
			for (int j = 0; j < temp.getRanking().getNrOfWinningDrivers(); j++) {
				int tempRidernr = temp.getWinningRiders()[j];
				if (tempRidernr == 0) {
					continue;
				}
				if (!map.containsKey(tempRidernr)) {
					SpecialPointHolder tempPointHolder = new SpecialPointHolder();
					tempPointHolder.setRider(tempRidernr);
					map.put(tempRidernr, tempPointHolder);
				}
				if (temp.getRanking().isPointBoni()) {
					map.get(tempRidernr).addPointBoni(
							temp.getRanking().getPointBonis().get(j));
				}
				if (temp.getRanking().isTimeBoni()) {
					map.get(tempRidernr).addTimeBoni(
							temp.getRanking().getTimeBonis().get(j));
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
		secondSpinner.setSelection(postion);
	}

}
