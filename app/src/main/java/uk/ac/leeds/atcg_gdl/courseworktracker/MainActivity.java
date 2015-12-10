package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Controls the main activity.
 */
public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Switch completedSwitch;
    private Coursework[] listedCoursework;

    /**
     * The method to create the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create references to widgets
        listView = (ListView) findViewById(R.id.listView);
        completedSwitch = (Switch) findViewById(R.id.switch_completed);
        completedSwitch.setOnCheckedChangeListener(new SwitchListener());

        // Load the coursework list
        completedSwitch.setChecked(new Database(getApplicationContext()).getPreference("showCompletedCourseworks", false));
        refreshCourseworks();
    }

    /**
     * Called when the user returns to the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        refreshCourseworks();
    }

    /**
     * Adds coursework items to the list view.
     */
    private void refreshCourseworks() {
        listedCoursework = new Database(getApplicationContext())
                .getCourseworks(completedSwitch.isChecked());

        ArrayList<String> courseworkProcessed = new ArrayList<>();
        for (int i = 0; i < listedCoursework.length; ++i) {
            Coursework c = listedCoursework[i];
            String completed = "";
            if (c.getCompleted()) {
                completed = "[COMPLETED] ";
            }
            courseworkProcessed.add(
                    completed +
                    c.getCourseworkName() + "\n" +
                            c.getModuleName() + " - " +
                            c.getFormattedDeadline());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                courseworkProcessed
        );

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new ListHandler());
    }

    /**
     * Handles the add button by launching the edit activity with a new intent.
     *
     * @param view
     */
    public void handleButtonAdd(View view) {
        Intent addIntent = new Intent(this, EditActivity.class);
        startActivityForResult(addIntent, -1);
    }

    /**
     * A class implementing AdapterView.OnItemClickListener, opening the edit screen when a list
     * item is clicked and passing it the Coursework object that was clicked using an intent.
     */
    private class ListHandler implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
            editIntent.putExtra("coursework", listedCoursework[position]);
            startActivity(editIntent);
        }
    }

    /**
     * A listener class which responds to the "show completed courseworks"
     * switch being changed.
     */
    private class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            refreshCourseworks();
            new Database(getApplicationContext()).setPreference("showCompletedCourseworks", isChecked);
        }
    }
}
