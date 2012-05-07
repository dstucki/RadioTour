package ch.hsr.sa.radiotour.technicalservices.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.domain.BicycleRider;
import ch.hsr.sa.radiotour.domain.Group;
import ch.hsr.sa.radiotour.domain.PointOfRace;
import ch.hsr.sa.radiotour.domain.Stage;
import ch.hsr.sa.radiotour.domain.Team;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "radioTour.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the BicycleRider table
	private RuntimeExceptionDao<BicycleRider, Integer> riderRuntimeDao = null;
	private RuntimeExceptionDao<Team, String> teamRuntimeDao = null;
	private RuntimeExceptionDao<Group, Integer> groupRuntimeDao = null;
	private RuntimeExceptionDao<Stage, Integer> stageRuntimeDao = null;
	private RuntimeExceptionDao<PointOfRace, Integer> pointOfRaceRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, BicycleRider.class);
			TableUtils.createTable(connectionSource, Team.class);
			TableUtils.createTable(connectionSource, Group.class);
			TableUtils.createTable(connectionSource, Stage.class);
			TableUtils.createTable(connectionSource, PointOfRace.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, BicycleRider.class, true);
			TableUtils.dropTable(connectionSource, Team.class, true);
			TableUtils.dropTable(connectionSource, Group.class, true);
			TableUtils.dropTable(connectionSource, Stage.class, true);
			TableUtils.dropTable(connectionSource, PointOfRace.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao
	 * for our BicycleRider class. It will create it or just give the cached
	 * value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<BicycleRider, Integer> getBicycleRiderDao() {
		if (riderRuntimeDao == null) {
			riderRuntimeDao = getRuntimeExceptionDao(BicycleRider.class);
		}
		return riderRuntimeDao;
	}

	public RuntimeExceptionDao<Group, Integer> getGroupDao() {
		if (groupRuntimeDao == null) {
			groupRuntimeDao = getRuntimeExceptionDao(Group.class);
		}
		return groupRuntimeDao;
	}

	public RuntimeExceptionDao<Team, String> getTeamDao() {
		if (teamRuntimeDao == null) {
			teamRuntimeDao = getRuntimeExceptionDao(Team.class);
		}
		return teamRuntimeDao;
	}

	public RuntimeExceptionDao<Stage, Integer> getStageDao() {
		if (stageRuntimeDao == null) {
			stageRuntimeDao = getRuntimeExceptionDao(Stage.class);
		}
		return stageRuntimeDao;
	}

	public RuntimeExceptionDao<PointOfRace, Integer> getPointOfRaceDao() {
		if (pointOfRaceRuntimeDao == null) {
			pointOfRaceRuntimeDao = getRuntimeExceptionDao(PointOfRace.class);
		}
		return pointOfRaceRuntimeDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		riderRuntimeDao = null;
		teamRuntimeDao = null;
		groupRuntimeDao = null;
		stageRuntimeDao = null;
		pointOfRaceRuntimeDao = null;
	}
}
