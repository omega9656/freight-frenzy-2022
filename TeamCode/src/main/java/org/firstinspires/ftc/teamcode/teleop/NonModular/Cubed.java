package org.firstinspires.ftc.teamcode.teleop.NonModular;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Cubed")
@Disabled
public class Cubed extends OmegaTeleop {

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.CUBED;
    }
}
