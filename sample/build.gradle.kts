plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()
    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework { baseName = "SampleApp"; isStatic = true }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":glasskit"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
        androidMain.dependencies { implementation(libs.androidx.activity.compose) }
    }
}

android {
    namespace = "sample"
    compileSdk = 36
    defaultConfig { applicationId = "io.github.majid460.glasskit.sample"; minSdk = 24; targetSdk = 35; versionCode = 1; versionName = "0.1" }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}

// Poster images live in src/commonMain/composeResources/drawable and are read as Res.drawable.poster_N
compose.resources {
    packageOfResClass = "sample.resources"
}
