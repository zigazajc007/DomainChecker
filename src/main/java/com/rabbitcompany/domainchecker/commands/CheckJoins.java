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

        for(String domain : Domains.domains.keySet()){
            String message = DomainChecker.config.getString("check_joins", "{subdomain}.tulipsurvival.com: {unique_joins}");
            sender.sendMessage(new ComponentBuilder(message.replace("{subdomain}", domain).replace("{unique_joins}", ""+Domains.domains_temp.getOrDefault(domain, 0))).color(ChatColor.GREEN).create());
        }
    }
}
