import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
}

kotlin {

  jvm("desktop")

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.shared)
      }
    }

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "me.sujanpoudel.kmp.config.sample"
      packageVersion = "1.0.0"
    }
  }
}
