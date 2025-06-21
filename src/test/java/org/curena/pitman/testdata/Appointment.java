package org.curena.pitman.testdata;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
