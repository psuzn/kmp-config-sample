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
}


dependencies {

  implementation(projects.shared)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
}
