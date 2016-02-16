Coursework Tracker
The app is to help university students keep track of outstanding courseworks. It allows them to record coursework names, deadlines, weights and notes, and check them off when they are done.

Features Used
- Main Activity
    - Display courseworks with a ListView widget
    - Switch used to toggle showing completed courseworks

- Add/Edit Activity
    - Custom menu buttons for save / delete shown in the action bar
    - DatePicker widget is used to select deadline dates
    - Hints shown in text fields

- Database Storage
    - Coursework stored in an SqLite database
    - User preferences (should completed courseworks be shown?) stored in the database
    - Database queried using the Database class, extending SqLiteOpenHelper

- Widgets Used
    - RelativeLayout
    - LinearLayout
    - ScrollView
    - ListView
    - ImageButton
    - Switch
    - TextView
    - EditText
    - DatePicker (extended with ScrollableDatePicker so it works properly in a scroll view)
    - Checkbox
  