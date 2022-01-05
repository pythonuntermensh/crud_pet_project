package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.PlayerEntity;
import com.game.entity.PlayerEntityList;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.BadPlayerParamsException;
import com.game.exception.InvalidParamException;
import com.game.exception.PlayerNotFoundException;
import com.game.repository.PlayerRepository;
import com.game.util.LevelAndExpCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final int MAX_EXPERIENCE = 10_000_000;
    private final int MAX_NAME_LENGTH = 12;
    private final int MAX_TITLE_LENGTH = 30;

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }



    public List<PlayerEntity> getAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned,
                                            Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, PlayerOrder order,
                                            Integer pageNumber, Integer pageSize) {
        PlayerEntityList filteredPlayersList = new PlayerEntityList(playerRepository.findAll());

        filteredPlayersList = filteredPlayersList.getFiltered(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel).filterByOrder(order);

        int skip = pageNumber * pageSize;
        List<PlayerEntity> result = new ArrayList<>();
        for (int i = skip; i < Math.min(skip + pageSize, filteredPlayersList.toList().size()); i++) {
            result.add(filteredPlayersList.toList().get(i));
        }

        return result;
    }

    public PlayerEntity getPlayer(Long id) throws PlayerNotFoundException, InvalidParamException {
        if (id == null || id < 1) {
            throw new InvalidParamException("Invalid id param");
        }

        Optional<PlayerEntity> player = playerRepository.findById(id);
        if (!player.isPresent()) {
            throw new PlayerNotFoundException("No players were found");
        }
        return player.get();
    }

    public void removePlayer(Long id) throws InvalidParamException, PlayerNotFoundException {
        playerRepository.delete(getPlayer(id));
    }

    public int countAllPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned,
                               Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        PlayerEntityList filteredPlayersList = new PlayerEntityList(playerRepository.findAll());
        return filteredPlayersList.getFiltered(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel).toList().size();
    }

    public PlayerEntity savePlayer(PlayerEntity playerEntity) throws BadPlayerParamsException {
        if (playerEntity.getName() == null
                || playerEntity.getName().length() > 12
                || playerEntity.getName().isEmpty()
                || playerEntity.getTitle().length() > 30
                || playerEntity.getExperience() < 0
                || playerEntity.getExperience() >  10000000
                || playerEntity.getBirthday().getTime() < 0) {
            throw new BadPlayerParamsException("Wrong player creating params");
        }
        if (playerEntity.getBanned() == null) playerEntity.setBanned(false);

        playerEntity.setLevel(LevelAndExpCounter.CountLevel(playerEntity.getExperience()));
        playerEntity.setUntilNextLevel(LevelAndExpCounter.CountExpUntilNextLevel(playerEntity.getLevel(), playerEntity.getExperience()));

        System.out.println(playerEntity);
        return playerRepository.save(playerEntity);
    }

    public PlayerEntity updatePlayer(Long id, PlayerEntity playerEntityUpdated) throws  InvalidParamException, PlayerNotFoundException {
        if (id == null || id < 1) {
            throw new InvalidParamException("Invalid id param");
        }

        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findById(id);

        if (!optionalPlayerEntity.isPresent()) {
            throw new PlayerNotFoundException("No players were found");
        }

        PlayerEntity playerEntity = optionalPlayerEntity.get();

        if (playerEntityUpdated.getName() != null) playerEntity.setName(playerEntityUpdated.getName());

        if (playerEntityUpdated.getTitle() != null) playerEntity.setTitle(playerEntityUpdated.getTitle());

        if (playerEntityUpdated.getRace() != null) playerEntity.setRace(playerEntityUpdated.getRace());

        if (playerEntityUpdated.getProfession() != null) playerEntity.setProfession(playerEntityUpdated.getProfession());

        if (playerEntityUpdated.getBirthday() != null
            && playerEntityUpdated.getBirthday().getTime() < 1) {
            throw new InvalidParamException("Invalid birthday param");
        } else if (playerEntityUpdated.getBirthday() != null){
            playerEntity.setBirthday(playerEntityUpdated.getBirthday());
        }

        if (playerEntityUpdated.getBanned() != null) playerEntity.setBanned(playerEntityUpdated.getBanned());

        if (playerEntityUpdated.getExperience() != null
            && (playerEntityUpdated.getExperience() > MAX_EXPERIENCE
            || playerEntityUpdated.getExperience() < 1)) {
            throw new InvalidParamException("Invalid experience param");
        } else if (playerEntityUpdated.getExperience() != null) {
            playerEntity.setExperience(playerEntityUpdated.getExperience());
        }

        playerEntity.setLevel(LevelAndExpCounter.CountLevel(playerEntity.getExperience()));
        playerEntity.setUntilNextLevel(LevelAndExpCounter.CountExpUntilNextLevel(playerEntity.getLevel(), playerEntity.getExperience()));

        return playerEntity;
    }
}
