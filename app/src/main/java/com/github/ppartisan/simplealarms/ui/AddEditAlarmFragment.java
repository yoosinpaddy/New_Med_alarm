package com.github.ppartisan.simplealarms.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.data.DatabaseHelper;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.service.AlarmReceiver;
import com.github.ppartisan.simplealarms.service.LoadAlarmsService;
import com.github.ppartisan.simplealarms.util.ViewUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.ContentValues.TAG;

public final class AddEditAlarmFragment extends Fragment implements IOnBackPressed{

    private TimePicker mTimePicker,mTimePicker2,mTimePicker3;
    private EditText mLabel,tablets,timeADay;
    private CheckBox mMon, mTues, mWed, mThurs, mFri, mSat, mSun;
    Boolean hasSaved=false;
    View v;
    int key=0;//add
    public static AddEditAlarmFragment newInstance(Alarm alarm, Alarm alarm2, Alarm alarm3, int key) {

        Bundle args = new Bundle();
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA2, alarm2);
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA3, alarm3);
        args.putInt("key", key);

        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_edit_alarm, container, false);

        setHasOptionsMenu(true);

        final Alarm alarm = getAlarm();
        final Alarm alarm2 = getAlarm2();
        final Alarm alarm3 = getAlarm3();
        key=getArguments().getInt("key");

        mTimePicker = (TimePicker) v.findViewById(R.id.edit_alarm_time_picker);
        mTimePicker2 = (TimePicker) v.findViewById(R.id.edit_alarm_time_picker2);
        mTimePicker3 = (TimePicker) v.findViewById(R.id.edit_alarm_time_picker3);
        ViewUtils.setTimePickerTime(mTimePicker, alarm.getTime());
        ViewUtils.setTimePickerTime2(mTimePicker2, alarm2.getTime());
        ViewUtils.setTimePickerTime3(mTimePicker3, alarm3.getTime());
        setUpTime();

        mLabel = (EditText) v.findViewById(R.id.edit_alarm_label);
        tablets = (EditText) v.findViewById(R.id.tablets);
        timeADay = (EditText) v.findViewById(R.id.timeADay);
        timeADay.addTextChangedListener(textWatcher);
        mLabel.setText(alarm.getLabel());

        mMon = (CheckBox) v.findViewById(R.id.edit_alarm_mon);
        mTues = (CheckBox) v.findViewById(R.id.edit_alarm_tues);
        mWed = (CheckBox) v.findViewById(R.id.edit_alarm_wed);
        mThurs = (CheckBox) v.findViewById(R.id.edit_alarm_thurs);
        mFri = (CheckBox) v.findViewById(R.id.edit_alarm_fri);
        mSat = (CheckBox) v.findViewById(R.id.edit_alarm_sat);
        mSun = (CheckBox) v.findViewById(R.id.edit_alarm_sun);

        setDayCheckboxes(alarm);

        return v;
    }

    private void setUpTime() {
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hr=calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int m=calendar.get(Calendar.MINUTE);        // gets hour in 12h format
        int ap=calendar.get(Calendar.AM_PM);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker2.setHour(getNext6Hour(hr));
            mTimePicker3.setHour(getNext12Hour(hr));
            Log.e(TAG, "setTimePickerTime2: "+getNext6Hour(hr) );
            Log.e(TAG, "setTimePickerTime3: "+getNext12Hour(hr) );
        } else {
            mTimePicker2.setCurrentHour(getNext6Hour(hr));
            mTimePicker3.setCurrentHour(getNext12Hour(hr));
            Log.e(TAG, "setTimePickerTime2: "+getNext6Hour(hr) );
            Log.e(TAG, "setTimePickerTime3: "+getNext12Hour(hr) );
        }
    }

    private static int getNext12Hour(int i) {
        if ((i+12)>23){
            return  24-(i+12);
        }else
            return i+12;
    }
    private static int getNext6Hour(int i) {
        if ((i+6)>23){
            return  24-(i+6);
        }else
            return i+6;
    }
    private TextWatcher textWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            setDayCheckboxesChecked();
            invalidate();
            if (!timeADay.getText().toString().trim().contentEquals("")){
                if(((Integer.parseInt(timeADay.getText().toString().trim())))==1){
                    ((RelativeLayout)v.findViewById(R.id.rl1)).setVisibility(View.VISIBLE);

                }else if (((Integer.parseInt(timeADay.getText().toString().trim())))==2){
                    ((RelativeLayout)v.findViewById(R.id.rl1)).setVisibility(View.VISIBLE);
                    ((RelativeLayout)v.findViewById(R.id.rl3)).setVisibility(View.VISIBLE);
                }else if (((Integer.parseInt(timeADay.getText().toString().trim())))==3){
                    ((RelativeLayout)v.findViewById(R.id.rl1)).setVisibility(View.VISIBLE);
                    ((RelativeLayout)v.findViewById(R.id.rl2)).setVisibility(View.VISIBLE);
                    ((RelativeLayout)v.findViewById(R.id.rl3)).setVisibility(View.VISIBLE);
                }
            }
        }
        private void invalidate() {
            ((RelativeLayout)v.findViewById(R.id.rl1)).setVisibility(View.GONE);
            ((RelativeLayout)v.findViewById(R.id.rl2)).setVisibility(View.GONE);
            ((RelativeLayout)v.findViewById(R.id.rl3)).setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_alarm_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            case R.id.action_delete:
                delete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }
    private Alarm getAlarm2() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA2);
    }
    private Alarm getAlarm3() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA3);
    }

    private void setDayCheckboxesChecked() {
        mMon.setChecked(true);
        mTues.setChecked(true);
        mWed.setChecked(true);
        mThurs.setChecked(true);
        mFri.setChecked(true);
        mSat.setChecked(true);
        mSun.setChecked(true);
    }
    private void setDayCheckboxes(Alarm alarm) {
        mMon.setChecked(alarm.getDay(Alarm.MON));
        mTues.setChecked(alarm.getDay(Alarm.TUES));
        mWed.setChecked(alarm.getDay(Alarm.WED));
        mThurs.setChecked(alarm.getDay(Alarm.THURS));
        mFri.setChecked(alarm.getDay(Alarm.FRI));
        mSat.setChecked(alarm.getDay(Alarm.SAT));
        mSun.setChecked(alarm.getDay(Alarm.SUN));
    }

    private void save() {
        hasSaved=true;

        final Alarm alarm = getAlarm();
        final Alarm alarm2 = getAlarm2();
        final Alarm alarm3 = getAlarm3();
        int number_of_times=Integer.parseInt(timeADay.getText().toString().trim());
        if (!validated(number_of_times)){
            return;
        }
        final Calendar time = Calendar.getInstance();
        final Calendar time2 = Calendar.getInstance();
        final Calendar time3 = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        time.set(Calendar.SECOND, 0);
        time2.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute2(mTimePicker2));
        time2.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour2(mTimePicker2));
        time2.set(Calendar.SECOND, 0);
        time3.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute3(mTimePicker3));
        time3.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour3(mTimePicker3));
        time3.set(Calendar.SECOND, 0);
        Log.e(TAG, "save: "+alarm.getId() );
        Log.e(TAG, "save: "+ViewUtils.getTimePickerHour(mTimePicker) );
        Log.e(TAG, "save: tm "+time.getTimeInMillis() );
        alarm2.setTime(time2.getTimeInMillis());
        alarm3.setTime(time3.getTimeInMillis());
        alarm.setTime(time.getTimeInMillis());
        Log.e(TAG, "save: at "+alarm.getTime() );
        String label;
        if (mLabel.getText().toString().contains(" X ")){
            label=mLabel.getText().toString();
        }else{
            label=mLabel.getText().toString()+" "+tablets.getText().toString().trim()+" X "+timeADay.getText().toString().trim();
        }

        alarm.setLabel(label);
        alarm2.setLabel(label);
        alarm3.setLabel(label);
        Log.e(TAG, "save: aL "+alarm.getTime() );
        alarm.setDay(Alarm.MON, mMon.isChecked());
        alarm.setDay(Alarm.TUES, mTues.isChecked());
        alarm.setDay(Alarm.WED, mWed.isChecked());
        alarm.setDay(Alarm.THURS, mThurs.isChecked());
        alarm.setDay(Alarm.FRI, mFri.isChecked());
        alarm.setDay(Alarm.SAT, mSat.isChecked());
        alarm.setDay(Alarm.SUN, mSun.isChecked());
        Log.e(TAG, "save: aD "+alarm.getTime() );

        alarm2.setDay(Alarm.MON, mMon.isChecked());
        alarm2.setDay(Alarm.TUES, mTues.isChecked());
        alarm2.setDay(Alarm.WED, mWed.isChecked());
        alarm2.setDay(Alarm.THURS, mThurs.isChecked());
        alarm2.setDay(Alarm.FRI, mFri.isChecked());
        alarm2.setDay(Alarm.SAT, mSat.isChecked());
        alarm2.setDay(Alarm.SUN, mSun.isChecked());

        alarm3.setDay(Alarm.MON, mMon.isChecked());
        alarm3.setDay(Alarm.TUES, mTues.isChecked());
        alarm3.setDay(Alarm.WED, mWed.isChecked());
        alarm3.setDay(Alarm.THURS, mThurs.isChecked());
        alarm3.setDay(Alarm.FRI, mFri.isChecked());
        alarm3.setDay(Alarm.SAT, mSat.isChecked());
        alarm3.setDay(Alarm.SUN, mSun.isChecked());

        final int rowsUpdated,rowsUpdated2,rowsUpdated3;
        final int messageId,messageId2,messageId3;
        int rowsDeleted;
        Log.e(TAG, "save: bS "+alarm.getTime() );
        switch (number_of_times){
            case 1:
                rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
                messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;
                Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "save: "+alarm.getTime() );
                AlarmReceiver.setReminderAlarm(getContext(), alarm);

                if (key==0){
                    //Cancel any pending notifications for this alarm
                    AlarmReceiver.cancelReminderAlarm(getContext(), alarm2);
                    AlarmReceiver.cancelReminderAlarm(getContext(), alarm3);

                    DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm2);
                    DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm3);
                }
                break;
            case 2:
                rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
                rowsUpdated3 = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm3);
                messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;
                messageId3 = (rowsUpdated3 == 1) ? R.string.update_complete : R.string.update_failed;
                Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), messageId3, Toast.LENGTH_SHORT).show();
                AlarmReceiver.setReminderAlarm(getContext(), alarm);
                AlarmReceiver.setReminderAlarm(getContext(), alarm2);

                if (key==0){
                    //Cancel any pending notifications for this alarm
                    AlarmReceiver.cancelReminderAlarm(getContext(), alarm2);
                    DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm2);
                }
                break;
            case 3:
                rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
                rowsUpdated2 = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm2);
                rowsUpdated3 = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm3);
                messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;
                messageId2 = (rowsUpdated2 == 1) ? R.string.update_complete : R.string.update_failed;
                messageId3 = (rowsUpdated3 == 1) ? R.string.update_complete : R.string.update_failed;
                Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), messageId2, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), messageId3, Toast.LENGTH_SHORT).show();
                AlarmReceiver.setReminderAlarm(getContext(), alarm);
                AlarmReceiver.setReminderAlarm(getContext(), alarm2);
                AlarmReceiver.setReminderAlarm(getContext(), alarm3);
                break;

        }
        getActivity().finish();

    }
    private boolean validated(int a){
        if (a<1){
            timeADay.setError("Must be more than zero");
            timeADay.requestFocus();
            return false;
        }else if (a>3){
            timeADay.setError("The max is 3");
            timeADay.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean onBackPressed() {
        Log.e(TAG, "onBackPressed: " );
        return false;
    }

    private void delete() {

        final Alarm alarm = getAlarm();
        final Alarm alarm2 = getAlarm2();
        final Alarm alarm3 = getAlarm3();

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext(), R.style.DeleteAlarmDialogTheme);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel any pending notifications for this alarm
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm);
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm2);
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm3);

                final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm2);
                DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm3);
                int messageId;
                if(rowsDeleted == 1) {
                    messageId = R.string.delete_complete;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                    LoadAlarmsService.launchLoadAlarmsService(getContext());
                    getActivity().finish();
                } else {
                    messageId = R.string.delete_failed;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();

    }



    @Override
    public void onDetach() {
        Log.e(TAG, "onDetach: "+hasSaved+key );
        if (!hasSaved){
            if (key==0){
                final Alarm alarm = getAlarm();
                final Alarm alarm2 = getAlarm2();
                final Alarm alarm3 = getAlarm3();
                //Cancel any pending notifications for this alarm
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm);
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm2);
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm3);

                DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm2);
                DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm3);
            }
        }
        super.onDetach();
    }
}
