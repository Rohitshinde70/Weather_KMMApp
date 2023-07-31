plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id ("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {

        val ktorVersion = "1.6.4"

        val commonMain by getting{
            dependencies{


// Ktor
                implementation ("io.ktor:ktor-client-json:$ktorVersion")
                implementation ("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation ("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                implementation ("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

               // implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting{
            dependencies{
                implementation ("androidx.core:core-ktx:1.10.1")
                implementation ("androidx.appcompat:appcompat:1.6.1")
                implementation ("com.google.android.material:material:1.8.0")

                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                implementation ("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")

                //    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
                implementation("io.ktor:ktor-client-android:$ktorVersion")

                // implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
                //  implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.example.apisample_kmmapp"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")
    implementation("androidx.contentpager:contentpager:1.0.0")
    implementation("com.google.firebase:firebase-database-ktx:20.0.4")
    testImplementation("junit:junit:4.12")
    testImplementation("junit:junit:4.12")

}