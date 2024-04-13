package ru.practicum.events.dto;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private String status;
}
