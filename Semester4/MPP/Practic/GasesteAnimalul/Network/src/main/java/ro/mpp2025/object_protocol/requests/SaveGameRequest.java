package ro.mpp2025.object_protocol.requests;

import ro.mpp2025.Game;

public class SaveGameRequest implements Request{
    private Game game;
    public SaveGameRequest(Game game) {
        this.game = game;
    }
    public Game getGame() {
        return game;
    }
}
