rootProject.name = "gradle-repository"

include("library", "server", "project")

dependencyResolutionManagement {
//    resolutionStrategy {
//        cacheChangingModulesFor(0, TimeUnit.SECONDS) // SNAPSHOTs immer aktualisieren
//    }
}