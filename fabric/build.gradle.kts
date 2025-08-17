plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
    `multiloader-loader`
    kotlin("jvm") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.14"
}

stonecutter {

}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${commonMod.mc}:$parchmentVersion@zip")
        }
    })

    implementation("me.xdrop:fuzzywuzzy:1.4.0")

    modImplementation("net.fabricmc:fabric-loader:${commonMod.dep("fabric-loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.dep("fabric-api")}+${commonMod.mc}")

    // Optional dependencies
    modImplementation("com.terraformersmc:modmenu:${commonMod.dep("modmenu")}")
}

loom {
    accessWidenerPath = common.project.file("../../src/main/resources/${mod.id}.accesswidener")
    //accessWidenerPath = project(":common:${stonecutter.current.project}").loom.accessWidenerPath

    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
        }
    }

    mixin {
        defaultRefmapName = "${mod.id}.refmap.json"
    }
}