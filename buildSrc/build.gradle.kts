import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  google()
  mavenCentral()
}

tasks.withType<KotlinCompile> {
  compilerOptions {
    freeCompilerArgs.add("-Xcontext-receivers")
  }
}
