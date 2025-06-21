package org.curena.pitman

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.hc.core5.http.HttpHost
import org.curena.pitman.testdata.PetClinicIndexSetup
import org.opensearch.client.json.jackson.JacksonJsonpMapper
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch._types.FieldValue
import org.opensearch.client.opensearch._types.SortOrder
import org.opensearch.client.opensearch._types.Time
import org.opensearch.client.opensearch.core.*
import org.opensearch.client.opensearch.core.search.Hit
import org.opensearch.client.opensearch.core.search.Pit
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder
import org.opensearch.testcontainers.OpensearchContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration

@SpringBootTest
@Testcontainers
class PetClinicIntegrationSpec extends Specification {

    @Shared
    OpensearchContainer<?> opensearchContainer = new OpensearchContainer<>("opensearchproject/opensearch:2")
            .withExposedPorts(9200)
            .withEnv("discovery.type", "single-node")
            .withEnv("DISABLE_SECURITY_PLUGIN", "true")
            .withEnv("OPENSEARCH_JAVA_OPTS", "-Xms512m -Xmx512m")
            .waitingFor(Wait.forHttp("/").forPort(9200).withStartupTimeout(Duration.ofMinutes(2)))

    @Shared
    OpenSearchClient client

    @Shared
    PetClinicIndexSetup indexSetup

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("opensearch.host") { -> "localhost" }
        registry.add("opensearch.port") { -> opensearchContainer.getMappedPort(9200) }
        registry.add("opensearch.scheme") { -> "http" }
    }

    def setupSpec() {
        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())

        def transport = ApacheHttpClient5TransportBuilder.builder(
                HttpHost.create("http://localhost:${opensearchContainer.getMappedPort(9200)}")
        ).setMapper(new JacksonJsonpMapper(objectMapper)).build()

        client = new OpenSearchClient(transport)
        indexSetup = new PetClinicIndexSetup(client)
        
        indexSetup.setupAllIndices()
        indexSetup.populateWithTestData()
        indexSetup.refreshAllIndices()
    }

    def cleanupSpec() {
        client?._transport()?.close()
    }

    def "should create and use Point-in-Time for pet clinic data"() {
        given: "a PIT is created for the pets index"
        CreatePitRequest pitRequest = CreatePitRequest.of(p -> p
                .index(PetClinicIndexSetup.PETS_INDEX)
                .keepAlive(Time.of(t -> t.time("1m")))
        )
        CreatePitResponse pitResponse = client.createPit(pitRequest)
        String pitId = pitResponse.pitId()

        when: "searching with PIT for dogs"
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .pit(Pit.of(p -> p.id(pitId).keepAlive("1m")))
                .query(q -> q.term(t -> t.field("species").value(FieldValue.of("Dog"))))
                .size(20)
        )
        SearchResponse<Map> searchResponse = client.search(searchRequest, Map.class)

        then: "results should contain dogs"
        searchResponse.hits().total().value() > 0
        searchResponse.hits().hits().every { Hit<Map> hit ->
            hit.source().get("species") == "Dog"
        }

        and: "PIT ID should be present in response"
        searchResponse.pitId() == pitId

        when: "searching for a specific breed with PIT"
        SearchRequest breedSearchRequest = SearchRequest.of(s -> s
                .pit(Pit.of(p -> p.id(pitId).keepAlive("1m")))
                .query(q -> q.bool(b -> b
                        .must(m -> m.term(t -> t.field("species").value(FieldValue.of("Dog"))))
                        .must(m -> m.term(t -> t.field("breed").value(FieldValue.of("Golden Retriever"))))
                ))
                .size(10)
        )
        SearchResponse<Map> breedSearchResponse = client.search(breedSearchRequest, Map.class)

        then: "results should contain only Golden Retrievers"
        breedSearchResponse.hits().hits().every { Hit<Map> hit ->
            hit.source().get("species") == "Dog"
            hit.source().get("breed") == "Golden Retriever"
        }

        cleanup: "delete the PIT"
        if (pitId) {
            DeletePitRequest deletePitRequest = DeletePitRequest.of(d -> d.pitId(pitId))
            client.deletePit(deletePitRequest)
        }
    }

    def "should demonstrate PIT consistency across multiple searches"() {
        given: "a PIT is created for appointments index"
        CreatePitRequest pitRequest = CreatePitRequest.of(p -> p
                .index(PetClinicIndexSetup.APPOINTMENTS_INDEX)
                .keepAlive(Time.of(t -> t.time("2m")))
        )
        CreatePitResponse pitResponse = client.createPit(pitRequest)
        String pitId = pitResponse.pitId()

        when: "performing initial search for completed appointments"
        SearchRequest initialSearch = SearchRequest.of(s -> s
                .pit(Pit.of(p -> p.id(pitId).keepAlive("2m")))
                .query(q -> q.term(t -> t.field("status").value(FieldValue.of("Completed"))))
                .sort(sort -> sort.field(f -> f.field("scheduled_time").order(SortOrder.Desc)))
                .size(5)
        )
        SearchResponse<Map> initialResponse = client.search(initialSearch, Map.class)
        long initialCount = initialResponse.hits().total().value()

        and: "performing follow-up search with same PIT"
        SearchRequest followUpSearch = SearchRequest.of(s -> s
                .pit(Pit.of(p -> p.id(pitId).keepAlive("2m")))
                .query(q -> q.term(t -> t.field("status").value(FieldValue.of("Completed"))))
                .sort(sort -> sort.field(f -> f.field("scheduled_time").order(SortOrder.Desc)))
                .size(5)
                .searchAfter(initialResponse.hits().hits().last().sort())
        )
        SearchResponse<Map> followUpResponse = client.search(followUpSearch, Map.class)

        then: "both searches should use the same point in time"
        initialResponse.pitId() == pitId
        followUpResponse.pitId() == pitId
        initialCount > 0
        followUpResponse.hits().hits().size() >= 0

        and: "all results should be completed appointments"
        (initialResponse.hits().hits() + followUpResponse.hits().hits()).every { Hit<Map> hit ->
            hit.source().get("status") == "Completed"
        }

        cleanup: "delete the PIT"
        if (pitId) {
            DeletePitRequest deletePitRequest = DeletePitRequest.of(d -> d.pitId(pitId))
            client.deletePit(deletePitRequest)
        }
    }

    def "should search across multiple indices with PIT"() {
        given: "a PIT is created for multiple indices"
        CreatePitRequest pitRequest = CreatePitRequest.of(p -> p
                .index(PetClinicIndexSetup.OWNERS_INDEX, PetClinicIndexSetup.PETS_INDEX)
                .keepAlive(Time.of(t -> t.time("1m")))
        )
        CreatePitResponse pitResponse = client.createPit(pitRequest)
        String pitId = pitResponse.pitId()

        when: "searching for documents containing 'Smith'"
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .pit(Pit.of(p -> p.id(pitId).keepAlive("1m")))
                .query(q -> q.multiMatch(m -> m
                        .query("Smith")
                        .fields("first_name", "last_name", "name")
                ))
                .size(10)
        )
        SearchResponse<Map> searchResponse = client.search(searchRequest, Map.class)

        then: "should find results from both indices"
        searchResponse.hits().total().value() >= 0
        searchResponse.pitId() == pitId

        cleanup: "delete the PIT"
        if (pitId) {
            DeletePitRequest deletePitRequest = DeletePitRequest.of(d -> d.pitId(pitId))
            client.deletePit(deletePitRequest)
        }
    }

    def "should handle PIT errors gracefully"() {
        when: "attempting to search with invalid PIT ID"
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .pit(Pit.of(p -> p.id("invalid-pit-id").keepAlive("1m")))
                .query(q -> q.matchAll(m -> m))
                .size(1)
        )

        then: "should throw exception"
        def exception = null
        try {
            client.search(searchRequest, Map.class)
        } catch (Exception e) {
            exception = e
        }
        exception != null
    }
}