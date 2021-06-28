import javax.swing.*;
import java.awt.*;

public class Shop {

    private static JLabel shopBalance;
    private static Main main;
    private static JFrame window;
    private static JPanel panel;

    private static final int[] cost = new int[1];
    private static final int[] multCost = new int[1];

    public interface buyAction{
        void buy();
    }

    public static void init(Main main){
        window = new JFrame("Shop");
        panel = new JPanel();

        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        Shop.main = main;

        shopBalance = new JLabel("Balance: " + main.getCoins());
        placeStuff();
        window.setSize(250,300);
        window.setVisible(false);
    }

    public static void updateLabels(){
        shopBalance.setText("Balance: " + Math.round(main.getCoins()));
        main.updateBalance();
    }

    public static void openShop(){
        window.setVisible(true);
        window.toFront();
        window.repaint();
    }

    //place swing objects
    public static void placeStuff(){

        cost[0] = (int) (Math.pow(main.getCoinsBonus(), 1.1) * 8) + 10;
        multCost[0] = (int)(Math.pow((main.getCoinsMult() - 1) * 10, 1.15) * 10) + 10;


        //i know it's redundant =/
        JLabel shopLabel = new JLabel("Shop!");
        shopLabel.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
        JLabel bonusUpgrade = new JLabel("Current Coin Bonus: " + main.getCoinsBonus());
        JButton bonusUpgradeButton = new JButton ("Cost to upgrade: " + cost[0]);
        final JLabel buyMiner = new JLabel("Buy random AutoMiner Lv" + main.getCoinsBonus() + " : " + 100 * (main.getCoinsBonus()+1) + "$");


        bonusUpgradeButton.addActionListener(e -> {
            buyItem(cost, ()->{
                main.setCoinsBonus(main.getCoinsBonus() + 1);
                bonusUpgradeButton.setText("Cost to upgrade: " + cost[0]);
                bonusUpgrade.setText("Current Coin Bonus: " + main.getCoinsBonus());
                buyMiner.setText("Buy random AutoMiner Lv" + main.getCoinsBonus() + " : " + 100 * (main.getCoinsBonus()+1) + "$");
            });
        });

        JLabel multUpgrade = new JLabel("Current Coin multiplier: x" + main.getCoinsMult());
        JButton multUpgradeButton = new JButton ("Cost to upgrade: " + multCost[0]);

        multUpgradeButton.addActionListener(e -> {
            buyItem(multCost, ()->{
                main.setCoinsMult(main.getCoinsMult() + 0.1);
                multUpgradeButton.setText("Cost to upgrade: " + multCost[0]);
                multUpgrade.setText("Current Coin multiplier: x" + Math.round(main.getCoinsMult() * 10) / 10.0);
            });
        });

        JButton buyMinerButton = new JButton ("Buy me!");

        buyMinerButton.addActionListener(e -> {
            buyItem(new int[]{100 * (main.getCoinsBonus()+1)}, ()->{
                main.addNewAutoMiner();
            });
        });



        window.setContentPane(panel);
        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);
        panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(shopLabel);
        panel.add(shopBalance);
        panel.add(bonusUpgrade);
        panel.add(bonusUpgradeButton);
        panel.add(multUpgrade);
        panel.add(multUpgradeButton);
        panel.add(buyMiner);
        panel.add(buyMinerButton);

        window.pack();
    }

    public static void buyItem(int[] cost, buyAction action){

        if (main.getCoins() < cost[0]){
            JOptionPane.showMessageDialog(panel, "You don't have enough coins!","Hey!",
                    JOptionPane.ERROR_MESSAGE);
        }
        else{
            main.setCoins(main.getCoins() - cost[0]);
            updateLabels();
            Shop.cost[0] = (int) (Math.pow(main.getCoinsBonus(), 1.1) * 4) + 10;// updates the cost to a new cost
            Shop.multCost[0] = (int) (Math.pow((main.getCoinsMult() - 1) * 10, 1.15) * 5)  + 10;
            action.buy();
        }
    }
}
