package by.cyberpunkfandom.controller

import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException

internal fun String?.requiredParameter(): String =
    this ?: throw GeneralException(ExceptionCodes.MISSING_PARAMETER)

internal fun String?.requiredIntParameter(): Int =
    this?.toIntOrNull() ?: throw GeneralException(ExceptionCodes.MISSING_PARAMETER)

internal fun String?.requiredFloatParameter(): Float =
    this?.toFloatOrNull() ?: throw GeneralException(ExceptionCodes.MISSING_PARAMETER)

internal fun String?.requiredBooleanParameter(): Boolean =
    this?.toBooleanStrictOrNull() ?: throw GeneralException(ExceptionCodes.MISSING_PARAMETER)
