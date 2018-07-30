package net.md_5.bungee.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Event;

@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class ServerPostConnectedEvent extends Event {

    private final ProxiedPlayer player;
    private final Server server;
    private Runnable runnable;

    public void post() {
        runnable.run();
    }
}
