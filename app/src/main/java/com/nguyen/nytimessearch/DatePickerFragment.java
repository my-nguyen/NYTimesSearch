package com.nguyen.nytimessearch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.nguyen.nytimessearch.databinding.FragmentDatePickerBinding;

import static com.nguyen.nytimessearch.SettingsFragment.EXTRA_DAY_OBJECT;

/**
 * Created by My on 11/10/2015.
 */
public class DatePickerFragment extends DialogFragment {
    private FragmentDatePickerBinding binding;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_picker, null, false);

        Date date = (Date)getArguments().getSerializable(EXTRA_DAY_OBJECT);
        if (date == null) {
            date = new Date();
        }
        binding.datePicker.init(date.year, date.month, date.day, null);

        // create and return AlertDialog
        return new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Date date1 = new Date(binding.datePicker.getYear(), binding.datePicker.getMonth(), binding.datePicker.getDayOfMonth());
                    if (getTargetFragment() != null) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_DAY_OBJECT, date1);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .create();
    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DAY_OBJECT, date);
        fragment.setArguments(bundle);
        return fragment;
    }
}
