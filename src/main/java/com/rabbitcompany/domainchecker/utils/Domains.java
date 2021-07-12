package com.rabbitcompany.domainchecker.utils;

import com.rabbitcompany.domainchecker.DomainChecker;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

public class Domains {
    public static HashMap<String, Configuration> domains = new HashMap<>();
    public static HashMap<String, Integer> domains_temp = new HashMap<>();

    public static void createDomainFile(String domain) {
        try {
            File domain_file = new File(DomainChecker.getInstance().getDataFolder(), domain);
            if (!domain_file.exists())
                try {
                    InputStream in = DomainChecker.getInstance().getResourceAsStream("traffic.yml");
                    try {
                        Files.copy(in, domain_file.toPath());
                        in.close();
                    } catch (Throwable throwable) {
                        if (in != null)
                            try {
                                in.close();
                            } catch (Throwable throwable1) {
                                throwable.addSuppressed(throwable1);
                            }
                        throw throwable;
                    }
                } catch (IOException ignored) {}
            Configuration domain_yml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(domain_file);
            domains.put(domain.replace(".yml", ""), domain_yml);
        } catch (IOException ignored) {}
    }

    public static void saveDomainFile(String domain) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(domains.get(domain), new File(DomainChecker.getInstance().getDataFolder(), domain + ".yml"));
        } catch (IOException ignored) {}
    }
}
