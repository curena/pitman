package org.curena.pitman.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataGenerator {
  private static final Random random = new Random();

  private static final String[] FIRST_NAMES = {
    "John", "Sarah", "Michael", "Emma", "David", "Jessica", "Robert", "Ashley", "William",
        "Jennifer",
    "James", "Amanda", "Christopher", "Lisa", "Daniel", "Michelle", "Matthew", "Melissa", "Anthony",
        "Kimberly"
  };

  private static final String[] LAST_NAMES = {
    "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez",
        "Martinez",
    "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson",
        "Martin"
  };

  private static final String[] DOG_NAMES = {
    "Buddy", "Max", "Charlie", "Cooper", "Rocky", "Duke", "Bear", "Tucker", "Jack", "Oliver",
    "Bella", "Lucy", "Daisy", "Luna", "Lola", "Sadie", "Molly", "Maggie", "Sophie", "Chloe"
  };

  private static final String[] CAT_NAMES = {
    "Whiskers", "Shadow", "Mittens", "Tiger", "Smokey", "Simba", "Oscar", "Felix", "Garfield",
        "Milo",
    "Princess", "Cleo", "Nala", "Angel", "Coco", "Ginger", "Lily", "Ruby", "Zoe", "Bella"
  };

  private static final String[] DOG_BREEDS = {
    "Golden Retriever", "Labrador Retriever", "German Shepherd", "Bulldog", "Poodle", "Beagle",
    "Rottweiler", "Yorkshire Terrier", "Dachshund", "Siberian Husky", "Boxer", "Border Collie"
  };

  private static final String[] CAT_BREEDS = {
    "Persian", "Maine Coon", "Siamese", "Ragdoll", "British Shorthair", "Abyssinian",
    "Russian Blue", "Scottish Fold", "Bengal", "American Shorthair", "Birman", "Oriental Shorthair"
  };

  private static final String[] CITIES = {
    "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio",
        "San Diego",
    "Dallas", "San Jose", "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte", "Detroit"
  };

  private static final String[] STATES = {
    "NY", "CA", "IL", "TX", "AZ", "PA", "FL", "OH", "NC", "MI"
  };

  private static final String[] VETERINARIANS = {
    "Dr. Anderson", "Dr. Brown", "Dr. Chen", "Dr. Davis", "Dr. Garcia", "Dr. Johnson",
    "Dr. Lee", "Dr. Martinez", "Dr. Smith", "Dr. Taylor", "Dr. Wilson", "Dr. Rodriguez"
  };

  private static final String[] APPOINTMENT_TYPES = {
    "Routine Checkup",
    "Vaccination",
    "Surgery",
    "Emergency",
    "Dental Care",
    "Grooming",
    "X-Ray",
    "Blood Work",
    "Spay/Neuter",
    "Microchipping"
  };

  private static final String[] APPOINTMENT_REASONS = {
    "Annual wellness exam", "Vaccination booster", "Skin condition", "Digestive issues", "Injury",
    "Behavior concerns", "Weight management", "Dental cleaning", "Preventive care",
        "Follow-up visit"
  };

  public static List<Owner> generateOwners(int count) {
    List<Owner> owners = new ArrayList<>();
    for (int i = 1; i <= count; i++) {
      Owner owner =
          new Owner(
              "owner-" + i,
              randomChoice(FIRST_NAMES),
              randomChoice(LAST_NAMES),
              generateEmail(),
              generatePhoneNumber(),
              generateAddress(),
              randomChoice(CITIES),
              randomChoice(STATES),
              generateZipCode(),
              generateRandomDate(2020, 2024));
      owners.add(owner);
    }
    return owners;
  }

  public static List<Pet> generatePets(List<Owner> owners, int petsPerOwner) {
    List<Pet> pets = new ArrayList<>();
    int petId = 1;

    for (Owner owner : owners) {
      int numPets = random.nextInt(petsPerOwner) + 1;
      for (int i = 0; i < numPets; i++) {
        String species = random.nextBoolean() ? "Dog" : "Cat";
        Pet pet =
            new Pet(
                "pet-" + petId++,
                species.equals("Dog") ? randomChoice(DOG_NAMES) : randomChoice(CAT_NAMES),
                species,
                species.equals("Dog") ? randomChoice(DOG_BREEDS) : randomChoice(CAT_BREEDS),
                random.nextInt(15) + 1,
                species.equals("Dog")
                    ? 10.0 + random.nextDouble() * 80.0
                    : 3.0 + random.nextDouble() * 15.0,
                generateColor(),
                owner.getId(),
                "MC" + String.format("%010d", random.nextInt(1000000000)),
                randomChoice(new String[] {"Up to date", "Overdue", "Partial", "Unknown"}),
                generateRandomDate(2023, 2024),
                generateMedicalNotes());
        pets.add(pet);
      }
    }
    return pets;
  }

  public static List<Appointment> generateAppointments(List<Pet> pets, int appointmentsPerPet) {
    List<Appointment> appointments = new ArrayList<>();
    int appointmentId = 1;

    for (Pet pet : pets) {
      int numAppointments = random.nextInt(appointmentsPerPet) + 1;
      for (int i = 0; i < numAppointments; i++) {
        LocalDateTime scheduledTime = generateRandomDateTime(2024, 2025);
        String status = generateAppointmentStatus(scheduledTime);

        Appointment appointment =
            new Appointment(
                "appointment-" + appointmentId++,
                pet.getId(),
                pet.getOwnerId(),
                randomChoice(VETERINARIANS),
                randomChoice(APPOINTMENT_TYPES),
                scheduledTime,
                30 + random.nextInt(90),
                status,
                randomChoice(APPOINTMENT_REASONS),
                status.equals("Completed") ? generateDiagnosis() : null,
                status.equals("Completed") ? generateTreatment() : null,
                status.equals("Completed") ? 50.0 + random.nextDouble() * 500.0 : null,
                generateAppointmentNotes());
        appointments.add(appointment);
      }
    }
    return appointments;
  }

  private static String randomChoice(String[] array) {
    return array[random.nextInt(array.length)];
  }

  private static String generateEmail() {
    return randomChoice(FIRST_NAMES).toLowerCase()
        + "."
        + randomChoice(LAST_NAMES).toLowerCase()
        + "@"
        + randomChoice(new String[] {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com"});
  }

  private static String generatePhoneNumber() {
    return String.format(
        "(%03d) %03d-%04d",
        random.nextInt(900) + 100, random.nextInt(900) + 100, random.nextInt(10000));
  }

  private static String generateAddress() {
    return (random.nextInt(9999) + 1)
        + " "
        + randomChoice(LAST_NAMES)
        + " "
        + randomChoice(new String[] {"St", "Ave", "Blvd", "Dr", "Ln", "Ct"});
  }

  private static String generateZipCode() {
    return String.format("%05d", random.nextInt(100000));
  }

  private static String generateColor() {
    return randomChoice(
        new String[] {
          "Brown", "Black", "White", "Golden", "Gray", "Tan", "Mixed", "Orange", "Cream"
        });
  }

  private static String generateMedicalNotes() {
    String[] notes = {
      "Healthy and active", "Mild skin allergies", "Previous surgery on left leg",
      "Requires special diet", "Anxious around other animals", "Good overall health",
      "Chronic ear infections", "Overweight - diet recommended"
    };
    return randomChoice(notes);
  }

  private static String generateDiagnosis() {
    String[] diagnoses = {
      "Healthy - no issues found", "Minor skin irritation", "Ear infection", "Dental disease",
      "Arthritis", "Upper respiratory infection", "Gastrointestinal upset", "Parasites detected"
    };
    return randomChoice(diagnoses);
  }

  private static String generateTreatment() {
    String[] treatments = {
      "Medication prescribed", "Antibiotics for 10 days", "Dietary changes recommended",
      "Follow-up in 2 weeks", "Topical treatment applied", "Vaccination administered",
      "Dental cleaning performed", "Surgery scheduled"
    };
    return randomChoice(treatments);
  }

  private static String generateAppointmentNotes() {
    String[] notes = {
      "Patient was cooperative",
      "Owner has questions about diet",
      "Rescheduled from previous date",
      "Emergency walk-in",
      "Regular patient",
      "First visit",
      "Follow-up appointment"
    };
    return randomChoice(notes);
  }

  private static LocalDate generateRandomDate(int startYear, int endYear) {
    int year = startYear + random.nextInt(endYear - startYear + 1);
    int month = random.nextInt(12) + 1;
    int day = random.nextInt(28) + 1;
    return LocalDate.of(year, month, day);
  }

  private static LocalDateTime generateRandomDateTime(int startYear, int endYear) {
    LocalDate date = generateRandomDate(startYear, endYear);
    int hour = 8 + random.nextInt(10);
    int minute = random.nextInt(4) * 15;
    return date.atTime(hour, minute);
  }

  private static String generateAppointmentStatus(LocalDateTime scheduledTime) {
    LocalDateTime now = LocalDateTime.now();
    if (scheduledTime.isBefore(now)) {
      return randomChoice(new String[] {"Completed", "No-show", "Cancelled"});
    } else {
      return randomChoice(new String[] {"Scheduled", "Confirmed"});
    }
  }
}
