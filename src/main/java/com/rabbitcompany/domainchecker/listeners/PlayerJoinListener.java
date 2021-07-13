package com.rabbitcompany.domainchecker.listeners;

import com.rabbitcompany.domainchecker.DomainChecker;
import com.rabbitcompany.domainchecker.utils.Domains;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoiEvent(LoginEvent e) {
        String subdomain = e.getConnection().getVirtualHost().getHostName().split("\\.")[0].toLowerCase();

        if (DomainChecker.config.getList("subdomain").contains(subdomain) && !Domains.domains.get(subdomain).contains(e.getConnection().getName())) {
            if(Domains.domains.get(subdomain).getKeys().contains(e.getConnection().getName())) return;
            String message = DomainChecker.config.getString("first_join", "[DomainChecker] {player} joined from {subdomain}.tulipsurvival.com for the first time!");
            DomainChecker.getInstance().getProxy().getConsole().sendMessage(new ComponentBuilder(message.replace("{player}", e.getConnection().getName()).replace("{subdomain}", subdomain)).color(ChatColor.GREEN).create());
            Domains.domains.get(subdomain).set(e.getConnection().getName(), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(System.currentTimeMillis())));
            Domains.saveDomainFile(subdomain);
            Domains.domains_temp.put(subdomain, Domains.domains_temp.getOrDefault(subdomain, 0) + 1);
        }

        if(subdomain.startsWith("tulip")){
            if(e.getConnection() instanceof ProxiedPlayer){
                ProxiedPlayer p = (ProxiedPlayer) e.getConnection();
                p.connect(ProxyServer.getInstance().getServerInfo("tulip"));
            }
        }
    }

}
