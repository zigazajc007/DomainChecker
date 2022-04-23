package com.rabbitcompany.domainchecker;

import com.rabbitcompany.domainchecker.commands.CheckJoins;
import com.rabbitcompany.domainchecker.listeners.PlayerJoinListener;
import com.rabbitcompany.domainchecker.utils.Domains;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class DomainChecker extends Plugin {

    private static DomainChecker instance;

    public static Configuration config = null;
    public static Configuration subdomains = null;

    @Override
    public void onEnable() {
        instance = this;
        try {
            mkdir();
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            subdomains = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "subdomains.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String domain : config.getStringList("subdomain")) Domains.createDomainFile(domain + ".yml");
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());

        getProxy().getPluginManager().registerCommand(this, new CheckJoins());
    }

    @Override
    public void onDisable() {}

    void mkdir() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        File file2 = new File(getDataFolder(), "subdomains.yml");

        if (!file.exists()){
            try {
                InputStream in = getResourceAsStream("config.yml");
                try {
                    Files.copy(in, file.toPath());
                    in.close();
                } catch (Throwable throwable) {
                    if (in == null) return;
                    try {
                        in.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!file2.exists()){
            try {
                InputStream in = getResourceAsStream("subdomains.yml");
                try {
                    Files.copy(in, file2.toPath());
                    in.close();
                } catch (Throwable throwable) {
                    if (in == null) return;
                    try {
                        in.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static DomainChecker getInstance() {
        return instance;
    }
}
