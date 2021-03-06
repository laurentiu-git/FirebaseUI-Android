plugins {
  id("com.android.library")
  id("com.vanniktech.maven.publish")
}

android {
    compileSdkVersion(Config.SdkVersions.compile)

    defaultConfig {
        minSdkVersion(Config.SdkVersions.min)
        targetSdkVersion(Config.SdkVersions.target)

        versionName = Config.version
        versionCode = 1

        resourcePrefix("fui_")
        vectorDrawables.useSupportLibrary = true
    }

    lintOptions {
        // Common lint options across all modules
        disable(
            "IconExpectedSize",
            "InvalidPackage", // Firestore uses GRPC which makes lint mad
            "NewerVersionAvailable", "GradleDependency", // For reproducible builds
            "SelectableText", "SyntheticAccessor" // We almost never care about this
        )

        isCheckAllWarnings = true
        isWarningsAsErrors = true
        isAbortOnError = false // Override

        baselineFile = file("$rootDir/library/quality/lint-baseline.xml")
    }
}

dependencies {
    api(project(":auth"))
    api(project(":database"))
    api(project(":firestore"))
    api(project(":storage"))
}

tasks.register("prepareArtifacts") {
    dependsOn("assembleRelease")
    dependsOn(*Config.submodules.map {
        ":$it:assembleRelease"
    }.toTypedArray())
}
