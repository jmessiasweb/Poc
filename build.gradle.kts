plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    id("org.jetbrains.kotlin.kapt") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.micronaut.application") version "1.5.4"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.32"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.0-M1"
}

version = "0.1"
group = "br.com.poc"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("br.com.poc.*")
    }
}

dependencies {
    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-management")
    //CSV
    implementation("org.apache.commons:commons-csv:1.9.0")
    //Cache
    implementation("io.micronaut.redis:micronaut-redis-lettuce:2.2.0")


    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    // JPA/Hibernate
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")

    // Logback
    runtimeOnly("ch.qos.logback:logback-classic")

    // Jackson
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Postgresql Driver
    runtimeOnly("org.postgresql:postgresql")


}


application {
    mainClass.set("br.com.poc.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("1.8")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
