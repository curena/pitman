package org.curena.pitman.testdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.*;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.ExistsRequest;
import org.opensearch.client.transport.endpoints.BooleanResponse;

public class PetClinicIndexSetup {
  private final OpenSearchClient client;

  public static final String OWNERS_INDEX = "pet-clinic-owners";
  public static final String PETS_INDEX = "pet-clinic-pets";
  public static final String APPOINTMENTS_INDEX = "pet-clinic-appointments";

  public PetClinicIndexSetup(OpenSearchClient client) {
    this.client = client;
  }

  public void setupAllIndices() throws IOException {
    setupOwnersIndex();
    setupPetsIndex();
    setupAppointmentsIndex();
  }

  public void setupOwnersIndex() throws IOException {
    deleteIndexIfExists(OWNERS_INDEX);

    Map<String, Property> ownerProperties = new HashMap<>();
    ownerProperties.put("id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    ownerProperties.put(
        "first_name", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    ownerProperties.put(
        "last_name", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    ownerProperties.put("email", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    ownerProperties.put("phone", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    ownerProperties.put(
        "address", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    ownerProperties.put("city", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    ownerProperties.put("state", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    ownerProperties.put("zip_code", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    ownerProperties.put(
        "registration_date",
        Property.of(p -> p.date(DateProperty.of(d -> d.format("yyyy-MM-dd")))));

    CreateIndexRequest createIndexRequest =
        CreateIndexRequest.of(
            c -> c.index(OWNERS_INDEX).mappings(m -> m.properties(ownerProperties)));

    client.indices().create(createIndexRequest);
  }

  public void setupPetsIndex() throws IOException {
    deleteIndexIfExists(PETS_INDEX);

    Map<String, Property> petProperties = new HashMap<>();
    petProperties.put("id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put(
        "name", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    petProperties.put("species", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put("breed", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put("age", Property.of(p -> p.integer(IntegerNumberProperty.of(i -> i))));
    petProperties.put("weight", Property.of(p -> p.double_(DoubleNumberProperty.of(d -> d))));
    petProperties.put("color", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put("owner_id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put("microchip_id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put(
        "vaccination_status", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    petProperties.put(
        "last_visit_date", Property.of(p -> p.date(DateProperty.of(d -> d.format("yyyy-MM-dd")))));
    petProperties.put(
        "medical_notes", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));

    CreateIndexRequest createIndexRequest =
        CreateIndexRequest.of(c -> c.index(PETS_INDEX).mappings(m -> m.properties(petProperties)));

    client.indices().create(createIndexRequest);
  }

  public void setupAppointmentsIndex() throws IOException {
    deleteIndexIfExists(APPOINTMENTS_INDEX);

    Map<String, Property> appointmentProperties = new HashMap<>();
    appointmentProperties.put("id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    appointmentProperties.put("pet_id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    appointmentProperties.put("owner_id", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    appointmentProperties.put(
        "veterinarian", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    appointmentProperties.put(
        "appointment_type", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    appointmentProperties.put(
        "scheduled_time",
        Property.of(p -> p.date(DateProperty.of(d -> d.format("yyyy-MM-dd'T'HH:mm:ss")))));
    appointmentProperties.put(
        "duration_minutes", Property.of(p -> p.integer(IntegerNumberProperty.of(i -> i))));
    appointmentProperties.put("status", Property.of(p -> p.keyword(KeywordProperty.of(k -> k))));
    appointmentProperties.put(
        "reason", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    appointmentProperties.put(
        "diagnosis", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    appointmentProperties.put(
        "treatment", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));
    appointmentProperties.put("cost", Property.of(p -> p.double_(DoubleNumberProperty.of(d -> d))));
    appointmentProperties.put(
        "notes", Property.of(p -> p.text(TextProperty.of(t -> t.analyzer("standard")))));

    CreateIndexRequest createIndexRequest =
        CreateIndexRequest.of(
            c -> c.index(APPOINTMENTS_INDEX).mappings(m -> m.properties(appointmentProperties)));

    client.indices().create(createIndexRequest);
  }

  public void populateWithTestData() throws IOException {
    List<Owner> owners = TestDataGenerator.generateOwners(50);
    List<Pet> pets = TestDataGenerator.generatePets(owners, 3);
    List<Appointment> appointments = TestDataGenerator.generateAppointments(pets, 4);

    bulkIndexOwners(owners);
    bulkIndexPets(pets);
    bulkIndexAppointments(appointments);
  }

  private void bulkIndexOwners(List<Owner> owners) throws IOException {
    for (Owner owner : owners) {
      client.index(i -> i.index(OWNERS_INDEX).id(owner.getId()).document(owner));
    }
  }

  private void bulkIndexPets(List<Pet> pets) throws IOException {
    for (Pet pet : pets) {
      client.index(i -> i.index(PETS_INDEX).id(pet.getId()).document(pet));
    }
  }

  private void bulkIndexAppointments(List<Appointment> appointments) throws IOException {
    for (Appointment appointment : appointments) {
      client.index(i -> i.index(APPOINTMENTS_INDEX).id(appointment.getId()).document(appointment));
    }
  }

  public void deleteAllIndices() throws IOException {
    deleteIndexIfExists(OWNERS_INDEX);
    deleteIndexIfExists(PETS_INDEX);
    deleteIndexIfExists(APPOINTMENTS_INDEX);
  }

  private void deleteIndexIfExists(String indexName) throws IOException {
    BooleanResponse exists = client.indices().exists(ExistsRequest.of(e -> e.index(indexName)));
    if (exists.value()) {
      client.indices().delete(DeleteIndexRequest.of(d -> d.index(indexName)));
    }
  }

  public void refreshAllIndices() throws IOException {
    client.indices().refresh(r -> r.index(OWNERS_INDEX, PETS_INDEX, APPOINTMENTS_INDEX));
  }
}
