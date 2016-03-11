package startup.irfananda.mydiary;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Alarm {

    private TimePicker myTimePicker;
    private TimePickerDialog timePickerDialog;
    private Context context;
    private Calendar alarmCal;

    public Alarm(Context context) {
        this.context = context;
    }

    public void openTimePickerDialog(boolean is24r){
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(
                context,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
//        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0)
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra(AlertReceiver.NOTIFICATION_ID, 1);
        intent.putExtra(AlertReceiver.NOTIFICATION, getNotification("Lets write diary !","Record your today moment"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                targetCal.getTimeInMillis(),
                //set alarm to repeat every 24 hours
                TimeUnit.HOURS.toMillis(24),
                pendingIntent);
        Log.i("infoirfan", "alarm time : " + targetCal.getTime());
        alarmCal = targetCal;
    }

    public Calendar getAlarmCal() {
        return alarmCal;
    }

    //create the notification in this method
    private Notification getNotification(String title, String content) {
        Intent myIntent = new Intent(context,MainActivity.class);
        //target intent when the notification touched
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK
        );

        //build the content of the notification
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker("Notification!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        }else {
            return builder.build();
        }
    }

}

