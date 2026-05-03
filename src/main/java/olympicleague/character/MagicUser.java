package character;

public interface MagicUser {
    int getMana();
    int getMaxMana();
    void useMana(int amount);
    void restoreMana(int amount);
}