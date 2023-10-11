import java.util.Random;
// код работает скорее всего не совсем правильно, и некоторые дейсвия с запозданием,
// но это все что у меня получилось, ну точно лучьше чем прошлый :0
public class Main {
    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static int bossHealth = 600;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {250, 500, 150, 250, 300, 450, 540, 500};
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 46, 0, 28};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Healer", "Golem", "Lucky", "Witcher", "Thor"};
    public static int roundNumber = 0;
    public static boolean medicHealing = true;
    public static boolean criticalDamageThisRound = false;
    public static boolean witcherUsedAbility = false;
    public static int resurrectedHeroIndex = -1;
    public static int witcherIndex = -1;
    public static boolean bossStunned = false;
    public static int bossStunDuration = 0;

    public static void playRound() {
        roundNumber++;
        if (bossStunned) {
            bossStunDuration--;
            if (bossStunDuration == 0) {
                bossStunned = false;
                System.out.println("Boss is no longer stunned!");
            }
        } else {
            chooseBossDefence();
            healHeroes();
            bossAttack();
        }
        heroesAttack();
        tryToResurrectHero();
        printStatistics();
        criticalDamageThisRound = false;
    }

    private static void healHeroes() {
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossAttack() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                int damageToGolem = bossDamage / 5;
                if (i == 4) {
                    heroesHealth[i] -= damageToGolem;
                    if (damageToGolem > 0) {
                        System.out.println("Golem takes 1/5 of the damage.");
                    }
                } else if (i == 5) {
                    Random random = new Random();
                    if (random.nextInt(2) == 0) {
                        System.out.println("Lucky dodged the boss's blow.");
                    } else {
                        int damage = bossDamage;
                        if (!criticalDamageThisRound && heroesAttackType[i] == bossDefence) {
                            Random randomCoeff = new Random();
                            int coeff = randomCoeff.nextInt(9) + 2;
                            damage = heroesDamage[i] * coeff;
                            System.out.println("Critical damage: " + damage);
                            criticalDamageThisRound = true;
                        }
                        if (heroesHealth[i] - damage < 0) {
                            heroesHealth[i] = 0;
                        } else {
                            heroesHealth[i] -= damage;
                        }
                    }
                } else if (i == 6) {
                    if (resurrectedHeroIndex != -1) {
                        heroesHealth[i] = 0;
                        System.out.println("Witcher sacrificed " + heroesAttackType[resurrectedHeroIndex]);
                    } else {
                        int damage = bossDamage;
                        if (heroesHealth[i] - damage < 0) {
                            heroesHealth[i] = 0;
                        } else {
                            heroesHealth[i] -= damage;
                        }
                    }
                } else {
                    int damage = bossDamage;
                    if (i != 4) {
                        damage -= damageToGolem;
                    }
                    if (!criticalDamageThisRound && heroesAttackType[i] == bossDefence) {
                        Random randomCoeff = new Random();
                        int coeff = randomCoeff.nextInt(9) + 2;
                        damage = heroesDamage[i] * coeff;
                        System.out.println("Critical damage: " + damage);
                        criticalDamageThisRound = true;
                    }
                    if (heroesHealth[i] - damage < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        heroesHealth[i] -= damage;
                    }
                }
            }
        }
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (!criticalDamageThisRound && heroesAttackType[i] == bossDefence) {
                    Random randomCoeff = new Random();
                    int coeff = randomCoeff.nextInt(9) + 2;
                    damage = heroesDamage[i] * coeff;
                    System.out.println("Critical damage: " + damage);
                    criticalDamageThisRound = true;
                }
                if (i == 7) {
                    Random random = new Random();
                    if (random.nextInt(3) == 0) {
                        bossStunned = true;
                        bossStunDuration = 1;
                        System.out.println("Thor stunned the boss for 1 round!");
                    }
                }
                if (!bossStunned) {
                    if (bossHealth - damage < 0) {
                        bossHealth = 0;
                    } else {
                        bossHealth -= damage;
                    }
                }
            }
        }
    }

    public static void tryToResurrectHero() {
        if (witcherUsedAbility || resurrectedHeroIndex != -1) {
            return;
        }

        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] <= 0) {
                resurrectedHeroIndex = i;
                witcherUsedAbility = true;
                heroesHealth[i] = 250;
                witcherIndex = i;
                System.out.println("Witcher resurrected and sacrificed " + heroesAttackType[i]);
                break;
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ---------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " +
                (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i == witcherIndex) {
                System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " (Witcher)");
            } else if (i == resurrectedHeroIndex) {
                System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " (Resurrected)");
            } else {
                System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]);
            }
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
        }
        return allHeroesDead;
    }
}
