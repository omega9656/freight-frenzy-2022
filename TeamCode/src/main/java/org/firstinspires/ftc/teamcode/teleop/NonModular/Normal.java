package org.firstinspires.ftc.teamcode.teleop.NonModular;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Normal Drive")
@Disabled
public class Normal extends OmegaTeleop {

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.NORMAL;
    }
}
