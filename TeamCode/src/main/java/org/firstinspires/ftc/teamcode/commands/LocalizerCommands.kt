package org.firstinspires.ftc.teamcode.commands

import org.firstinspires.ftc.teamcode.alonlib.commands.Command
import org.firstinspires.ftc.teamcode.alonlib.math.geometry.Pose2d
import org.firstinspires.ftc.teamcode.alonlib.units.Alliance
import org.firstinspires.ftc.teamcode.subsystems.vision.LocalizerConstants.BLUE_RESET_POSITION
import org.firstinspires.ftc.teamcode.subsystems.vision.LocalizerConstants.RED_RESET_POSITION
import org.firstinspires.ftc.teamcode.subsystems.vision.LocalizerSubsystem

fun LocalizerSubsystem.setBotPoseCommand(pos: Pose2d): Command {
    return run { this.setBotPose2d(pos) }
}

fun LocalizerSubsystem.resetAtGoalCommand(alliance: Alliance): Command {
    return run {
        this.setBotPose2d(
            when (alliance) {
                Alliance.Blue -> BLUE_RESET_POSITION
                Alliance.Red -> RED_RESET_POSITION
            }
        )
    }
}