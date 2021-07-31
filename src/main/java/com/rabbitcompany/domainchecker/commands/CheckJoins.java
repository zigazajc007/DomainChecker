package com.rabbitcompany.domainchecker.commands;

import com.rabbitcompany.domainchecker.DomainChecker;
import com.rabbitcompany.domainchecker.utils.Domains;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class CheckJoins extends Command {

    public CheckJoins() {
        super("checkjoins");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!sender.hasPermission("domainchecker.checkjoins")) return;

        int sum = 0;

        for(String domain : Domains.domains.keySet()){
            String message = DomainChecker.config.getString("check_joins", "{subdomain}.tulipsurvival.com: {unique_joins}");
            int joins = Domains.domains_temp.getOrDefault(domain, 0);
            sum += joins;
            sender.sendMessage(new ComponentBuilder(message.replace("{subdomain}", domain).replace("{unique_joins}", ""+joins)).color(ChatColor.GREEN).create());
        }
        sender.sendMessage((new ComponentBuilder("Total: " + sum).color(ChatColor.GREEN).create()));
    }
}
