package com.game.controller;

import com.game.entity.PlayerEntity;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.BadPlayerParamsException;
import com.game.exception.InvalidParamException;
import com.game.exception.PlayerNotFoundException;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerEntity>> getPlayers(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String title,
                                                         @RequestParam(required = false) Race race,
                                                         @RequestParam(required = false) Profession profession,
                                                         @RequestParam(required = false) Long after,
                                                         @RequestParam(required = false) Long before,
                                                         @RequestParam(required = false) Boolean banned,
                                                         @RequestParam(required = false) Integer minExperience,
                                                         @RequestParam(required = false) Integer maxExperience,
                                                         @RequestParam(required = false) Integer minLevel,
                                                         @RequestParam(required = false) Integer maxLevel,
                                                         @RequestParam(required = false) PlayerOrder order,
                                                         @RequestParam(defaultValue = "0") Integer pageNumber,
                                                         @RequestParam(defaultValue = "3") Integer pageSize) {

        return ResponseEntity.ok(playerService.getAllPlayers(name, title, race, profession, after, before, banned,
                                minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize));
    }

    @PostMapping("/players")
    public ResponseEntity createPlayer(@RequestBody PlayerEntity playerEntity) {
        try {
            return ResponseEntity.ok(playerService.savePlayer(playerEntity));
        } catch (BadPlayerParamsException e) {
            return ResponseEntity.badRequest().body(400);
        }
    }

    @GetMapping("/players/count")
    public ResponseEntity getCount(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) Integer minExperience,
                                   @RequestParam(required = false) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel) {
        return ResponseEntity.ok(playerService.countAllPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel));
    }

    @GetMapping("/players/{id}")
    public ResponseEntity getPlayer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(playerService.getPlayer(id));
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(400);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity removePlayer(@PathVariable Long id) {
        try {
            playerService.removePlayer(id);
            return ResponseEntity.ok(200);
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(400);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable Long id,
                                       @RequestBody PlayerEntity playerEntityUpdated) {
        try {
            return ResponseEntity.ok(playerService.updatePlayer(id, playerEntityUpdated));
        } catch (InvalidParamException e) {
            return ResponseEntity.badRequest().body(404);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
