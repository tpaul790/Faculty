package ro.mpp2025.object_protocol.requests;

import ro.mpp2025.Player;

public class LogOutRequest implements Request {
    private Player player;
    public LogOutRequest(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }

}
