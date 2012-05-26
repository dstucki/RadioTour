package ch.hsr.sa.radiotour.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import android.widget.Toast;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.MaillotsListAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.Rider;
import ch.hsr.sa.radiotour.domain.RiderStageConnection;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;
import ch.hsr.sa.radiotour.technicalservices.importer.CSVReader;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class AdminFragment extends Fragment {

	private ArrayAdapter<Stage> adapterForStageSpinner;
	private int lastClickedImportId;
	private RadioTour app;
	private RadioTourActivity activity;
	private EditText start, destination, distance;
	private RuntimeExceptionDao<Stage, Integer> stageDbDao;
	private RuntimeExceptionDao<Rider, Integer> riderDbDao;
	private RuntimeExceptionDao<RiderStageConnection, Integer> riderStageDao;
	private DatabaseHelper helper;
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
			RiderStageConnection conn;
			for (Rider rider : app.getRiders()) {
				conn = new RiderStageConnection(actualStage, rider);
				riderStageDao.create(conn);
			}
			loadInformation(actualStage);
		}
	};

	private final OnClickListener addMaillotListener = new OnClickListener() {
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
			riderStageDao.delete(riderStageDao
					.queryForEq("etappe", actualStage));
		}
	};

	private final OnItemSelectedListener stageListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			final Stage actualStage = adapterForStageSpinner.getItem(position);
			activity.updateStage(actualStage);
			app.getRiderPerStage().clear();
			List<RiderStageConnection> conns = riderStageDao.queryForEq(
					"etappe", actualStage);
			if (conns.size() > 0) {
				for (RiderStageConnection conn : riderStageDao.queryForEq(
						"etappe", actualStage)) {
					app.add(conn);
				}
			} else {
				for (Rider rider : app.getRiders()) {
					final RiderStageConnection tempConn = new RiderStageConnection(
							actualStage, rider);
					new Thread(new Runnable() {
						@Override
						public void run() {
							riderStageDao.create(tempConn);
						}
					}).start();
				}
			}
			loadInformation(actualStage);
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
		helper = DatabaseHelper.getHelper(activity);
		stageDbDao = helper.getStageDao();
		riderDbDao = helper.getBicycleRiderDao();
		riderStageDao = helper.getRiderStageDao();
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
				addMaillotListener);

		adapterForStageSpinner = new ArrayAdapter<Stage>(getActivity(),
				android.R.layout.simple_spinner_item);
		adapterForStageSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterForStageSpinner.addAll(helper.getStageDao().queryForAll());
		stageSpinner.setAdapter(adapterForStageSpinner);
		stageSpinner.setSelection(adapterForStageSpinner.getPosition(app
				.getActualSelectedStage()));
		loadInformation((Stage) stageSpinner.getSelectedItem());
		stageSpinner.setOnItemSelectedListener(stageListener);

		maillot_lv = (ListView) view.findViewById(R.id.list_maillots);
		maillot_lv.setAdapter(new MaillotsListAdapter(activity,
				(ArrayList<Maillot>) helper.getMaillotRuntimeDao()
						.queryForAll()));

		return view;
	}

	private void loadInformation(Stage selectedItem) {
		if (selectedItem != null) {
			start.setText(selectedItem.getStart());
			destination.setText(selectedItem.getDestination());
			distance.setText(String.valueOf(selectedItem.getWholeDistance()));
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
			pointOfRaceDao = helper.getPointOfRaceDao();
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
		List<String[]> list;
		try {
			list = getArrayListFromCSV(file);
		} catch (FileNotFoundException e) {
			return;
		}
		dropAndCreateTable(Rider.class, RiderStageConnection.class);

		Rider bicycleRider;
		for (String[] riderAsString : list) {
			Team team = app.getTeam(riderAsString[2]) == null ? new Team(
					riderAsString[2]) : app.getTeam(riderAsString[2]);
			bicycleRider = new Rider(Integer.valueOf(riderAsString[0]),
					riderAsString[1], team);
			riderDbDao.create(bicycleRider);
			helper.getTeamDao().createOrUpdate(team);
			final RiderStageConnection conn = new RiderStageConnection(
					app.getActualSelectedStage(), bicycleRider);
			riderStageDao.create(conn);
			app.add(bicycleRider);
			app.add(conn);
		}
	}

	private ArrayList<String[]> getArrayListFromCSV(File file)
			throws FileNotFoundException {
		CSVReader reader = new CSVReader(new FileInputStream(file));
		return reader.readFile();
	}

	private void importDriverWithTime(File file) {

		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
		RiderStageConnection conn;
		List<String[]> list;
		try {
			list = getArrayListFromCSV(file);
		} catch (FileNotFoundException e1) {
			return;
		}
		String[] riderStageString1 = list.get(0);
		Calendar leaderOfficialTime = Calendar.getInstance(TimeZone
				.getDefault());
		try {
			leaderOfficialTime.setTime(formater.parse(riderStageString1[4]));

		} catch (ParseException e) {
			Toast.makeText(app, e.getMessage(), Toast.LENGTH_SHORT);
			return;
		}

		conn = app.getRiderStage(Integer.valueOf(riderStageString1[1]));
		conn.setOfficialDeficit(new Date(0, 0, 0, 0, 0, 0));
		conn.setOfficialTime(leaderOfficialTime.getTime());
		conn.setOfficialRank(Integer.valueOf(riderStageString1[0]));
		riderStageDao.update(conn);
		Date followerOfficialDeficit;

		for (String[] riderStageString : list.subList(1, list.size())) {
			try {
				followerOfficialDeficit = formater.parse(riderStageString[4]);
			} catch (ParseException e) {
				Toast.makeText(app, e.getMessage(), Toast.LENGTH_SHORT);
				continue;
			}
			conn = app.getRiderStage(Integer.valueOf(riderStageString[1]));
			conn.setOfficialDeficit(followerOfficialDeficit);

			Calendar followerOfficialTime = (Calendar) leaderOfficialTime
					.clone();
			followerOfficialTime.add(Calendar.HOUR_OF_DAY,
					followerOfficialDeficit.getHours());
			followerOfficialTime.add(Calendar.MINUTE,
					followerOfficialDeficit.getMinutes());
			followerOfficialTime.add(Calendar.SECOND,
					followerOfficialDeficit.getSeconds());
			conn.setOfficialTime(followerOfficialTime.getTime());
			conn.setOfficialRank(Integer.valueOf(riderStageString[0]));
			riderStageDao.update(conn);
		}

	}

	public void importStages(File file) {
		try {
			dropAndCreateTable(Stage.class, RiderStageConnection.class);

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

	private void dropAndCreateTable(Class<?>... classes) {
		ConnectionSource src = helper.getConnectionSource();
		for (int i = 0; i < classes.length; i++) {
			try {
				TableUtils.dropTable(src, classes[i], true);
				TableUtils.createTable(src, classes[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}