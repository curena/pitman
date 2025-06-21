package org.curena.pitman.testdata;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("species")
  private String species;

  @JsonProperty("breed")
  private String breed;

  @JsonProperty("age")
  private Integer age;

  @JsonProperty("weight")
  private Double weight;

  @JsonProperty("color")
  private String color;

  @JsonProperty("owner_id")
  private String ownerId;

  @JsonProperty("microchip_id")
  private String microchipId;

  @JsonProperty("vaccination_status")
  private String vaccinationStatus;

  @JsonProperty("last_visit_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate lastVisitDate;

  @JsonProperty("medical_notes")
  private String medicalNotes;

  @Override
  public String toString() {
    return "Pet{id='"
        + id
        + "', name='"
        + name
        + "', species='"
        + species
        + "', breed='"
        + breed
        + "'}";
  }
}
