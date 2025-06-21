plugins {
    `java-library`
    `maven-publish`
    groovy
    id("org.springframework.boot") version "3.5.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.4"
}

group = "org.curena"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.2")
        mavenBom("org.testcontainers:testcontainers-bom:1.21.2")
    }
}

dependencies {
    api("org.opensearch.client:opensearch-java:3.1.0")
    api("org.opensearch.client:spring-data-opensearch:1.8.0") {
        exclude(group = "org.opensearch.client", module = "opensearch-rest-high-level-client")
    }

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework:spring-context")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.1")
    implementation("org.slf4j:slf4j-api")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.opensearch:opensearch-testcontainers:2.1.3")
    testImplementation("org.testcontainers:spock:1.21.2")
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")
    testImplementation("org.apache.groovy:groovy-all:4.0.23")
    testImplementation("net.bytebuddy:byte-buddy:1.15.10")
    testImplementation("org.objenesis:objenesis:3.4")

    testRuntimeOnly("ch.qos.logback:logback-classic")
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        importOrder("java", "javax", "org", "com", "")
        endWithNewline()
        trimTrailingWhitespace()
    }

    kotlin {
        ktlint()
        endWithNewline()
        trimTrailingWhitespace()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("user.timezone", "UTC")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-parameters", "-Xlint:unchecked", "-Xlint:deprecation"))
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name = "Pitman"
                description = "A Java library for managing OpenSearch Point-in-Time contexts"
                url = "https://github.com/curena/pitman"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                developers {
                    developer {
                        id = "curena"
                        name = "Cecil Ureña"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/curena/pitman.git"
                    developerConnection = "scm:git:ssh://github.com:curena/pitman.git"
                    url = "https://github.com/curena/pitman/tree/main"
                }
            }
        }
    }
}
