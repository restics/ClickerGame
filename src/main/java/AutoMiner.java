import java.util.Random;

public class AutoMiner{


    /**
     * generates coins automatically (not done)
     * Rates: 64% Common, 22% Rare, 10% Epic, 4% Legendary
     *
     */

    public Random rand = new Random();

    private double cooldown;
    private double maxCoolDown;
    private double coinAmount;
    private boolean isRunning;
    private Main main;
    private String name;
    private int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int rarity; // 0 - common, 1 - rare, 2 - epic, 3 - legendary


    public double getCooldown() {
        return maxCoolDown;
    }

    public void setCooldown(double cooldown) {
        maxCoolDown = cooldown;
    }

    public double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(double coinAmount) {
        this.coinAmount = coinAmount;
    }

    public AutoMiner(double cd, double coinAmount){
        findRarity();
        cooldown = cd;
        maxCoolDown = cd;
        this.coinAmount = coinAmount;
        isRunning = true;

    }
    public AutoMiner(){
        findRarity();
        cooldown = (rand.nextInt(30) + (30 * (3 - rarity))) /  10.0;
        maxCoolDown = cooldown;
        coinAmount = (rand.nextInt(5) + 1) * ((rarity + level * 0.2) + 1) + 1 ;
        isRunning = true;

    }

    public void findRarity(){
        int random = rand.nextInt(100);
        int level = Main.frame.getCoinsBonus();
        if (random < 65){
            name  = "Common Autominer Lv" + level;
            rarity = 0;
        }
        else if (random < 87){
            name  = "Rare Autominer Lv" + level;
            rarity = 1;
        }
        else if (random < 97){
            name  = "Epic Autominer Lv" + level;
            rarity = 2;
        }
        else{
            name  = "Legendary Autominer Lv" + level;
            rarity = 3;
        }
    }

    public void registerMain(Main main){
        this.main = main;
    }

    public void countDownCooldown(){
        cooldown -= 0.1;
        if (cooldown <= 0){
            cooldown = maxCoolDown;
            Shop.updateLabels();
            Main.frame.setCoins(Main.frame.getCoins() + (coinAmount * Main.frame.getCoinsMult() * 0.5) );
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
