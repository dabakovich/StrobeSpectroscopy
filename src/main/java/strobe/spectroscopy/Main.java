package strobe.spectroscopy;

import strobe.utils.SerialTest;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by David on 19.02.2017.
 */
public class Main {

    private final static Scanner SC = new Scanner(System.in);

    private  static SerialTest SERIAL;

    public static void main(String[] args) throws InterruptedException, IOException {

        SERIAL = new SerialTest();
        SERIAL.initialize();

//        Thread t = new Thread() {
//            public void run() {
//                try {
//                    Thread.sleep(1000000);
//                } catch (InterruptedException ie) {
//
//                }
//            }
//        };
//        t.start();
        Thread.sleep(2000);

        // dp - David's Protocol
        // dp://mt/speed/{speed}
        // dp://mt/spandstp/{speed}/{step}
        // dp://mt/goto/{position}/{speed}
        showMenu();
        SERIAL.close();
    }

    private static void showMenu() {
        boolean end = false;
        int speed;
        int step;
        while (!end) {
            System.out.println("Choose your option:");
            System.out.println("1. Start step motor.");
            System.out.println("2. Start step motor and do measuring.");
            System.out.println("0. Exit.");
            switch (SC.nextInt()) {
                case 1:
                    System.out.print("Enter speed: ");
                    speed = SC.nextInt();
                    startStepMotor(speed);
                    showControlMotorMenu();
                    break;
                case 2:
                    System.out.print("Enter speed: ");
                    speed = SC.nextInt();
                    System.out.print("Enter step for measuring: ");
                    step = SC.nextInt();
                    startStepMotorAndDoMeasure(speed, step);
                    showControlMotorMenu();
                    break;
                default:
                    end = true;
            }
        }
    }

    private static void showControlMotorMenu() {
        boolean end = false;
        while (!end) {
            System.out.println("Choose your option:");
            System.out.println("1. Change speed.");
            System.out.println("0. Stop motor end exit.");
            switch (SC.nextInt()) {
                case 1:
                    System.out.print("Enter speed: ");
                    int speed = SC.nextInt();
                    startStepMotor(speed);
                    break;
                default:
                    stopStepMotor();
                    end = true;
            }

        }
    }

    private static void startStepMotor(int speed) {
        SERIAL.sendString("dp://mt/speed/" + speed);
    }

    private static void startStepMotorAndDoMeasure(int speed, int step) {
        SERIAL.sendString("dp://mt/spandstp/" + speed + "/" + step);
    }

    private static void stopStepMotor() {
        SERIAL.sendString("dp://mt/speed/0");
    }
}
