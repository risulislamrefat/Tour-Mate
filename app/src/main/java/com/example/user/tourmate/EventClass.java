package com.example.user.tourmate;

public class EventClass {
    private String travelDestination;
    private String fromDate;
    private String toDate;
    private String estimatedBudget;
    private String eventId;

    public EventClass() {
    }

    public String getTravelDestination() {
        return travelDestination;
    }

    public void setTravelDestination(String travelDestination) {
        this.travelDestination = travelDestination;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(String estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public EventClass(String travelDestination, String fromDate, String toDate, String estimatedBudget, String eventId) {
        this.travelDestination = travelDestination;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.estimatedBudget = estimatedBudget;
        this.eventId = eventId;
    }
}
