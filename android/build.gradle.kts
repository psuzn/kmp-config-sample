import config.sample.VariantDimension
import config.sample.VariantDimension.dev
import config.sample.VariantDimension.prod
import config.sample.VariantDimension.staging

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.jetbrains.kotlin.android)
}

android {
  namespace = "me.sujanpoudel.kmp.config.sample"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "me.sujanpoudel.kmp.config.sample"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {
    compose = true
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  flavorDimensions.add(VariantDimension.name)

  productFlavors {
    create(dev) {
      dimension = VariantDimension.name
      isDefault = true
      applicationIdSuffix = ".dev"
      resValue("string", "app_name", "Config Sample Dev")
    }

    create(staging) {
      dimension = VariantDimension.name
      applicationIdSuffix = ".staging"
      resValue("string", "app_name", "Config Sample Staging")
    }

    create(prod) {
      dimension = VariantDimension.name
    }
  }
}

dependencies {
  implementation(projects.shared)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
}
