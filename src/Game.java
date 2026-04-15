import java.util.Scanner;

public class Game {
    private Tamagotchi tamagotchi;
    private Scanner scanner;
    private boolean running;

    //Konstruktor
    public Game() {
        scanner = new Scanner(System.in);
        running = true;
    }

    public void start() {
        System.out.println("=== Willkommen bei deinem Tamagotchi! ===");

        //Versuche gespeichertes Tamagotchi zu laden
        tamagotchi = Tamagotchi.load();

        if (tamagotchi != null) {
            System.out.println("Gespeichertes Tamagotchi gefunden.");
            tamagotchi.updateNeeds(); //Zeit seit letztem Speichern berechnen
            System.out.println("Willkommen zurück, " + tamagotchi.getName() + "! \uD83D\uDC23");
        } else {
            System.out.println("Gib deinem Tamagotchi einen Namen: ");
            String name = scanner.nextLine();
            tamagotchi = new Tamagotchi(name);
            System.out.println("\n" + name + " wurde geboren! \uD83D\uDC23");
        }
        gameLoop();
    }
    //Hauptspielschleife
    private void gameLoop() {
        while (running && tamagotchi.isAlive()) {
            tamagotchi.updateNeeds();
            showMenu();
            handleInput();
        }
        if (!tamagotchi.isAlive()) {
            System.out.println("\n💔 Game Over - " + tamagotchi.getName() + " ist gestorben.");
            tamagotchi.save();
        }
        scanner.close();
    }
    private void showMenu() {
        System.out.println(tamagotchi.getStatus());
        System.out.println("""
                Was möchtest du tun?
                1 - Mahlzeit geben
                2 - Snack geben
                3 - Spielen
                4 - Streicheln
                5 - Waschen
                6 - Schlafen legen
                7 - Medizin geben
                8 - Namen ändern
                0 - Spiel beenden
                Deine Wahl:""");
    }
    private void handleInput() {
        int choice = scanner.nextInt();
        scanner.nextLine(); //leere Zeile entfernen

        switch (choice) {
            case 1:
                tamagotchi.feedMeal();
                break;
            case 2:
                tamagotchi.giveSnack();
                break;
            case 3:
                tamagotchi.play();
                break;
            case 4:
                tamagotchi.pet();
                break;
            case 5:
                tamagotchi.clean();
                break;
            case 6:
                tamagotchi.sleep();
                break;
            case 7:
                tamagotchi.giveMedicine();
                break;
            case 8:
                System.out.println("Neuer Name:");
                String newName = scanner.nextLine();
                tamagotchi.setName(newName);
                System.out.println("Name geändert zu: " + newName);
                break;
            case 0:
                tamagotchi.save();
                running = false;
                System.out.println("Auf Wiedersehen");
                break;
            default:
                System.out.println("Ungültige Eingabe.");
                break;
        }
    }
}
