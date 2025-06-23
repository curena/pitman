package org.curena.pitman;

import java.io.IOException;
import java.util.List;

import org.opensearch.client.opensearch._types.Time;
import org.opensearch.client.opensearch.core.search.Pit;

/**
 * Interface for managing Point-in-Time (PIT) contexts in OpenSearch. Provides methods for creating,
 * using, and cleaning up PIT contexts.
 */
public interface PitManager {

  /**
   * Creates a new PIT context for the specified indices.
   *
   * @param indices The indices to create a PIT for
   * @param keepAlive The keep-alive time for the PIT
   * @return The PIT ID
   * @throws IOException If an error occurs during PIT creation
   */
  String createPit(List<String> indices, Time keepAlive) throws IOException;

  /**
   * Creates a new PIT context for the specified indices.
   *
   * @param indices The indices to create a PIT for
   * @param keepAlive The keep-alive time for the PIT as a string (e.g., "1m", "5h")
   * @return The PIT ID
   * @throws IOException If an error occurs during PIT creation
   */
  String createPit(List<String> indices, String keepAlive) throws IOException;

  /**
   * Creates a new PIT context for a single index.
   *
   * @param index The index to create a PIT for
   * @param keepAlive The keep-alive time for the PIT
   * @return The PIT ID
   * @throws IOException If an error occurs during PIT creation
   */
  String createPit(String index, Time keepAlive) throws IOException;

  /**
   * Creates a new PIT context for a single index.
   *
   * @param index The index to create a PIT for
   * @param keepAlive The keep-alive time for the PIT as a string (e.g., "1m", "5h")
   * @return The PIT ID
   * @throws IOException If an error occurs during PIT creation
   */
  String createPit(String index, String keepAlive) throws IOException;

  /**
   * Creates a new PIT context for the specified indices using varargs.
   *
   * @param keepAlive The keep-alive time for the PIT
   * @param indices The indices to create a PIT for as varargs
   * @return The PIT ID
   * @throws IOException If an error occurs during PIT creation
   */
  String createPit(Time keepAlive, String... indices) throws IOException;

  /**
   * Creates a new PIT context for the specified indices using varargs.
   *
   * @param keepAlive The keep-alive time for the PIT as a string (e.g., "1m", "5h")
   * @param indices The indices to create a PIT for as varargs
   * @return The PIT ID
   * @throws IOException If an error occurs during PIT creation
   */
  String createPit(String keepAlive, String... indices) throws IOException;

  /**
   * Creates a Pit object for use in search requests.
   *
   * @param pitId The PIT ID
   * @param keepAlive The keep-alive time for the PIT
   * @return A Pit object for use in search requests
   */
  Pit createPitForSearch(String pitId, String keepAlive);

  /**
   * Deletes a PIT context.
   *
   * @param pitId The PIT ID to delete
   * @return true if the PIT was successfully deleted, false otherwise
   * @throws IOException If an error occurs during PIT deletion
   */
  boolean deletePit(String pitId) throws IOException;
}
