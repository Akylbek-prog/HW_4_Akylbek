import java.util.Random;

public class hw_4_Akylbek {

    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static boolean bossStunned = false; // Tracks if the boss is stunned

    public static int[] heroesHealth = {270, 260, 250, 255, 400, 200, 300, 280}; // Thor has 280 health
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 10, 0, 25}; // Thor has 25 damage
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};

    public static int roundNumber;
    public static boolean witcherUsedAbility = false;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }

        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        if (!bossStunned) {
            bossAttack();
        } else {
            System.out.println("Boss is stunned and skips this round!");
            bossStunned = false; // Reset stun status
        }
        witcherRevive();
        heroesAttack();
        medicHeal();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex;
        do {
            randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2,3,4,5,6,7
        } while (heroesAttackType[randomIndex].equals("Medic") || heroesAttackType[randomIndex].equals("Witcher"));
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coef = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coef;
                    System.out.println("Critical Damage: " + damage);
                }
                bossHealth = Math.max(bossHealth - damage, 0);

                // Thor's special ability: chance to stun the boss
                if (heroesAttackType[i].equals("Thor")) {
                    Random random = new Random();
                    boolean stun = random.nextBoolean(); // 50% chance to stun
                    if (stun) {
                        bossStunned = true;
                        System.out.println("Thor stunned the boss!");
                    }
                }
            }
        }
    }

    public static void bossAttack() {
        int totalDamage = bossDamage;
        int golemIndex = 4; // Golem's index in the arrays
        int luckyIndex = 5; // Lucky's index in the arrays
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && i != golemIndex) {
                if (i == luckyIndex) {
                    Random random = new Random();
                    boolean isLucky = random.nextBoolean(); // 50% chance to evade
                    if (isLucky) {
                        System.out.println("Lucky evaded the boss's attack!");
                        continue;
                    }
                }
                int damageBlocked = (i != luckyIndex) ? bossDamage / 5 : 0;
                heroesHealth[i] = Math.max(heroesHealth[i] - (totalDamage - damageBlocked), 0);
                heroesHealth[golemIndex] = Math.max(heroesHealth[golemIndex] - damageBlocked, 0);
            }
        }

        System.out.println("Golem absorbed part of the damage!");
    }

    public static void witcherRevive() {
        if (!witcherUsedAbility && heroesHealth[6] > 0) { // Witcher is alive and ability not used
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] <= 0 && i != 6) { // Find the first dead hero (not Witcher)
                    heroesHealth[i] = heroesHealth[6]; // Revive with Witcher's health
                    heroesHealth[6] = 0; // Witcher sacrifices himself
                    witcherUsedAbility = true;
                    System.out.println("Witcher revived " + heroesAttackType[i] + " with his life.");
                    break;
                }
            }
        }
    }

    public static void medicHeal() {
        if (heroesHealth[3] > 0) { // Check if Medic is alive
            for (int i = 0; i < heroesHealth.length; i++) {
                if (i != 3 && heroesHealth[i] > 0 && heroesHealth[i] < 100) { // Medic can't heal himself or dead heroes
                    heroesHealth[i] += 50; // Heal for 50 health points
                    System.out.println("Medic healed " + heroesAttackType[i] + " for 50 health points.");
                    break; // Medic heals only one hero per round
                }
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND: " + roundNumber + " ----------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: "
                + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }
}