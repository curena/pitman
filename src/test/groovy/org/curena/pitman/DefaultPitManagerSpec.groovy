package org.curena.pitman

import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch._types.Time
import org.opensearch.client.opensearch.core.CreatePitRequest
import org.opensearch.client.opensearch.core.CreatePitResponse
import org.opensearch.client.opensearch.core.DeletePitRequest
import org.opensearch.client.opensearch.core.search.Pit
import spock.lang.Specification
import spock.lang.Subject

import java.util.function.Function

class DefaultPitManagerSpec extends Specification {

    def client = Mock(OpenSearchClient)

    @Subject
    DefaultPitManager pitManager

    def setup() {
        pitManager = new DefaultPitManager(client)
    }

    def "createPit should create a PIT for a list of indices with Time keepAlive"() {
        given: "a list of indices and a keepAlive time"
        def indices = ["index1", "index2"]
        def keepAlive = Time.of(t -> t.time("1m"))
        def expectedPitId = "test-pit-id"
        def pitResponse = CreatePitResponse.builder().pitId(expectedPitId).build()

        when: "creating a PIT"
        def result = pitManager.createPit(indices, keepAlive)

        then: "the client should be called and return the expected response"
        1 * client.createPit({ CreatePitRequest request ->
            request.index().containsAll(indices) &&
            request.keepAlive() == keepAlive
        }) >> pitResponse

        and: "the method should return the PIT ID"
        result == expectedPitId
    }

    def "createPit should create a PIT for a single index with String keepAlive"() {
        given: "a single index and a keepAlive string"
        def index = "index1"
        def keepAliveStr = "1m"
        def expectedPitId = "test-pit-id"
        def createPitResponse = CreatePitResponse.builder().pitId(expectedPitId).build()

        when: "creating a PIT"
        def result = pitManager.createPit(index, keepAliveStr)

        then: "the client should be called with the correct parameters and return the expected response"
        1 * client.createPit({ CreatePitRequest request ->
            request.index().contains(index) &&
            request.keepAlive().time() == keepAliveStr
        }) >> createPitResponse

        and: "the method should return the PIT ID"
        result == expectedPitId
    }

    def "createPit should create a PIT for varargs indices with Time keepAlive"() {
        given: "varargs indices and a keepAlive time"
        def indices = ["index1", "index2"] as String[]
        def keepAlive = Time.of(t -> t.time("1m"))
        def expectedPitId = "test-pit-id"
        def createPitResponse = CreatePitResponse.builder().pitId(expectedPitId).build()

        when: "creating a PIT"
        def result = pitManager.createPit(keepAlive, indices)

        then: "the client should be called with the correct parameters and return the expected response"
        1 * client.createPit({ CreatePitRequest request ->
            request.index().containsAll(indices.toList()) &&
            request.keepAlive() == keepAlive
        }) >> createPitResponse

        and: "the method should return the PIT ID"
        result == expectedPitId
    }

    def "createPit should create a PIT for varargs indices with String keepAlive"() {
        given: "varargs indices and a keepAlive string"
        def indices = ["index1", "index2"] as String[]
        def keepAliveStr = "1m"
        def expectedPitId = "test-pit-id"
        def createPitResponse = CreatePitResponse.builder().pitId(expectedPitId).build()

        when: "creating a PIT"
        def result = pitManager.createPit(keepAliveStr, indices)

        then: "the client should be called with the correct parameters and return the expected response"
        1 * client.createPit({ CreatePitRequest request ->
            request.index().containsAll(indices.toList()) &&
            request.keepAlive().time() == keepAliveStr
        }) >> createPitResponse

        and: "the method should return the PIT ID"
        result == expectedPitId
    }

    def "createPitForSearch should create a Pit object with the correct parameters"() {
        given: "a PIT ID and keepAlive string"
        def pitId = "test-pit-id"
        def keepAlive = "1m"

        when: "creating a Pit object for search"
        def result = pitManager.createPitForSearch(pitId, keepAlive)

        then: "the Pit object should have the correct properties"
        result.id() == pitId
        result.keepAlive() == keepAlive
    }

    def "deletePit should delete a PIT with the given ID"() {
        given: "a PIT ID"
        def pitId = "test-pit-id"

        when: "deleting a PIT"
        def result = pitManager.deletePit(pitId)

        then: "the client should be called with the correct parameters"
        1 * client.deletePit(_) >> { DeletePitRequest request ->
            request.pitId().size() == 1
            request.pitId().contains(pitId)
            return null // The actual response doesn't matter as it's not used
        }

        and: "the method should return true"
        result
    }

    def "createPit should handle IOException and rethrow it"() {
        given: "a list of indices and a keepAlive time"
        def indices = ["index1", "index2"]
        def keepAlive = Time.of(t -> t.time("1m"))
        def ioException = new IOException("Test exception")

        when: "creating a PIT that throws an exception"
        pitManager.createPit(indices, keepAlive)

        then: "the client should be called and throw an exception"
        1 * client.createPit(_) >> { throw ioException }

        and: "the exception should be rethrown"
        thrown(IOException)
    }

    def "deletePit should handle IOException and rethrow it"() {
        given: "a PIT ID"
        def pitId = "test-pit-id"
        def ioException = new IOException("Test exception")

        when: "deleting a PIT that throws an exception"
        pitManager.deletePit(pitId)

        then: "the client should be called and throw an exception"
        1 * client.deletePit(_) >> { throw ioException }

        and: "the exception should be rethrown"
        thrown(IOException)
    }
}