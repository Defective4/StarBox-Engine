package net.defekt.minecraft.starbox.storage;

import java.util.ArrayList;
import java.util.List;

public class Whitelist {
    private final List<String> whitelisted = new ArrayList<>();
    private boolean enabled = false;

    public List<String> getWhitelisted() {
        return new ArrayList<>(whitelisted);
    }

    public boolean isWhitelisted(String entry) {
        return whitelisted.contains(entry.toLowerCase());
    }

    public boolean remove(String entry) {
        return whitelisted.remove(entry.toLowerCase());
    }

    public boolean add(String entry) {
        entry = entry.toLowerCase();
        if (whitelisted.contains(entry)) return false;
        whitelisted.add(entry);
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
