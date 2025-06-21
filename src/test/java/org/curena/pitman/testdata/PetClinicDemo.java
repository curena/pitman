package org.curena.pitman.testdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.core.search.Pit;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class PetClinicDemo {
  private static final String OPENSEARCH_URL = "http://localhost:9200";

  public static void main(String[] args) {
    try {
      runDemo();
    } catch (Exception e) {
      System.err.println("Demo failed: " + e.getMessage());
    }
  }

  public static void runDemo() throws IOException, URISyntaxException {
    System.out.println("=== Pet Clinic OpenSearch PIT Demo ===\n");

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    var transport =
        ApacheHttpClient5TransportBuilder.builder(HttpHost.create(OPENSEARCH_URL)).build();

    try (transport) {
      OpenSearchClient client = new OpenSearchClient(transport);

      PetClinicIndexSetup indexSetup = new PetClinicIndexSetup(client);

      System.out.println("Setting up indices and populating with test data...");
      indexSetup.setupAllIndices();
      indexSetup.populateWithTestData();
      indexSetup.refreshAllIndices();
      System.out.println("Test data loaded successfully!\n");

      demonstratePITUsage(client);
      demonstratePITConsistency(client);
      demonstrateMultiIndexPIT(client);

      System.out.println("Cleaning up indices...");
      indexSetup.deleteAllIndices();
      System.out.println("Demo completed successfully!");
    }
  }

  private static void demonstratePITUsage(OpenSearchClient client) throws IOException {
    System.out.println("=== Demonstrating Basic PIT Usage ===");

    CreatePitRequest pitRequest =
        CreatePitRequest.of(
            p -> p.index(PetClinicIndexSetup.PETS_INDEX).keepAlive(Time.of(t -> t.time("1m"))));

    CreatePitResponse pitResponse = client.createPit(pitRequest);
    String pitId = pitResponse.pitId();
    assert pitId != null;
    System.out.println("Created PIT with ID: " + pitId.substring(0, 20) + "...");

    try {
      SearchRequest searchRequest =
          SearchRequest.of(
              s ->
                  s.pit(Pit.of(p -> p.id(pitId).keepAlive("1m")))
                      .query(q -> q.term(t -> t.field("species").value(FieldValue.of("Dog"))))
                      .size(5));

      SearchResponse<Pet> searchResponse = client.search(searchRequest, Pet.class);

      assert searchResponse.hits().total() != null;
      System.out.println("Found " + searchResponse.hits().total().value() + " dogs:");
      for (Hit<Pet> hit : searchResponse.hits().hits()) {
        Pet source = hit.source();
        if (source == null) {
          continue;
        }
        System.out.println("  - " + source.getName() + " (" + source.getBreed() + ")");
      }

    } finally {
      DeletePitRequest deletePitRequest = DeletePitRequest.of(d -> d.pitId(pitId));
      client.deletePit(deletePitRequest);
      System.out.println("PIT deleted\n");
    }
  }

  private static void demonstratePITConsistency(OpenSearchClient client) throws IOException {
    System.out.println("=== Demonstrating PIT Consistency ===");

    CreatePitRequest pitRequest =
        CreatePitRequest.of(
            p ->
                p.index(PetClinicIndexSetup.APPOINTMENTS_INDEX)
                    .keepAlive(Time.of(t -> t.time("2m"))));

    CreatePitResponse pitResponse = client.createPit(pitRequest);
    String pitId = pitResponse.pitId();
    System.out.println("Created PIT for appointments index");

    try {
      SearchRequest searchRequest =
          SearchRequest.of(
              s ->
                  s.pit(Pit.of(p -> p.id(pitId).keepAlive("2m")))
                      .query(q -> q.term(t -> t.field("status").value(FieldValue.of("Completed"))))
                      .size(3)
                      .sort(
                          sort ->
                              sort.field(
                                  f ->
                                      f.field("scheduled_time")
                                          .order(
                                              org.opensearch.client.opensearch._types.SortOrder
                                                  .Desc))));

      SearchResponse<Appointment> firstPage = client.search(searchRequest, Appointment.class);
      System.out.println(
          "First page - " + firstPage.hits().hits().size() + " completed appointments:");

      for (Hit<Appointment> hit : firstPage.hits().hits()) {
        Appointment source = hit.source();
        System.out.println(
            "  - "
                + Objects.requireNonNull(source).getAppointmentType()
                + " with "
                + source.getVeterinarian());
      }

      if (!firstPage.hits().hits().isEmpty()) {
        var lastSort = firstPage.hits().hits().getLast().sort();

        SearchRequest nextPageRequest =
            SearchRequest.of(
                s ->
                    s.pit(Pit.of(p -> p.id(pitId).keepAlive("2m")))
                        .query(
                            q -> q.term(t -> t.field("status").value(FieldValue.of("Completed"))))
                        .size(3)
                        .sort(
                            sort ->
                                sort.field(
                                    f ->
                                        f.field("scheduled_time")
                                            .order(
                                                org.opensearch.client.opensearch._types.SortOrder
                                                    .Desc)))
                        .searchAfter(lastSort));

        SearchResponse<Appointment> secondPage = client.search(nextPageRequest, Appointment.class);
        System.out.println(
            "Second page - " + secondPage.hits().hits().size() + " completed appointments:");

        for (Hit<Appointment> hit : secondPage.hits().hits()) {
          Appointment source = hit.source();
          System.out.println(
              "  - "
                  + Objects.requireNonNull(source).getAppointmentType()
                  + " with "
                  + source.getVeterinarian());
        }
      }

    } finally {
      DeletePitRequest deletePitRequest = DeletePitRequest.of(d -> d.pitId(pitId));
      client.deletePit(deletePitRequest);
      System.out.println("PIT deleted\n");
    }
  }

  private static void demonstrateMultiIndexPIT(OpenSearchClient client) throws IOException {
    System.out.println("=== Demonstrating Multi-Index PIT ===");

    CreatePitRequest pitRequest =
        CreatePitRequest.of(
            p ->
                p.index(PetClinicIndexSetup.OWNERS_INDEX, PetClinicIndexSetup.PETS_INDEX)
                    .keepAlive(Time.of(t -> t.time("1m"))));

    CreatePitResponse pitResponse = client.createPit(pitRequest);
    String pitId = pitResponse.pitId();
    System.out.println("Created multi-index PIT covering owners and pets");

    try {
      SearchRequest searchRequest =
          SearchRequest.of(
              s ->
                  s.pit(Pit.of(p -> p.id(pitId).keepAlive("1m")))
                      .query(
                          q ->
                              q.bool(
                                  b ->
                                      b.should(
                                              should ->
                                                  should.match(
                                                      m ->
                                                          m.field("first_name")
                                                              .query(FieldValue.of("John"))))
                                          .should(
                                              should ->
                                                  should.match(
                                                      m ->
                                                          m.field("name")
                                                              .query(FieldValue.of("Buddy"))))))
                      .size(10));

      SearchResponse<Map> searchResponse = client.search(searchRequest, Map.class);
      System.out.println(
          "Multi-index search results ("
              + Objects.requireNonNull(searchResponse.hits().total()).value()
              + " total):");

      for (Hit<Map> hit : searchResponse.hits().hits()) {
        @SuppressWarnings("unchecked")
        Map<String, Object> source = (Map<String, Object>) hit.source();
        String indexName = hit.index();

        if (PetClinicIndexSetup.OWNERS_INDEX.equals(indexName)) {
          System.out.println(
              "  - Owner: " + Objects.requireNonNull(source).get("first_name") + " " + source.get("last_name"));
        } else if (PetClinicIndexSetup.PETS_INDEX.equals(indexName)) {
          System.out.println("  - Pet: " + Objects.requireNonNull(source).get("name") + " (" + source.get("species") + ")");
        }
      }

    } finally {
      DeletePitRequest deletePitRequest = DeletePitRequest.of(d -> d.pitId(pitId));
      client.deletePit(deletePitRequest);
      System.out.println("Multi-index PIT deleted\n");
    }
  }
}
