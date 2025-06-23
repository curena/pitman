import org.jreleaser.model.Active

plugins {
    `java-library`
    `maven-publish`
    groovy
    id("org.springframework.boot") version "3.5.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.4"
    id("org.jreleaser") version "1.17.0"

}

group = "org.curena"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenLocal()
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
            groupId = project.group.toString()
            artifactId = "pitman"
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
                        email = "curena@hey.com"
                        url = "https://curena.dev"
                    }
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/curena/pitman/issues"
                }

                scm {
                    connection = "scm:git:git://github.com/curena/pitman.git"
                    developerConnection = "scm:git:ssh://github.com:curena/pitman.git"
                    url = "https://github.com/curena/pitman/tree/main"
                }
                /*
                 * <server>
	<id>${server}</id>
	<username>O6+dHWYR</username>
	<password>TWWc5OstNt7U25ZTNGFDzq35dXVL3HyW772wnjZvfsU/</password>
</server>

                 */
                scm {
                    connection = "scm:git:git://github.com/curena/pitman.git"
                    developerConnection = "scm:git:ssh://github.com:curena/pitman.git"
                    url = "https://github.com/curena/pitman/tree/main"
                }
            }
        }
    }
    repositories {
        maven {
            name = "Sonatype"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = properties["centralPortalUsername"].toString()
                password = properties["centralPortalPassword"].toString()
            }
        }
    }
}

jreleaser {
    project {
        inceptionYear = "2025"
        author("@curena")
    }
    release {
        github {
            sign = true
            branch = "main"
            branchPush = "main"
            overwrite = true
        }
    }

    signing {
        active = Active.ALWAYS
        armored = true
        verify = true
        publicKey = providers.environmentVariable("JRELEASER_GPG_PUBLIC_KEY").orElse("")
        secretKey = providers.environmentVariable("JRELEASER_GPG_SECRET_KEY").orElse("")
        passphrase = providers.environmentVariable("JRELEASER_GPG_PASSPHRASE").orElse("")
    }

    deploy {
        maven {
            mavenCentral.create("sonatype") {
                active = Active.ALWAYS
                url = "https://central.sonatype.com/api/v1/publisher"
                username = providers.environmentVariable("JRELEASER_MAVENCENTRAL_USERNAME").orElse("")
                password = providers.environmentVariable("JRELEASER_MAVENCENTRAL_TOKEN").orElse("")
                stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
                setAuthorization("Basic")
                retryDelay = 60
            }
        }
    }
}
