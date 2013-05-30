/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 *
 * @author thedadams
 */
public class GetPlayerInfo {

    public static String[][] main(String playerFirstName, String playerLastName, String yearStart, String yearEnd, String hasPlayerID) {

        String playerID = "", playerDebut = "", tmp;
        int gameCounter = 0;
        String[][] games = new String[3560][5];
        File playerList = new File("Players/PlayerIDsPost1915.txt");
        String previousGame = "";
        Scanner std;

        try {
            std = new Scanner(playerList);
            std.useDelimiter(",");
        } catch (FileNotFoundException e) {

            System.out.println("Player list file not found.");
            return null;

        }

        while (std.hasNextLine()) {

            if (std.next().equals(playerLastName)) {

                if (std.next().equals(playerFirstName)) {

                    if (hasPlayerID.equals("")) {

                        playerID = std.next();

                    } else {

                        std.next();
                        playerID = hasPlayerID;

                    }

                    playerDebut = std.nextLine();
                    if (hasPlayerID.equals("") && std.next().equals(playerLastName) && std.next().equals(playerFirstName)) {

                        System.out.println(hasPlayerID);
                        ArrayList<String> extraPlayerDebuts = new ArrayList<String>(0);
                        ArrayList<String> extraPlayerIDs = new ArrayList<String>(0);
                        extraPlayerIDs.add(playerID);
                        extraPlayerDebuts.add(playerDebut.substring(1, 11));
                        extraPlayerIDs.add(std.next());
                        extraPlayerDebuts.add(std.nextLine().substring(1, 11));
                        while (std.hasNextLine() && std.next().equals(playerLastName) && std.next().equals(playerFirstName)) {

                            extraPlayerIDs.add(std.next());
                            extraPlayerDebuts.add(std.nextLine().substring(1, 11));

                        }

                        Object[] extraPlayerDebutsArray = extraPlayerDebuts.toArray();
                        String pickedPlayer = (String) JOptionPane.showInputDialog(null, "Multiple players found.  Pick a debut data for the player you want.", "Input", JOptionPane.INFORMATION_MESSAGE, null, extraPlayerDebutsArray, extraPlayerDebutsArray[0]);
                        playerDebut = "," + pickedPlayer;
                        playerID = extraPlayerIDs.get(extraPlayerDebuts.indexOf(pickedPlayer));
                        System.out.println(playerID + " " + playerDebut);

                    }

                    break;

                }

            }

            std.nextLine();

        }

        if (playerID.equals("")) {

            System.out.println("Player you are looking for does not exists");
            return null;

        }

        std.close();

        if (yearStart.equals("0") || yearStart.equals("")) {

            yearStart = playerDebut.substring(7, 11);

        }

        if (yearEnd.equals("0") || yearEnd.equals("")) {

            yearEnd = String.valueOf(Double.POSITIVE_INFINITY);

        }

        File directory = new File("Players/" + playerFirstName + playerLastName + playerID + ".dat");

        try {

            std = new Scanner(directory);
            std.useDelimiter(",");

        } catch (FileNotFoundException e) {

            System.out.println("Player file for " + playerFirstName + " " + playerLastName + " with player ID " + playerID + " not found.");
            return null;

        }

        while (std.hasNextLine()) {

            tmp = std.next();

            if (Integer.parseInt(yearStart) > Integer.parseInt(tmp.substring(0, 4))) {

                tmp = std.nextLine();
                if (!std.hasNextLine()) {

                    std.close();
                    return null;

                }
                continue;

            }

            if (previousGame.equals(tmp)) {

                tmp = std.nextLine();
                continue;

            }

            if (Double.parseDouble(yearEnd) < Double.parseDouble(tmp.substring(0, 4))) {

                break;

            }

            games[gameCounter][0] = tmp;
            games[gameCounter][1] = std.next();
            games[gameCounter][2] = std.next();
            games[gameCounter][3] = std.next();
            games[gameCounter][4] = std.nextLine().substring(1, 2);

            previousGame = tmp;

            gameCounter++;

        }

        if (games[0][0] == null) {

            std.close();
            return null;

        }

        Arrays.sort(games, 0, gameCounter, new TwoDArrayCompare());

        std.close();

        return games;

    }
}
