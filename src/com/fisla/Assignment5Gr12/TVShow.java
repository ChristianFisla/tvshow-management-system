// Christian Fisla

package com.fisla.Assignment5Gr12;

import java.io.*;
import java.util.*;

public class TVShow implements Comparable<TVShow> {

    private String title, genre;
    private int seasonForEpisode, totalEpisodes;
    private int totalSeasons = 0;
    private Time time;

    private ArrayList<Integer> seasonNumbers = new ArrayList<>(); // Keeps track of the season numbers
    private ArrayList<Episode> episodes = new ArrayList<>(); // ArrayList of Episode Objects

    private Scanner in = new Scanner(System.in);
    private String input;

    private static int totalShows = 0;

    // Desc: TVShow object Constructor. Takes in attributes and creates the TVShow object
    // Param: The Scanner Object to parse the input file
    // Return: n/a
    public TVShow (Scanner inFile) throws IOException {

        // Take in first 2 lines as title and genre guaranteed
        this.title = inFile.nextLine();
        this.genre = inFile.nextLine();

        // Loop through groups of inputs
        while (inFile.hasNextLine()) {

            String tempLine = inFile.nextLine();

            // If the next line is giving us info about the season, take in as such and take the next in as the total episodes
            if (tempLine.substring(0, 6).equalsIgnoreCase("season")) {

                totalSeasons++;

                this.seasonForEpisode = Integer.parseInt(tempLine.substring(tempLine.length() - 1));

                seasonNumbers.add(seasonForEpisode);

                this.totalEpisodes = Integer.parseInt(inFile.nextLine());

                // Take in the episode number and parse it for just the integer
                String episodeTemp = inFile.nextLine();
                int epNumber = Integer.parseInt(episodeTemp.substring(8));

                // Create new Episode object with the next three lines
                episodes.add(new Episode(epNumber, inFile.nextLine(), inFile.nextLine(), false, seasonForEpisode));

            } else {

                // Take in the episode number and parse it for just the integer
                int epNumber = Integer.parseInt(tempLine.substring(8));

                // Create new Episode object with the next three lines
                episodes.add(new Episode(epNumber, inFile.nextLine(), inFile.nextLine(), false, seasonForEpisode));

            }

        }

        totalShows++;

        time = new Time(0, 0, 0);

        // Add up the total time of every Time object to display on the
        for (int i = 0; i < episodes.size(); i++) {
            time.addTime(episodes.get(i).getTime());
        }
    }
    // Overloaded method for the purpose of defining key attributes for Collections.binarySearch()

    // Desc: Secondary TVShow Constructor for the purpose of using it for binary search.
    // Param: isKey with is used to differentiate between Constructors, and the title
    // Return: n/a
    public TVShow (boolean isKey, String title) {
        this.title = title;
    }
    // Desc: Display all the episodes within the chosen show and season
    // Param: The current season the user has chosen
    // Return: n/a
    public void displayEpisodes(int seasonAccess) {

        // If no episodes, alert the user and skip
        if (episodes.size() == 0) {

            System.out.printf("%nThere are no episodes in this show.%n");

        } else {

            System.out.println("Episode List");
            System.out.println("----------------");

            int count = 0;

            // Loop through all episodes
            for (int i = 0; i < episodes.size(); i++) {

                // Only print the Episode if it is in the desired season
                if (episodes.get(i).getSeason() == seasonAccess) {
                    System.out.println((count + 1) + ". " + episodes.get(i).getTitle());
                    count++;
                }
            }
        }

    }

    // Desc: Display a single episode of the user's choice
    // Param: The current season the user has chosen
    // Return: n/a
    public void displayEpisodeSingle(int seasonAccess) {

        // Ask the user what they'd like to search for
        System.out.print("What episode would you like to get info on? (Search by title) ");
        input = in.nextLine();

        // Create a new ArrayList with only the episodes in the season
        ArrayList<Episode> onlyEpisodesInSeason = new ArrayList<>();

        // Loop through and add only the episodes in the season
        for (int i = 0; i < episodes.size(); i++) {
            if (episodes.get(i).getSeason() == seasonAccess) {
                onlyEpisodesInSeason.add(episodes.get(i));
            }
        }

        // Sort list
        Collections.sort(onlyEpisodesInSeason, new SortByTitleEpisode());

        // Search for their input
        int index = Collections.binarySearch(onlyEpisodesInSeason, new Episode(true, input), new SortByTitleEpisode());

        // If found, print out the singular episode
        if (index >= 0) {

            System.out.println(onlyEpisodesInSeason.get(index));

        } else {
            System.out.println("Episode not found.");
        }

    }

    // Desc: Create an episode
    // Param: The Scanner Object to prompt for input and the season choice
    // Return: n/a
    public void createEpisode(Scanner in, int currentSeason) {

        // Ask for the title
        String titleInput;
        do {
            System.out.print("Title: ");
            titleInput = in.nextLine();
        } while (titleInput.equalsIgnoreCase(""));

        String epNum;
        int epNumInt = 0;
        boolean validInput;

        // Prompt for the episode #
        do {
            try {
                System.out.print("Episode #: ");
                epNum = in.nextLine();
                epNumInt = Integer.parseInt(epNum);
                validInput = true;
            } catch (NumberFormatException e) {
                validInput = false;
            }
        } while (!validInput);

        String length = "";
        boolean validTimeInput;

        // Prompt for the length
        do {
            // Try to create the Time object with the given input, loop if the input is invalid.
            try {
                System.out.print("Length: ");
                length = in.nextLine();

                StringTokenizer tS = new StringTokenizer(length, ":");
                new Time(Integer.parseInt(tS.nextToken()), Integer.parseInt(tS.nextToken()), Integer.parseInt(tS.nextToken()));
                validTimeInput = true;
            } catch (Exception e) {
                validTimeInput = false;
            }
        } while (!validTimeInput);

        // Add the new episode to the list
        episodes.add(new Episode(epNumInt, titleInput, length, false, currentSeason));

    }

    // Desc: Remove an episode within the desired season
    // Param: The list of TVShows, the episode number to be removed, and the current season
    // Return: n/a
    public void removeEpisode(ArrayList<TVShow> showList, int episodeNumber, int currentSeason) {
        int max = episodes.size();

        for (int i = 0; i < max; i++) {
            if (episodes.get(i).getEpisode() == episodeNumber) {
                if (episodes.get(i).getSeason() == currentSeason)

                    // Reduce the time when an episode is removed
                    this.time.subTime(episodes.get(i).getTime());

                    episodes.remove(i);
                    max--;
            }
        }

        if (episodes.size() == 0) {
            showList.remove(this);
        }

        checkRemainingInSeason(currentSeason);

    }
    // Desc: Remove a range of episodes within the desired season
    // Param: The list of TVShows, the starting value in the range, the ending value in the range, and the current season
    // Return: n/a
    public void removeEpisode(ArrayList<TVShow> showList, int start, int end, int currentSeason) {

        if (start == end) {
            removeEpisode(showList, start, currentSeason);
        }

        int max = episodes.size();

        for (int i = 0; i < max; i++) {
            if (episodes.get(i).getSeason() == currentSeason) {
                if (episodes.get(i).getEpisode() >= start && episodes.get(i).getEpisode() <= end) {

                    // Reduce the time when an episode is removed
                    this.time.subTime(episodes.get(i).getTime());

                    episodes.remove(i);
                    max--;
                    i--;
                }
            }
        }

        if (episodes.size() == 0) {
            showList.remove(this);
        }

        checkRemainingInSeason(currentSeason);
    }
    // Desc: Remove an episode with a given title
    // Param: The list of TVShows, the title to remove by, and the current season
    // Return: n/a
    public void removeEpisode(ArrayList<TVShow> showList, String title, int currentSeason) {
        int max = episodes.size();

        for (int i = 0; i < max; i++) {
            if (episodes.get(i).getTitle().equalsIgnoreCase(title)) {
                if (episodes.get(i).getSeason() == currentSeason)

                    // Reduce the time when an episode is removed
                    this.time.subTime(episodes.get(i).getTime());

                    episodes.remove(i);
                    max--;
                    i--;
            }
        }

        if (episodes.size() == 0) {
            showList.remove(this);
        }

        checkRemainingInSeason(currentSeason);
    }
    // Desc: Remove all watched episodes
    // Param: The list of TVShows and the current season
    // Return: n/a
    public void removeWatchedEpisodes(ArrayList<TVShow> showList, int currentSeason) {
        int max = episodes.size();

        for (int i = 0; i < max; i++) {
            if (episodes.get(i).getIsWatched()) {
                if (episodes.get(i).getSeason() == currentSeason)

                    // Reduce the time when an episode is removed
                    this.time.subTime(episodes.get(i).getTime());

                    episodes.remove(i);
                    max--;
                    i--;
            }
        }

        if (episodes.size() == 0) {
            showList.remove(this);
        }

        checkRemainingInSeason(currentSeason);
    }
    // Desc: Reduce the amount seasons in a TVShow by one
    // Param: n/a
    // Return: n/a
    public void reduceSeason() {
        this.totalSeasons--;
    }
    // Desc: Sort the episodes by episode #
    // Param: The current season
    // Return: n/a
    public void sortNum(int seasonAccess) {
        Collections.sort(episodes, new SortByNumEpisode());

        System.out.println("");
        displayEpisodes(seasonAccess);
    }
    // Desc: Sort the episodes by title
    // Param: The current season
    // Return: n/a
    public void sortTitle(int seasonAccess) {
        Collections.sort(episodes, new SortByTitleEpisode());

        System.out.println("");
        displayEpisodes(seasonAccess);
    }
    // Desc: Sort the episodes by duration
    // Param: The current season
    // Return: n/a
    public void sortTime(int seasonAccess) {
        Collections.sort(episodes, new SortByTimeEpisode());

        System.out.println("");
        displayEpisodes(seasonAccess);
    }

    // Desc: Check if there are any episodes left in a given season
    // Param: The current season
    // Return: n/a
    public void checkRemainingInSeason(int currentSeason) {
        int epsRemainingInSeason = 0;

        // Loop through all episodes and count those that are remaining in the desired season
        for (int i = 0; i < episodes.size(); i++) {
            if (episodes.get(i).getSeason() == currentSeason) {
                epsRemainingInSeason++;
            }
        }

        // If no episodes remain, remove the season and adjust attributes accordingly
        if (epsRemainingInSeason == 0) {
            seasonNumbers.remove(seasonNumbers.indexOf(currentSeason));
            reduceSeason();
        }
    }

    // GETTERS AND SETTERS
    public String getTitle() {
        return this.title;
    }
    public int getTotalEpisodes() {
        return this.totalEpisodes;
    }
    public int getTotalSeasons() {
        return this.totalSeasons;
    }
    public ArrayList<Episode> getEpisodeList() {
        return this.episodes;
    }
    public ArrayList<Integer> getSeasonNumbers() {
        return this.seasonNumbers;
    }
    public Time getTime() { return this.time; }

    // toString()
    public String toString() {
        return String.format("%nTitle: %s%nGenre: %s%nSeasons: %d%n", this.title, this.genre, this.totalSeasons) + time.toString();
    }

    // Specify the default sorting method
    @Override
    public int compareTo(TVShow t) {
        return this.title.compareTo(t.title);
    }
}