package govan;

import android.annotation.TargetApi;
import android.app.*;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.*;

/**
 * Created by rafae_000 on 15/07/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}