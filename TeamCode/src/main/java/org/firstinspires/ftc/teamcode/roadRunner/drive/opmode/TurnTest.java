package org.firstinspires.ftc.teamcode.roadRunner.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.oldPrograms.usedAtState.DriveTrain6547State;
import org.firstinspires.ftc.teamcode.roadRunner.drive.DriveTrain6547Offseason;

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
@Autonomous(group = "drive")
public class TurnTest extends LinearOpMode {
    public static double ANGLE = 90; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrain6547Offseason bot = new DriveTrain6547Offseason(this);

        waitForStart();

        if (isStopRequested()) return;

        bot.turnSync(Math.toRadians(90));
    }
}
