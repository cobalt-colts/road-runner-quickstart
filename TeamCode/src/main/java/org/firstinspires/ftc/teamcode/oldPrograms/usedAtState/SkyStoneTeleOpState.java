package org.firstinspires.ftc.teamcode.oldPrograms.usedAtState;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.oldPrograms.usedAtState.DriveTrain6547State;
import org.firstinspires.ftc.teamcode.util.state.ToggleBoolean;
import org.firstinspires.ftc.teamcode.util.state.ToggleDouble;

/*
This is the tele-op we use to drive the robot
 */
@Config
@TeleOp(name = "SkyStone Tele-op State", group = "_teleOp")
@Disabled
public class SkyStoneTeleOpState extends LinearOpMode {

    public static double slideSpeed = .0045; //speed of horizontal slide in servo position units

    public static double speedModifer=.7; //lowers the speed so it's easier to drive

    private boolean intake = false;
    private boolean outtake = false;

    private double leftFrontPower;
    private double rightFrontPower;
    private double leftBackPower;
    private double rightBackPower;

    private ToggleBoolean feildRealtive = new ToggleBoolean(true);

    //edit the array to change the foundation grabber position(s)
    private ToggleDouble fondationGrabberPos = new ToggleDouble(new double[] {0,1},0);
    private ToggleDouble grabberToggle = new ToggleDouble(new double[] {0, 1}, 0);

    private DriveTrain6547State bot; //the robot class

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry()); //makes telemetry output to the FTC Dashboard
        bot = new DriveTrain6547State(this);
        telemetry.update();

        bot.disableEncoders();

        telemetry.log().add("DONE INITIALING");

        double startingAngle = bot.readFile(bot.GYRO_ANGLE_FILE_NAME);

        bot.setAngleZzeroValue(-startingAngle);
        //bot.setPoseEstimate(new Pose2d(-36,-63,startingAngle));

        //get the angle the robot was at when auton ended
        //bot.setAngleZzeroValue(-bot.readFile(bot.GYRO_ANGLE_FILE_NAME));
        bot.writeFile(bot.GYRO_ANGLE_FILE_NAME, 0); //reset the old angle to zero

        telemetry.log().add("Ready to start");
        telemetry.log().add("gyro angle: " + bot.getIMUAngle());
        telemetry.log().add("lift max: " + bot.liftMax);

        bot.setBulkReadAuto();

        waitForStart();

        while (opModeIsActive()) {

            //grabberToggle = new ToggleDouble(new double[] {bot.grabberMin, bot.grabberMax}, grabberToggle.getToggleIndex());
//            fondationGrabberPos.changeValue(FoundationGrabberMax,1);
//            fondationGrabberPos.changeValue(FoundationGrabberMin,0);
//            frontGrabberPos.changeValue(FrontGrabberMin,0);
//            frontGrabberPos.changeValue(FrontGrabberMax,1);
//            backGrabberPos.changeValue(BackGrabberMin,0);
//            backGrabberPos.changeValue(BackGrabberMax,1);

            bot.updateGamepads();

            /*
            Speed Modifers
             */
            if (bot.x1.onPress()) speedModifer=.60;
            if (bot.b1.onPress() && !bot.start1.isPressed()) speedModifer=.9;
            if (bot.a1.onPress() && !bot.start1.isPressed()) speedModifer=1.7; //trig math caps speed at .7, 1.3 balences it out

            if (bot.y1.onPress()) feildRealtive.toggle(); //toggle field realtive

            if (feildRealtive.output()) //if field relative is enabled
            {
                double speed = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y); //get speed
                double LeftStickAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4; //get angle
                double robotAngle = Math.toRadians(bot.getIMUAngle()); //angle of robot
                double rightX=gamepad1.right_stick_x*2; //rotation
                rightX*=.5; //half rotation value for better turning
                //offset the angle by the angle of the robot to make it field realtive
                leftFrontPower =  speed * Math.cos(LeftStickAngle-robotAngle) + rightX;
                rightFrontPower =  speed * Math.sin(LeftStickAngle-robotAngle) - rightX;
                leftBackPower =  speed * Math.sin(LeftStickAngle-robotAngle) + rightX;
                rightBackPower =  speed * Math.cos(LeftStickAngle-robotAngle) - rightX;

                telemetry.addData("LS angle",Math.toDegrees(LeftStickAngle));
                telemetry.addData("driving toward",LeftStickAngle-robotAngle);
                telemetry.addData("ROBOT ANGLE",Math.toDegrees(robotAngle));
                telemetry.addData("RAW ANGLE", Math.toDegrees(bot.getRawExternalHeading()));
            }
            else //regular drive (different math because this is faster than sins and cosines
            {
                leftFrontPower=-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
                rightFrontPower=-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
                leftBackPower=-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
                rightBackPower=-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
            }

            //set motor powers based on previous calculations
            bot.setMotorPowers(leftFrontPower*speedModifer, leftBackPower*speedModifer, rightBackPower*speedModifer, rightFrontPower*speedModifer);

            /*
            Toggle Intake:
            Triggers control intake/outtake
            Push the same trigger twice to turn the intake motors off
             */
            if (bot.rightTrigger2.onPress()) //intake
            {
                if (!intake)
                {
                    intake = true;
                    outtake = false;
                    if (!bot.isStoneAtEnd()) {
                        bot.intake(1);
                    }
                    else bot.intake(.5);
                }
                else //intake button pressed again
                {
                    intake = false;
                    outtake = false;
                    bot.stopIntake();
                }
            }
            else if (bot.leftTrigger2.onPress()) //outtake
            {
                if (!outtake) {
                    intake = false;
                    outtake = true;
                    bot.outtake(1);
                }
                else //outtake button pressed again
                {
                    intake = false;
                    outtake = false;
                    bot.stopIntake();
                }
            }

            if (bot.a2.onPress()) //toggle fondation grabber
            {
                fondationGrabberPos.toggle();
                bot.setFondationGrabber(fondationGrabberPos.output());
            }

            if (bot.b2.onPress()) //toggle stone grabber
            {
                grabberToggle.toggle();
                bot.setGrabber(grabberToggle.output());
            }
            if (bot.x2.onPress())
            {
                bot.setGrabber(2);
            }
            if (bot.y2.isPressed())
            {
                bot.extendMeasuingTape();
            }
            else if (bot.dpadUp2.isPressed())
            {
                bot.retractMeasuringTape();
            }
            else
            {
                bot.stopMeasuringTape();
            }

            double liftSpeed = -gamepad2.left_stick_y;
            /*
            Lift controls
            Deadzone of .05
            has a maximum, but no minimum
             */
            if (liftSpeed > .05 || liftSpeed < -.05) //if lift is below max and speed is outside of deadzone
            {
                bot.setLiftPower(liftSpeed);
            }
            else
            {
                bot.setLiftPower(0);
            }

            //old way to move servo
            //if (bot.rightBumper2.isPressed()) bot.updateServo(bot.grabberSlide, 1, slideSpeed, bot.grabberMax, bot.grabberMin); //move horizontal slide back
            //if (bot.leftBumper2.isPressed()) bot.updateServo(bot.grabberSlide, -1, slideSpeed, bot.grabberMax, bot.grabberMin); //move horizontal slide forward

            if (bot.rightBumper2.isPressed()) bot.ExtendGrabberSlide();
            else if (bot.leftBumper2.isPressed()) bot.RetractGrabberSlide();
            else bot.stopGrabberSlide();

            if (gamepad1.right_bumper && gamepad1.left_bumper) //calibrate gyro
            {
                double zeroVal = -Math.toDegrees(bot.getRawExternalHeading());
                bot.setAngleZzeroValue(zeroVal);
                telemetry.log().add("Calibrated, set zero value to" + zeroVal);
            }

            /*
            Telemetry
             */
            //Pose2d pos = bot.getPoseEstimate();
            //bot.setPoseEstimate(new Pose2d(pos.getX(), pos.getY(), bot.getRawExternalHeading()+Math.toRadians(90)));
            //bot.updateRobotPosRoadRunner(); //display robot's position
            telemetry.addData("LEFT FRONT AMPS:", bot.leftFront.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("RIGHT FRONT AMPS",bot.rightFront.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("LEFT BACK AMPS:", bot.leftRear.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("RIGHT BACK AMPS",bot.rightRear.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("LIFT AMPS",bot.lift.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("INTAKE AMPS",bot.intake.getCurrent(CurrentUnit.AMPS));
            telemetry.update();
        }
        bot.stopRobot();
    }
}
