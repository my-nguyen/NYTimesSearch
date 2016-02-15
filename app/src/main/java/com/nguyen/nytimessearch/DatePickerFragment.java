package com.nguyen.nytimessearch;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by My on 11/10/2015.
 */
public class DatePickerFragment extends DialogFragment {
   @Bind(R.id.date_picker) DatePicker datePicker;

   @Override
   public Dialog onCreateDialog(Bundle bundle)
   {
      // use the DatePicker layout
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
      ButterKnife.bind(this, view);
      // final DatePicker datePicker = (DatePicker)view.findViewById(R.id.date_picker);
      // extract the Date object passed in via newInstance()
      Date date = (Date)getArguments().getSerializable("DAY_IN");
      if (date == null)
         date = new Date();
      // initialize the DatePicker dialog with the date values in year, month and day
      datePicker.init(date.year, date.month, date.day, null);

      // create and return AlertDialog
      return new AlertDialog.Builder(getActivity())
            // configure dialog to display View between the title and buttons
            .setView(view)
            .setTitle(R.string.date_picker_title)
            // when the user presses the positive button in the dialog, retrieve the Date from
            // DatePicker and send the result back to DetailFragment
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               // upon OK button click, this callback method packages up the newly selected Date
               // and call the SettingsActivity callback save() to pass the Date back to that
               // Activity and also update the Date EditText in that Activity
               public void onClick(DialogInterface dialog, int which) {
                  Date date = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                  // pass the selected Date object out to the target fragment (SettingsFragment)
                  if (getTargetFragment() != null) {
                     // create an Intent to stuff a Date object in it
                     Intent intent = new Intent();
                     intent.putExtra("DAY_OUT", Parcels.wrap(date));
                     // pass the selected Date object back to the calling fragment (SettingsFragment)
                     getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                  }
               }
            })
            .create();
   }

   // this convenient method is called by beginDate.onClick() in SettingsActivity. it creates a new
   // DatePickerFragment object and stashes the current Date object in that DatePickerFragment
   public static DatePickerFragment newInstance(Date date)
   {
      DatePickerFragment fragment = new DatePickerFragment();
      // pass a Date object into DatePickerFragment
      Bundle bundle = new Bundle();
      bundle.putParcelable("DAY_IN", Parcels.wrap(date));
      fragment.setArguments(bundle);
      return fragment;
   }
}
