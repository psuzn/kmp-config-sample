package config.sample

import org.gradle.api.Project
import java.util.regex.Pattern

@Suppress("ConstPropertyName")
object VariantDimension {
  const val name = "variant"

  const val dev = "dev"
  const val prod = "prod"
  const val staging = "staging"
}

fun Project.getAndroidBuildVariantOrNull(): String? {
  val taskRequestsStr = gradle.startParameter.taskRequests.toString()
  val pattern: Pattern = if (taskRequestsStr.contains("assemble")) {
    Pattern.compile("assemble(\\w+)(Release|Debug)")
  } else {
    Pattern.compile("bundle(\\w+)(Release|Debug)")
  }

  val matcher = pattern.matcher(taskRequestsStr)
  return if (matcher.find()) {
    matcher.group(1).lowercase()
  } else {
    null
  }
}
