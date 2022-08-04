package org.example.sports;

import org.example.shared.io.UserInputService;
import org.example.shared.io.UserOutputService;
import org.example.shared.io.console.ConsoleUserInputServiceImpl;
import org.example.shared.io.console.ConsoleUserOutputServiceImpl;
import org.example.shared.io.validation.NonBlankInputValidationRule;
import org.example.sports.db.StatisticRepository;
import org.example.sports.model.Statistic;

public class SportsMain {
    public static void main(String[] args) throws Exception {
        //create UserInputService and UserOutputService object

        UserOutputService userOutputService = new ConsoleUserOutputServiceImpl();
        try (UserInputService userInputService = new ConsoleUserInputServiceImpl(userOutputService)) {
            userOutputService.print("WELCOME");

            boolean runValue = true;
            while (runValue) {
                // Get User Choice
                int userChoice = userInputService.getUserChoice("""
                        What would you like to do?
                        1. Add an athlete with a game statistic
                        2. Add another game statistic for the same athlete
                        3. Get statistics information on that athlete
                        4. Exit program.""", 1, 2, 3, 4);

                // Statistic Repository
                StatisticRepository statRepo = new StatisticRepository();

                // Single Statistic
                Statistic singleStatistic = new Statistic();

                switch (userChoice) {
                    case 1:
                        addAthleteWithStatistic(singleStatistic, userInputService);

                        System.out.println(statRepo.save(singleStatistic).toString() + " saved...");
                        break;
                    case 2:
                        String playerName = statRepo.getDistinctPlayerNameFromList(userInputService);

                        addStatisticToPlayer(singleStatistic, playerName, userInputService);

                        System.out.println(statRepo.save(singleStatistic).toString() + " saved...");
                        break;
                    case 3:
                        int userStatisticChoice = userInputService.getUserChoice("""
                                What statistic would you like to see?
                                "1. Print their average points per game
                                "2. Max number of points in a game""", 1, 2);

                        switch (userStatisticChoice) {
                            case 1:
//                                Statistic statisticToGetAVG = getDistinctPlayerForNewStatistic(userInputService, entityManager);
//                                String query = "SELECT S from Statistic S where S.name = '" + statisticToGetAVG.getName() + "'";
//                                List<Statistic> statisticsToAverage = entityManager.createQuery(query,
//                                        Statistic.class).getResultList();
//
//                                float average = 0;
//                                double count = 0;
//                                double total = 0;
//                                for (Statistic stat : statisticsToAverage) {
//                                    total += stat.getScoreInGame();
//                                    count++;
//                                }
//                                average = (float) (total / count);
//                                System.out.println("Average score= " + average);
                                break;
                            case 2:
//                                Statistic statisticToGetMax = getDistinctPlayerForNewStatistic(userInputService, entityManager);
//                                String queryMax = "SELECT S from Statistic S where S.name = '" + statisticToGetMax.getName() + "'";
//                                List<Statistic> statisticsToMax = entityManager.createQuery(queryMax,
//                                        Statistic.class).getResultList();
//                                int max = 0;
//                                for (Statistic stat : statisticsToMax) {
//                                    if (max < stat.getScoreInGame()) {
//                                        max = stat.getScoreInGame();
//                                    }
//                                }
//                                System.out.println("Max points= " + max);
                                break;
                            default:
                                break;
                        }
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

    public static void addAthleteWithStatistic(Statistic singleStatistic, UserInputService userInputService) {
        String name = userInputService.getUserInput("What's the athletes name?",
                new NonBlankInputValidationRule());
        int score = Integer.parseInt(userInputService.getUserInput("What's the athletes score in this game?",
                new NonBlankInputValidationRule()));
        singleStatistic.setName(name);
        singleStatistic.setScoreInGame(score);
    }

    public static void addStatisticToPlayer(Statistic singleStatistic, String name, UserInputService userInputService) {
        int score = Integer.parseInt(userInputService.getUserInput("What's the athletes score in this game?",
                new NonBlankInputValidationRule()));
        singleStatistic.setName(name);
        singleStatistic.setScoreInGame(score);
    }
}



