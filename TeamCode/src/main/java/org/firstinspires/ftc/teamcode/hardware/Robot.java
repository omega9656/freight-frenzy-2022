package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public DeviceManager deviceManager;

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
    void init(boolean autoRunning){
        deviceManager.init(autoRunning);
    }
}
