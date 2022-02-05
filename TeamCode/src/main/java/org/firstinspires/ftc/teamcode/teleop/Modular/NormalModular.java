package org.firstinspires.ftc.teamcode.teleop.Modular;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.NonModular.OmegaTeleopModular;

@TeleOp(name = "Modular Normal Drive")
@Disabled
public class NormalModular extends OmegaTeleopModular {

    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.NORMAL;
    }
}
