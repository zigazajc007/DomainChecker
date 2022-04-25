package com.rabbitcompany.domainchecker.listeners;

import com.rabbitcompany.domainchecker.DomainChecker;
import com.rabbitcompany.domainchecker.utils.Domains;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PlayerJoinListener implements Listener {

    HashMap<String, String> redirect = new HashMap<>();

    @EventHandler
    public void onPlayerJoinEvent(LoginEvent e) {
        String subdomain = e.getConnection().getVirtualHost().getHostName().split("\\.")[0].toLowerCase();
        redirect.put(e.getConnection().getName(), subdomain);
        if (DomainChecker.config.getList("subdomain").contains(subdomain) && !Domains.domains.get(subdomain).contains(e.getConnection().getName())) {
            if(Domains.domains.get(subdomain).getKeys().contains(e.getConnection().getName())) return;
            String message = DomainChecker.config.getString("first_join", "[DomainChecker] {player} joined from {subdomain}.tulipsurvival.com for the first time!");
            DomainChecker.getInstance().getProxy().getConsole().sendMessage(new ComponentBuilder(message.replace("{player}", e.getConnection().getName()).replace("{subdomain}", subdomain)).color(ChatColor.GREEN).create());
            Domains.domains.get(subdomain).set(e.getConnection().getName(), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(System.currentTimeMillis())));
            Domains.saveDomainFile(subdomain);
            Domains.domains_temp.put(subdomain, Domains.domains_temp.getOrDefault(subdomain, 0) + 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerConnectedEvent(ServerConnectEvent e) {
        if(!e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) return;
        String subdomain = redirect.getOrDefault(e.getPlayer().getName(), "");
        String serverName = DomainChecker.subdomains.getString(subdomain, null);
        if(serverName != null)
            e.setTarget(ProxyServer.getInstance().getServerInfo(serverName));
        redirect.remove(e.getPlayer().getName());
    }

}
