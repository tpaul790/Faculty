package ro.mpp2025.interfaces;

import ro.mpp2025.Game;

import java.util.List;

public interface IGameRepo extends Repository<Integer, Game> {
    List<Game> findAllLost();
}
