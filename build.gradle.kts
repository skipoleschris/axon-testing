import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.1.0"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.8.0"
  kotlin("plugin.spring") version "1.8.0"
  kotlin("plugin.jpa") version "1.8.0"
  application
}

group = "com.crownagentsbank.payments"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  implementation(platform("org.axonframework:axon-bom:4.7.4"))
  implementation("org.axonframework:axon-server-connector")
  implementation("org.axonframework:axon-spring-boot-starter")
  implementation("org.axonframework.extensions.reactor:axon-reactor")
  implementation("org.axonframework.extensions.kotlin:axon-kotlin")

  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  runtimeOnly("com.h2database:h2")
  runtimeOnly("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("io.projectreactor:reactor-core")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation(kotlin("test"))
  testImplementation("io.mockk:mockk:1.13.5")
  testImplementation("io.kotest:kotest-assertions-core:5.6.2")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(17) }

application { mainClass.set("com.crownagentsbank.payments.axontesting.MainKt") }

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}
