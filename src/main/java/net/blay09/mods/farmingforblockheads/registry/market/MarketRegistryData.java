package net.blay09.mods.farmingforblockheads.registry.market;

import java.util.List;
import java.util.Map;

public class MarketRegistryData {
    private Map<String, MarketOverrideData> groupOverrides;
    private Map<String, MarketOverrideData> entryOverrides;
    private List<MarketEntryData> customEntries;

    public Map<String, MarketOverrideData> getGroupOverrides() {
        return groupOverrides;
    }

    public void setGroupOverrides(Map<String, MarketOverrideData> groupOverrides) {
        this.groupOverrides = groupOverrides;
    }

    public Map<String, MarketOverrideData> getEntryOverrides() {
        return entryOverrides;
    }

    public void setEntryOverrides(Map<String, MarketOverrideData> entryOverrides) {
        this.entryOverrides = entryOverrides;
    }

    public List<MarketEntryData> getCustomEntries() {
        return customEntries;
    }

    public void setCustomEntries(List<MarketEntryData> customEntries) {
        this.customEntries = customEntries;
    }
}
