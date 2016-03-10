package com.example.test.externalcardreader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startScanButton, viewScanResult;
    private ProgressBar progressBar;
    private File root;
    private ArrayList<File> fileList;
    private ScanOperation scanOperation;
    private boolean performScan;
    private ShareActionProvider mShareActionProvider;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startScanButton = (Button) findViewById(R.id.start_scan_button);
        startScanButton.setOnClickListener(this);

        viewScanResult = (Button) findViewById(R.id.view_results_button);
        viewScanResult.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fileList = new ArrayList<File>();

        performScan = true;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.share);


        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(null);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_scan_button:
                viewScanResult.setVisibility(View.GONE);
                boolean canReadSdCard = isExternalStorageReadable();
                if (canReadSdCard && performScan) {
                    progressBar.setVisibility(View.VISIBLE);
                    startScanButton.setText(getString(R.string.stop_scanning));
                    scanOperation = new ScanOperation();
                    scanOperation.execute();
                    performScan = false;
                    sendNotification(getString(R.string.scan_operation), getString(R.string.scan_external_storage));
                } else {
                    progressBar.setVisibility(View.GONE);
                    scanOperation.cancel(true);
                    startScanButton.setText(getString(R.string.start_scan));
                    performScan = true;
                }
                break;
            case R.id.view_results_button:

                Intent intent = new Intent(MainActivity.this, ScanResultsActivity.class);
                intent.putExtra("FILES", fileList);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scanOperation.cancel(true);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private class ScanOperation extends AsyncTask<Void, Integer, ArrayList<File>> {
        private ArrayList<File> files;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<File> doInBackground(Void... params) {

            files = new ArrayList<File>();
            //getting SDcard root path
            root = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());
            files = getFiles(root);
            int i = 0;
            while (i <= 100) {
                try {
                    Thread.sleep(1);
                    publishProgress(i);
                    i++;
                } catch (Exception e) {
                    Log.i("test", e.getMessage());
                }
            }
            return files;
        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            super.onPostExecute(files);

            fileList = files;
            progressBar.setProgress(100);
            startScanButton.setText(getString(R.string.scan_complete));
            viewScanResult.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        private ArrayList<File> getFiles(File dir) {
            File listFile[] = dir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {

                    if (listFile[i].isDirectory()) {
                        files.add(listFile[i]);
                        getFiles(listFile[i]);

                    } else {
                        files.add(listFile[i]);
                    }

                }
            }
            return files;
        }
    }

    private void sendNotification(String notificationTitle, String notificationMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
        Notification notification = new Notification(R.mipmap.ic_launcher,
                "New Message", System.currentTimeMillis());

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(MainActivity.this, notificationTitle,
                notificationMessage, pendingIntent);
        notificationManager.notify(9999, notification);
    }
}
