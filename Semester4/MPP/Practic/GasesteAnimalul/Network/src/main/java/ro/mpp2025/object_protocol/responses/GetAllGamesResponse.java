package ro.mpp2025.object_protocol.responses;

import ro.mpp2025.Game;

public class GetAllGamesResponse implements Response {
    private Game[] games;
    public GetAllGamesResponse(Game[] games) {
        this.games = games;
    }
    public Game[] getGames() {
        return games;
    }
}
