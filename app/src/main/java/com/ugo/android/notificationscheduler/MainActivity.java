package com.ugo.android.notificationscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private JobScheduler jobScheduler;
    private static final int JOB_ID = 0;
    private Switch deviceIdleSwitch;
    private Switch deviceChargingSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceIdleSwitch = findViewById(R.id.idleSwitch);
        deviceChargingSwitch = findViewById(R.id.chargingSwitch);
    }

    public void scheduleJob(View view) {
        RadioGroup networkOptions = findViewById(R.id.networkOptions);
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        //Get the selected network ID and save it in an integer variable
        int selectedNetworkID = networkOptions.getCheckedRadioButtonId();

        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        switch (selectedNetworkID) {
            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }

        //Create a ComponentName object which is used to associate the JobService with the JobInfo object.
        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());

        //Create a JobInfo object
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(deviceIdleSwitch.isChecked())
                .setRequiresCharging(deviceChargingSwitch.isChecked());
        //Schedule the job and notify the user
        JobInfo myJobInfo = builder.build();
        jobScheduler.schedule(myJobInfo);

        //Show a Toast message, letting user know the job was scheduled
        Toast.makeText(this, "Job Scheduled, job will run when " +
                "the constraints are met.", Toast.LENGTH_LONG).show();
    }

    public void cancelJobs(View view) {
        if (jobScheduler != null) {
            jobScheduler.cancelAll();
            jobScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_LONG).show();
        }
    }
}