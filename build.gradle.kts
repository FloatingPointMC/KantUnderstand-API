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
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

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
            name = "ossrh"
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl

            credentials {
                username = "%SONATYPE_USERNAME%"
                password = "%SONATYPE_PASSWORD%"
            }
        }
    }
}

signing {
    val signingKeyId = findProperty("signing.keyId") as? String ?: "%SIGNING_KEY_ID%"
    val signingKey = findProperty("signing.key") as? String ?: "%SIGNING_KEY%"
    val signingPassword = findProperty("signing.password") as? String ?: "%SIGNING_PASSWORD%"
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}