import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
}

group = "me.tomasan7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.exposed.core)
    runtimeOnly(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    runtimeOnly(libs.h2)
    implementation(libs.hoplite.core)
    implementation(libs.hoplite.hocon)
    implementation(libs.kotlin.logging)

    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(libs.compose.previewer)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenModel)

    implementation(libs.diglolCrypto.kdf)
    implementation(libs.diglolCrypto.hash)
}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "me.tomasan7.databaseprogram.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "database-program"
            packageVersion = "1.0.0"
        }
    }
}
