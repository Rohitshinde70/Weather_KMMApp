plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.2").apply(false)
    id("com.android.library").version("7.4.2").apply(false)
    kotlin("android").version("1.8.10").apply(false)
    kotlin("multiplatform").version("1.8.10").apply(false)
}

buildscript {
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.5.21")
        classpath ("com.android.tools.build:gradle:7.1.0")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
