package net.blay09.mods.farmingforblockheads;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.farmingforblockheads.registry.AbstractRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandFarmingForBlockheads {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("farmingforblockheads").requires(it -> it.hasPermissionLevel(2)).then(Commands.literal("reload").executes(it -> {
            AbstractRegistry.registryErrors.clear();
            MarketRegistry.INSTANCE.load(FarmingForBlockheads.configDir);
            it.getSource().sendFeedback(new TextComponentTranslation("commands.farmingforblockheads:reload.success"), true);
            if (AbstractRegistry.registryErrors.size() > 0) {
                it.getSource().sendFeedback(new TextComponentString(TextFormatting.RED + "There were errors loading the Farming for Blockheads registries:"), true);
                TextFormatting lastFormatting = TextFormatting.WHITE;
                for (String error : AbstractRegistry.registryErrors) {
                    it.getSource().sendFeedback(new TextComponentString(lastFormatting + "* " + error), true);
                    lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
                }
            }
            return Command.SINGLE_SUCCESS;
        })));
    }

}
