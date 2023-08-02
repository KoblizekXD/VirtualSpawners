/*
 * Decompiled with CFR 0.2.0 (FabricMC d28b102d).
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  org.bukkit.NamespacedKey
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.bunnie.virtualspawners.commands.vs;

import me.bunnie.virtualspawners.VirtualSpawners;
import me.bunnie.virtualspawners.commands.SubCommand;
import me.bunnie.virtualspawners.utils.ChatUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class UpgradeCommand
extends SubCommand {
    private final VirtualSpawners plugin;

    public UpgradeCommand(VirtualSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    public String getDescription() {
        return "Upgrades the spawner in hand!";
    }

    @Override
    public String getSyntax() {
        return "/vs upgrade <upgrade> <level>";
    }

    /*
     * Exception decompiling
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:538)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at cuchaz.enigma.source.cfr.CfrSource.ensureDecompiled(CfrSource.java:83)
         *     at cuchaz.enigma.source.cfr.CfrSource.asString(CfrSource.java:51)
         *     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:312)
         *     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:287)
         *     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
         *     at java.base/java.util.AbstractList$RandomAccessSpliterator.forEachRemaining(AbstractList.java:722)
         *     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
         *     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
         *     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:754)
         *     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:387)
         *     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1311)
         *     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1840)
         *     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1806)
         *     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private boolean tierIsValid(EntityType entityType, int tier) {
        for (String s : this.plugin.getTiersYML().getConfigurationSection(entityType.name() + ".tier").getKeys(false)) {
            int i = Integer.parseInt((String)s);
            if (i != tier) continue;
            return true;
        }
        return false;
    }

    private boolean lootingIsValid(int looting) {
        int min = this.plugin.getConfigYML().getInt("settings.min-looting");
        int max = this.plugin.getConfigYML().getInt("settings.max-looting");
        return looting >= min && looting <= max;
    }

    private boolean efficiencyIsValid(int efficiency) {
        int min = this.plugin.getConfigYML().getInt("settings.min-efficiency");
        int max = this.plugin.getConfigYML().getInt("settings.max-efficiency");
        return efficiency >= min && efficiency <= max;
    }

    private void updateItem(Player player, ItemStack itemStack, String key, int level) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.remove(new NamespacedKey((Plugin)this.plugin, key));
        pdc.set(new NamespacedKey((Plugin)this.plugin, key), PersistentDataType.INTEGER, level);
        itemStack.setItemMeta(meta);
        player.sendMessage(ChatUtils.format(this.plugin.getConfigYML().getString("messages.on-upgrade").replace((CharSequence)"%prefix%", (CharSequence)this.plugin.getPrefix()).replace((CharSequence)"%upgrade%", (CharSequence)key).replace((CharSequence)"%level%", (CharSequence)String.valueOf((int)level))));
    }
}