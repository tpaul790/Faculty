package ro.mpp2025;

import org.springframework.web.bind.annotation.*;
import ro.mpp2025.interfaces.IGameRepo;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ro/mpp/players")
public class GameServices {
    private IGameRepo gameRepo;

    public GameServices(IGameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{nickname}/games")
    public List<GameDTO> getAllGamesForPlayer(@PathVariable String nickname) {
        List<Game> games = gameRepo.findAll();
        List<GameDTO> result = new ArrayList<>();
        for(Game game : games) {
            GameDTO gameDTO = new GameDTO(game.getPlayer().getNickname(),game.getConfiguration().getId(),game.getCorrect());
            game.setConfiguration(null);
            if(game.getPlayer().getNickname().equals(nickname) && game.getCorrect() >= 2) {
                result.add(gameDTO);
            }
        }
        return result;
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public List<Player> getAllPlayers() {
//        return playerRepo.findAll();
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public Player savePlayer(@RequestBody Player player) {
//        Optional<Player> op = playerRepo.save(player);
//        return op.orElse(null);
//    }
//
//    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
//    public Player updatePlayer(@PathVariable Integer id,@RequestBody Player player) {
//        player.setId(id);
//        Optional<Player> op = playerRepo.update(player);
//        return op.orElse(null);
//    }
}
