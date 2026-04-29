package character;

public class Orris extends GameCharacter {
    public Orris() {
        super("Orris", "A demigod son of Poseidon...", 1500, 600);

        this.addSkill(new Skill("Tidal Wave", 1, "300 Base Dmg. Cost: 130MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 130) return "Not enough mana!";
                source.useMana(130);
                return "TIDAL WAVE crashes for " + target.takeDamage(source.randomDamage(300, 25)) + " damage!";
            }
        });

        this.addSkill(new Skill("Ocean's Shield", 3, "Absorb 20% Dmg for 2 turns. Cost: 110MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 110) return "Not enough mana!";
                source.useMana(110);
                source.setDefenseBonus(0.8);
                source.setStatusEffectTurns(2);
                return "OCEAN'S SHIELD rises! Absorbs 20% damage for 2 turns!";
            }
        });

        this.addSkill(new Skill("Poseidon's Wrath", 6, "500 True Dmg. Cost: 180MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 180) return "Not enough mana!";
                source.useMana(180);
                return "POSEIDON'S WRATH! Dealt " + target.takeTrueDamage(500) + " True Damage!";
            }
        });
    }
}