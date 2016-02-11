package com.nguyen.nytimessearch;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by My on 11/10/2015.
 */
public class DatePickerFragment extends DialogFragment {
   private DateSaver dateSaver;

   // this interface is used to pass data (Calendar) from DatePickerFragment to SettingsActivity
   public interface DateSaver {
      public void save(Calendar date);
   }

   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      dateSaver = (DateSaver)activity;
   }

   @Override
   public void onDetach() {
      super.onDetach();
      dateSaver = null;
   }

   @Override
   public Dialog onCreateDialog(Bundle bundle)
   {
      // use the DatePicker layout
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
      final DatePicker datePicker = (DatePicker)view.findViewById(R.id.date_picker);
      // extract the Calendar object stashed in via newInstance()
      Calendar calendar = (Calendar)getArguments().getSerializable("DAY_IN");
      // initialize the DatePicker dialog with the date values in year, month and day
      datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

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
                  Calendar date = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                  dateSaver.save(date);
               }
            })
            .create();
   }

   // this convenient method is called by beginDate.onClick() in SettingsActivity. it creates a new
   // DatePickerFragment object and stashes the current Date object in that DatePickerFragment
   public static DatePickerFragment newInstance(Calendar date)
   {
      DatePickerFragment fragment = new DatePickerFragment();
      Bundle bundle = new Bundle();
      bundle.putSerializable("DAY_IN", date);
      fragment.setArguments(bundle);
      return fragment;
   }
}
