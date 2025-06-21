# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Pitman is a Java library for managing Elasticsearch/OpenSearch Point-in-Time (PIT) contexts. It provides utilities to simplify PIT lifecycle management for applications using Elasticsearch or OpenSearch.

## Build System

This project uses Gradle with Kotlin DSL (build.gradle.kts) and Java 21 with Eclipse Temurin (Adoptium).

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

## Project Structure

Standard Maven/Gradle layout:
- `src/main/java/` - Java library source code
- `src/main/resources/` - Configuration files and Spring Boot auto-configuration
- `src/test/java/` - JUnit test code
- `src/test/groovy/` - Spock Framework tests
- `src/test/resources/` - Test resources
- `docker-compose.yml` - Local OpenSearch development environment

## Library Architecture

As a Java library for Elasticsearch/OpenSearch PIT management, this will likely provide:
- PIT context creation and management APIs
- Automatic PIT lifecycle handling (creation, keepalive, cleanup)
- Integration with Elasticsearch/OpenSearch Java clients
- Thread-safe PIT operations for concurrent applications
- Configuration options for PIT timeouts and cleanup strategies

## Technology Stack

- **Java 21** (Eclipse Temurin)
- **Gradle** with Kotlin DSL
- **OpenSearch Java Client 2.15.0**
- **Spring Data OpenSearch 5.3.6**
- **Spring Boot 3.3.6** (for auto-configuration)
- **Spock Framework 2.3** (Groovy-based testing)
- **Testcontainers 1.20.4** (integration testing)

## Development Setup

1. Ensure Java 21+ is installed
2. Start local OpenSearch: `docker-compose up -d`
3. Build the project: `./gradlew build`
4. Run tests: `./gradlew test`