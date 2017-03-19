package strobe.controller;

import strobe.spectroscopy.StrobeSpectroscopy;
import strobe.utils.SerialPortListener;

import java.util.function.BiConsumer;

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

    private SerialPortListener serial = new SerialPortListener(this::readLine);
    private BiConsumer<Long, Double> updateDataConsumer;
//    private StrobeSpectroscopy spectroscopy;

    public Stepper(BiConsumer<Long, Double> updateDataConsumer) {
        this.updateDataConsumer = updateDataConsumer;
    }

    public void readLine(String line) {
        if (line.substring(0, 5).equals("dp://")) {
            if (line.substring(5, 8).equals("res")) {
                int separatorPosition = line.indexOf("/", 9);
                long position = Long.valueOf(line.substring(9, separatorPosition));
                double value = Double.valueOf(line.substring(separatorPosition + 1));
                System.out.println(position + ", " + value);
                updateDataConsumer.accept(position, value);
            }
        }
    }

    public boolean isConnected() {

        return true;
    }

    public void moveStepperToPos(long pos, int speed) {
        serial.sendString("dp://mt/goto/" + pos + "/" + speed);
    }

    public void moveStepperToPosAndMeasure(long pos, int speed, int step) {
        serial.sendString("dp://mt/measure/" + pos + "/" + speed + "/" + step);
    }

    public void stopStepper() {
        serial.sendString("dp://mt/stop");
    }

    public void initSerial() {
        serial.initialize();
    }

    public void closeSerial() {
        serial.close();
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
