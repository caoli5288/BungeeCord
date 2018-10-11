package net.md_5.bungee.api.event;

import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerPostConnectedEvent extends AsyncEvent<ServerPostConnectedEvent> {

    private final Channel connector;
    private final ProxiedPlayer player;
    private final Server server;

    public ServerPostConnectedEvent(Channel connector, ProxiedPlayer player, Server server, Callback<ServerPostConnectedEvent> done) {
        super(done);
        this.connector = connector;
        this.player = player;
        this.server = server;
    }

}
