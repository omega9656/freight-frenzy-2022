package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.hardware.Robot;


public abstract class OmegaTeleopSlowModular extends OpMode {
    ElapsedTime time = new ElapsedTime();
    Robot robot;

    boolean stalled = false;

    public static double slow_multiplier = 0.55;

    public enum DriveMode {
        SQUARED,
        CUBED,
        NORMAL
    }

    abstract public DriveMode getCurrentMode();

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        robot.init(false);
    }

    @Override
    public void loop() {
        intake();
        drive(2, getCurrentMode());
        duckMech();
        //slides();
        dropHeight();
        down();
        tray();
        telemetry.addData("front right", robot.drivetrain.frontRight.getPower());
        telemetry.addData("front left", robot.drivetrain.frontLeft.getPower());
        telemetry.addData("back right", robot.drivetrain.backRight.getPower());
        telemetry.addData("back left", robot.drivetrain.backLeft.getPower());
        telemetry.addData("duck mech velo: ", robot.duckMech.duckMech.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("slides pos", robot.slides.slides.getCurrentPosition());
        telemetry.addData("tray pos", robot.trayTilt.trayTilt.getPosition());
        telemetry.addData("intake velo", robot.intake.intake.getVelocity(AngleUnit.DEGREES));
    }

    public void drive(double strafe, DriveMode driveMode) {
        // https://gm0.copperforge.cc/en/stable/docs/software/mecanum-drive.html
        // https://www.chiefdelphi.com/t/paper-mecanum-and-omni-kinematic-and-force-analysis/106153/5 (3rd paper)

        // moving left joystick up means robot moves forward
        double vertical = -gamepad1.left_stick_y;  // flip sign because y axis is reversed on joystick

        // moving left joystick to the right means robot moves right
        double horizontal = gamepad1.left_stick_x * strafe;  // counteract imperfect strafing by multiplying by constant

        // moving right joystick to the right means clockwise rotation of robot
        double rotate = gamepad1.right_stick_x;

        // calculate initial power from gamepad inputs
        // to understand this, draw force vector diagrams (break into components)
        // and observe the goBILDA diagram on the GM0 page (linked above)
        double frontLeftPower = vertical + horizontal + rotate;
        double backLeftPower = vertical - horizontal + rotate;
        double frontRightPower = vertical - horizontal - rotate;
        double backRightPower = vertical + horizontal - rotate;

        if(gamepad1.left_trigger > 0.5) {
            slow_multiplier = 1;
        }
        else {
            slow_multiplier = 0.3;
        }

        frontLeftPower *= slow_multiplier;
        backLeftPower *= slow_multiplier;
        frontRightPower *= slow_multiplier;
        backRightPower *= slow_multiplier;

        // if there is a power level that is out of range
        if (
                Math.abs(frontLeftPower) > 1 ||
                        Math.abs(backLeftPower) > 1 ||
                        Math.abs(frontRightPower) > 1 ||
                        Math.abs(backRightPower) > 1
        ) {
            // scale the power within [-1, 1] to keep the power levels proportional
            // (if the power is over 1 the FTC SDK will just make it 1)

            // find the largest power
            double max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            max = Math.max(Math.abs(frontRightPower), max);
            max = Math.max(Math.abs(backRightPower), max);

            // scale everything with the ratio max:1
            // don't need to worry about signs because max is positive
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        // square or cube gamepad inputs
        if (driveMode == DriveMode.SQUARED) {
            // need to keep the sign, so multiply by absolute value of itself
            frontLeftPower *= Math.abs(frontLeftPower);
            backLeftPower *= Math.abs(backLeftPower);
            frontRightPower *= Math.abs(frontRightPower);
            backRightPower *= Math.abs(backRightPower);
        } else if (driveMode == DriveMode.CUBED) {
            frontLeftPower = Math.pow(frontLeftPower, 3);
            backLeftPower = Math.pow(backLeftPower, 3);
            frontRightPower = Math.pow(frontRightPower, 3);
            backRightPower = Math.pow(backRightPower, 3);
        } // if drive mode is normal, don't do anything

        // set final power values to motors
        robot.drivetrain.frontLeft.setPower(frontLeftPower);
        robot.drivetrain.backLeft.setPower(backLeftPower);
        robot.drivetrain.frontRight.setPower(frontRightPower);
        robot.drivetrain.backRight.setPower(backRightPower);

//        if (gamepad1.left_trigger > 0.3) {
//            // set final power values to motors
//            robot.deviceManager.frontLeft.setPower(frontLeftPower);
//            robot.deviceManager.backLeft.setPower(backLeftPower);
//            robot.deviceManager.frontRight.setPower(frontRightPower);
//            robot.deviceManager.backRight.setPower(backRightPower);
//        } else {
//            robot.deviceManager.frontLeft.setPower(SLOW_MULTIPLIER * frontLeftPower);
//            robot.deviceManager.backLeft.setPower(SLOW_MULTIPLIER * backLeftPower);
//            robot.deviceManager.frontRight.setPower(SLOW_MULTIPLIER * frontRightPower);
//            robot.deviceManager.backRight.setPower(SLOW_MULTIPLIER * backRightPower);
//        }
    }

    public void intake(){
        final double STALL_CURRENT = 9.2;
        final double WAIT_TIME = 500;

        if(gamepad2.right_trigger > 0.3){
            robot.intake.in();
        } else if(gamepad2.left_trigger > 0.3){
            robot.intake.out();
        } else if(gamepad2.left_bumper){
            robot.intake.in();
            // if current surpasses or reaches stall current and not stalled, intakes out
            if (robot.intake.intake.getCurrent(CurrentUnit.AMPS) >= STALL_CURRENT && !stalled) {
                stalled = true;
                robot.intake.out();
                time.reset();
                // if the time we run the intake outward is expired, stop the intake
                if (time.milliseconds() > WAIT_TIME && stalled) {
                    robot.intake.stop();
                    stalled = false;
                }
            }
        }  else {
            robot.intake.stop();
        }

    }

//    public void slides(){
//        if(gamepad2.dpad_down){
//            robot.slides.pickUp();
//        }
//    }

    public void tray() {
        if (gamepad2.b) {
            robot.trayTilt.ready();
        } else if(gamepad2.a){
            robot.trayTilt.tilt();
        } else if(gamepad2.y){
            robot.trayTilt.parallel();
        }
    }

    public void duckMech(){
        if(gamepad2.right_bumper){
            time.reset();
            while(time.milliseconds() < 2000){
                drive(2, getCurrentMode());
                robot.duckMech.duckMech.setVelocity(robot.duckMech.INITIAL_VELO + (time.seconds() * robot.duckMech.ACCEL), AngleUnit.DEGREES);
            }
            robot.duckMech.duckMech.setVelocity(0);
        }
    }

    public void dropHeight(){
        if(gamepad2.dpad_up){
            robot.trayTilt.parallel();
            robot.slides.dropOffHigh();
        }
        if(gamepad2.dpad_right){
            robot.trayTilt.parallel();
            robot.slides.dropOffMiddle();
        }
        if(gamepad2.dpad_left){
            robot.trayTilt.parallel();
            robot.slides.dropOffLow();
        }
    }

    public void down(){
        if(gamepad2.dpad_down){
            time.reset();
            while(time.milliseconds() < 1000){
                drive(2, getCurrentMode());
                robot.trayTilt.tilt();
            }
            robot.trayTilt.parallel();
            robot.slides.pickUp();
            while(robot.slides.slides.getCurrentPosition()-20 > robot.slides.slides.getTargetPosition()){
                drive(2, getCurrentMode());
            }
            robot.trayTilt.ready();
        }
    }
}
