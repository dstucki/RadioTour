package ch.hsr.sa.radiotour.dialogs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.adapter.PointsOfRaceAdapter;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.technicalservices.database.DatabaseHelper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

public class MarchTableDialog extends DialogFragment {
	private View v;
	private final Stage stage;
	private final RuntimeExceptionDao<PointOfRace, Integer> pointOfRaceDao;

	public MarchTableDialog(Stage acStage) {
		stage = acStage;
		pointOfRaceDao = DatabaseHelper.getHelper(getActivity())
				.getPointOfRaceDao();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.march_table));
		v = inflater.inflate(R.layout.march_table_dialog, container, false);

		ListView lv = (ListView) v.findViewById(R.id.listView_march_table);

		try {
			ArrayList<PointOfRace> points = (ArrayList<PointOfRace>) getPoints(stage);
			lv.setAdapter(getAdapter(points));

		} catch (SQLException e) {
			Toast.makeText(getActivity(), getString(R.string.err_marchtable),
					Toast.LENGTH_LONG);
			dismiss();
		}

		return v;
	}

	private List<PointOfRace> getPoints(Stage stage) throws SQLException {
		return pointOfRaceDao.queryForEq("etappe", stage);
	}

	public PointsOfRaceAdapter getAdapter(final ArrayList<PointOfRace> points) {
		return new PointsOfRaceAdapter(getActivity(), R.layout.point_item,
				R.id.txt_point_name, points);
	}

}