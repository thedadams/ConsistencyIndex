/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 *
 * @author thedadams
 */
public class GetPlayerInfo {

    public static String[][] main(String playerFirstName, String playerLastName, String yearStart, String yearEnd) {

        String playerID = "", playerDebut = "", tmp, currentGame = "";
        boolean didPlay = false;
        int year, gameCounter = 0;
        String[][] games = new String[3560][5];
        int ab = 0, pa = 0, hits = 0, bb = 0, currentPA;
        File playerList = new File("CompiledFiles/PlayerIDs.txt");
        Scanner std;

        try {
            std = new Scanner(playerList);
            std.useDelimiter(",");
        } catch (FileNotFoundException e) {

            System.out.println("File not found.");
            return null;

        }

        while (std.hasNextLine()) {

            if (std.next().equals(playerLastName)) {

                if (std.next().equals(playerFirstName)) {

                    playerID = std.next();
                    playerDebut = std.nextLine();
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

            year = Integer.parseInt(playerDebut.substring(7, 11));

        } else {

            year = Integer.parseInt(yearStart);

        }

        if (yearEnd.equals("0") || yearEnd.equals("")) {

            yearEnd = String.valueOf(Double.POSITIVE_INFINITY);

        }

        File directory = new File("CompiledFiles/" + year);

        while (directory.exists()) {

            File[] files = directory.listFiles();

            for (int i = 0; i < files.length; i++) {

                try {

                    std = new Scanner(files[i]);
                    std.useDelimiter(",");

                } catch (FileNotFoundException e) {

                    System.out.println("File not found.");
                    return null;

                }

                tmp = std.next();
                while (true) {

                    if (std.next().equals("\"" + playerID + "\"")) {

                        currentGame = tmp;
                        didPlay = true;
                        currentPA = std.nextInt();
                        if (std.next().equals("\"F\"")) {
                            std.nextLine();
                            if (std.hasNextLine()) {

                                tmp = std.next();

                            } else {

                                break;

                            }
                            continue;
                        }
                        pa++;
                        if (currentPA >= 20 && currentPA != 24) {

                            hits++;

                        } else if (currentPA >= 14 && currentPA <= 17) {

                            bb++;

                        }

                        if (std.nextLine().equals(",\"T\"")) {

                            ab++;

                        }

                    } else {

                        std.nextLine();

                    }

                    if (std.hasNextLine()) {

                        tmp = std.next();

                    } else {

                        break;

                    }

                    if (didPlay && !(tmp.equals(currentGame))) {

                        games[gameCounter][0] = currentGame;
                        games[gameCounter][1] = String.valueOf(pa);
                        games[gameCounter][2] = String.valueOf(ab);
                        games[gameCounter][3] = String.valueOf(hits);
                        games[gameCounter][4] = String.valueOf(bb);
                        gameCounter++;
                        pa = ab = hits = bb = 0;
                        didPlay = false;

                    }

                }

                std.close();

            }

            year++;
            if (Double.parseDouble(yearEnd) < (double) year) {

                break;

            }

            directory = new File("CompiledFiles/" + year);

        }

        Arrays.sort(games, 0, gameCounter, new DateCompare());

        return games;

    }
}
