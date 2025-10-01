package ro.mpp2025.interfaces;

import ro.mpp2025.Player;

import java.util.Optional;

public interface IPlayerRepo extends Repository<Integer, Player> {
    Optional<Player> findBy(String nickname);
}
