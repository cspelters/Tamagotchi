import java.io.*; //für Speicherfunktion

public class Tamagotchi {
    private String name;
    private int hunger;
    private int hygiene;
    private int attention;
    private int health;
    private int energy;
    private boolean isAlive;
    private long lastUpdateTime;

    //Konstruktor
    public Tamagotchi(String name) {
        this.name = name;
        this.hunger = 50;
        this.hygiene = 90;
        this.attention = 90;
        this.health = 100;
        this.energy = 100;
        this.isAlive = true;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    //Getter & Setter
    //gibt Namen zurück
    public String getName() {
        return name;
    }
    public int getHunger() {
        return hunger;
    }
    public int getHygiene() {
        return hygiene;
    }
    public int getAttention() {
        return attention;
    }
    public int getHealth() {
        return health;
    }
    public int getEnergy() {
        return energy;
    }

    //ändert den Namen (damit man ihn ändern kann)
    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return isAlive; //isAlive()=Methodenaufruf - isAlive=Attribut(ohne Klammern)
    }

    public void feedMeal() {
        if (isAlive) { //Prüfung, ob es lebt
            hunger += 30; //hunger um 30 erhöhen (weniger hungrig = höherer Wert)
            if (hunger > 100) {
                hunger = 100; //falls über 100, setzen wir es auf Maximum
            }
            System.out.println(name + " hat eine Mahlzeit bekommen. \uD83C\uDF74");
        }
    }
    public void giveSnack() {
        if (isAlive) {
            hunger += 10;
            if (hunger > 100) {
                hunger = 100;
            }
            System.out.println(name + " hat einen Snack bekommen. \uD83C\uDF69");
        }
    }
    public void play() {
        if (isAlive) {
            attention += 20;
            energy -= 15;
            hunger -= 5;
            if (attention > 100) {
                attention = 100;
            }
            if (energy < 0) {
                energy = 0;
            }
            if (hunger < 0) {
                hunger = 0;
            }
            System.out.println("Du hast mit " + name + " gespielt. ⚽");
        }
    }
    public void pet() {
        if (isAlive) {
            attention += 10;
            if (attention > 100) {
                attention = 100;
            }
            System.out.println("Du hast " + name + " gestreichelt. ❤\uFE0F");
        }
    }
    public void clean() {
        if (isAlive) {
            hygiene = 100;
            System.out.println(name + " ist jetzt sauber. \uD83D\uDEC1");
        }
    }
    public void sleep() {
        if (isAlive) {
            energy = 100;
            System.out.println(name + " hat geschlafen und ist ausgeruht. \uD83C\uDF19");
        }
    }
    public void giveMedicine() {
        if (isAlive) {
            health += 30;
            if (health > 100) {
                health = 100;
            }
            System.out.println(name + " hat Medizin bekommen. \uD83D\uDC8A");
        }
    }
    public void updateNeeds() {
        if (!isAlive) {
            return; //wenn tot, nichts mehr aktualisieren
        }
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long minutesPassed = timePassed / 60000; //60000ms = 1 Minute

        if (minutesPassed > 0) {
            //Reduziere Bedürfnisse (und sicherstellen, dass sie nicht unter 0 fallen)
            hunger -= minutesPassed * 5; //war auf 0.15
            if (hunger < 0) {
                hunger = 0;
            }
            hygiene -= minutesPassed * 3; //war auf 0.10
            if (hygiene < 0) {
                hygiene = 0;
            }
            attention -= minutesPassed * 8; //war auf 0.20
            if (attention < 0) {
                attention = 0;
            }
            energy -= minutesPassed * 4; //war auf 0.15
            if (energy < 0) {
                energy = 0;
            }
            //Wenn Bedürfnisse kritisch niedrig sind, sinkt Gesundheit
            if (hunger < 20 || hygiene < 20 || attention < 20) {
                health -= minutesPassed * 10; //war auf 0.5
                if (health < 0) {
                    health = 0;
                }
            }
            //wenn Gesundheit auf 0, stirbt das Tamagotchi
            if (health <= 0) {
                isAlive = false;
                System.out.println(name + " ist gestorben... 💔");
            }
            lastUpdateTime = currentTime;
        }
    }
    public String getStatus() {
        if (!isAlive) {
            return name + " ist gestorben... 💔";
        }
        String status = "\n======== " + name + " ========\n";
        status += "Hunger: " + hunger + "/100\n";
        status += "Hygiene: " + hygiene + "/100\n";
        status += "Aufmerksamkeit: " + attention + "/100\n";
        status += "Gesundheit: " + health + "/100\n";
        status += "Energie: " + energy + "/100\n";
        status += "====================\n";

        return status;
    }
    public void save() {
        try {
            FileWriter writer = new FileWriter("tamagotchi_save.txt");
            writer.write(name + "\n");
            writer.write(String.valueOf(hunger) + "\n");
            writer.write(String.valueOf(hygiene) + "\n");
            writer.write(String.valueOf(attention) + "\n");
            writer.write(String.valueOf(health) + "\n");
            writer.write(String.valueOf(energy) + "\n");
            writer.write(isAlive + "\n");
            writer.write(String.valueOf(lastUpdateTime) + "\n");
            //auch richtig:
            //writer.write("" + hunger + "\n");
            writer.close();
            System.out.println("Spielstand gespeichert.");
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern.");
        }
    }
    public static Tamagotchi load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("tamagotchi_save.txt"));
            String name = reader.readLine();
            int hunger = Integer.parseInt(reader.readLine());
            int hygiene = Integer.parseInt(reader.readLine());
            int attention = Integer.parseInt(reader.readLine());
            int health = Integer.parseInt(reader.readLine());
            int energy = Integer.parseInt(reader.readLine());
            boolean isAlive = Boolean.parseBoolean(reader.readLine());
            long lastUpdateTime = Long.parseLong(reader.readLine());
            reader.close();

            //Tamagotchi erstellen und Werte setzen
            Tamagotchi tama = new Tamagotchi(name);
            tama.hunger = hunger;
            tama.hygiene = hygiene;
            tama.attention = attention;
            tama.health = health;
            tama.energy = energy;
            tama.isAlive = isAlive;
            tama.lastUpdateTime = lastUpdateTime;

            return tama;
        } catch (IOException e) {
            return null; //keine Speicherdatei gefunden
        }
    }



}






































