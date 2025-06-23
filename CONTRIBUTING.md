# Contributing to Pitman

Thank you for considering contributing to Pitman! This document provides guidelines and instructions for contributing to the project.

## Code of Conduct

Please be respectful and considerate of others when contributing to this project. We aim to foster an inclusive and welcoming community.

## How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature-name`)
3. Make your changes
4. Run tests to ensure your changes don't break existing functionality
5. Commit your changes (`git commit -m 'Add some feature'`)
6. Push to the branch (`git push origin feature/your-feature-name`)
7. Create a new Pull Request

## Development Workflow

1. Ensure Java 21+ is installed
2. Start local OpenSearch: `docker-compose up -d`
3. Build the project: `./gradlew build`
4. Run tests: `./gradlew test`
5. Run `./gradlew spotlessApply` before committing to ensure code style compliance

## Code Style Guidelines

- Follow Google Java code style conventions
- Use four spaces for indentation
- Maximum line length: 120 characters
- Include JavaDoc for public methods and classes
- Use meaningful variable and method names
- Write unit tests for new functionality
- Write integration tests for each of the big functionality features
- Maintain backward compatibility when possible
- Use Lombok to reduce boilerplate
- Use @lombok.extern.Slf4j for logging

## Testing Guidelines

- Always run tests to verify the correctness of proposed solutions
- Use `./gradlew test` to run all tests
- For specific test classes, use: `./gradlew test --tests "org.curena.pitman.TestClassName"`
- Integration tests use Testcontainers, which requires Docker to be running
- Check both unit tests and integration tests pass before submitting

## Pull Request Process

1. Ensure your code follows the code style guidelines
2. Update the README.md with details of changes if appropriate
3. The PR should work on the main branch
4. Include tests that cover your changes
5. Update documentation as necessary

## Reporting Bugs

When reporting bugs, please include:

1. A clear and descriptive title
2. Steps to reproduce the issue
3. Expected behavior
4. Actual behavior
5. Environment details (OS, Java version, etc.)

## Feature Requests

Feature requests are welcome. Please provide:

1. A clear and descriptive title
2. Detailed description of the proposed feature
3. Any relevant examples or use cases
4. Explanation of why this feature would be useful to most users

## Questions?

If you have any questions about contributing, please open an issue with your question.

Thank you for contributing to Pitman!