package com.example.test.externalcardreader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ashu on 3/9/16.
 */
public class ScanResultsActivity extends AppCompatActivity {

    private LinearLayout firstBigFile, secondBigFile, thirdBigFile, fourthBigFile, fifthBigFile, sixthBigFile, seventhBigFile, eigthBigFile, ninthBigFile, tenthBigFile;
    private LinearLayout firstFrequentFile, secondFrequentFile, thirdFrequentFile, fourthFrequentFile, fifthFrequentFile;
    private TextView firstBigFileName, secondBigFileName, thirdBigFileName, fourthBigFileName, fifthBigFileName, sixthBigFileName, seventhBigFileName, eighthBigFileName, ninthBigFileName, tenthBigFileName;
    private TextView firstFrequentFileExtensionName, secondFrequentFileExtensionName, thirdFrequentFileExtensionName, fourthFrequentFileExtensionName, fifthFrequentFileExtensionName;
    private TextView firstBigFileSize, secondBigFileSize, thirdBigFileSize, fourthBigFileSize, fifthBigFileSize, sixthBigFileSize, seventhBigFileSize, eightBigFileSize, ninthBigFileSize, tenthBigFileSize;
    private TextView firstFrequentFileExtensionCount, secondFrequentFileExtensionCount, thirdFrequentFileExtensionCount, fourthFrequentFileExtensionCount, fifthFrequentFileExtensionCount;
    private TextView averageFileSize;

    private ArrayList<File> files,bigFiles;
    private ShareActionProvider mShareActionProvider;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.statistics_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstBigFile = (LinearLayout) findViewById(R.id.first_big_file);
        secondBigFile = (LinearLayout) findViewById(R.id.second_big_file);
        thirdBigFile = (LinearLayout) findViewById(R.id.third_big_file);
        fourthBigFile = (LinearLayout) findViewById(R.id.fourth_big_file);
        fifthBigFile = (LinearLayout) findViewById(R.id.fifth_big_file);
        sixthBigFile = (LinearLayout) findViewById(R.id.sixth_big_file);
        seventhBigFile = (LinearLayout) findViewById(R.id.seventh_big_file);
        eigthBigFile = (LinearLayout) findViewById(R.id.eighth_big_file);
        ninthBigFile = (LinearLayout) findViewById(R.id.ninth_big_file);
        tenthBigFile = (LinearLayout) findViewById(R.id.tenth_big_file);

        firstFrequentFile = (LinearLayout) findViewById(R.id.first_frequent_file);
        secondFrequentFile = (LinearLayout) findViewById(R.id.second_frequent_file);
        thirdFrequentFile = (LinearLayout) findViewById(R.id.third_frequent_file);
        fourthFrequentFile = (LinearLayout) findViewById(R.id.fourth_frequent_file);
        fifthFrequentFile = (LinearLayout) findViewById(R.id.fifth_frequent_file);

        firstBigFileName = (TextView) findViewById(R.id.big_file_1_name);
        secondBigFileName = (TextView) findViewById(R.id.big_file_2_name);
        thirdBigFileName = (TextView) findViewById(R.id.big_file_3_name);
        fourthBigFileName = (TextView) findViewById(R.id.big_file_4_name);
        fifthBigFileName = (TextView) findViewById(R.id.big_file_5_name);
        sixthBigFileName = (TextView) findViewById(R.id.big_file_6_name);
        seventhBigFileName = (TextView) findViewById(R.id.big_file_7_name);
        eighthBigFileName = (TextView) findViewById(R.id.big_file_8_name);
        ninthBigFileName = (TextView) findViewById(R.id.big_file_9_name);
        tenthBigFileName = (TextView) findViewById(R.id.big_file_10_name);

        firstBigFileSize = (TextView) findViewById(R.id.big_file_1_size);
        secondBigFileSize = (TextView) findViewById(R.id.big_file_2_size);
        thirdBigFileSize = (TextView) findViewById(R.id.big_file_3_size);
        fourthBigFileSize = (TextView) findViewById(R.id.big_file_4_size);
        fifthBigFileSize = (TextView) findViewById(R.id.big_file_5_size);
        sixthBigFileSize = (TextView) findViewById(R.id.big_file_6_size);
        seventhBigFileSize = (TextView) findViewById(R.id.big_file_7_size);
        eightBigFileSize = (TextView) findViewById(R.id.big_file_8_size);
        ninthBigFileSize = (TextView) findViewById(R.id.big_file_9_size);
        tenthBigFileSize = (TextView) findViewById(R.id.big_file_10_size);

        firstFrequentFileExtensionName = (TextView) findViewById(R.id.frequent_extension_file_1_name);
        secondFrequentFileExtensionName = (TextView) findViewById(R.id.frequent_extension_file_2_name);
        thirdFrequentFileExtensionName = (TextView) findViewById(R.id.frequent_extension_file_3_name);
        fourthFrequentFileExtensionName = (TextView) findViewById(R.id.frequent_extension_file_4_name);
        fifthFrequentFileExtensionName = (TextView) findViewById(R.id.frequent_extension_file_5_name);

        firstFrequentFileExtensionCount = (TextView) findViewById(R.id.frequent_extension_file_1_count);
        secondFrequentFileExtensionCount = (TextView) findViewById(R.id.frequent_extension_file_2_count);
        thirdFrequentFileExtensionCount = (TextView) findViewById(R.id.frequent_extension_file_3_count);
        fourthFrequentFileExtensionCount = (TextView) findViewById(R.id.frequent_extension_file_4_count);
        fifthFrequentFileExtensionCount = (TextView) findViewById(R.id.frequent_extension_file_5_count);

        averageFileSize = (TextView) findViewById(R.id.average_file_size);

        Intent intent = getIntent();

        files = (ArrayList<File>) intent.getSerializableExtra("FILES");

        bigFiles = calculateBiggestFile(files);
        if (bigFiles != null) {
            for (int i = 0; i < bigFiles.size(); i++) {
                switch (i) {
                    case 0:
                        firstBigFile.setVisibility(View.VISIBLE);
                        firstBigFileName.setText(bigFiles.get(i).getName());
                        firstBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 1:
                        secondBigFile.setVisibility(View.VISIBLE);
                        secondBigFileName.setText(bigFiles.get(i).getName());
                        secondBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 2:
                        thirdBigFile.setVisibility(View.VISIBLE);
                        thirdBigFileName.setText(bigFiles.get(i).getName());
                        thirdBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 3:
                        fourthBigFile.setVisibility(View.VISIBLE);
                        fourthBigFileName.setText(bigFiles.get(i).getName());
                        fourthBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 4:
                        fifthBigFile.setVisibility(View.VISIBLE);
                        fifthBigFileName.setText(bigFiles.get(i).getName());
                        fifthBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 5:
                        sixthBigFile.setVisibility(View.VISIBLE);
                        sixthBigFileName.setText(bigFiles.get(i).getName());
                        sixthBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 6:
                        seventhBigFile.setVisibility(View.VISIBLE);
                        seventhBigFileName.setText(bigFiles.get(i).getName());
                        seventhBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 7:
                        eigthBigFile.setVisibility(View.VISIBLE);
                        eighthBigFileName.setText(bigFiles.get(i).getName());
                        eightBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 8:
                        ninthBigFile.setVisibility(View.VISIBLE);
                        ninthBigFileName.setText(bigFiles.get(i).getName());
                        ninthBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                    case 9:
                        tenthBigFile.setVisibility(View.VISIBLE);
                        tenthBigFileName.setText(bigFiles.get(i).getName());
                        tenthBigFileSize.setText("" + bigFiles.get(i).length());
                        break;
                }
            }
        }

        averageFileSize.setText(""+calculateAverageFileSize(files));

        TreeMap<String, Integer> mostFrequentFileExtensions = calculateHighestFrequencyFileExtensions(files);
        Iterator it = mostFrequentFileExtensions.entrySet().iterator();
        int count =0 ;
        while (it.hasNext() & count <5) {
            Map.Entry pair = (Map.Entry) it.next();
            switch (count){
                case 0:
                    firstFrequentFile.setVisibility(View.VISIBLE);
                    firstFrequentFileExtensionName.setText(pair.getKey().toString());
                    firstFrequentFileExtensionCount.setText(pair.getValue().toString());
                    break;
                case 1:
                    secondFrequentFile.setVisibility(View.VISIBLE);
                    secondFrequentFileExtensionName.setText(pair.getKey().toString());
                    secondFrequentFileExtensionCount.setText(pair.getValue().toString());
                    break;
                case 2:
                    thirdFrequentFile.setVisibility(View.VISIBLE);
                    thirdFrequentFileExtensionName.setText(pair.getKey().toString());
                    thirdFrequentFileExtensionCount.setText(pair.getValue().toString());
                    break;
                case 3:
                    fourthFrequentFile.setVisibility(View.VISIBLE);
                    fourthFrequentFileExtensionName.setText(pair.getKey().toString());
                    fourthFrequentFileExtensionCount.setText(pair.getValue().toString());
                    break;
                case 4:
                    fifthFrequentFile.setVisibility(View.VISIBLE);
                    fifthFrequentFileExtensionName.setText(pair.getKey().toString());
                    fifthFrequentFileExtensionCount.setText(pair.getValue().toString());
                    break;
            }
            count++;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item=menu.findItem(R.id.share);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.results_available));
        sendIntent.setType("text/plain");

        mShareActionProvider=(ShareActionProvider)MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(sendIntent);

        return(true);
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

    private TreeMap<String,Integer> calculateHighestFrequencyFileExtensions(ArrayList<File> fileList) {
        HashMap<String, Integer> fileExtensionMap = new HashMap<String,Integer>();
        ValueComparator bvc = new ValueComparator(fileExtensionMap);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

        for (File file : fileList) {
            if (getFileExtension(file.getName()) != null) {
                if (fileExtensionMap.get(getFileExtension(file.getName())) != null) {
                    Integer count = fileExtensionMap.get(getFileExtension(file.getName()));
                    fileExtensionMap.put(getFileExtension(file.getName()), count + 1);
                } else {
                    fileExtensionMap.put(getFileExtension(file.getName()), 1);
                }
            }
        }

        sorted_map.putAll(fileExtensionMap);
        return sorted_map;
    }

    private String getFileExtension(String name) {
        String filenameArray[] = name.split("\\.");
        String extension = null;
        if (filenameArray.length > 1) {
            extension = filenameArray[filenameArray.length - 1];
        }
        return extension;
    }

    private class ValueComparator implements Comparator<String> {
        Map<String,Integer> base;

        public ValueComparator(Map<String,Integer> base) {
            this.base = base;
        }

        @Override
        public int compare(String lhs, String rhs) {
            if (base.get(lhs) >= base.get(rhs)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private ArrayList<File> calculateBiggestFile(ArrayList<File> fileList) {
        File[] files = fileList.toArray(new File[fileList.size()]);

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                int n1 = (int) file1.length();
                int n2 = (int) file2.length();
                return n2 - n1;
            }
        });

        File[] tenBigFiles = new File[10];
        for (int i = 0; i < 10; i++) {
            tenBigFiles[i] = files[i];
        }
        return new ArrayList<File>(Arrays.asList(tenBigFiles)) ;
    }

    private long calculateAverageFileSize(ArrayList<File> fileList) {
        long averageSize = 0;
        for (File file : fileList) {
            averageSize += file.length();
        }
        return averageSize / fileList.size();
    }
}
