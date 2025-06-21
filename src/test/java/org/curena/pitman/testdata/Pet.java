package org.curena.pitman.testdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

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

    public Pet() {}

    public Pet(String id, String name, String species, String breed, Integer age, Double weight,
               String color, String ownerId, String microchipId, String vaccinationStatus,
               LocalDate lastVisitDate, String medicalNotes) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.color = color;
        this.ownerId = ownerId;
        this.microchipId = microchipId;
        this.vaccinationStatus = vaccinationStatus;
        this.lastVisitDate = lastVisitDate;
        this.medicalNotes = medicalNotes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    
    public String getMicrochipId() { return microchipId; }
    public void setMicrochipId(String microchipId) { this.microchipId = microchipId; }
    
    public String getVaccinationStatus() { return vaccinationStatus; }
    public void setVaccinationStatus(String vaccinationStatus) { this.vaccinationStatus = vaccinationStatus; }
    
    public LocalDate getLastVisitDate() { return lastVisitDate; }
    public void setLastVisitDate(LocalDate lastVisitDate) { this.lastVisitDate = lastVisitDate; }
    
    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pet{id='" + id + "', name='" + name + "', species='" + species + "', breed='" + breed + "'}";
    }
}