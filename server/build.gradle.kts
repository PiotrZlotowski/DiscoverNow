val detektVersion  = "1.0.0-RC14"
val swaggerVersion  = "2.9.2"
val liquibaseVersion  = "3.6.2"
val postgresqlDriverVersion  = "42.2.2"
val assertJVersion  = "3.10.0"
val romeTools  = "1.10.0"
val orikaCore  = "1.5.2"
val jacksonKotlin  = "2.9.+"
val mockitoKotlinVersion  = "1.6.0"
val mockitoVersion  = "2.23.0"
val mockkVersion  = "1.9.1"
val jsoupVersion  = "1.11.3"
val springMockkVersion  = "1.1.2"
val kotlinLoggingVersion = "1.6.26"
val archUnitVersion = "0.11.0"

buildscript {
    val kotlinVersion  = "1.3.30"
    val springBootVersion  = "2.1.4.RELEASE"

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "1.3.30"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
    id("org.sonarqube") version "2.6.2"
    id("com.google.cloud.tools.jib") version "1.8.0"
}
apply {
    plugin("kotlin-spring")
    plugin("kotlin-jpa")
    plugin("eclipse")
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
}

val group  = "com.discover"
val version  = "0.0.1-SNAPSHOT"
val sourceCompatibility  = 1.8

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
//        kotlinOptions.freeCompilerArgs = ["-Xjsr305 =strict"]
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
//        kotlinOptions.freeCompilerArgs = ["-Xjsr305 =strict"]
    }
    test {
        useJUnitPlatform()
        testLogging.events("failed")
//        testLogging.events = "failed"
//        testLogging.exceptionFormat = "full"
    }
}

detekt {
    input = files("src/main/kotlin")
    filters = ".*/resources/.*,.*/build/.*"
    config = files("detekt.yml")
}

jib {
    from {
        image = "openjdk:11"
    }
    to {
        image = "discover-server:${project.version}"
    }
    container {
        ports = listOf("8080")
    }
}

repositories {
    mavenCentral()
    jcenter()
}



dependencies {
    compile("org.springframework.session:spring-session-jdbc")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-starter-security")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-validation")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtime("org.springframework.boot:spring-boot-devtools")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("io.springfox:springfox-swagger2:${swaggerVersion}")
    compile("io.springfox:springfox-swagger-ui:${swaggerVersion}")
    compile("org.liquibase:liquibase-core:${liquibaseVersion}")
    compile("org.postgresql:postgresql:${postgresqlDriverVersion}")
    compile("com.rometools:rome:${romeTools}")
    compile("ma.glasnost.orika:orika-core:${orikaCore}")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonKotlin}")
    compile("org.jsoup:jsoup:${jsoupVersion}")
    runtime("com.h2database:h2")
    compile("io.github.microutils:kotlin-logging:${kotlinLoggingVersion}")
    testCompile("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testCompile("org.assertj:assertj-core:${assertJVersion}")
    testCompile("org.springframework.security:spring-security-test")
    testCompile("com.nhaarman:mockito-kotlin:${mockitoKotlinVersion}")
    testCompile("org.mockito:mockito-junit-jupiter:${mockitoVersion}")
    testCompile("com.tngtech.archunit:archunit-junit5-api:${archUnitVersion}")
    testRuntime("com.tngtech.archunit:archunit-junit5-engine:${archUnitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    compile("com.h2database:h2")
    implementation(project(":web"))
}