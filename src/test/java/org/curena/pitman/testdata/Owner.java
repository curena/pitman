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
public class Owner {
  @JsonProperty("id")
  private String id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("address")
  private String address;

  @JsonProperty("city")
  private String city;

  @JsonProperty("state")
  private String state;

  @JsonProperty("zip_code")
  private String zipCode;

  @JsonProperty("registration_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate registrationDate;
}
