pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "catalyst"

fun includeProject(name: String) {
    val module = name.substring(1).replace(':', '-')
    val path = name.substring(1).replace(':', '/')

    include(":$module")
    project(":$module").projectDir = File(path)
}

// Core
includeProject(":core:common")
includeProject(":core:paper")
includeProject(":core:velocity")
includeProject(":core:fabric")
