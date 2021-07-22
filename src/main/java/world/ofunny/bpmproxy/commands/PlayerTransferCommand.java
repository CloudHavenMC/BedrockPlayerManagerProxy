package world.ofunny.bpmproxy.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PlayerTransferCommand extends Command {

    private final ProxyServer proxyServer;
    private final String serverName;
    private final BaseComponent[] alreadyConnected;

    public PlayerTransferCommand(String serverName, String commandName, String serverPermission) {

        // Call constructor of the Command class
        super(commandName, serverPermission);

        // Local dependencies
        this.proxyServer = ProxyServer.getInstance();
        this.serverName = serverName;
        this.alreadyConnected = new ComponentBuilder("You're already connected to the desired server: \""+serverName+"\".").color(ChatColor.DARK_GREEN).create();

    }// end ServerAliasCommand

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        // Only players can actually call this command (the console does not need that command at all).
        if (!(commandSender instanceof ProxiedPlayer)) return;

        // Cast the commandSender to a ProxiedPlayer object (can not be anything else at this point).
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;

        // Check first if the player is already connected to the given Minecraft server!
        if (proxiedPlayer.getServer().getInfo().getName().equalsIgnoreCase(serverName)) {
            proxiedPlayer.sendMessage(alreadyConnected);
            return;
        }// end if already connected

        // Transfer the player to the given Minecraft server.
        proxiedPlayer.connect(proxyServer.getServerInfo(serverName));

    }// end execute

}// end playerTransferCommand
