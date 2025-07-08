package by.cyberpunkfandom.domain.models.events

import java.time.Instant

abstract class OrderEvent(val happenedAt: Instant)
