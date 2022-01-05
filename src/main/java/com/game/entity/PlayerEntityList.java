package com.game.entity;

import com.game.controller.PlayerOrder;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PlayerEntityList {

    private List<PlayerEntity> playerEntityList;

    public PlayerEntityList(List<PlayerEntity> playerEntityListList) {
        this.playerEntityList = playerEntityListList;
    }

    public PlayerEntityList filterName(String name) {
        if (name != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getName().toLowerCase().contains(name)).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterTitle(String title) {
        if (title != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getTitle().toLowerCase().contains(title)).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterRace(Race race) {
        if (race != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getRace() == race).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterProfession(Profession profession) {
        if (profession != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getProfession() == profession).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterAfter(Long after) {
        if (after != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getBirthday().getTime() > after).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterBefore(Long before) {
        if (before != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getBirthday().getTime() < before).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterBanned(Boolean banned) {
        if (banned != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getBanned() == banned).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterMinExperience(Integer minExperience) {
        if (minExperience != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getExperience() > minExperience).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterMaxExperience(Integer maxExperience) {
        if (maxExperience != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getExperience() < maxExperience).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterMinLevel(Integer minLevel) {
        if (minLevel != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getLevel() > minLevel).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList filterMaxLevel(Integer maxLevel) {
        if (maxLevel != null) {
            playerEntityList = playerEntityList.stream().filter(p -> p.getLevel() < maxLevel).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public PlayerEntityList getFiltered(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned,
                                        Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        return new PlayerEntityList(playerEntityList).filterName(name).filterTitle(title).filterRace(race).filterProfession(profession)
                .filterAfter(after).filterBefore(before).filterBanned(banned).filterMinExperience(minExperience).filterMaxExperience(maxExperience)
                .filterMinLevel(minLevel).filterMaxLevel(maxLevel);
    }

    public PlayerEntityList filterByOrder(PlayerOrder order) {
        if (order == null) order = PlayerOrder.ID;
        switch (order) {
            case NAME:
                playerEntityList = playerEntityList.stream().sorted(Comparator.comparing(PlayerEntity::getName)).collect(Collectors.toList());
            case EXPERIENCE:
                playerEntityList = playerEntityList.stream().sorted(Comparator.comparing(PlayerEntity::getExperience)).collect(Collectors.toList());
            case BIRTHDAY:
                playerEntityList = playerEntityList.stream().sorted(Comparator.comparing(PlayerEntity::getBirthday)).collect(Collectors.toList());
            case LEVEL:
                playerEntityList = playerEntityList.stream().sorted(Comparator.comparing(PlayerEntity::getLevel)).collect(Collectors.toList());
            default:
                playerEntityList = playerEntityList.stream().sorted(Comparator.comparing(PlayerEntity::getId)).collect(Collectors.toList());
        }
        return new PlayerEntityList(playerEntityList);
    }

    public List<PlayerEntity> toList() {
        return playerEntityList;
    }
}
