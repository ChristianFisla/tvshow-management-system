// Christian Fisla
// November 21st, 2022 --> Part C of Program (FINAL PART)
// This is a program that allows the user to navigate an interface where they can add TVShows, watch, sort, and manage them!
package com.fisla.Assignment5Gr12;

import java.io.*;
import java.util.*;

public class Driver {

    // Desc: Displays a list of all the shows currently added
    // Param: Takes in the ArrayList of shows
    // Return: n/a
    public static void displayShowList(ArrayList<TVShow> showList) {

        // If there are no shows then give the user appropriate feedback
        if (showList.size() == 0) {

            System.out.printf("%nThere are no current shows added to your list.%n");

        } else {

            System.out.println("Show List");
            System.out.println("----------------");

            // Loop through all shows and number them in a list
            for (int i = 0; i < showList.size(); i++) {
                System.out.println((i + 1) + ". " + showList.get(i).getTitle());
            }
        }

    }

    // Desc: Remove a show by their title
    // Param: The ArrayList of all the shows, and the Scanner to prompt an input
    // Return: n/a
    public static void removeShowTitle(ArrayList<TVShow> showList, Scanner in) {
        String title;

        // If there are no shows, do not prompt for input
        if (showList.size() == 0) {
            System.out.println("There are no current shows added to your list.");
        } else {
            System.out.print("Remove by title: ");
            title = in.nextLine();

            // Loop through all shows and remove the one with the matching title
            for (int i = 0; i < showList.size(); i++) {
                if (title.equalsIgnoreCase(showList.get(i).getTitle())) showList.remove(i);
            }
        }
    }
    // Desc: Remove a season within a chosen show
    // Param: The ArrayList of all the shows, and the Scanner to prompt an input
    // Return: n/a
    public static void removeShowSeason(ArrayList<TVShow> showList, Scanner in) {

        int inputDisplayShowNum = 0;

        displayShowList(showList);

        // Only ask for a season if the TVShow exists
        if (!(showList.size() == 0)) {
            System.out.println("");

            boolean validShowInput;

            do {
                // Try and parse the user input for an integer, only loop if there is an error
                try {
                    // Prompt the user to choose a show
                    System.out.print("Which show would you like to choose? ");
                    inputDisplayShowNum = Integer.parseInt(in.nextLine());
                    validShowInput = (inputDisplayShowNum > 0) && (inputDisplayShowNum < (showList.size() + 1));
                } catch (NumberFormatException e) {
                    validShowInput = false;
                }
            } while (!validShowInput);

            // Prompt the user for the season they'd like to remove
            System.out.print("What season would you like to remove? ");
            String removeEp = in.nextLine();

            // Define our upper loop range
            int epList = showList.get(inputDisplayShowNum - 1).getEpisodeList().size();

            // Loop through all episodes and remove any that has matching season attributes to the user entry
            for (int i = 0; i < epList; i++) {
                if (removeEp.equalsIgnoreCase(showList.get(inputDisplayShowNum - 1).getEpisodeList().get(i).getSeason() + "")) {

                    // Reduce the Time of the TVShow
                    showList.get(inputDisplayShowNum - 1).getTime().subTime(showList.get(inputDisplayShowNum - 1).getEpisodeList().get(i).getTime());

                    showList.get(inputDisplayShowNum - 1).getEpisodeList().remove(i);
                    epList--;
                    i--;
                }
            }

            // Remove the season from the TVShow database and adjust attributes accordingly
            showList.get(inputDisplayShowNum - 1).getSeasonNumbers().remove(showList.get(inputDisplayShowNum - 1).getSeasonNumbers().indexOf(Integer.parseInt(removeEp)));
            showList.get(inputDisplayShowNum - 1).reduceSeason();

            // Remove the TVShow if there are no more episodes/seasons in the show
            if (showList.get(inputDisplayShowNum - 1).getEpisodeList().size() == 0)
                showList.remove(inputDisplayShowNum - 1);
        }
    }
    // Desc: Show current status of a show
    // Param: The ArrayList of all the shows, and the Scanner to prompt an input
    // Return: n/a
    public static void currentStatus (ArrayList<TVShow> showList, Scanner in) {

        displayShowList(showList);

        // Only prompt for a show if there are any to begin with
        if (!(showList.size() == 0)){
            System.out.println("");

            int inputDisplayShowNum = 0;
            boolean validShowInput;

            // Try to ask for a valid input, only loop if the entry cannot be parsed
            do {
                try {
                    System.out.print("Which show would you like to choose? ");
                    inputDisplayShowNum = Integer.parseInt(in.nextLine());
                    validShowInput = (inputDisplayShowNum > 0) && (inputDisplayShowNum < (showList.size() + 1));
                } catch (NumberFormatException e) {
                    validShowInput = false;
                }
            } while (!validShowInput);

            System.out.println("");

            int epWatched = 0;

            // Loop through all the seasons...
            for (int i = 0; i < showList.get(inputDisplayShowNum - 1).getTotalSeasons(); i++) {

                // ...and all the episodes within those seasons
                // To find out how many episodes total are watched
                for (int j = 0; j < showList.get(inputDisplayShowNum - 1).getEpisodeList().size(); j++) {
                    if (showList.get(inputDisplayShowNum - 1).getEpisodeList().get(j).getIsWatched() && showList.get(inputDisplayShowNum - 1).getEpisodeList().get(j).getSeason() == showList.get(inputDisplayShowNum - 1).getSeasonNumbers().get(i)) {
                        epWatched++;
                    }
                }

                System.out.printf("Season %d = %d eps watched out of total %d eps%n", showList.get(inputDisplayShowNum - 1).getSeasonNumbers().get(i), epWatched, showList.get(inputDisplayShowNum - 1).getTotalEpisodes());
                epWatched = 0;
            }

            int unwatchedSeasons = showList.get(inputDisplayShowNum - 1).getSeasonNumbers().size();

            // Display total unwatched seasons
            season: for (int i = 0; i < showList.get(inputDisplayShowNum - 1).getSeasonNumbers().size(); i++) {
                for (int j = 0; j < showList.get(inputDisplayShowNum - 1).getEpisodeList().size(); j++) {
                    if (showList.get(inputDisplayShowNum - 1).getEpisodeList().get(j).getSeason() == showList.get(inputDisplayShowNum - 1).getSeasonNumbers().get(i) && showList.get(inputDisplayShowNum - 1).getEpisodeList().get(j).getIsWatched()) {
                        unwatchedSeasons--;
                        continue season;
                    }
                }
            }

            System.out.println(unwatchedSeasons + " season(s) unwatched");

            // Display total unwatched episodes
            int epUnwatched = 0;

            for (int i = 0; i < showList.get(inputDisplayShowNum - 1).getEpisodeList().size(); i++) {
                if (!(showList.get(inputDisplayShowNum - 1).getEpisodeList().get(i).getIsWatched())) {
                    epUnwatched++;
                }
            }
            System.out.println(epUnwatched + " episode(s) unwatched");
        }

    }
    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        String input;

        ArrayList<TVShow> showList = new ArrayList<>();

        // Loop until the user exits
        do {
            // Error check for main menu. Make sure its a valid input
            do {

                System.out.printf("%nMain Menu%n--------------------------------------------------%n1) Accessing your TV Shows list%n2) Accessing within a particular TV Show%n3) Exit%n");
                input = in.nextLine();
            } while (!(input.equalsIgnoreCase("1") || input.equalsIgnoreCase("2") || input.equalsIgnoreCase("3")));

            // When the user accesses the first sub menu
            if (input.equals("1")) {

                // Loop until the exit
                do {
                    // Error check to make sure it's a valid input
                    do {

                        System.out.printf("%nSub-Menu for Option #1%n--------------------------------------------------%n1) Display a list of all your TV shows%n2) Display information about a particular TV show%n3) Add a TV show%n4) Remove (2 options)%n5) Show status%n6) Return back to main menu%n");
                        input = in.nextLine();

                    } while (!(input.equalsIgnoreCase("1") || input.equalsIgnoreCase("2") || input.equalsIgnoreCase("3") || input.equalsIgnoreCase("4") || input.equalsIgnoreCase("5") || input.equalsIgnoreCase("6")));

                    // Sub Menu 1, Option 1
                    if (input.equalsIgnoreCase("1")) {
                        displayShowList(showList);
                    }

                    // Sub Menu 1, Option 2
                    if (input.equalsIgnoreCase("2")) {

                        displayShowList(showList);

                        if (!(showList.size() == 0)){
                                System.out.println("");

                            int inputDisplayShowNum = 0;
                            boolean validShowInput;

                            // Prompt user for what show to choose
                            do {
                                try {
                                    System.out.print("Which show would you like to choose? ");
                                    inputDisplayShowNum = Integer.parseInt(in.nextLine());
                                    validShowInput = (inputDisplayShowNum > 0) && (inputDisplayShowNum < (showList.size() + 1));
                                } catch (NumberFormatException e) {
                                    validShowInput = false;
                                }
                            } while (!validShowInput);

                            System.out.println(showList.get(inputDisplayShowNum - 1));
                        }
                    }

                    // Sub Menu 1, Option 3
                    if (input.equalsIgnoreCase("3")) {

                        System.out.println("What show would you like to add? (Ensure filename is exactly as follows: yourinput.txt) ");
                        String directory = in.nextLine();

                        Scanner inFile = null;

                        // Try to find the file
                        try {
                            inFile = new Scanner(new File(directory + ".txt"));
                        } catch(FileNotFoundException e) {
                            System.out.println("File not found");
                        }

                        // If the file was not found, skip
                        if (inFile != null) {

                            // Call on TVShow constructor and add it to the showList
                            showList.add(new TVShow(inFile));

                        }
                    }

                    // Sub Menu 1, Option 4
                    if (input.equalsIgnoreCase("4")) {

                        String inputRemove;

                        // Prompt the user to remove by title or season
                        do {
                            System.out.println("Would you like to remove by title or season #? (t/s) ");
                            inputRemove = in.nextLine();
                        } while (!(inputRemove.equalsIgnoreCase("t") || inputRemove.equalsIgnoreCase("s")));

                        // Call different methods depending on user choice
                        if (inputRemove.equalsIgnoreCase("t")) {
                            removeShowTitle(showList, in);
                        } else {
                            removeShowSeason(showList, in);
                        }
                    }

                    // Sub Menu 1, Option 5
                    if (input.equalsIgnoreCase("5")) {
                        currentStatus(showList, in);
                    }

                } while (!input.equalsIgnoreCase("6"));
            }

            // When the user chooses sub menu 2
            if (input.equalsIgnoreCase("2")) {

                // Ask the user for a TVShow to search
                System.out.print("What TVShow would you like to access? ");
                String tVShowTitle = in.nextLine();

                // Sort list
                Collections.sort(showList, new SortByTitleTVShow());

                // Search for their show in showList
                int index = Collections.binarySearch(showList, new TVShow(true, tVShowTitle), new SortByTitleTVShow());

                // If found, continue. If not, alert the user and skip.
                if (index >= 0) {

                    int inputDisplaySeasonNum = 0;
                    boolean validSeasonInput = false;

                    // Prompt the user for their choice of season to access
                    do {
                        try {
                            System.out.print("Which season would you like to access? ");
                            inputDisplaySeasonNum = Integer.parseInt(in.nextLine());

                            for (int i = 0; i < showList.get(index).getSeasonNumbers().size(); i++) {
                                if (inputDisplaySeasonNum == showList.get(index).getSeasonNumbers().get(i)) validSeasonInput = true;
                            }

                        } catch (NumberFormatException e) {
                            validSeasonInput = false;
                        }

                    } while (!validSeasonInput);


                    // Loop until the user exits
                    do {
                        // Error check to ensure valid input
                        do {

                            System.out.printf("%nSub-Menu for Option #2 (%s, Season %d)%n--------------------------------------------------%n1) Display all episodes (in the last sorted order)%n2) Display information on a particular episode%n3) Watch an episode%n4) Add an episode%n5) Remove episode (4 options)%n6) Sort episodes (3 options)%n7) Return back to main menu%n", showList.get(index).getTitle(), inputDisplaySeasonNum);
                            input = in.nextLine();

                        } while (!(input.equalsIgnoreCase("1") || input.equalsIgnoreCase("2") || input.equalsIgnoreCase("3") || input.equalsIgnoreCase("4") || input.equalsIgnoreCase("5") || input.equalsIgnoreCase("6") || input.equalsIgnoreCase("7")));

                        // Sub Menu 2, Option 1
                        if (input.equalsIgnoreCase("1")) {
                            showList.get(index).displayEpisodes(inputDisplaySeasonNum);
                        }

                        // Sub Menu 2, Option 2
                        if (input.equalsIgnoreCase("2")) {
                            showList.get(index).displayEpisodeSingle(inputDisplaySeasonNum);
                        }

                        // Sub Menu 2, Option 3
                        if (input.equalsIgnoreCase("3")) {

                            System.out.print("Which episode would you like to watch? (Search by title) ");
                            String inputDisplayEpName = in.nextLine();

                            // Create a new ArrayList
                            ArrayList<Episode> onlyEpisodesInSeason = new ArrayList<>();

                            // Add only the episodes with the requested season to the ArrayList
                            for (int i = 0; i < showList.get(index).getEpisodeList().size(); i++) {
                                if (showList.get(index).getEpisodeList().get(i).getSeason() == inputDisplaySeasonNum) {
                                    onlyEpisodesInSeason.add(showList.get(index).getEpisodeList().get(i));
                                }
                            }

                            // Only search in that specific ArrayList, ...
                            Collections.sort(onlyEpisodesInSeason, new SortByTitleEpisode());
                            int epIndex = Collections.binarySearch(onlyEpisodesInSeason, new Episode(true, inputDisplayEpName), new SortByTitleEpisode());

                            // ...and only display from it
                            if (epIndex > -1) {
                                onlyEpisodesInSeason.get(epIndex).watch();
                            } else {
                                System.out.println("Episode not found.");
                            }

                        }

                        // Sub Menu 2, Option 4
                        if (input.equalsIgnoreCase("4")) {
                            showList.get(index).createEpisode(in, showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));
                        }

                        // Sub Menu 2, Option 5
                        if (input.equalsIgnoreCase("5")) {

                            String removeInput = "";

                            do {
                                System.out.print("Remove by episode #, title, range, or by watched? (1/2/3/4) ");
                                removeInput = in.nextLine();
                            } while (!(removeInput.equalsIgnoreCase("1") || removeInput.equalsIgnoreCase("2") || removeInput.equalsIgnoreCase("3") || removeInput.equalsIgnoreCase("4")));

                            if (removeInput.equalsIgnoreCase("1")) {

                                int epChoiceNum = 0;
                                boolean epChoiceValidInput;

                                // Prompt the user for an input
                                do {
                                    try {
                                        System.out.print("What episode number to remove? ");
                                        epChoiceNum = Integer.parseInt(in.nextLine());
                                        epChoiceValidInput = true;
                                    } catch (NumberFormatException e) {
                                        epChoiceValidInput = false;
                                    }
                                } while (!epChoiceValidInput);

                                // Call the remove episode, if an error has occurred because the TVShow has no more episodes and is removed, then catch the error and set the input to "7" to bring the user back to the main menu
                                try {
                                    showList.get(index).removeEpisode(showList, epChoiceNum, showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));
                                } catch (Exception e) {
                                    input = "7";
                                }

                                if (showList.size() == 0) {
                                    input = "7";
                                }

                            } else if (removeInput.equalsIgnoreCase("3")) {

                                int epChoiceNum = 0;
                                boolean epChoiceValidInput;

                                System.out.println("Choose a start and end range to remove episodes (inclusive both ends)");

                                // Prompt the user for an input
                                do {
                                    try {
                                        System.out.print("Start range: ");
                                        epChoiceNum = Integer.parseInt(in.nextLine());
                                        epChoiceValidInput = true;
                                    } catch (NumberFormatException e) {
                                        epChoiceValidInput = false;
                                    }
                                } while (!epChoiceValidInput);

                                int epChoiceNum2 = 0;
                                boolean epChoiceValidInput2;

                                // Prompt the user for an input
                                do {
                                    try {
                                        System.out.print("End range: ");
                                        epChoiceNum2 = Integer.parseInt(in.nextLine());
                                        epChoiceValidInput2 = true;
                                    } catch (NumberFormatException e) {
                                        epChoiceValidInput2 = false;
                                    }
                                } while (!epChoiceValidInput2);

                                // Call the remove episode, if an error has occurred because the TVShow has no more episodes and is removed, then catch the error and set the input to "7" to bring the user back to the main menu
                                try {
                                    showList.get(index).removeEpisode(showList, epChoiceNum, epChoiceNum2, showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));
                                } catch (Exception e) {
                                    input = "7";
                                }

                                if (showList.size() == 0) {
                                    input = "7";
                                }

                            } else if (removeInput.equalsIgnoreCase("2")) {

                                String titleRemove = "";

                                // Prompt the user for an input
                                do {
                                    System.out.print("Remove by title: ");
                                    titleRemove = in.nextLine();
                                } while (titleRemove.equalsIgnoreCase(""));

                                // Call the remove episode, if an error has occurred because the TVShow has no more episodes and is removed, then catch the error and set the input to "7" to bring the user back to the main menu
                                try {
                                    showList.get(index).removeEpisode(showList, titleRemove, showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));
                                } catch (Exception e) {
                                    input = "7";
                                }

                                if (showList.size() == 0) {
                                    input = "7";
                                }

                            } else {

                                // Call the remove episode, if an error has occurred because the TVShow has no more episodes and is removed, then catch the error and set the input to "7" to bring the user back to the main menu
                                try {
                                    showList.get(index).removeWatchedEpisodes(showList, showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));
                                } catch (Exception e) {
                                    input = "7";
                                }

                                if (showList.size() == 0) {
                                    input = "7";
                                }
                            }

                        }

                        // Sub Menu 2, Option 6
                        if (input.equalsIgnoreCase("6")) {

                            String sortInput = "";

                            // Prompt the user for an input
                            do {
                                System.out.print("Sort by episode #, title, or time? (1/2/3) ");
                                sortInput = in.nextLine();
                            } while (!(sortInput.equalsIgnoreCase("1") || sortInput.equalsIgnoreCase("2") || sortInput.equalsIgnoreCase("3")));

                            // Call different methods depending on choice
                            if (sortInput.equalsIgnoreCase("1")) {

                                showList.get(index).sortNum(showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));

                            } else if (sortInput.equalsIgnoreCase("2")) {

                                showList.get(index).sortTitle(showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));

                            } else {

                                showList.get(index).sortTime(showList.get(index).getSeasonNumbers().get(showList.get(index).getSeasonNumbers().indexOf(inputDisplaySeasonNum)));

                            }
                        }

                    } while (!input.equalsIgnoreCase("7"));

                } else {
                    System.out.println("TVShow not found.");
                }

            }

        } while (!(input.equals("3")));

        // Exit the program
        System.exit(0);
    }
}