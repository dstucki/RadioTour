package ch.hsr.sa.radiotour.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

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
import android.widget.Spinner;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.technicalservices.importer.CSVReader;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;

public class AdminFragment extends Fragment {

	private ArrayAdapter<Stage> adapterForStageSpinner;
	private RadioTour app;
	private RadioTourActivity activity;
	private EditText start, destination, distance;
	private RuntimeExceptionDao<Stage, Integer> stageDbDao;
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
			activity.showFileExplorerDialog(AdminFragment.this);

		}
	};

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
		View view = inflater.inflate(R.layout.admin_fragment, container, false);
		activity = (RadioTourActivity) getActivity();
		app = (RadioTour) activity.getApplication();
		stageDbDao = ((RadioTourActivity) getActivity()).getHelper()
				.getStageDao();
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
		return view;
	}

	private void loadInformation(Stage selectedItem) {
		start.setText(selectedItem.getStart());
		destination.setText(selectedItem.getDestination());
		distance.setText(selectedItem.getWholeDistance() + "");
	}

	private void fillStageInformation(Stage selectedItem) {
		selectedItem.setStart(start.getText().toString());
		selectedItem.setDestination(destination.getText().toString());
		selectedItem.setWholeDistance(Double.valueOf(distance.getText()
				.toString()));
	}

	public void setImportFile(File file) {
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
}