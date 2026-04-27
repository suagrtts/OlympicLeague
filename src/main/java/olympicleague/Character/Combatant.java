package Character;

public interface Combatant {
    String takeTurn(GameCharacter target, int skillIndex);
    String autoTakeTurn(GameCharacter target);
    void updateTurnEffects();
    void resetForNewRound();
}
