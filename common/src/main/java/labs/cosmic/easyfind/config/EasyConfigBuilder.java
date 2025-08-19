package labs.cosmic.easyfind.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EasyConfigBuilder {
    public static Screen init(Screen parent) {
        EasyConfig.HANDLER.load();
        return YetAnotherConfigLib.createBuilder()
            .title(Component.translatable("efs.settei"))

            // Cosmetics Category
            .category(ConfigCategory.createBuilder()
                .name(Component.translatable("efs.cosmetics"))

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.darkenBG"))
                    .description(OptionDescription.of(Component.translatable("easyfind.darkenBG.desc")))
                    .binding(true, () -> EasyConfig.darkenBG, newVal -> EasyConfig.darkenBG = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.showDescription"))
                    .description(OptionDescription.of(Component.translatable("easyfind.showDescription.desc")))
                    .binding(true, () -> EasyConfig.showDescription, newVal -> EasyConfig.showDescription = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.coloredRarity"))
                    .description(OptionDescription.of(Component.translatable("easyfind.coloredRarity.desc")))
                    .binding(true, () -> EasyConfig.coloredRarity, newVal -> EasyConfig.coloredRarity = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .build())

            // Behaviour Category
            .category(ConfigCategory.createBuilder()
                .name(Component.translatable("efs.behaviour"))

                .option(Option.<EasyConfig.ReplaceNeighbor>createBuilder()
                    .name(Component.translatable("easyfind.replaceNeighbor"))
                    .description(OptionDescription.of(Component.translatable("easyfind.replaceNeighbor.desc")))
                    .binding(EasyConfig.ReplaceNeighbor.CURRENT, () -> EasyConfig.replaceNeighbor, newVal -> EasyConfig.replaceNeighbor = newVal)
                    .controller(opt -> EnumControllerBuilder.create(opt).enumClass(EasyConfig.ReplaceNeighbor.class))
                    .build())

                .option(Option.<EasyConfig.KeepScreen>createBuilder()
                    .name(Component.translatable("easyfind.keepScreenOn"))
                    .description(OptionDescription.of(Component.translatable("easyfind.keepScreenOn.desc")))
                    .binding(EasyConfig.KeepScreen.SHIFT, () -> EasyConfig.keepScreenOn, newVal -> EasyConfig.keepScreenOn = newVal)
                    .controller(opt -> EnumControllerBuilder.create(opt).enumClass(EasyConfig.KeepScreen.class))
                    .build())

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.forcedReplace"))
                    .description(OptionDescription.of(Component.translatable("easyfind.forcedReplace.desc")))
                    .binding(false, () -> EasyConfig.forcedReplace, newVal -> EasyConfig.forcedReplace = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.ignoreExisting"))
                    .description(OptionDescription.of(Component.translatable("easyfind.ignoreExisting.desc")))
                    .binding(false, () -> EasyConfig.ignoreExisting, newVal -> EasyConfig.ignoreExisting = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.saveHistory"))
                    .description(OptionDescription.of(Component.translatable("easyfind.saveHistory.desc")))
                    .binding(true, () -> EasyConfig.saveHistory, newVal -> EasyConfig.saveHistory = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("easyfind.showDisabledItem"))
                    .description(OptionDescription.of(Component.translatable("easyfind.showDisabledItem.desc")))
                    .binding(false, () -> EasyConfig.showDisabledItem, newVal -> EasyConfig.showDisabledItem = newVal)
                    .controller(BooleanControllerBuilder::create)
                    .build())

                .build())

            .save(() -> EasyConfig.HANDLER.save())
            .build()
            .generateScreen(parent);
    }
}
