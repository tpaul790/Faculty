package ro.mpp2025.object_protocol.requests;

import ro.mpp2025.Player;

public class LogInRequest implements Request {
    private Player player;
    public LogInRequest(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }
}
