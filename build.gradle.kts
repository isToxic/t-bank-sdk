plugins {
    `java-library`
    `maven-publish`

    id("io.spring.dependency-management") version "1.1.7"
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.protobuf.plugin)
    alias(libs.plugins.openapi.generator)

}


group = "com.github.isToxic"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}")
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    api(libs.jakarta.annotation.api)
    api(libs.jackson.databind.nullable)

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-grpc-client")

    api("org.apache.httpcomponents.client5:httpclient5")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    api(libs.resilience4j.circuitbreaker)
    api(libs.resilience4j.ratelimiter)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.wiremock:wiremock-standalone:3.9.1")
    testImplementation("io.grpc:grpc-inprocess:1.68.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}


sourceSets {
    main {
        java.srcDirs(
            "${layout.buildDirectory.asFile.get().absolutePath}/generated/sources/open-api/main/java",
            "${layout.buildDirectory.asFile.get().absolutePath}/generated/sources/proto/main/java",
            "${layout.buildDirectory.asFile.get().absolutePath}/generated/sources/proto/main/grpc",
            "src/main/java",
        )
        resources.srcDirs("src/main/resources")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java"
        }
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("gRPC & REST sdk for T-bank invest api")
                description.set("Custom gRPC & REST sdk for T-bank invest api")
                url.set("https://github.com/isToxic/t-bank-sdk")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("isToxic")
                        name.set("Stepan Mokrov")
                        email.set("s.b.mokrov@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/isToxic/t-bank-sdk.git")
                    developerConnection.set("scm:git:ssh://github.com:isToxic/t-bank-sdk.git")
                    url.set("https://github.com/isToxic/t-bank-sdk")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/istoxic/t-bank-sdk")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GH_USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GH_TOKEN")
            }
        }
    }
}

tasks{

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    processTestResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    named<JavaCompile>("compileJava") {
        options.encoding = "UTF-8"
        dependsOn("codegen")
    }

    named<Jar>("sourcesJar") {
        dependsOn("codegen", "generateProto")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = false
    }

    withType<Test> {
        useJUnitPlatform()
    }

    register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("codegen") {
        description = "tinkoff-swagger.yaml codegen"

        val spec = "$rootDir/src/main/resources/open-api-docs/tinkoff-swagger.yaml"
        val openApiIgnore = "$rootDir/.openapi-generator-ignore"

        generatorName.set("java")
        library.set("restclient")

        outputDir.set("${layout.buildDirectory.asFile.get().absolutePath}/generated/sources/open-api/main/java")
        inputSpec.set(spec)

        generateApiTests.set(false)
        generateModelTests.set(false)
        generateModelDocumentation.set(false)
        generateApiDocumentation.set(false)

        apiPackage.set("is.toxic.tbank.sdk.bot.api")
        modelPackage.set("is.toxic.tbank.sdk.model")
        invokerPackage.set("is.toxic.tbank.sdk.invoker")

        ignoreFileOverride.set(openApiIgnore)

        configOptions.set(
            mapOf(
                "sourceFolder" to "",
                "dateLibrary" to "java8",
                "useJakartaEe" to "true",
                "useJackson3" to "true",
                "useSpringBoot4" to "true"
            ),
        )

        inputs
            .files(file(openApiIgnore))
            .withPathSensitivity(PathSensitivity.NONE)
            .withPropertyName("openApiGeneratorIgnoreFile")
    }
}
