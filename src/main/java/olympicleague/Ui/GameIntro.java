package ui;

import java.io.IOException;
import Util.GameUtils;

public class GameIntro {

    public static void showIntro() {
        System.out.println("\n\n");
        GameUtils.centerPrint("WELCOME TO LIGA OLYMPICA", 25);
        printOlympicRings();

        System.out.println("\n");
        printWarriorArt();

        System.out.println("\n");

        printAnimatedBorder("═");
        GameUtils.centerPrint("IN THE MYSTICAL REALM OF OLYMPICA", 25);
        printAnimatedBorder("═");

        System.out.println();

        GameUtils.typewriter("           Legendary warriors gather from across the realm...", 20);
        GameUtils.typewriter("           Each bearing ancient powers and unmatched skill...", 20);
        GameUtils.typewriter("           Only ONE shall claim the throne of champions!", 20);

        System.out.println("\n");
        printTournamentBanner();

        System.out.println("\n");
        printCreatorsSection();

        System.out.println("\n");
        GameUtils.centerPrint("Press ENTER to start....", 10);

        try {
            System.in.read();
        } catch (IOException e) {
            // Ignored
        }
    }

    private static void printColorfulTitle() {
        String[] title = {
                "     ╔══════════════════════════════════════════════════════════════════════════════════╗",
                "     ║                                                                                  ║",
                "     ║                      ██╗     ██╗ ██████╗  █████╗                                 ║",
                "     ║                      ██║     ██║██╔════╝ ██╔══██╗                                ║",
                "     ║                      ██║     ██║██║  ███╗███████║                                ║",
                "     ║                      ██║     ██║██║   ██║██╔══██║                                ║",
                "     ║                      ███████╗██║╚██████╔╝██║  ██║                                ║",
                "     ║                      ╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═╝                                ║",
                "     ║                                                                                  ║",
                "     ║          ██████╗ ██╗     ██╗   ██╗███╗   ███╗██████╗ ██╗ ██████╗ █████╗          ║",
                "     ║         ██╔═══██╗██║     ╚██╗ ██╔╝████╗ ████║██╔══██╗██║██╔════╝██╔══██╗         ║",
                "     ║         ██║   ██║██║      ╚████╔╝ ██╔████╔██║██████╔╝██║██║     ███████║         ║",
                "     ║         ██║   ██║██║       ╚██╔╝  ██║╚██╔╝██║██╔═══╝ ██║██║     ██╔══██║         ║",
                "     ║         ╚██████╔╝███████╗   ██║   ██║ ╚═╝ ██║██║     ██║╚██████╗██║  ██║         ║",
                "     ║              ╚═════╝ ╚══════╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝ ╚═╝╚═╝  ╚═╝         ║",
                "     ║                                                                                  ║",
                "     ╚══════════════════════════════════════════════════════════════════════════════════╝"
        };

        for (String line : title) {
            System.out.println(line);
            try { Thread.sleep(5); } catch (InterruptedException ignored) {}
        }
    }

    private static void printOlympicRings() {
        String[] rings = {
                "                         ████      ████      ████",
                "                       ██    ██  ██    ██  ██    ██",
                "                       ██    ████    ████    ████",
                "                         ████  ██  ██  ██  ██  ████",
                "                             ██████████████████    ██",
                "                               ████      ████      ██",
                "                                            ██████████"
        };

        for (String line : rings) {
            System.out.println(line);
            try { Thread.sleep(5); } catch (InterruptedException ignored) {}
        }
    }

    private static void printWarriorArt() {
        String[] warrior = {
                "                                THE ARENA AWAITS  ",
                "",
                "                                  /\\    /\\",
                "                                 /  \\__/  \\",
                "                                |   o  o   |",
                "                                |     >    |",
                "                                 \\  \\__/  /",
                "                                  |____|",
                "                               ___/|  |\\___",
                "                              /   /|  |\\   \\",
                "                             |___| |  | |___|",
                "                                  ||  ||",
                "                                  ||  ||",
                "                                 _||  ||_",
                "                                |__________|"
        };

        for (String line : warrior) {
            GameUtils.centerPrint(line, 5);
        }
    }

    private static void printTournamentBanner() {
        String[] banner = {
                "        ╔═══════════════════════════════════════════════════════════╗",
                "        ║              THE TOURNAMENT OF LEGENDS BEGINS             ║",
                "        ║                                                           ║",
                "        ║                  Victory awaits the bold                  ║",
                "        ║                  Glory favors the brave                   ║",
                "        ║               The throne demands the strongest            ║",
                "        ║                                                           ║",
                "        ╚═══════════════════════════════════════════════════════════╝"
        };

        for (String line : banner) {
            System.out.println(line);
            try { Thread.sleep(80); } catch (InterruptedException ignored) {}
        }
    }

    private static void printCreatorsSection() {
        String[] creators = {
                "               ╔═══════════════════════════════════════════╗",
                "               ║             FORGED BY LEGENDS:            ║",
                "               ╠═══════════════════════════════════════════╣",
                "               ║                                           ║",
                "               ║                   Rovpoli                 ║",
                "               ║                      kd                   ║",
                "               ║                     biji                  ║",
                "               ║                    selwyn                 ║",
                "               ║                    ronnel                 ║",
                "               ║                                           ║",
                "               ╚═══════════════════════════════════════════╝"
        };

        for (String line : creators) {
            System.out.println(line);
            try { Thread.sleep(70); } catch (InterruptedException ignored) {}
        }
    }

    private static void printAnimatedBorder(String symbol) {
        StringBuilder border = new StringBuilder("        ");
        for (int i = 0; i < 63; i++) {
            border.append(symbol);
        }

        for (char c : border.toString().toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(5); } catch (InterruptedException ignored) {}
        }
        System.out.println();
    }
}