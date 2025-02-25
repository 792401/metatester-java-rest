/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.9.1/userguide/building_java_projects.html
 */
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.*
import java.io.ByteArrayOutputStream


val gitCommitHash: Provider<String> = project.provider {
    try {
        val process = ProcessBuilder("git", "rev-parse", "--short=7", "HEAD").start()
        val outputStream = ByteArrayOutputStream()
        process.inputStream.copyTo(outputStream)
        process.waitFor()
        outputStream.toString().trim()
    } catch (e: Exception) {
        println("Error getting git commit hash: ${e.message}")
        "unknown"
    }
}

group "io.metatester"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.metatester"
            artifactId = "metatester"
            version = if (gitCommitHash.get() != "unknown") "1.0.0-dev-${gitCommitHash.get()}" else "1.0.0-SNAPSHOT"
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/792401/metatester")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.aspectj.post-compile-weaving") version "8.6"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(project(":lib"))
    implementation("org.aspectj:aspectjrt:1.9.22")
    api("org.aspectj:aspectjweaver:1.9.22")
    implementation("io.rest-assured:rest-assured:5.3.0")
    implementation("io.rest-assured:json-path:5.3.0")
    testImplementation("org.projectlombok:lombok:1.18.26")
    compileOnly("org.aspectj:aspectjtools:1.9.22")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.11.4")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    compileOnly("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.junit.platform:junit-platform-launcher:1.9.3")
    api("org.apache.commons:commons-math3:3.6.1")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.json:json:20250107")
    implementation("com.github.tomakehurst:wiremock:3.0.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("io.swagger.parser.v3:swagger-parser:2.1.22")
}


//tasks.test {
//    useJUnitPlatform()
//    jvmArgs(
//            "-javaagent:${configurations.compileClasspath.get().find { it.name.contains("aspectjweaver") }?.absolutePath}",
////            "-Daj.weaving.verbose=true",
////            "-Dorg.aspectj.weaver.showWeaveInfo=true",
////            "-Dorg.aspectj.matcher.verbosity=5",
//            "-Xmx2g", // Increase maximum heap size to 2GB
//            "-Xms512m" // Set initial heap size to 512MB
//    )
//}
tasks.test {
    useJUnitPlatform()
    doFirst {
        jvmArgs(
            "-javaagent:${configurations.runtimeClasspath.get().find { it.name.contains("aspectjweaver") }?.absolutePath}",
//            "-Daj.weaving.verbose=true",
//            "-Dorg.aspectj.weaver.showWeaveInfo=true",
//            "-Dorg.aspectj.matcher.verbosity=5",
            "-Xmx2g",
            "-Xms512m"
        )
    }
}