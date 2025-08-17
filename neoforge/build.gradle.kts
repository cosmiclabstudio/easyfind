plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    kotlin("jvm") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.14"
}

stonecutter {

}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

neoForge {
    enable {
        version = commonMod.dep("neoforge")
    }
}

dependencies {
    implementation("com.mojang:minecraft:${commonMod.mc}")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
}

neoForge {
    accessTransformers.from(project.file("../../src/main/resources/META-INF/accesstransformer.cfg").absolutePath)

    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${project.path})"
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${project.path})"
        }
    }

    parchment {
        commonMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = commonMod.mc
        }
    }

    mods {
        register(commonMod.id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("${mod.id}.accesswidener")
    }
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(":neoforge:${commonMod.propOrNull("minecraft_version")}:stonecutterGenerate")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(findProperty("java.version")?.toString() ?: "17"))
    }
}
