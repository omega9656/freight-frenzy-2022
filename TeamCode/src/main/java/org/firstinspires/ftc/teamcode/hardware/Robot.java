package org.firstinspires.ftc.teamcode.hardware;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public DeviceManager deviceManager;

    public Drivetrain drivetrain;

    public Slides slides;
    public DuckMech duckMech;
    public TrayTilt trayTilt;
    public Intake intake;

    /***
     * takes hardware map from OpMode and creates deviceManager object
     * @param hardwareMap - hardwareMap from OpMode
     */
    public Robot(HardwareMap hardwareMap){
        deviceManager = new DeviceManager(hardwareMap);
    }

    /***
     * initializes subassemblies using a DeviceManager
     * @param autoRunning - checks if auto is running to initialize wheels or not
     */
    public void init(boolean autoRunning){
        deviceManager.init(autoRunning);

        if (!autoRunning) {
            drivetrain = new Drivetrain(deviceManager);
        }

        intake = new Intake(deviceManager);
        slides = new Slides(deviceManager);
        duckMech = new DuckMech(deviceManager);
        trayTilt = new TrayTilt(deviceManager);
    }
}
