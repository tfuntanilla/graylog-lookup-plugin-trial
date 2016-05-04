package com.cisco.clip.lookup;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class LookupComponentMetaData implements PluginMetaData {
    
	@Override
    public String getUniqueId() {
        return "com.cisco.clip.lookup.LookupComponentPlugin";
    }

    @Override
    public String getName() {
        return "LookupComponent";
    }

    @Override
    public String getAuthor() {
        // TODO Insert author name
        return "CLIP Team";
    }

    @Override
    public URI getURL() {
        // TODO Insert correct plugin website
        return URI.create("https://www.graylog.org/");
    }

    @Override
    public Version getVersion() {
        return new Version(0, 0, 0);
    }

    @Override
    public String getDescription() {
        // TODO Insert correct plugin description
        return "Lookup component for CLIP.";
    }

    @Override
    public Version getRequiredVersion() {
        return new Version(0, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
