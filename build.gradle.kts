plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.cubxity.server"
version = "0.0.1"

repositories {
    mavenCentral()
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.Minestom:Minestom:6ef04ae618")
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to "dev.cubxity.server.MicroQueue")
        }
    }
    shadowJar {
        archiveClassifier.set("")
    }
}
