package com.nguyen.nytimessearch;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.nguyen.nytimessearch.databinding.FragmentSettingsBinding;

/**
 * Created by My on 2/10/2016.
 * Updated by My on 7/7/2020:
 * 1. migrated to AndroidX
 * 2. replaced ButterKnife with data binding
 * 3. replaced Volley with Retrofit
 * 4. replaced LinearLayout and RelativeLayout with ConstraintLayout
 * 5. implemented MVVM
 * 6. partially implemented Dagger DI
 */
public class SettingsFragment extends DialogFragment {
    public static final String EXTRA_SETTINGS_OBJECT = "SETTINGS_OBJECT";
    public static final String EXTRA_DAY_OBJECT = "DAY_OBJECT";

    private static final String TAG = "SettingsFragment";
    private static final int REQUEST_CODE_DATE = 31;

    private Settings settings;
    private DialogListener listener;
    private FragmentSettingsBinding binding;

    // interface used to pass a Settings object from this SettingsFragment to MainActivity
    public interface DialogListener {
        void onFinish(Settings settings);
    }

    // empty constructor required
    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(Settings settings) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SETTINGS_OBJECT, settings);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settings = (Settings)getArguments().getSerializable(EXTRA_SETTINGS_OBJECT);
        Log.i(TAG, "SettingsActivity received Settings from MainActivity: " + settings);

        // set up "Begin Date"
        if (settings.beginDate != null) {
            binding.beginDateText.setText(settings.beginDate.toString());
        }
        binding.beginDateText.requestFocus();
        /*float textSize = binding.beginDateText.getTextSize();
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        Log.d(TAG, "text size: " + (textSize / scaledDensity));*/

        // set up DatePicker dialog
        binding.beginDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = DatePickerFragment.newInstance(settings.beginDate);
                datePicker.setTargetFragment(SettingsFragment.this, REQUEST_CODE_DATE);
                datePicker.show(getActivity().getSupportFragmentManager(), "DatePickerFragment");
            }
        });

        // set up "Sort Order"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_order_array, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sortOrderSpinner.setAdapter(adapter);
        Log.i(TAG, "at spinner, settings: " + settings);
        int position = adapter.getPosition(settings.sortOrder);
        binding.sortOrderSpinner.setSelection(position);
        binding.sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.sortOrder = position == 0 ? null : parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // set up "News Desk Values"
        binding.artsCheckBox.setChecked(settings.arts);
        binding.artsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.arts = isChecked;
            }
        });
        binding.fashionStyleCheckBox.setChecked(settings.fashionStyle);
        binding.fashionStyleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.fashionStyle = isChecked;
            }
        });
        binding.sportsCheckBox.setChecked(settings.sports);
        binding.sportsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.sports = isChecked;
            }
        });

        // set up Save button
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinish(settings);
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DialogListener) context;
    }

    // receive a Date object returned back from DatePickerFragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DATE) {
            Date date = (Date)data.getSerializableExtra(EXTRA_DAY_OBJECT);
            binding.beginDateText.setText(date.toString());
            settings.beginDate = date;
        }
    }

    // make the DialogFragment occupy full screen
    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
