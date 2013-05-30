/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consistencyindex;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author thedadams
 */
public class YearlyTopCIs {

    public static void main(String[] args) {

        File directory = new File("Players");
        File[] files = directory.listFiles();
        String[][] players = new String[files.length - 1][3];
        File playerList = new File("Players/PlayerIDsPost1915.txt");
        ConsistencyIndexObject CI = new ConsistencyIndexObject();
        File CareerTop100OnBase, CareerTop100Hits;
        FileWriter writer;
        Scanner std1 = new Scanner("");
        String playerID, firstName, lastName;
        int findDot, numPlayers;

        for (int j = 1915; j <= 2012; j++) {

            System.out.println(j);

            CareerTop100OnBase = new File("TopLists/" + j + "TopOnBase.csv");
            CareerTop100Hits = new File("TopLists/" + j + "TopHits.csv");
            playerID = "";
            firstName = "";
            lastName = "";
            findDot = 0;
            numPlayers = 0;

            for (int i = 0; i < players.length; i++) {

                try {

                    std1 = new Scanner(playerList);
                    std1.useDelimiter(",");

                } catch (FileNotFoundException e) {
                    System.out.println("Broken.");
                    return;
                }


                if (files[i].getName().equals("PlayerIDsPost1915.txt")) {

                    continue;

                }

                findDot = files[i].getName().lastIndexOf(".");
                playerID = files[i].getName().substring(findDot - 8, findDot);

                while (true) {

                    lastName = std1.next();
                    firstName = std1.next();

                    if (std1.next().equals(playerID)) {

                        break;

                    }

                    std1.nextLine();

                }

                std1.close();

                CI.Calculate(firstName, lastName, Integer.toString(j), Integer.toString(j), playerID);

                if (CI.pa < 500 || CI.error) {

                    continue;

                }

                players[numPlayers][0] = firstName + " " + lastName;
                players[numPlayers][1] = Double.toString(CI.onBaseCI);
                players[numPlayers][2] = Double.toString(CI.hitCI);

                numPlayers++;

            }

            try {

                writer = new FileWriter(CareerTop100OnBase);
                Arrays.sort(players, 0, numPlayers, new TwoDArrayCompare(1));

                writer.write("PlayerName" + ",");
                writer.write("On Base CI" + ",");
                writer.write("Hit CI" + "\n");

                for (int i = 0; i < numPlayers; i++) {

                    writer.write(players[i][0] + ",");
                    writer.write(players[i][1] + ",");
                    writer.write(players[i][2] + "\n");

                }
                writer.close();
                writer = new FileWriter(CareerTop100Hits);
                Arrays.sort(players, 0, numPlayers, new TwoDArrayCompare(2));

                writer.write("PlayerName" + ",");
                writer.write("On Base CI" + ",");
                writer.write("Hit CI" + "\n");

                for (int i = 0; i < numPlayers; i++) {

                    writer.write(players[i][0] + ",");
                    writer.write(players[i][1] + ",");
                    writer.write(players[i][2] + "\n");

                }

                writer.close();

            } catch (IOException e) {
            }

        }

    }
}
