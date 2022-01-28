package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Normal Drive")
@Disabled
public class Normal extends OmegaTeleop{

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.NORMAL;
    }
}
