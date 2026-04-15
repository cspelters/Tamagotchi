import javax.swing.*;
import java.awt.*;

public class TamagotchiGUI extends JFrame{ //die Klasse ist ein Fenster
    private Tamagotchi tamagotchi;

    //GUI-Komponenten - Attribute
    private JLabel nameLabel; //Text anzeigen
    private JProgressBar hungerBar; //Balken für die Bedürfnisse
    private JProgressBar hygieneBar;
    private JProgressBar attentionBar;
    private JProgressBar healthBar;
    private JProgressBar energyBar;
    private JLabel statusLabel;
    private Timer timer;
    private Timer scrollTimer;
    private String currentMessage = "";
    private int scrollPosition = -1;


    public TamagotchiGUI() {
        //Tamagotchi laden oder neu erstellen
        tamagotchi = Tamagotchi.load();
        if (tamagotchi == null) {
            String name = JOptionPane.showInputDialog("Gib deinem Tamagotchi einen Namen:"); //Popup für Eingabe
            tamagotchi = new Tamagotchi(name);
        }
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Tamagotchi");
        setSize(500,600);
//        setLocationRelativeTo(null); //zentriert auf Hauptmonitor
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        if (screens.length > 1) {
            // Öffne auf zweitem Monitor (Index 1)
            setLocation(screens[1].getDefaultConfiguration().getBounds().x, 100);
        } else {
            setLocationRelativeTo(null);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() { //WindowListener reagiert auf Fenster-Events
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) { //wird aufgerufen bevor Fenster schließt
                tamagotchi.save();
            }
        });
        setLayout(new BorderLayout()); //Layout-Manager für's Hauptfenster

        //Hauptpanel erstellen
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //Komponenten untereinander anordnen
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //Unsichtbarer Rand

        //Name anzeigen
        JPanel namePanel = new JPanel(new BorderLayout());
        nameLabel = new JLabel("Name: " + tamagotchi.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        namePanel.add(nameLabel, BorderLayout.WEST);
        mainPanel.add(namePanel);

        //Hunger-Bar
        hungerBar = new JProgressBar(0, 100);
        hungerBar.setStringPainted(true);
        mainPanel.add(createStatusBar("Hunger: ", hungerBar));


        //Hygiene-Bar
        hygieneBar = new JProgressBar(0,100);
        hygieneBar.setStringPainted(true);
        mainPanel.add(createStatusBar("Hygiene: ", hygieneBar));

        //Aufmerksamkeits-Bar
        attentionBar = new JProgressBar(0, 100);
        attentionBar.setStringPainted(true);
        mainPanel.add(createStatusBar("Aufmerksamkeit: ", attentionBar));

        //Gesundheits-Bar
        healthBar = new JProgressBar(0, 100);
        healthBar.setStringPainted(true);
        mainPanel.add(createStatusBar("Gesundheit: ", healthBar));

        //Energie-Bar
        energyBar = new JProgressBar(0, 100);
        energyBar.setStringPainted(true);
        mainPanel.add(createStatusBar("Energie: ", energyBar));

//        alt
//        JPanel hungerPanel = new JPanel(new BorderLayout());
//        hungerPanel.add(new JLabel("Hunger: "), BorderLayout.WEST);
//        hungerBar = new JProgressBar(0, 100); //Balken von 0-100
//        hungerBar.setStringPainted(true); //zeigt Wert im Balken an
//        hungerPanel.add(hungerBar, BorderLayout.CENTER);
//        mainPanel.add(hungerPanel);
//        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(Box.createVerticalStrut(20)); //größerer Abstand

        //Obere Buttons (3x2)
        JPanel topButtonPanel = new JPanel(new GridLayout(2, 3, 10, 10)); //Zeilen, Spalten, px Abstand

        JButton feedButton = new JButton("\uD83C\uDF74  Füttern");
        feedButton.addActionListener(e -> { tamagotchi.feedMeal(); updateDisplay(); });

        JButton snackButton = new JButton("\uD83C\uDF69  Snack");
        snackButton.addActionListener(e -> { tamagotchi.giveSnack(); updateDisplay(); });

        JButton playButton = new JButton("⚽  Spielen");
        playButton.addActionListener(e -> { tamagotchi.play(); updateDisplay(); });

        JButton petButton = new JButton("❤\uFE0F  Streicheln");
        petButton.addActionListener(e -> { tamagotchi.pet(); updateDisplay(); });

        JButton cleanButton = new JButton("\uD83D\uDEC1  Waschen");
        cleanButton.addActionListener(e -> { tamagotchi.clean(); updateDisplay(); });

        JButton sleepButton = new JButton("\uD83C\uDF19  Schlafen");
        sleepButton.addActionListener(e -> { tamagotchi.sleep(); updateDisplay(); });

        topButtonPanel.add(feedButton);
        topButtonPanel.add(snackButton);
        topButtonPanel.add(playButton);
        topButtonPanel.add(petButton);
        topButtonPanel.add(cleanButton);
        topButtonPanel.add(sleepButton);

        //Medizin-Button zentriert
        JPanel medicinePanel = new JPanel((new GridLayout(1, 3, 10, 0)));
        medicinePanel.add(new JLabel("")); //leerer Platz links
        JButton medicineButton = new JButton("\uD83D\uDC8A  Medizin");
        medicineButton.addActionListener(e -> { tamagotchi.giveMedicine(); updateDisplay(); });
        medicinePanel.add(medicineButton);
        medicinePanel.add(new JLabel("")); //leerer Platz rechte

        //Untere Buttons (Name ändern & Neustart)
        JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JButton nameButton = new JButton("\uD83D\uDD8A\uFE0F  Name ändern");
        nameButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Neuer Name: ");
            if (newName != null && !newName.trim().isEmpty()) {
                tamagotchi.setName(newName);
                updateDisplay();
            }
        });

        JButton restartButton = new JButton("\uD83D\uDD04 Neustart");
        restartButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Wirklich neu starten? " +
                    "Dein aktuelles Tamagotchi geht verloren! Neustart bestätigen");
            if (confirm == JOptionPane.YES_OPTION) {
                String name = JOptionPane.showInputDialog("Gib deinem neuen Tamagotchi einen Namen:");
                if (name != null && !name.trim().isEmpty()) {
                    tamagotchi = new Tamagotchi(name);
                    updateDisplay();
                    timer.restart();
                }
            }
        });
        bottomButtonPanel.add(nameButton);
        bottomButtonPanel.add(restartButton);

//        JButton emptyButton = new JButton("");
//        emptyButton.setEnabled(false); // Button deaktivieren
//        emptyButton.setVisible(false);

        //Alle Buttons zum Hauptpanel hinzufügen
        mainPanel.add(topButtonPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(medicinePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(bottomButtonPanel);

        //Status-Panel mit voller Breite
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25)); //volle Breite erzwingen

        //Status-Label ganz unten
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setPreferredSize(new Dimension(464, 20)); //Feste Breite für Label

        statusLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel);

        add(mainPanel, BorderLayout.CENTER);
        updateDisplay();
        //Timer für automatische Updates (alle 5 Sekunden)
        timer = new Timer(5000, e -> {
            tamagotchi.updateNeeds();
            updateDisplay();
            checkIfAlive();
        });
        timer.start();

        //Scroll-Timer für Lauftext (alle 150ms)
        scrollPosition = 0;

        scrollTimer = new Timer(150, e -> {
            if (currentMessage.length() > 0) {
                int labelWidth = 64; //Wie viele Zeichen ins Label passen

                //Gesamttext: Leerzeichen + Message
                String fullText = " ".repeat(labelWidth) + currentMessage;

                //Berechne was angezeigt wird
                String display = "";
                int endPos = scrollPosition + labelWidth;

                if (scrollPosition < fullText.length()) {
                    display = fullText.substring(scrollPosition, Math.min(endPos, fullText.length()));
                }
                //Auffüllen mit Leerzeichen rechts falls nötig
                while (display.length() < labelWidth) {
                    display += " ";
                }
                statusLabel.setText(display);
                scrollPosition ++;

                //Wenn komplett durchgelaufen, neu starten
                if (scrollPosition >= fullText.length()) {
                    scrollPosition = 0;
                }
            }
        });
        scrollTimer.start();

        setVisible(true);
    }

    private void updateDisplay() {
        //Werte aus Tamagotchi holen und anzeigen
        hungerBar.setValue(tamagotchi.getHunger());
        updateBarColor(hungerBar, tamagotchi.getHunger());

        hygieneBar.setValue(tamagotchi.getHygiene());
        updateBarColor(hygieneBar, tamagotchi.getHygiene());

        attentionBar.setValue(tamagotchi.getAttention());
        updateBarColor(attentionBar, tamagotchi.getAttention());

        healthBar.setValue(tamagotchi.getHealth());
        updateBarColor(healthBar, tamagotchi.getHealth());

        energyBar.setValue(tamagotchi.getEnergy());
        updateBarColor(energyBar, tamagotchi.getEnergy());

        nameLabel.setText("Name: " + tamagotchi.getName());

        updateStatusMessage();
    }
    private void checkIfAlive() {
        if (!tamagotchi.isAlive()) {
            timer.stop();
            JOptionPane.showMessageDialog(this,tamagotchi.getName() + " ist gestorben... \uD83D\uDC94 Game Over!");
        }
    }
    private JPanel createStatusBar(String labelText, JProgressBar progressBar) {
        JPanel panel = new JPanel(new BorderLayout(5, 0)); //5px Abstand zwischen Label & Bar
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); //Ränder

        progressBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.DARK_GRAY;
            }

            protected Color getSelectionForeground() {
                return Color.DARK_GRAY;
            }
        });

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 20)); //Feste Breite: 120px

        panel.add(label, BorderLayout.WEST);
        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }
    //Helper-Methode
    private void updateBarColor(JProgressBar bar, int value) {
        if (value >= 70) {
            bar.setForeground(new Color(76, 175, 80)); //grün
        } else if (value >= 40) {
            bar.setForeground(new Color(255, 193, 7)); //gelb
        } else {
            bar.setForeground(new Color(244, 67, 54)); //rot
        }
    }
    private void updateStatusMessage() {
        if (!tamagotchi.isAlive()) {
            currentMessage = "\uD83D\uDC94 " + tamagotchi.getName() + " ist gestorben...";
            return;
        }
        //Prüfe kritische Werte
        if (tamagotchi.getHunger() < 30) {
            currentMessage = "~~~~~ \uD83C\uDF74 " + tamagotchi.getName() + " hat Hunger! ~~~~~";
        } else if (tamagotchi.getAttention() < 30) {
            currentMessage = "~~~~~ \uD83D\uDE41 " + tamagotchi.getName() + " fühlt sich einsam! ~~~~~";
        } else if (tamagotchi.getEnergy() < 30) {
            currentMessage = "~~~~~ \uD83D\uDCA4 " + tamagotchi.getName() + " ist müde! ~~~~~";
        } else if (tamagotchi.getHygiene() < 30) {
            currentMessage = "~~~~~ 💩 " + tamagotchi.getName() + " ist dreckig! ~~~~~";
        } else if (tamagotchi.getHealth() < 60) {
            currentMessage = "~~~~~ \uD83D\uDC8A " + tamagotchi.getName() + " ist krank! ~~~~~";
        } else {
            currentMessage = "~~~~~ ☺\uFE0F " + tamagotchi.getName() + " fühlt sich gut! ~~~~~";
        }
    }
}
