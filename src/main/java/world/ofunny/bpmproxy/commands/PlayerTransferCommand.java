package world.ofunny.bpmproxy.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import world.ofunny.bpmproxy.BedrockPlayerManagerProxy;

public class PlayerTransferCommand implements SimpleCommand {

    private final ProxyServer proxyServer;
    private final String serverName;
    private final String serverPermission;

    public PlayerTransferCommand(String serverName, String serverPermission) {

        // Local dependencies
        this.proxyServer = BedrockPlayerManagerProxy.getInstance().getProxyServer();
        this.serverName = serverName;
        this.serverPermission = serverPermission;

    }// end ServerAliasCommand

    @Override
    public void execute(Invocation invocation) {
        // Only players can actually call this command (the console does not need that command at all).
        if (!(invocation.source() instanceof Player player)) return;

        // Check first if the player is already connected to the given Minecraft server!
        if (player.getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(serverName)) {
            player.sendMessage(Component.text("You're already connected to the desired server: \""+serverName+"\".").color(NamedTextColor.DARK_GREEN));
            return;
        }// end if already connected

        // Transfer the player to the given Minecraft server.
        proxyServer.getServer(serverName).ifPresent(server -> {
            player.createConnectionRequest(server).fireAndForget();
        });
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(serverPermission);
    }
}// end playerTransferCommand
