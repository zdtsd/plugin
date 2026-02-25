package io.github.z.plugin.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.z.plugin.Plugin;

public class ProtocolLibManager {
    //TODO: USE MAVEN DEPENDENCY INSTEAD OF CLASSPATH DEPENDENCY FOR PROTOCOLLIB
    //WARNING: DEPENDENCY CURRENTLY BREAKS WHEN MAVEN IS REFRESHED
    public ProtocolLibManager(){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new DropKeyPacketListener(Plugin.getPlugin()));
    }
}
