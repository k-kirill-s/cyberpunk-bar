package by.cyberpunkfandom.barfrontend.core

fun nextAdaptiveRefreshDelay(
    changed: Boolean,
    activeMs: Long = 2_500L,
    idleMs: Long = 7_500L,
    hiddenMs: Long = 20_000L,
): Long {
    return if (changed) activeMs else idleMs.coerceAtMost(hiddenMs)
}
