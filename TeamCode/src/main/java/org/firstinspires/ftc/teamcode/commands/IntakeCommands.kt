package org.firstinspires.ftc.teamcode.commands

import org.firstinspires.ftc.teamcode.alonlib.commands.Command
import org.firstinspires.ftc.teamcode.alonlib.units.Percentage
import org.firstinspires.ftc.teamcode.alonlib.units.fraction
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeConstants.SHOOTING_POWER_LEVEL
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeSubsystem

fun IntakeSubsystem.defaultIntakeCommand(): Command = run { setMotorPower(0.fraction) }

fun IntakeSubsystem.intakeCommand(power: Percentage): Command =
    run { setMotorPower(power) }

fun IntakeSubsystem.shootCommand(): Command = run { setMotorPower(SHOOTING_POWER_LEVEL.fraction) }
