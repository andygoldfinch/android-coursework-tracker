package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

import java.util.Date;

/**
 * Handles saving and loading coursework from a local sql database.
 */
public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Coursework.db";
    public static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "coursework";
    private static final String PREFERENCES_TABLE_NAME = "preferences";

    /**
     * Creates a Database instance.
     *
     * @param context The context
     */
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is being created.
     *
     * @param db The database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "moduleName TEXT, " +
                "courseworkName TEXT, " +
                "deadline INT, " +
                "weight INT, " +
                "notes TEXT, " +
                "completed INT, " +
                "notified INT, " +
                "PRIMARY KEY(moduleName, courseworkName))");

        db.execSQL("CREATE TABLE " + PREFERENCES_TABLE_NAME + " (" +
                "name TEXT PRIMARY KEY, " +
                "value INT)");
    }

    /**
     * Called when the database schema has been updated.
     *
     * @param db         The database instance
     * @param oldVersion The old schema version number
     * @param newVersion The new schema version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Just delete the databases and start again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PREFERENCES_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Called when the database schema has been downgraded.
     *
     * @param db         The database instance
     * @param oldVersion The old schema version number
     * @param newVersion The new schema version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Just delete the database and start again
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Saves a coursework in the database.
     * This will overwrite any coursework with the same module and coursework name.
     *
     * @param coursework The coursework to save.
     */
    public void saveCoursework(Coursework coursework) {
        ContentValues values = new ContentValues();
        values.put("moduleName", coursework.getModuleName());
        values.put("courseworkName", coursework.getCourseworkName());
        values.put("deadline", coursework.getDeadline().getTime());
        values.put("weight", coursework.getWeight());
        values.put("notes", coursework.getNotes());
        values.put("completed", coursework.getCompleted() ? 1 : 0);

        getWritableDatabase().replace(TABLE_NAME, null, values);
    }

    /**
     * Deletes a coursework instance from the database.
     *
     * @param coursework The coursework to delete
     */
    public void deleteCoursework(Coursework coursework) {
        String whereClause = "moduleName=? AND courseworkName=?";
        String[] whereArgs = new String[]{
                coursework.getModuleName(),
                coursework.getCourseworkName()
        };

        getWritableDatabase().delete(TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * Finds all Courseworks that are not marked as completed.
     *
     * @param includeCompleted If false, only uncompleted courseworks are returned
     * @return An array of Coursework instances.
     */
    public Coursework[] getCourseworks(boolean includeCompleted) {
        String[] columns = {
                "moduleName",
                "courseworkName",
                "deadline",
                "weight",
                "notes",
                "completed",
        };

        String selection = includeCompleted ? "" : "completed=0";
        String sortOrder = "DEADLINE ASC";

        Cursor c = getReadableDatabase().query(
                TABLE_NAME,
                columns,
                selection,
                null,
                null,
                null,
                sortOrder,
                null
        );

        Coursework[] result = new Coursework[c.getCount()];
        for (int i = 0; i < result.length; i++) {
            c.moveToPosition(i);

            result[i] = new Coursework(
                    c.getString(c.getColumnIndexOrThrow("moduleName")),
                    c.getString(c.getColumnIndexOrThrow("courseworkName")),
                    new Date(c.getLong(c.getColumnIndexOrThrow("deadline"))),
                    c.getInt(c.getColumnIndexOrThrow("weight")),
                    c.getString(c.getColumnIndexOrThrow("notes")),
                    c.getInt(c.getColumnIndexOrThrow("completed")) == 1
            );
        }

        return result;
    }

    /**
     * Retrieves a preference value from the database.
     *
     * @param name The preference name
     * @return The preference value
     */
    public boolean getPreference(String name) {
        String[] columns = {"value"};
        String selection = "name=?";
        String[] arguments = {name};

        Cursor c = getReadableDatabase().query(
                TABLE_NAME,
                columns,
                selection,
                arguments,
                null,
                null,
                null,
                null
        );

        return c.getInt(c.getColumnIndexOrThrow("value")) == 1;
    }

    /**
     * Stores a named preference value in the database.
     *
     * @param name  The preference name
     * @param value The preference value
     */
    public void setPreference(String name, boolean value) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value ? 1 : 0);

        getWritableDatabase().replace(PREFERENCES_TABLE_NAME, null, values);
    }
}