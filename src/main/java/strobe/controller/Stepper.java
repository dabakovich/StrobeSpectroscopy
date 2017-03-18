package strobe.controller;

import strobe.spectroscopy.StrobeSpectroscopy;
import strobe.utils.SerialPortListener;

/**
 * Created by Олег on 24.02.2017.
 */
public class Stepper {

    // SENDING:
    // dp - David's Protocol
    // dp://mt/goto/{position}/{speed}
    // dp://mt/measure/{position}/{speed}/{step}
    //
    // READING:
    // dp://res/{position}/{intensity}

    private int currentWave;
    private long stepperPosition;

    private SerialPortListener SERIAL = new SerialPortListener();
    private StrobeSpectroscopy SS;

    public void readLine(String line) {
        if (line.substring(0, 5) == "dp://") {
            if (line.substring(5, 8) == "res") {
                int separatorPosition = line.indexOf("/", 9);
                long position = Long.valueOf(line.substring(9, separatorPosition));
                double value = Double.valueOf(line.substring(separatorPosition + 1));
                SS.updateNewData(position, value);
            }
        }
        SS.updateNewData(400, 0.7);
    }

    public boolean isConnected() {

        return true;
    }

    public void moveStepperToPos(long pos, int speed) {
        SERIAL.sendString("dp://mt/goto/" + pos + "/" + speed);
    }

    public void moveStepperToPosAndMeasure(long pos, int speed, int step) {
        SERIAL.sendString("dp://mt/measure/" + pos + "/" + speed + "/" + step);
    }

    public void stopStepper() {
        SERIAL.sendString("dp://mt/stop");
    }

    public void initSerial() {
        SERIAL.initialize();
    }

    public void closeSerial() {
        SERIAL.close();
    }

    public static int getSpeed(Speed speed) {
        int sp;
        switch (speed) {
            case LOW: {
                sp = 10;
                break;
            }
            case MEDIUM: {
                sp = 100;
                break;
            }
            case FAST: {
                sp = 200;
                break;
            }
            default: sp = 0;
        }
        return sp;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(int currentWave) {
        this.currentWave = currentWave;
    }

    public long getPosFromWave(int wave) {
        return (wave - 400) * 1;
    }

    public int getWaveFromPos(long pos) {
        return (int) (pos / 1) + 400;
    }
}
