plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android)
            implementation(libs.androidx.startup.runtime)
            implementation("com.google.android.gms:play-services-maps:18.2.0")
            implementation("com.google.android.gms:play-services-location:21.1.0")
            implementation("com.google.maps.android:maps-compose:2.11.2")
        }
        commonMain.dependencies {
            api(compose.material3)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logger)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.web.socket)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.decompose)
            implementation(libs.decomposeExtensions)
            implementation(libs.decompose.lifecycle.corutines)
            implementation(libs.kotlinxSerializationJson)
            implementation(libs.bundles.coil)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.kotlinx.datetime)
            // peekaboo-ui
            implementation("io.github.onseok:peekaboo-ui:0.5.1")

            // peekaboo-image-picker
            implementation("io.github.onseok:peekaboo-image-picker:0.5.1")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
        }
    }
}

android {
    namespace = "com.grabit"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.grabit"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
        implementation(libs.kotlinx.coroutines.android)
        commonMainApi("dev.icerock.moko:geo:0.6.0")

        // Compose Multiplatform
        commonMainApi("dev.icerock.moko:geo-compose:0.6.0")
    }
}

sqldelight {
    databases {
        create("GrabItDatabase") {
            packageName.set("com.grabit")
        }
        linkSqlite.set(true)
    }
}

secrets {
    defaultPropertiesFileName = "default.local.properties"
    propertiesFileName = "local.properties"
}
