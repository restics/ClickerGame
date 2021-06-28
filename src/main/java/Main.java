import javax.swing.*;
import java.util.ArrayList;


/**
 * ClickerGame by Michael Liu
 * Instructions: run the program, and start by clicking on the first button.
 * Every time you click, you gain a certain amount of coins.
 * Spend those coins in the shop.
 * You can upgrade the multiplier as well as the bonus.
 *
 * (note: i should've made everything in main to be static . - .
 */

public class Main extends JFrame implements Runnable{

    private final int baseBonusAmount = 1;

    private double coins;
    private int coinsBonus;
    private double coinsMult;
    private Thread gameThread;
    private boolean isRunning;
    public static Main frame;

    JLabel balanceLabel;

    private ArrayList<AutoMiner> minerList;
    private AutoMiner equippedMiner;
    private JLabel showCoinAmount;
    private JLabel showCooldown ;
    private JComboBox<AutoMiner> listOfMiners;

    private JPanel panel;


    //initialize variables
    public Main(){
        super("Clicker Game");
        coins = 0;
        coinsMult = 1;
        coinsBonus = 0;
        panel = new JPanel();
        minerList = new ArrayList<>();
        isRunning = true;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel);

        placeStuff();
    }

    // put all the buttons in
    public void placeStuff(){
        balanceLabel = new JLabel("Balance: " + coins);

        JButton button = new JButton("Click for coins!");
        JButton shop = new JButton("Shop");
        button.addActionListener(e -> {
            coins += (baseBonusAmount + coinsBonus) * coinsMult;
            balanceLabel.setText("Balance: " + coins);
            Shop.updateLabels();
        });

        shop.addActionListener(e ->{
            Shop.openShop();
        });


        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);
        panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        panel.add(balanceLabel);
        panel.add(button);
        panel.add(shop);
        JPanel autoMinerPanel = createAutoMinerPanel();
        panel.add(autoMinerPanel);
        this.pack();
    }

    public void addNewAutoMiner(){
        AutoMiner testam = new AutoMiner();
        testam.registerMain(this);
        listOfMiners.addItem(testam); // test item
        updateAutoMinerPanel();
    }

    public void updateAutoMinerPanel(){
        if (equippedMiner == null){
            showCoinAmount.setText("Coins per load: NONE");
            showCooldown.setText("Seconds per Load: NONE");
        }
        else{
            showCoinAmount.setText("Coins per load: " + Math.round(equippedMiner.getCoinAmount() * 10) / 10.0);
            showCooldown.setText("Seconds per Load:"  + Math.round(equippedMiner.getCooldown() * 10) / 10.0);
        }
    }

    public JPanel createAutoMinerPanel(){
        JPanel amPanel = new JPanel();
        JLabel titleLabel = new JLabel("AutoMiner settings");
        JButton removeCurrent = new JButton("Delete equipped AutoMiner");
        listOfMiners = new JComboBox<>();
        listOfMiners.setModel(new DefaultComboBoxModel<>());
        for (AutoMiner am : minerList){
            listOfMiners.addItem(am);
        }

        showCoinAmount = new JLabel("Coins per load: NONE");
        showCooldown = new JLabel("Seconds per Load: NONE");

        equippedMiner = (AutoMiner) listOfMiners.getSelectedItem();
        updateAutoMinerPanel();

        BoxLayout bl = new BoxLayout(amPanel, BoxLayout.Y_AXIS);

        amPanel.setLayout(bl);
        amPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        amPanel.add(titleLabel);
        amPanel.add(listOfMiners);
        amPanel.add(showCoinAmount);
        amPanel.add(showCooldown);
        amPanel.add(removeCurrent);

        listOfMiners.addActionListener(e -> {
            equippedMiner = (AutoMiner) listOfMiners.getSelectedItem();
            updateAutoMinerPanel();
        });
        removeCurrent.addActionListener(e -> listOfMiners.removeItem(equippedMiner));
        return amPanel;
    }

    public void updateBalance(){
        balanceLabel.setText("Balance: " + Math.round(coins));
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public int getCoinsBonus() {
        return coinsBonus;
    }

    public void setCoinsBonus(int coinsBonus) {
        this.coinsBonus = coinsBonus;
    }

    public double getCoinsMult() {
        return coinsMult;
    }

    public void setCoinsMult(double coinsMult) {
        this.coinsMult = coinsMult;
    }

    public void start(){
        if(gameThread == null){
            gameThread = new Thread (this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        while (isRunning){
            if (equippedMiner != null) equippedMiner.countDownCooldown();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        frame = new Main();
        Shop.init(frame);
        frame.setSize(350,250);
        frame.setVisible(true);
        frame.start();
    }



}
