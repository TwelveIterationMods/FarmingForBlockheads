package net.blay09.mods.farmingforblockheads.registry.market;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class MarketRegistryData {
    private Map<String, MarketOverrideData> groupOverrides;
    private Map<String, MarketOverrideData> entryOverrides;
    private List<MarketEntryData> customEntries;

    @Nullable
    public Map<String, MarketOverrideData> getGroupOverrides() {
        return groupOverrides;
    }

    public void setGroupOverrides(Map<String, MarketOverrideData> groupOverrides) {
        this.groupOverrides = groupOverrides;
    }

    @Nullable
    public Map<String, MarketOverrideData> getEntryOverrides() {
        return entryOverrides;
    }

    public void setEntryOverrides(Map<String, MarketOverrideData> entryOverrides) {
        this.entryOverrides = entryOverrides;
    }

    @Nullable
    public List<MarketEntryData> getCustomEntries() {
        return customEntries;
    }

    public void setCustomEntries(List<MarketEntryData> customEntries) {
        this.customEntries = customEntries;
    }
}
