package org.example.sports;

import org.example.shared.io.UserInputService;
import org.example.shared.io.UserOutputService;
import org.example.shared.io.console.ConsoleUserInputServiceImpl;
import org.example.shared.io.console.ConsoleUserOutputServiceImpl;
import org.example.shared.io.validation.NonBlankInputValidationRule;
import org.example.sports.model.Statistic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class SportsMain {
    public static void main(String[] args) throws Exception {
        // create a new student instance
//        Statistic statistic = new Statistic();
//        statistic.setId(1);
//        statistic.setName("Jack");
        //create UserInputService and UserOutputService object

        UserOutputService userOutputService = new ConsoleUserOutputServiceImpl();
        try (UserInputService userInputService = new ConsoleUserInputServiceImpl(userOutputService)) {
            userOutputService.print("WELCOME");


            // create EntityManager


            // create and use transactions
            int userChoice = getUserChoice(userInputService);
            boolean runValue = true;
            while (runValue) {
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example");
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                // access transaction object
                EntityTransaction transaction = entityManager.getTransaction();
                switch (userChoice) {
                    case 1:
                        Statistic statistic = addAthleteWithStatistic(userInputService);
                        transaction.begin();
                        entityManager.persist(statistic);
                        transaction.commit();
                        userChoice = getUserChoice(userInputService);
                        break;
                    case 2:
                        transaction.begin();
                        Statistic statistic1 = getDistinctPlayerForNewStatistic(userInputService, entityManager);
                        Statistic anotherStatistic = addStatisticToPlayer(statistic1, entityManager, userInputService);
                        entityManager.persist(anotherStatistic);
                        transaction.commit();
                        userChoice = getUserChoice(userInputService);
                        break;
                    case 3:
                        int userStatisticChoice = getUserStatisticChoice(userInputService);
                        switch (userStatisticChoice) {
                            case 1:
                                Statistic statisticToGetAVG = getDistinctPlayerForNewStatistic(userInputService, entityManager);
                                String query = "SELECT S from Statistic S where S.name = '" + statisticToGetAVG.getName() + "'";
                                List<Statistic> statisticsToAverage = entityManager.createQuery(query,
                                        Statistic.class).getResultList();

                                float average = 0;
                                double count = 0;
                                double total = 0;
                                for (Statistic stat : statisticsToAverage) {
                                    total += stat.getScoreInGame();
                                    count++;
                                }
                                average = (float) (total / count);
                                System.out.println("Average score= " + average);
                                break;
                            case 2:
                                Statistic statisticToGetMax = getDistinctPlayerForNewStatistic(userInputService, entityManager);
                                String queryMax = "SELECT S from Statistic S where S.name = '" + statisticToGetMax.getName() + "'";
                                List<Statistic> statisticsToMax = entityManager.createQuery(queryMax,
                                        Statistic.class).getResultList();
                                int max = 0;
                                for (Statistic stat : statisticsToMax) {
                                    if (max < stat.getScoreInGame()) {
                                        max = stat.getScoreInGame();
                                    }
                                }
                                System.out.println("Max points= " + max);
                                break;
                            default:
                                break;
                        }
                        userChoice = getUserChoice(userInputService);
                        break;
                    case 4:
                        runValue = false;
                        break;
                    default:
                        System.out.println("Please choose a number from the selection given");
                        break;
                }

            }

        }
    }


    private static int getUserStatisticChoice(UserInputService userInputService) {
        return Integer.parseInt(userInputService.getUserInput("What statistic would you like to see?\n" +
                        "1. Print their average points per game\n" +
                        "2. Max number of points in a game\n",
                new NonBlankInputValidationRule()));
    }

    private static int getUserChoice(UserInputService userInputService) {
        return Integer.parseInt(userInputService.getUserInput("What would you like to do?\n" +
                        "1. Add an athlete with a game statistic\n" +
                        "2. Add another game statistic for the same athlete\n" +
                        "3. Get statistics information on that athlete\n" +
                        "4. Exit program.",
                new NonBlankInputValidationRule()));

    }

    public static Statistic addAthleteWithStatistic(UserInputService userInputService) {
        Statistic statistic = new Statistic();
        String response = userInputService.getUserInput("What's the athletes name?",
                new NonBlankInputValidationRule());
        int score = Integer.parseInt(userInputService.getUserInput("What's the athletes score in this game?",
                new NonBlankInputValidationRule()));
        statistic.setName(response);
        statistic.setScoreInGame(score);
        return statistic;
    }


    public static Statistic getDistinctPlayerForNewStatistic(UserInputService userInputService, EntityManager entityManager) {
        String hql = "SELECT S from Statistic S";

        List<Statistic> statisticNamesList = entityManager.createQuery(hql,
                Statistic.class).getResultList();
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < statisticNamesList.size(); i++) {
            if (!playerNames.contains(statisticNamesList.get(i).getName())) {
                playerNames.add(statisticNamesList.get(i).getName());
            }
        }
        String playerNamesString = "";
        int count = 0;
        for (String playerName : playerNames) {
            playerNamesString += "\n" + count + ". " + playerName;
            count++;

        }
        int chosenPlayer = Integer.parseInt(userInputService.getUserInput("Which athlete do you want to add another statistic for?" + playerNamesString,
                new NonBlankInputValidationRule()));
        Statistic statistic = new Statistic();
        statistic.setName(statisticNamesList.get(chosenPlayer).getName());


        return statistic;
    }


    public static Statistic addStatisticToPlayer(Statistic statistic, EntityManager entityManager, UserInputService userInputService) {

        int score = Integer.parseInt(userInputService.getUserInput("What's the athletes score in this game?",
                new NonBlankInputValidationRule()));
        statistic.setScoreInGame(score);
        return statistic;
    }
}



