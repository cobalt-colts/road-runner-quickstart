package org.firstinspires.ftc.teamcode.oldPrograms.badStuff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.oldPrograms.usedAtState.DriveTrain6547State;
import org.firstinspires.ftc.teamcode.util.SkyStoneLoc;

/**
 * Created by Drew from 6547 on 9/27/2019.
 */
@Autonomous(name = "Blue single skystone Road Runner", group = "auton")
@Disabled
public class BlueSingleSkyStoneRoadRunne extends LinearOpMode {

    public void runOpMode()
    {
        DriveTrain6547State bot = new DriveTrain6547State(this); //the bot
          /*
        Make the lift stay where it's at.  The scissor lift's force is stronger
        then a motor on brake mode, so we have the lift motor try to keep is's original encoder value
        by going up or down if the scissor lift pushes the motor off its original
        target value
         */
        bot.setLiftTargetPos(bot.liftStartingPos);
        bot.setRunLift(true);

        //set bot position
        bot.setPoseEstimate(new Pose2d(-35,62,Math.toRadians(270)));

        telemetry.log().add("Ready to start");
        waitForStart();

        if (isStopRequested()) return;

        //drive to skystones
        bot.followTrajectorySync(bot.trajectoryBuilder()
                .strafeTo(new Vector2d(-36,36))
                .build());
        //scan stones
        if (bot.isSkystone(bot.colorSensorSideLeft)) //scan stones
        {
            // ---SKYSTONE LEFT---
            bot.skyStoneLoc = SkyStoneLoc.LEFT;
            //back up a bit and strafe left
            bot.followTrajectorySync(bot.trajectoryBuilder()
                    .back(2)
                    .strafeLeft(15)
                    .build());
            telemetry.log().add("LEFT");


        }
        else if (bot.isSkystone(bot.colorSensorSideRight))
        {
            // ---SKYSTONE RIGHT---
            telemetry.log().add("RIGHT");
            bot.skyStoneLoc = SkyStoneLoc.RIGHT;
            //drive back a bit and strafe right
            bot.followTrajectorySync(bot.trajectoryBuilder()
                    .back(2)
                    .strafeRight(15)
                    .build());
        }
        else
        {
            //---SKYSTONE CENTER---
            //don't move, the robot is already in right spot
            telemetry.log().add("CENTER");
            bot.skyStoneLoc = SkyStoneLoc.CENTER;

        }

        //open intake
        bot.outtake(1);
        sleep(500);
        bot.intake(1);

        //drive forward and intake stone
        bot.followTrajectorySync(bot.trajectoryBuilder()
                .forward(18)
                .build());
        //spline to under the skybridge
        bot.followTrajectorySync(bot.trajectoryBuilder()
                // .reverse
                .splineTo(new Pose2d(0,44,Math.toRadians(180)))
                .build());
        bot.turnRealtiveSync(Math.toRadians(0));

        bot.setLiftTargetPos(bot.getLiftStartingPos()+250);

        //drive forward a bit to relay the skystone in the build zone
        bot.followTrajectorySync(bot.trajectoryBuilder()
                .back(14)
                .build());

        bot.moveLift(500,50);

        //go back under the skybridge

        bot.setLiftTargetPos(bot.getLiftStartingPos()+ 250);
        bot.followTrajectorySync(bot.trajectoryBuilder()
                .forward(12)
                .build());

        bot.moveLift(0,50);
        bot.writeFile(bot.GYRO_ANGLE_FILE_NAME, bot.getIMUAngle());

        while (opModeIsActive())
        {
            bot.outputTelemetry();
        }

    }
}
