package ro.mpp2025;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.mpp2025.interfaces.IGameRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/ro/mpp/games")
public class GameController {
    private final IGameRepo repo;

    public GameController(IGameRepo repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "/{nickname}", method = RequestMethod.GET)
    public List<GameDTO> findAllLostGames(@PathVariable String nickname) {
        List<Game> lostGames = repo.findAllLost();
        lostGames = lostGames.stream().filter(g -> Objects.equals(g.getPlayer().getNickname(), nickname)).toList();

        List<GameDTO> gameDTOs = new ArrayList<>();
        for (Game game : lostGames) {
            GameDTO dto = new GameDTO(
                    game.getAttempts().size(),
                    game.getAttempts().toArray(new Attempt[0]),
                    game.getConfiguration().getRow(),
                    game.getConfiguration().getColumn());
            gameDTOs.add(dto);
        }
        return gameDTOs;
    }
}
