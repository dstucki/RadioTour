package ch.hsr.sa.radiotour.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
import ch.hsr.sa.radiotour.domain.Maillot;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.fragments.controller.AdminFragmentController;

public class AdminFragment extends Fragment {
	private AdminFragmentController controller;

	private ArrayAdapter<Stage> adapterForStageSpinner;
	private int lastClickedImportId;
	private RadioTour app;
	private RadioTourActivity activity;
	private EditText start, destination, distance;
	private Spinner stageSpinner;

	private final OnClickListener importListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			lastClickedImportId = v.getId();
			activity.showFileExplorerDialog(AdminFragment.this);
		}
	};
	private ListView maillot_lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.admin_fragment, container, false);
		activity = (RadioTourActivity) getActivity();
		app = (RadioTour) activity.getApplication();
		controller = new AdminFragmentController(this);
		assignTextFields(view);
		asssignListeners(view);
		createAndFillSpinner(view);
		createAndFillMaillots(view);

		return view;
	}

	private void createAndFillMaillots(View view) {
		maillot_lv = (ListView) view.findViewById(R.id.list_maillots);
		maillot_lv.setAdapter(new MaillotsListAdapter(activity,
				(ArrayList<Maillot>) controller.getMaillots()));
	}

	private void assignTextFields(View view) {
		start = (EditText) view.findViewById(R.id.edtxt_start_stage);
		destination = (EditText) view
				.findViewById(R.id.edtxt_destination_stage);
		distance = (EditText) view.findViewById(R.id.edtxt_distance_stage);
	}

	private void createAndFillSpinner(View view) {
		stageSpinner = (Spinner) view.findViewById(R.id.spinner1);
		adapterForStageSpinner = new ArrayAdapter<Stage>(getActivity(),
				android.R.layout.simple_spinner_item);
		adapterForStageSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterForStageSpinner.addAll(controller.getStages());
		stageSpinner.setAdapter(adapterForStageSpinner);
		stageSpinner.setSelection(adapterForStageSpinner.getPosition(app
				.getActualSelectedStage()));
		loadInformation(getStage());
		stageSpinner.setOnItemSelectedListener(new StageSelectionListener());
	}

	private void asssignListeners(View view) {
		view.findViewById(R.id.btn_new_stage).setOnClickListener(
				new NewStageListener());
		view.findViewById(R.id.btn_save_stage).setOnClickListener(
				new SaveStageListener());
		view.findViewById(R.id.btn_addmaillot).setOnClickListener(
				new AddMaillotListener());
		view.findViewById(R.id.btn_delete_stage).setOnClickListener(
				new DeleteListener());
		view.findViewById(R.id.btn_import_stage).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_import_match_table).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_import_driver).setOnClickListener(
				importListener);
		view.findViewById(R.id.btn_import_driver_time).setOnClickListener(
				importListener);
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

	public void setImportFile(final File file) {
		switch (lastClickedImportId) {
		case R.id.btn_import_stage:
			importStages(file);
			break;
		case R.id.btn_import_driver:
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						controller.importDriver(file, getStage());
					} catch (FileNotFoundException e) {
					}
				}
			}, "ImportDriver").start();
			break;
		case R.id.btn_import_driver_time:
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						controller.importRiderStageConnections(file);
					} catch (FileNotFoundException e) {
					}
				}
			}, "ImportRiderWithTime").start();
			break;
		case R.id.btn_import_match_table:
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						controller.importMarchTable(file, getStage());
					} catch (FileNotFoundException e) {
					}
				}
			}, "ImportMarchTable").start();
			break;
		default:
			break;
		}
	}

	public void importStages(File file) {
		try {
			controller.importStages(file);
		} catch (FileNotFoundException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
		adapterForStageSpinner.clear();
		adapterForStageSpinner.addAll(controller.getStages());
	}

	private Stage getStage() {
		return (Stage) stageSpinner.getSelectedItem();
	}

	public void newMaillotAdded(Maillot m) {
		((MaillotsListAdapter) maillot_lv.getAdapter()).add(m);
	}

	/*
	 * OnclickListeners
	 */
	private class SaveStageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			fillStageInformation(getStage());
			loadInformation(getStage());
			controller.update(getStage());
		}
	};

	private class NewStageListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			final Stage actualStage = controller.createStage();
			adapterForStageSpinner.add(actualStage);
			stageSpinner.setSelection(
					adapterForStageSpinner.getPosition(actualStage), true);
			loadInformation(actualStage);
		}
	};

	private class AddMaillotListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			activity.showMaillotDialog(AdminFragment.this);
		}
	};

	private class DeleteListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			controller.delete(getStage());
			adapterForStageSpinner.remove(getStage());
		}
	};

	/*
	 * ItemSelectedListener
	 */
	private class StageSelectionListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parentView,
				View selectedItemView, int position, long id) {
			activity.updateStage(getStage());
			controller.stageChanged(getStage());
			loadInformation(getStage());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

}