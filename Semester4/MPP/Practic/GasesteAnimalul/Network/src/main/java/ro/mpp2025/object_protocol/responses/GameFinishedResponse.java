package ro.mpp2025.object_protocol.responses;

import ro.mpp2025.Game;
import ro.mpp2025.object_protocol.UpdateResponse;

public class GameFinishedResponse implements UpdateResponse {
    private Game game;
    public GameFinishedResponse(Game game) {
        this.game = game;
    }
    public Game getGame() {
        return game;
    }
}
