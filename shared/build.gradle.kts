import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigTask
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import config.sample.VariantDimension.dev
import config.sample.VariantDimension.prod
import config.sample.VariantDimension.staging
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import java.util.Properties
import java.util.regex.Pattern

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.buildkonfig)
}

kotlin {

  androidTarget()

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs()

  js {
    browser()
  }

  jvm("desktop")

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "SampleShared"
      isStatic = true
    }
  }

  sourceSets {
    val desktopMain by getting

    androidMain.dependencies {
      implementation(compose.preview)
      implementation(libs.androidx.activity.compose)
    }

    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material)
      implementation(compose.ui)
      implementation(compose.components.uiToolingPreview)
    }

    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
    }
  }
}

android {
  namespace = "me.sujanpoudel.kmp.config.sample"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {
    compose = true
  }
}

project.extra.set("buildkonfig.flavor", currentBuildVariant())

tasks.withType<BuildKonfigTask> {
  this.exposeObject = false
  this.hasJsTarget = false
}

buildkonfig {
  packageName = "me.sujanpoudel.kmp.config.sample"
  objectName = "SampleConfig"

  defaultConfigs {
    field("variant", dev)
    configsFromProperties("dev.sample.properties")
  }

  defaultConfigs(dev) {
    field("variant", dev)
    configsFromProperties("dev.sample.properties")
  }

  defaultConfigs(staging) {
    field("variant", staging)
    configsFromProperties("staging.sample.properties")
  }

  defaultConfigs(prod) {
    field("variant", prod)
    configsFromProperties("prod.sample.properties")
  }
}


fun Project.getAndroidBuildVariantOrNull(): String? {
  val variants = setOf(dev, prod, staging)
  val taskRequestsStr = gradle.startParameter.taskRequests.toString()
  val pattern: Pattern = if (taskRequestsStr.contains("assemble")) {
    Pattern.compile("assemble(\\w+)(Release|Debug)")
  } else {
    Pattern.compile("bundle(\\w+)(Release|Debug)")
  }

  val matcher = pattern.matcher(taskRequestsStr)
  val variant = if (matcher.find()) matcher.group(1).lowercase() else null
  return if (variant in variants) {
    variant
  } else {
    null
  }
}

private fun Project.currentBuildVariant(): String {
  val variants = setOf(dev, prod, staging)
  return getAndroidBuildVariantOrNull()
    ?: System.getenv()["VARIANT"]
      .toString()
      .takeIf { it in variants } ?: dev
}


private fun TargetConfigDsl.configsFromProperties(file: String) {
  val properties = Properties().apply {
    val propertiesFile = rootProject.layout.projectDirectory.file("config/$file").asFile
    load(propertiesFile.inputStream())
  }

  properties.stringPropertyNames()
    .forEach { key ->
      field(key.asConfigKey(), properties.getProperty(key))
    }
}

private fun String.asConfigKey() = this.split(".", "-")
  .mapIndexed { index: Int, s: String -> if (index == 0) s else s.uppercaseFirstChar() }
  .joinToString("")

private fun <T> TargetConfigDsl.field(key: String, value: T) {
  val spec = when (value) {
    is String -> FieldSpec.Type.STRING
    is Int -> FieldSpec.Type.INT
    is Float -> FieldSpec.Type.FLOAT
    is Long -> FieldSpec.Type.LONG
    is Boolean -> FieldSpec.Type.BOOLEAN
    else -> error("Unsupported build config value '$value' for '$key'")
  }

  buildConfigField(spec, key, value.toString().trim().removeSurrounding("\""))
}
