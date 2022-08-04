package org.example.sports.db;

import org.example.shared.io.UserInputService;
import org.example.shared.io.db.Repository;
import org.example.shared.io.validation.NonBlankInputValidationRule;
import org.example.sports.model.Statistic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatisticRepository implements Repository<Statistic> {

    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;
    EntityTransaction transaction;

    public StatisticRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("example");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
    }

    @Override
    public Statistic save(Statistic statistic) {
        transaction.begin();
        entityManager.persist(statistic);
        transaction.commit();
        return statistic;
    }

    @Override
    public Optional<Statistic> findById(Long id) {

        return Optional.empty();
    }

    public List<Statistic> getStatistics() {
        return entityManager
                .createQuery("SELECT S from Statistic S", Statistic.class)
                .getResultList();
    }

    public List<Statistic> getDistinctPlayersList() {
        return entityManager
                .createQuery("SELECT Distinct S FROM Statistic S", Statistic.class)
                .getResultList();
    }

    public String getDistinctPlayersListString() {
        List<Statistic> statisticsList = getDistinctPlayersList();
        List<String> playerNames = new ArrayList<>();

        for (int i = 0; i < statisticsList.size(); i++) {
            if (!playerNames.contains(statisticsList.get(i).getName())) {
                playerNames.add(statisticsList.get(i).getName());
            }
        }

        String playerNamesString = "";
        int count = 0;
        for (String playerName : playerNames) {
            playerNamesString += "\n" + count + ". " + playerName;
            count++;
        }
        return playerNamesString;

//        System.out.println("Player Names: \n" + playerNamesString);
//
//        return statisticNamesList;
    }

    public String getDistinctPlayerNameFromList(UserInputService userInputService) {
        int chosenPlayer = Integer.parseInt(userInputService.getUserInput("Which athlete do you want to choose?" + getDistinctPlayersListString(),
                new NonBlankInputValidationRule()));

        return getDistinctPlayersList().get(chosenPlayer).getName();
    }
}
