package uk.ac.leeds.atcg_gdl.courseworktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdd;
    private ListView listView;
    private Switch completedSwitch;
    private Coursework[] courseworkArray;

    public static final String MESSAGE_KEY = "uk.ac.leeds.atcg_gdl.courseworktracker.MESSAGE";

    /**
     * The method to create the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        listView = (ListView) findViewById(R.id.listView);
        completedSwitch = (Switch) findViewById(R.id.switch_completed);
        completedSwitch.setOnCheckedChangeListener(new SwitchListener());
    }

    protected void onResume() {
        super.onResume();
        courseworkArray = new Database(getApplicationContext()).getCourseworks(false);
        populateListView(courseworkArray);
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
     * Populate data in the list view from an array of Coursework objects using an ArrayAdapter.
     *
     * @param courseworkArray The array containing the objects to be displayed.
     */
    private void populateListView(Coursework[] courseworkArray) {
        ArrayList<String> courseworkProcessed = new ArrayList<String>();
        for (int i = 0; i < courseworkArray.length; ++i) {
            courseworkProcessed.add(
                    courseworkArray[i].getCourseworkName() + "\n" +
                            courseworkArray[i].getModuleName() + " - " +
                            courseworkArray[i].getFormattedDeadline());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                courseworkProcessed
        );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new ListHandler());
    }

    /**
     * A class implementing AdapterView.OnItemClickListener, opening the edit screen when a list
     * item is clicked and passing it the Coursework object that was clicked using an intent.
     */
    private class ListHandler implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
            editIntent.putExtra("coursework", courseworkArray[position]);
            startActivity(editIntent);
        }
    }

    private class SwitchListener implements CompoundButton.OnCheckedChangeListener
    {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            courseworkArray = new Database(getApplicationContext()).getCourseworks(isChecked);
            populateListView(courseworkArray);
        }
    }
}
