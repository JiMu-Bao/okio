import aQute.bnd.gradle.BundleTaskConvention
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  dependencies {
    classpath(deps.android.gradlePlugin)
    classpath(deps.kotlin.gradlePlugin)
    classpath(deps.animalSniffer.gradlePlugin)
    classpath(deps.japicmp)
    classpath(deps.dokka)
    classpath(deps.shadow)
    classpath(deps.jmh.gradlePlugin)
    classpath(deps.spotless)
    classpath(deps.bnd)
    // https://github.com/melix/japicmp-gradle-plugin/issues/36
    classpath("com.google.guava:guava:28.2-jre")
  }

  repositories {
    mavenCentral()
    gradlePluginPortal()
    jcenter()
    google()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://kotlin.bintray.com/kotlinx/")
  }
}

// When scripts are applied the buildscript classes are not accessible directly therefore we save
// the class here to make it accessible.
ext.set("bndBundleTaskConventionClass", BundleTaskConvention::class.java)

allprojects {
  group = project.property("GROUP") as String
  version = project.property("VERSION_NAME") as String
}

subprojects {
  repositories {
    mavenCentral()
    jcenter()
    google()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    maven(url = "https://kotlin.bintray.com/kotlinx/")
  }

  apply(plugin = "com.diffplug.spotless")

  configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt")
      ktlint(versions.ktlint).userData(mapOf("indent_size" to  "2"))
      trimTrailingWhitespace()
      endWithNewline()
    }
  }

  tasks.withType<KotlinCompile>().all {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs += "-Xjvm-default=all"
    }
  }

  tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
  }
}
