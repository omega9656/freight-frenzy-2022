package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.R;

public abstract class OmegaTeleop extends OpMode {

    public DcMotorEx intake;

    public DcMotorEx backRight;
    public DcMotorEx frontRight;
    public DcMotorEx backLeft;
    public DcMotorEx frontLeft;

    public CRServo duckMech;

    public DcMotorEx slides;

    public Servo trayTilt;

    int startingSlidePos;

    public enum DriveMode{
        SQUARED,
        CUBED,
        NORMAL;
    }

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);


        backRight = hardwareMap.get(DcMotorEx.class, "back_right");
        frontRight = hardwareMap.get(DcMotorEx.class, "front_right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back_left");
        frontLeft = hardwareMap.get(DcMotorEx.class, "front_left");

        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        slides = hardwareMap.get(DcMotorEx.class, "slides");

        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setDirection(DcMotorSimple.Direction.REVERSE);

        trayTilt = hardwareMap.get(Servo.class, "tray_tilt");
        trayTilt.setDirection(Servo.Direction.REVERSE);
        trayTilt.setPosition(0.1);


        duckMech = hardwareMap.get(CRServo.class, "duck_mechanism");
        duckMech.setDirection(DcMotorSimple.Direction.FORWARD);
        telemetry.addData("slides pos", slides.getCurrentPosition());
        telemetry.addData("tray pos", trayTilt.getPosition());

        //trayTilt.scaleRange();
    }

    @Override
    public void loop() {
        intake();
        drive(2, getCurrentMode());
        duckMech();
        slides();
        tilt();

        telemetry.addData("starting pos: ", startingSlidePos);
        telemetry.addData("slides pos: ", slides.getCurrentPosition());
        telemetry.addData("target pos: ", slides.getTargetPosition());

        telemetry.addData("tray pos", trayTilt.getPosition());
        telemetry.update();
    }

    abstract public DriveMode getCurrentMode();

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
        // both our front wheel powers are set to negative because of gears
        double frontLeftPower = (vertical + horizontal + rotate);
        double backLeftPower = vertical - horizontal + rotate;
        double frontRightPower = -(vertical - horizontal - rotate); // used to be * 0.4, idk y
        double backRightPower = vertical + horizontal - rotate;

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
        if (getCurrentMode() == DriveMode.SQUARED) {
            // need to keep the sign, so multiply by absolute value of itself
            frontLeftPower *= Math.abs(frontLeftPower);
            backLeftPower *= Math.abs(backLeftPower);
            frontRightPower *= Math.abs(frontRightPower);
            backRightPower *= Math.abs(backRightPower);
        } else if (getCurrentMode() == DriveMode.CUBED) {
            frontLeftPower = Math.pow(frontLeftPower, 3);
            backLeftPower = Math.pow(backLeftPower, 3);
            frontRightPower = Math.pow(frontRightPower, 3);
            backRightPower = Math.pow(backRightPower, 3);
        } // if drive mode is normal, don't do anything

        // set final power values to motors
        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void intake(){
        if(gamepad2.right_bumper){
            intake.setPower(0.40);
        } else if(gamepad2.left_bumper){
            intake.setPower(-0.3);
        } else {
            intake.setPower(0);
        }
    }

    public void tilt(){
        if(gamepad2.x){
            trayTilt.setDirection(Servo.Direction.FORWARD);
            trayTilt.setPosition(0.5);
        }
        else if(gamepad2.y){
            //trayTilt.setDirection(Servo.Direction.REVERSE);
            trayTilt.setPosition(0);
        }
    }

    public void slides(){
        if(gamepad2.dpad_right){
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slides.setTargetPosition(1586);
            slides.setPower(0.4);
        } else {
            slides.setPower(0);
        }
        /*if(gamepad2.dpad_right){
            slides.setTargetPosition(1200);
            slides.setPower(0.3);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(gamepad2.dpad_left){
            slides.setTargetPosition(50);
            slides.setPower(-0.3);
            slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }*/
    }

    public void duckMech(){
        if(gamepad2.dpad_down){
            duckMech.setPower(1);
        }else {
            duckMech.setPower(0);
        }
    }
}
