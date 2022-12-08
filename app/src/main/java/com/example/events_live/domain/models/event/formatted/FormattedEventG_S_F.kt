package com.example.events_live.domain.models.event.formatted

import com.example.events_live.domain.models.event.EventX


class FormattedEventG_S_F(var eventType:EventKind,var eventDetail: EventX)

enum class EventKind{
    GOAL,
    GOAL_HEADING,
    FOUL,
    FOUL_HEADING,
    SUBSTITUTION_HEADING,
    SUBSTITUTION
}