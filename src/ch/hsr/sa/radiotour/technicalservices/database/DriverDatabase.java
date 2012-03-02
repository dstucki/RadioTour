package ch.hsr.sa.radiotour.technicalservices.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DriverDatabase extends SQLiteOpenHelper {
	private final String table_name = "driver";

	public DriverDatabase(Context context) {
		super(context, DatabaseConstants.DATABASE_NAME, null,
				DatabaseConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE fahrer (startnummer INTEGER PRIMARY KEY, name TEXT, team TEXT, team_kurz TEXT, land TEXT, kommentar TEXT, offiziell_zeit NUMERIC, offiziell_rueckstand NUMERIC, virtuel_rueckstand NUMERIC, aktiv TEXT, kategorie TEXT, uci TEXT, neo NUMERIC, sprache TEXT, url TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
