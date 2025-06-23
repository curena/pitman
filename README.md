# Pitman

A Spring Boot opinionated OpenSearch PIT manager

## Project Overview
Pitman is a Java library for managing OpenSearch Point-in-Time (PIT) contexts. 
It provides utilities to simplify PIT lifecycle management 
for applications using OpenSearch.

The intention is for consuming services to add this library as a dependency and either:
- Use Spring Boot to inject the library client (Spring Data OpenSearch will be available to the context)
- Instantiate the library client directly and use the OpenSearch Java client

## Project Structure
- `src/main/java/` - Java library source code
- `src/main/resources/` - Configuration files and Spring Boot auto-configuration
- `src/test/java/` - JUnit test code
- `src/test/groovy/` - Spock Framework tests
- `src/test/resources/` - Test resources
- `docker-compose.yml` - Local OpenSearch development environment

## Technology Stack
- Java 21 (Eclipse Temurin)
- Gradle with Kotlin DSL (build.gradle.kts)
- OpenSearch Java Client 3.x.x
- Spring Data OpenSearch 1.8.x
- Spring Boot 3.x.x
- Spock Framework 2.3-groovy-4.0 
- Testcontainers (Spock, testcontainers-opensearch) for integration testing

## Library Architecture
Pitman provides:
- APIs for creating, listing and deleting PITs
- Automatic PIT lifecycle handling (creation, keepalive, cleanup)
- Integration with the OpenSearch Java client
- Thread-safe PIT operations for concurrent applications
- Configuration options for PIT timeouts and cleanup strategies

## Build and Test Instructions

### Common Commands
```bash
# Build the library
./gradlew build

# Run tests (includes Spock and Testcontainers integration tests)
./gradlew test

# Clean build artifacts
./gradlew clean

# Publish to local Maven repository
./gradlew publishToMavenLocal

# See all available tasks
./gradlew tasks --all

# Start local OpenSearch for development
docker-compose up -d

# Stop local OpenSearch
docker-compose down
```

## Development Setup
1. Ensure Java 21+ is installed
2. Start local OpenSearch: `docker-compose up -d`
3. Build the project: `./gradlew build`
4. Run tests: `./gradlew test`

## Contributing
Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute to this project.

## License
This project is licensed under the terms of the license included in the [LICENSE](LICENSE) file.
