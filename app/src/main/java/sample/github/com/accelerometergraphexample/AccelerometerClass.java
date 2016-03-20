package sample.github.com.accelerometergraphexample;

/*
created by: Shubhi Sharma 19 March 2016
 */

public class AccelerometerClass {

    private float xAxisValue;

    private float yAxisValue;

    private float zAxisValue;

    private long timestamp;

    private float accuracy;

    //constructor
    public AccelerometerClass(){

    }

    //initializing constructor
    public AccelerometerClass(float xAxisValue, float yAxisValue, float zAxisValue, long timestamp, float accuracy) {
        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
        this.zAxisValue = zAxisValue;
        this.timestamp = timestamp;
        this.accuracy = accuracy;
    }

    public float getxAxisValue() {
        return xAxisValue;
    }

    public void setxAxisValue(float xAxisValue) {
        this.xAxisValue = xAxisValue;
    }

    public float getyAxisValue() {
        return yAxisValue;
    }

    public void setyAxisValue(float yAxisValue) {
        this.yAxisValue = yAxisValue;
    }

    public float getzAxisValue() {
        return zAxisValue;
    }

    public void setzAxisValue(float zAxisValue) {
        this.zAxisValue = zAxisValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
