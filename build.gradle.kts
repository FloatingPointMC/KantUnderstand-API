import java.net.HttpURLConnection
import java.net.URI
import java.util.Base64

plugins {
    id("java")
    id("maven-publish")
    id("signing")
}

java {
    toolchain {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    withSourcesJar()
    withJavadocJar()
}

group = "io.github.floatingpointmc"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.jetbrains:annotations:26.1.0")
    annotationProcessor("org.jetbrains:annotations:26.1.0")

    compileOnly("org.projectlombok:lombok:1.18.48")
    annotationProcessor("org.projectlombok:lombok:1.18.48")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "kantunderstand-api"

            pom {
                name = "KantUnderstand-API"
                description = "Public API annotations for the KantUnderstand obfuscation framework."
                url = "https://github.com/floatingpointmc/KantUnderstand-API"
                inceptionYear = "2026"

                licenses {
                    license {
                        name = "MIT-0"
                        url = "https://opensource.org/license/mit-0"
                    }
                }

                developers {
                    developer {
                        id = "floatingpointmc"
                        name = "FloatingPoint-MC"
                        email = "vlouyearlinjinhua@outlook.com"
                    }
                }

                scm {
                    url = "https://github.com/floatingpointmc/KantUnderstand-API"
                    connection = "scm:git:git://github.com/floatingpointmc/KantUnderstand-API.git"
                    developerConnection = "scm:git:ssh://github.com/floatingpointmc/KantUnderstand-API.git"
                }
            }
        }
    }

    repositories {
        maven {
            name = "central"

            url = uri(
                "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
            )

            credentials {
                username = findProperty("ossrhUsername") as? String
                password = findProperty("ossrhPassword") as? String
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

val publishToCentralPortal = tasks.register("publishToCentralPortal") {
    group = "publishing"
    description = "Transfers the OSSRH staging repository to the Central Publisher Portal."

    doLast {
        val username = findProperty("ossrhUsername") as? String
            ?: throw GradleException(
                "Missing ossrhUsername in gradle.properties"
            )

        val password = findProperty("ossrhPassword") as? String
            ?: throw GradleException(
                "Missing ossrhPassword in gradle.properties"
            )

        val namespace = project.group.toString()

        val token = Base64.getEncoder().encodeToString(
            "$username:$password".toByteArray(Charsets.UTF_8)
        )

        val url = URI(
            "https://ossrh-staging-api.central.sonatype.com" +
                    "/manual/upload/defaultRepository/" +
                    "${namespace}?publishing_type=automatic"
        ).toURL()

        println()
        println("Submitting deployment to Maven Central...")
        println("Namespace: $namespace")

        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )
            connection.setRequestProperty(
                "Accept",
                "application/json"
            )
            connection.doOutput = true

            connection.outputStream.use { }

            val responseCode = connection.responseCode

            val response = try {
                connection.inputStream.bufferedReader().use { it.readText() }
            } catch (_: Exception) {
                connection.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: ""
            }

            if (responseCode !in 200..299) {
                throw GradleException(
                    """
                    Failed to submit deployment to Central.

                    HTTP $responseCode
                    $response
                    """.trimIndent()
                )
            }

            println("Central deployment submitted successfully.")
            if (response.isNotBlank()) {
                println(response)
            }
        } finally {
            connection.disconnect()
        }
    }
}

tasks.named("publish") {
    finalizedBy(publishToCentralPortal)
}
