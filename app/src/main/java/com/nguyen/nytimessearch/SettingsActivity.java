package com.nguyen.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

/**
 * Created by My on 2/10/2016.
 */
public class SettingsActivity extends AppCompatActivity implements DatePickerFragment.DateSaver {
   private Settings settings;
   private EditText beginDate;

   public static Intent newIntent(Context context, Settings options) {
      Intent intent = new Intent(context, SettingsActivity.class);
      intent.putExtra("SETTINGS_IN", options);
      return intent;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_settings);

      // retrieve data sent from MainActivity
      settings = (Settings)getIntent().getSerializableExtra("SETTINGS_IN");
      Log.i("NGUYEN", "SettingsActivity received Settings from MainActivity: " + settings);

      // set up "Begin Date"
      beginDate = (EditText)findViewById(R.id.begin_date_edit_text);
      // display the current date in the EditText
      beginDate.setText(settings.beginDate.toString());
      // set up DatePicker dialog
      beginDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // pop up a DatePicker dialog, passing the current Date to it.
            DatePickerFragment dialog = DatePickerFragment.newInstance(settings.beginDate);
            dialog.show(getSupportFragmentManager(), "DialogDate");
         }
      });

      // set up "Sort Order"
      // set up Spinner on screen by populating the drop-down list
      Spinner sortOrder = (Spinner)findViewById(R.id.sort_order_spinner);
      // create an ArrayAdapter using sort_order_array and a default spinner layout
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.sort_order_array, android.R.layout.simple_spinner_item);
      // specify the layout to use when the list of choices appears
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      // apply the adapter to the spinner
      sortOrder.setAdapter(adapter);
      // set the Spinner to the pre-selected Sort Order
      sortOrder.setSelection(adapter.getPosition(settings.sortOrder));
      sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            settings.sortOrder = parent.getItemAtPosition(position).toString();
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      // set up "News Desk Values"
      CheckBox arts = (CheckBox)findViewById(R.id.arts_check_box);
      arts.setChecked(settings.arts);
      arts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            settings.arts = isChecked;
         }
      });
      CheckBox fashionStyle = (CheckBox)findViewById(R.id.fashion_style_check_box);
      fashionStyle.setChecked(settings.fashionStyle);
      fashionStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            settings.fashionStyle = isChecked;
         }
      });
      CheckBox sports = (CheckBox)findViewById(R.id.sports_check_box);
      sports.setChecked(settings.sports);
      sports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            settings.sports = isChecked;
         }
      });

      // set up Save button
      Button save = (Button)findViewById(R.id.save_button);
      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // package up a Settings object to send back to the parent, MainActivity
            Intent data = new Intent();
            data.putExtra("SETTINGS_OUT", settings);
            setResult(RESULT_OK, data);
            finish();
         }
      });
   }

   @Override
   // this method is used to pass data from DatePickerFragment to this Activity
   public void save(Date date) {
      // save the data being passed from DatePickerFragment
      settings.beginDate = date;
      // update the EditText to reflect the new Date
      beginDate.setText(date.toString());
      Log.i("NGUYEN", "SettingsActivity received Date from DatePickerFragment: " + settings.beginDate);
   }

   private String formatDate(Calendar date) {
      StringBuilder builder = new StringBuilder();
      builder.append(date.get(Calendar.MONTH) + 1)
            .append("/")
            .append(date.get(Calendar.DAY_OF_MONTH))
            .append("/")
            .append(date.get(Calendar.YEAR));
      return builder.toString();
   }
}
