package org.firstinspires.ftc.teamcode.teleop.Modular;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.NonModular.OmegaTeleopModular;

@TeleOp(name = "Cubed Modular")
@Disabled
public class CubedModular extends OmegaTeleopModular {
    @Override
    public DriveMode getCurrentMode() {
        return DriveMode.CUBED;
    }
}
