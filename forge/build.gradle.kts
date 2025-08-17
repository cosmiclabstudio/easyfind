import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    `multiloader-loader`
    java
    kotlin("jvm") version "2.2.0"
    id("net.minecraftforge.gradle") version "6.0.43"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}
apply(from = "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-3.5.0.gradle")

repositories {
    maven("https://libraries.minecraft.net")
    mavenCentral()
}

dependencies {
    "minecraft"("net.minecraftforge:forge:${commonMod.dep("forge")}")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    commonMod.depOrNull("parchment")?.let { parchmentVersion ->
        implementation("org.parchmentmc.data:parchment-${commonMod.mc}:$parchmentVersion@zip")
    }
}

val Project.minecraft: net.minecraftforge.gradle.common.util.MinecraftExtension
    get() = extensions.getByType()

minecraft.let {
    it.mappings("official", commonMod.mc)
    it.runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                this.create("easyfind") {
                    source(sourceSets.main.get())
                }
            }
        }
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                this.create("easyfind") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}
