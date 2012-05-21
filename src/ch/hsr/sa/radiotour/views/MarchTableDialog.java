package ch.hsr.sa.radiotour.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.activities.RadioTourActivity;
import ch.hsr.sa.radiotour.adapter.PointsOfRaceAdapter;
import ch.hsr.sa.radiotour.application.RadioTour;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.Stage;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class MarchTableDialog extends DialogFragment {
	View v;

	public MarchTableDialog() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Marschtabelle");
		v = inflater.inflate(R.layout.march_table_dialog, container, false);

		ListView lv = (ListView) v.findViewById(R.id.listView_march_table);
		try {
			final ArrayList<PointOfRace> points = (ArrayList<PointOfRace>) getPoints(((RadioTour) getActivity()
					.getApplication()).getActualSelectedStage());
			final PointsOfRaceAdapter pointsOfRaceAdapter = new PointsOfRaceAdapter(
					getActivity(), R.layout.point_item, R.id.txt_point_name,
					points);
			lv.setAdapter(pointsOfRaceAdapter);

			lv.setSelectionFromTop(
					points.indexOf(pointsOfRaceAdapter.getActualPoint()), 10);
		} catch (SQLException e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
		}
		return v;
	}

	private List<PointOfRace> getPoints(Stage stage) throws SQLException {
		RuntimeExceptionDao<PointOfRace, Integer> pointOfRaceDao = ((RadioTourActivity) getActivity())
				.getHelper().getPointOfRaceDao();
		QueryBuilder<PointOfRace, Integer> queryBuilder = pointOfRaceDao
				.queryBuilder();
		queryBuilder.where().eq("etappe", stage);
		return pointOfRaceDao.query(queryBuilder.prepare());
	}

}