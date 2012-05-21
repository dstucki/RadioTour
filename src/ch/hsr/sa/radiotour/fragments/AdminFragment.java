package ch.hsr.sa.radiotour.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.MaillotsListAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.RiderState;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.technicalservices.importer.CSVReader;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

public class AdminFragment extends Fragment {

	private ArrayAdapter<Stage> adapterForStageSpinner;
	private int lastClickedImportId;
	private RadioTour app;
	private RadioTourActivity activity;
	private EditText start, destination, distance;
	private RuntimeExceptionDao<Stage, Integer> stageDbDao;
	private RuntimeExceptionDao<BicycleRider, Integer> riderDbDao;
	private RuntimeExceptionDao<Maillot, Integer> maillotDbDao;
	private Spinner stageSpinner;
	private final OnClickListener saveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Stage actualStage = (Stage) stageSpinner.getSelectedItem();
			fillStageInformation(actualStage);
			stageDbDao.update(actualStage);
		}
	};
	private final OnClickListener newListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Stage actualStage = new Stage("Start", "Ende");
			actualStage.setWholeDistance(150d);
			stageDbDao.create(actualStage);
			adapterForStageSpinner.add(actualStage);
			stageSpinner.setSelection(adapterForStageSpinner
					.getPosition(actualStage));
			loadInformation(actualStage);
		}
	};

	private final OnClickListener maillotListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			((RadioTourActivity) getActivity())
					.showMaillotDialog(AdminFragment.this);
		}
	};
	private final OnClickListener deleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Stage actualStage = (Stage) stageSpinner.getSelectedItem();
			adapterForStageSpinner.remove(actualStage);
			stageDbDao.delete(actualStage);
		}
	};

	private final OnItemSelectedListener stageListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			final Stage actualStage = adapterForStageSpinner.getItem(position);
			app.setActualSelectedStage(actualStage);
			loadInformation(actualStage);
			activity.updateStage(actualStage);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	private final OnClickListener importListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lastClickedImportId = v.getId();
			activity.showFileExplorerDialog(AdminFragment.this);
		}
	};
	private RuntimeExceptionDao<PointOfRace, Integer> pointOfRaceDao;
	private ListView maillot_lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.admin_fragment, container, false);
		activity = (RadioTourActivity) getActivity();
		app = (RadioTour) activity.getApplication();
		stageDbDao = activity.getHelper().getStageDao();
		riderDbDao = activity.getHelper().getBicycleRiderDao();
		maillotDbDao = activity.getHelper().getMaillotRuntimeDao();
		stageSpinner = (Spinner) view.findViewById(R.id.spinner1);
		start = (EditText) view.findViewById(R.id.edtxt_start_stage);
		destination = (EditText) view
				.findViewById(R.id.edtxt_destination_stage);
		distance = (EditText) view.findViewById(R.id.edtxt_distance_stage);

		view.findViewById(R.id.btn_new_stage).setOnClickListener(newListener);
		view.findViewById(R.id.btn_save_stage).setOnClickListener(saveListener);
		view.findViewById(R.id.btn_delete_stage).setOnClickListener(
				deleteListener);
		view.findViewById(R.id.btn_import_stage).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_import_match_table).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_import_driver).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_import_driver_time).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_addmaillot).setOnClickListener(
				maillotListener);

		adapterForStageSpinner = new ArrayAdapter<Stage>(getActivity(),
				android.R.layout.simple_spinner_item);
		adapterForStageSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterForStageSpinner.addAll(activity.getHelper().getStageDao()
				.queryForAll());
		stageSpinner.setAdapter(adapterForStageSpinner);
		stageSpinner.setSelection(adapterForStageSpinner.getPosition(app
				.getActualSelectedStage()));
		loadInformation((Stage) stageSpinner.getSelectedItem());
		stageSpinner.setOnItemSelectedListener(stageListener);

		maillot_lv = (ListView) view.findViewById(R.id.list_maillots);
		maillot_lv.setAdapter(new MaillotsListAdapter(activity,
				(ArrayList<Maillot>) activity.getHelper()
						.getMaillotRuntimeDao().queryForAll()));

		return view;
	}

	private void loadInformation(Stage selectedItem) {
		if (selectedItem != null) {
			start.setText(selectedItem.getStart());
			destination.setText(selectedItem.getDestination());
			distance.setText(selectedItem.getWholeDistance() + "");
		}
	}

	private void fillStageInformation(Stage selectedItem) {
		selectedItem.setStart(start.getText().toString());
		selectedItem.setDestination(destination.getText().toString());
		selectedItem.setWholeDistance(Double.valueOf(distance.getText()
				.toString()));
	}

	public void setImportFile(File file) {
		switch (lastClickedImportId) {
		case R.id.btn_import_stage:
			importStages(file);
			break;
		case R.id.btn_import_driver:
			importDriver(file);
			break;
		case R.id.btn_import_driver_time:
			importDriverWithTime(file);
			break;
		case R.id.btn_import_match_table:
			importMarchTable(file);
			break;
		default:
			break;
		}
	}

	private void importMarchTable(File file) {
		try {
			pointOfRaceDao = activity.getHelper().getPointOfRaceDao();
			QueryBuilder<PointOfRace, Integer> queryBuilder = pointOfRaceDao
					.queryBuilder();
			queryBuilder.where().eq("etappe", stageSpinner.getSelectedItem());
			pointOfRaceDao.delete(pointOfRaceDao.query(queryBuilder.prepare()));

			CSVReader reader = new CSVReader(new FileInputStream(file));
			PointOfRace point;
			for (String[] line : reader.readFile()) {
				point = convertStringArrayToPoint(line);
				point.setStage((Stage) stageSpinner.getSelectedItem());
				pointOfRaceDao.create(point);
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}

	}

	private PointOfRace convertStringArrayToPoint(String[] pointAsString) {
		int altitude = Integer.valueOf(pointAsString[0]);
		String name = pointAsString[3];
		double distance = Double.valueOf(pointAsString[1]);
		Date date = null;
		try {
			date = new SimpleDateFormat("HH:mm").parse(pointAsString[6]);
		} catch (ParseException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}

		return new PointOfRace(altitude, distance, name, date, 0);
	}

	private void importDriver(File file) {
		try {
			TableUtils.dropTable(activity.getHelper().getConnectionSource(),
					BicycleRider.class, true);
			TableUtils.createTable(activity.getHelper().getConnectionSource(),
					BicycleRider.class);
		} catch (SQLException e1) {
			Log.e(getClass().getSimpleName(), e1.getMessage());
		}
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());

		}
		BicycleRider bicycleRider;
		for (String[] riderAsString : reader.readFile()) {
			bicycleRider = new BicycleRider(Integer.valueOf(riderAsString[0]),
					riderAsString[1], riderAsString[2], riderAsString[3], "");
			bicycleRider.setRiderState(RiderState.ACTIV);
			riderDbDao.create(bicycleRider);
			app.add(bicycleRider);
		}

	}

	private void importDriverWithTime(File file) {
		// TODO Auto-generated method stub

	}

	public void importStages(File file) {
		try {
			TableUtils.dropTable(activity.getHelper().getConnectionSource(),
					Stage.class, true);
			TableUtils.createTable(activity.getHelper().getConnectionSource(),
					Stage.class);
		} catch (SQLException e1) {
			Log.e(getClass().getSimpleName(), e1.getMessage());
		}
		try {
			CSVReader reader = new CSVReader(new FileInputStream(file));
			Stage temp;
			for (String[] array : reader.readFile()) {
				temp = new Stage(array[0], array[1]);
				temp.setWholeDistance(Double.valueOf(array[2]));
				stageDbDao.create(temp);
			}
			adapterForStageSpinner.clear();
			adapterForStageSpinner.addAll(stageDbDao.queryForAll());
		} catch (FileNotFoundException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
	}

	public void newMaillotAdded(Maillot m) {
		((MaillotsListAdapter) maillot_lv.getAdapter()).add(m);
	}

}