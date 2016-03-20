package sample.github.com.accelerometergraphexample;


/*
created by: Shubhi Sharma 19 March 2016
 */

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //setting variables
    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private List<AccelerometerClass> accelerometerDataList;
    private AccelerometerClass accelerometerData;
    private GraphView accelerometerGraph;
    private LineGraphSeries<DataPoint> seriesX;
    private LineGraphSeries<DataPoint> seriesY;
    private LineGraphSeries<DataPoint> seriesZ;
    int index=0;
    long curTime;
    long diffTime;
    long lastUpdate= System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to identify the sensors available in an android device
        this.mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //to check if accelerometer is present in a device
        if(this.mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)!=null){

            System.out.println("inside");

            //setting reference of the accelerometer to the variable accelerometerSensor
                this.accelerometerSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                //initializing arraylist
                this.accelerometerDataList = new ArrayList<AccelerometerClass>();

                //displaying message accelerometer found
                Toast.makeText(this, "Accelerometer available!", Toast.LENGTH_LONG);

            //adding accelerometer data list values for the starting
                this.accelerometerDataList.add(new AccelerometerClass(0, 0, 0, 0, 0));

            //intializing accelerometer graph
            initializeAccelerometerGraph();

        }else{
            //displaying message if no accelerometer sensor is found in the device.
            Toast.makeText(this, "There is no accelerometer in this device!", Toast.LENGTH_LONG);
        }
    }


    //this method is implemented as part of SensorEventListener
    //it is called automatically at specific time intervals by the phone to retrieve accelerometer values
    //event object contains sensor values at a timeinstance
    @Override
    public void onSensorChanged(SensorEvent event) {

        //creating a accelerometerclass and filling in all the data
        this.accelerometerData = new AccelerometerClass();
        this.accelerometerData.setxAxisValue(event.values[0]);
        this.accelerometerData.setyAxisValue(event.values[1]);
        this.accelerometerData.setzAxisValue(event.values[2]);
        this.accelerometerData.setAccuracy(event.accuracy);

        //calculating time lapse
        this.curTime = System.currentTimeMillis();
        diffTime = (curTime - this.lastUpdate);
        this.lastUpdate = curTime ;

        //setting time lapse between consecutive datapoints
        this.accelerometerData.setTimestamp(diffTime);

        //adding the class to the list of accelerometer data points
        this.accelerometerDataList.add(accelerometerData);

        //displaying accelerometer values on the console
        String display = String.valueOf(this.accelerometerData.getxAxisValue())+ "; "
                +String.valueOf(this.accelerometerData.getyAxisValue())+"; "
                        +String.valueOf(this.accelerometerData.getzAxisValue())+"; "
                        +String.valueOf(this.accelerometerData.getTimestamp());
        //Toast.makeText(this, display, Toast.LENGTH_LONG);
        System.out.println(display);

        //updating graph display
        updateAccelerometerGraph();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //registering the sensor when application is resumed
    //continues retrieving data from sensor
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, this.accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    //method called when application is on pause
    @Override
    protected void onPause() {
        super.onPause();

        //unregistering sensor when application is on pause
        //this is done to save battery
        mSensorManager.unregisterListener(this);

        //saving data onto a file
        File myFile = new File(Environment.getExternalStorageDirectory()+"/Documents/accelerometerData.txt");
        try {

            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);

            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            for(AccelerometerClass accel: this.accelerometerDataList) {
                myOutWriter.append(String.valueOf(accel.getxAxisValue()));
                myOutWriter.append('\t');
                myOutWriter.append(String.valueOf(accel.getyAxisValue()));
                myOutWriter.append('\t');
                myOutWriter.append(String.valueOf(accel.getzAxisValue()));
                myOutWriter.append('\t');
                myOutWriter.append(String.valueOf(accel.getTimestamp()));
                myOutWriter.append('\n');
            }
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initializeAccelerometerGraph(){

        this.accelerometerGraph = (GraphView) findViewById(R.id.accelerometerGraph);
        //creating series for x axis plot
        this.seriesX = new LineGraphSeries<DataPoint>();
        this.seriesX.setColor(Color.RED);
        //creating series for y axis plot
        this.seriesY = new LineGraphSeries<DataPoint>();
        this.seriesY.setColor(Color.BLUE);
        //creating series for z axis plot
        this.seriesZ = new LineGraphSeries<DataPoint>();
        this.seriesZ.setColor(Color.GREEN);

        // legend
        this.seriesX.setTitle("xAxis");
        this.seriesY.setTitle("yAxis");
        this.seriesZ.setTitle("zAxis");
        this.accelerometerGraph.getLegendRenderer().setVisible(true);
        this.accelerometerGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        // set manual X bounds
        this.accelerometerGraph.getViewport().setXAxisBoundsManual(true);
        this.accelerometerGraph.getViewport().setMinX(0);
        this.accelerometerGraph.getViewport().setMaxX(9);

        // set manual Y bounds
        this.accelerometerGraph.getViewport().setYAxisBoundsManual(true);
        this.accelerometerGraph.getViewport().setMinY(-10);
        this.accelerometerGraph.getViewport().setMaxY(10);
        this.accelerometerGraph.addSeries(this.seriesX);
        this.accelerometerGraph.addSeries(this.seriesY);
        this.accelerometerGraph.addSeries(this.seriesZ);
    }

    //update accelerometer data
    public void updateAccelerometerGraph(){

        this.index=this.index+1;
        System.out.println("inside update!");

        this.seriesX.appendData(new DataPoint(index, this.accelerometerDataList.get(this.index).getxAxisValue()),
                true, 10);

        this.seriesY.appendData(new DataPoint(index, this.accelerometerDataList.get(this.index).getyAxisValue()),
                true, 10);

        this.seriesZ.appendData(new DataPoint(index, this.accelerometerDataList.get(this.index).getzAxisValue()),
                true, 10);
    }
}
