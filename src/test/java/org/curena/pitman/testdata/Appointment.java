package org.curena.pitman.testdata;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Appointment {
  @JsonProperty("id")
  private String id;

  @JsonProperty("pet_id")
  private String petId;

  @JsonProperty("owner_id")
  private String ownerId;

  @JsonProperty("veterinarian")
  private String veterinarian;

  @JsonProperty("appointment_type")
  private String appointmentType;

  @JsonProperty("scheduled_time")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime scheduledTime;

  @JsonProperty("duration_minutes")
  private Integer durationMinutes;

  @JsonProperty("status")
  private String status;

  @JsonProperty("reason")
  private String reason;

  @JsonProperty("diagnosis")
  private String diagnosis;

  @JsonProperty("treatment")
  private String treatment;

  @JsonProperty("cost")
  private Double cost;

  @JsonProperty("notes")
  private String notes;

  public Appointment() {}

  public Appointment(
      String id,
      String petId,
      String ownerId,
      String veterinarian,
      String appointmentType,
      LocalDateTime scheduledTime,
      Integer durationMinutes,
      String status,
      String reason,
      String diagnosis,
      String treatment,
      Double cost,
      String notes) {
    this.id = id;
    this.petId = petId;
    this.ownerId = ownerId;
    this.veterinarian = veterinarian;
    this.appointmentType = appointmentType;
    this.scheduledTime = scheduledTime;
    this.durationMinutes = durationMinutes;
    this.status = status;
    this.reason = reason;
    this.diagnosis = diagnosis;
    this.treatment = treatment;
    this.cost = cost;
    this.notes = notes;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPetId() {
    return petId;
  }

  public void setPetId(String petId) {
    this.petId = petId;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getVeterinarian() {
    return veterinarian;
  }

  public void setVeterinarian(String veterinarian) {
    this.veterinarian = veterinarian;
  }

  public String getAppointmentType() {
    return appointmentType;
  }

  public void setAppointmentType(String appointmentType) {
    this.appointmentType = appointmentType;
  }

  public LocalDateTime getScheduledTime() {
    return scheduledTime;
  }

  public void setScheduledTime(LocalDateTime scheduledTime) {
    this.scheduledTime = scheduledTime;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  public String getTreatment() {
    return treatment;
  }

  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Appointment appointment = (Appointment) o;
    return Objects.equals(id, appointment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Appointment{id='"
        + id
        + "', petId='"
        + petId
        + "', appointmentType='"
        + appointmentType
        + "', scheduledTime="
        + scheduledTime
        + "}";
  }
}
