android {
    namespace = "io.github.joaogouveia89.randomuser"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.joaogouveia89.randomuser"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

// Configure JVM arguments specifically for the test tasks.
// - `-Dnet.bytebuddy.experimental=true`: Enables experimental features in ByteBuddy,
//   allowing dynamic interception and manipulation of bytecode during runtime.
//   This is essential for certain mocking or bytecode manipulation libraries.
// - `-XX:+EnableDynamicAgentLoading`: Explicitly allows dynamic agent loading,
//   which is required for tools like ByteBuddy to attach agents during runtime.
//   Suppresses warnings about the future default behavior of disallowing this feature.
// Considerations:
// 1. Ensure that these arguments are necessary for this project, as enabling experimental
//    or dynamic features can introduce subtle issues or dependencies on specific JVM versions.
// 2. Future versions of Java may restrict dynamic agent loading by default. Monitor the release
//    notes of both the JDK and ByteBuddy to adapt accordingly.
// 3. For production environments, review whether these arguments should be disabled to avoid
//    unnecessary overhead or unintended behaviors.
tasks.withType<Test> {
    jvmArgs = listOf(
        "-Dnet.bytebuddy.experimental=true",
        "-XX:+EnableDynamicAgentLoading"
    )
}

dependencies {

    testImplementation(libs.byte.buddy)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)


    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.kotlinx.datetime)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)

    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)


    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}