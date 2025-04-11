plugins {
    id("java")
    id("application")
}

group = "com.pong"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")
}

application {
    mainClass.set("com.pong.Main")
}

// Configure source sets to define the directories for source code and resources.
sourceSets {
    main {
        java {
            setSrcDirs(listOf(file("src/main/java")))
        }
        resources {
            setSrcDirs(listOf(file("src/main/resources")))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}