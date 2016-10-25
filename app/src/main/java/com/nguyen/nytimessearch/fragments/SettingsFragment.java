package com.nguyen.nytimessearch.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.nguyen.nytimessearch.R;
import com.nguyen.nytimessearch.models.Settings;
import com.nguyen.nytimessearch.models.Date;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by My on 2/10/2016.
 */
public class SettingsFragment extends DialogFragment {
   private static final int REQUEST_DATE = 30;
   private Settings mSettings;
   private SettingsSaver mSettingsSaver;
   @Bind(R.id.begin_date_edit_text) EditText mBeginDate;
   @Bind(R.id.sort_order_spinner) Spinner sortOrder;
   @Bind(R.id.arts_check_box) CheckBox arts;
   @Bind(R.id.fashion_style_check_box) CheckBox fashionStyle;
   @Bind(R.id.sports_check_box) CheckBox sports;
   @Bind(R.id.save_button) Button save;

   // this interface is used to pass data (Settings) from SettingsFragment to MainActivity
   public interface SettingsSaver {
      public void save(Settings settings);
   }

   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      mSettingsSaver = (SettingsSaver)activity;
   }

   @Override
   public void onDetach() {
      super.onDetach();
      mSettingsSaver = null;
   }

   // empty constructor is required for DialogFragment
   public SettingsFragment() {
   }

   public static SettingsFragment newInstance(Settings settings) {
      SettingsFragment fragment = new SettingsFragment();
      Bundle args = new Bundle();
      args.putParcelable("SETTINGS_IN", Parcels.wrap(settings));
      fragment.setArguments(args);
      return fragment;
   }

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_settings, container);
      ButterKnife.bind(this, view);
      return view;
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      ButterKnife.unbind(this);
   }

   @Override
   public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      // extract the Settings object passed in via NewInstance()
      mSettings = (Settings)Parcels.unwrap(getArguments().getParcelable("SETTINGS_IN"));
      Log.i("TRUONG", "SettingsActivity received Settings from MainActivity: " + mSettings);

      // set up "Begin Date"
      // mBeginDate = (EditText)view.findViewById(R.id.begin_date_edit_text);
      // display the current date in the EditText
      if (mSettings.beginDate != null)
         mBeginDate.setText(mSettings.beginDate.toString());
      // request focus to field
      mBeginDate.requestFocus();
      // Show soft keyboard automatically
      // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
      // set up DatePicker dialog
      mBeginDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // pop up a DatePicker dialog, passing the current Date to it.
            DatePickerFragment dialog = DatePickerFragment.newInstance(mSettings.beginDate);
            dialog.setTargetFragment(SettingsFragment.this, REQUEST_DATE);
            dialog.show(getFragmentManager(), "DatePickerDialog");
         }
      });

      // set up "Sort Order"
      // set up Spinner on screen by populating the drop-down list
      // Spinner sortOrder = (Spinner)view.findViewById(R.id.sort_order_spinner);
      // create an ArrayAdapter using sort_order_array and a default spinner layout
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.sort_order_array, android.R.layout.simple_spinner_item);
      // specify the layout to use when the list of choices appears
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      // apply the adapter to the spinner
      sortOrder.setAdapter(adapter);
      Log.i("TRUONG", "at spinner, settings: " + mSettings);
      // set the Spinner to the pre-selected Sort Order
      sortOrder.setSelection(adapter.getPosition(mSettings.sortOrder));
      sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSettings.sortOrder = position == 0 ? null : parent.getItemAtPosition(position).toString();
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      // set up "News Desk Values"
      // CheckBox arts = (CheckBox)view.findViewById(R.id.arts_check_box);
      arts.setChecked(mSettings.arts);
      arts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSettings.arts = isChecked;
         }
      });
      // CheckBox fashionStyle = (CheckBox)view.findViewById(R.id.fashion_style_check_box);
      fashionStyle.setChecked(mSettings.fashionStyle);
      fashionStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSettings.fashionStyle = isChecked;
         }
      });
      // CheckBox sports = (CheckBox)view.findViewById(R.id.sports_check_box);
      sports.setChecked(mSettings.sports);
      sports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSettings.sports = isChecked;
         }
      });

      // set up Save button
      // Button save = (Button)view.findViewById(R.id.save_button);
      save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // package up a Settings object to send back to the parent, MainActivity
            Intent data = new Intent();
            data.putExtra("SETTINGS_OUT", Parcels.wrap(mSettings));
            dismiss();
         }
      });
   }

   @Override
   // this method receives a Date object passed back from DatePickerFragment
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_DATE) {
         Date date = (Date)Parcels.unwrap(data.getParcelableExtra("DAY_OUT"));
         mBeginDate.setText(date.toString());
         mSettings.beginDate = date;
      }
   }
}
