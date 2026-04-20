package ligaolympica.character;

public interface CharacterInterface {
    String getName();
    int getHealth();
    int getMaxHealth();
    int getMana();
    int getMaxMana();
    boolean isAlive();

    void skill1(GameCharacter target);
    void skill2(GameCharacter target);
    void skill3(GameCharacter target);

    void takeTurn(GameCharacter target);
    void autoTakeTurn(GameCharacter target);

    void heal(int amount);
    void takeDamage(int amount);
    void useMana(int amount);
    void restoreMana(int amount);
    void takeTrueDamage(int amount);
    void displayStats();
    void showInfo();
    void resetForNewRound();
    void updateTurnEffects();
    boolean isStunned();
    void setStunned(boolean stunned);
    boolean hasEscaped();
    void setEscaped(boolean escaped);
}
