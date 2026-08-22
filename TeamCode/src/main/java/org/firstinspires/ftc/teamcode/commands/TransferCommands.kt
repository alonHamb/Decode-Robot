package org.firstinspires.ftc.teamcode.commands

import org.firstinspires.ftc.teamcode.alonlib.commands.Command
import org.firstinspires.ftc.teamcode.subsystems.transfer.TransferConstants.SHOOTING_POWER
import org.firstinspires.ftc.teamcode.subsystems.transfer.TransferSubsystem

fun TransferSubsystem.defaultTransferCommand(): Command = run { power = 0.0 }

fun TransferSubsystem.shootingCommand(): Command = run { power = SHOOTING_POWER }

fun TransferSubsystem.intakeCommand(power: Double): Command = run { this.power = power }
