package ch.hsr.sa.radiotour.fragments;

import java.sql.SQLException;
import java.util.List;

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
import android.widget.Spinner;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.domain.Judgement;
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

	private final OnItemSelectedListener listener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			actualSpecialRanking = specialDatabaseDao.queryForAll().get(
					position);
			fillJudgements();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};
	private final OnItemSelectedListener listener2 = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			actualJudgement = judgementDatabaseDao.queryForAll().get(position);
			fillJudgementInfo();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	private EditText getEditText(int edtxtDistance) {
		return (EditText) v.findViewById(edtxtDistance);

	}

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
			llparent.addView(ll);
		}
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

		final Spinner secondSpinner = (Spinner) view
				.findViewById(R.id.spinner_judgement);

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

		view.findViewById(R.id.button_add_new_judgement).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						actualJudgement = new Judgement("Test"
								+ System.currentTimeMillis());
						if (actualSpecialRanking == null) {
							actualSpecialRanking = (SpecialRanking) spinner
									.getSelectedItem();
						}
						actualJudgement.setRanking(actualSpecialRanking);
						judgementDatabaseDao.create(actualJudgement);
						fillJudgements();
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
		queryBuilder.where().eq("specialranking", ranking);
		return judgementDatabaseDao.query(queryBuilder.prepare());
	}

	public void fillJudgements() {
		adapterForJudgementSpinner.clear();
		try {
			List<Judgement> test = getJudgement(actualSpecialRanking);
			Log.d(getClass().getSimpleName(), test.size() + " ");
			adapterForJudgementSpinner
					.addAll(getJudgement(actualSpecialRanking));
			Log.d(getClass().getSimpleName(), judgementDatabaseDao
					.queryForAll().size() + " ");

		} catch (SQLException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
	}
}
