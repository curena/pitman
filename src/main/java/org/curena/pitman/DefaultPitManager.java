package org.curena.pitman;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch.core.CreatePitRequest;
import org.opensearch.client.opensearch.core.CreatePitResponse;
import org.opensearch.client.opensearch.core.DeletePitRequest;
import org.opensearch.client.opensearch.core.search.Pit;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the PitManager interface. Uses the OpenSearch client to manage PIT
 * contexts.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultPitManager implements PitManager {

  private final OpenSearchClient client;

  @Override
  public String createPit(List<String> indices, Time keepAlive) throws IOException {
    log.debug("Creating PIT for indices {} with keepAlive {}", indices, keepAlive);

    CreatePitRequest.Builder builder = new CreatePitRequest.Builder();
    builder.keepAlive(keepAlive);
    for (String index : indices) {
      builder.index(index);
    }

    CreatePitResponse response = client.createPit(builder.build());
    String pitId = response.pitId();

    log.debug("Created PIT with ID: {}", pitId);
    return pitId;
  }

  @Override
  public String createPit(List<String> indices, String keepAlive) throws IOException {
    return createPit(indices, Time.of(t -> t.time(keepAlive)));
  }

  @Override
  public String createPit(String index, Time keepAlive) throws IOException {
    return createPit(Collections.singletonList(index), keepAlive);
  }

  @Override
  public String createPit(String index, String keepAlive) throws IOException {
    return createPit(Collections.singletonList(index), keepAlive);
  }

  @Override
  public String createPit(Time keepAlive, String... indices) throws IOException {
    return createPit(List.of(indices), keepAlive);
  }

  @Override
  public String createPit(String keepAlive, String... indices) throws IOException {
    return createPit(List.of(indices), keepAlive);
  }

  @Override
  public Pit createPitForSearch(String pitId, String keepAlive) {
    log.debug(
        "Creating Pit object for search with PIT ID: {} and keepAlive: {}", pitId, keepAlive);
    return Pit.of(p -> p.id(pitId).keepAlive(keepAlive));
  }

  @Override
  public boolean deletePit(String pitId) throws IOException {
    log.debug("Deleting PIT with ID: {}", pitId);

    DeletePitRequest request = DeletePitRequest.of(d -> d.pitId(pitId));
    client.deletePit(request);

    // Assuming deletion was successful if no exception was thrown
    log.debug("PIT deletion completed: {}", pitId);

    return true;
  }
}
