package ro.mpp2025.object_protocol.responses;

import ro.mpp2025.Player;

public class LogInResponse implements Response {
    private Player player;
    public LogInResponse(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }
}
