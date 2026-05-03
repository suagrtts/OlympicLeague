package character;

public interface Damageable {
    int getHealth();
    int getMaxHealth();
    boolean isAlive();
    int takeDamage(int amount);
    int takeTrueDamage(int amount);
    void heal(int amount);
}