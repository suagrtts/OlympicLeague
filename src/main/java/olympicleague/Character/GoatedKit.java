package character;

public class GoatedKit extends GameCharacter {
    public GoatedKit() {
        super("Goated Kit", "A legendary warrior blessed by Talona...", 1200, 500);

        this.addSkill(new Skill("Kit Kit", 1, "300 Base Bite Dmg. Cost: 120MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 120) return "Not enough mana!";
                source.useMana(120);
                return "KIT KIT bites hard! Rabies infects for " + target.takeDamage(source.randomDamage(300, 20)) + " damage!";
            }
        });

        this.addSkill(new Skill("Rat Spot", 4, "Untargetable for 2 turns. Cost: 250MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 250) return "Not enough mana!";
                source.useMana(250);
                source.setUntargetable(true);
                source.setStatusEffectTurns(2);
                return "RAT SPOT! Goated Kit becomes elusive for 2 turns!";
            }
        });

        this.addSkill(new Skill("Talona's Might", 5, "+20% bite damage for 3 turns. Cost: 400MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 400) return "Not enough mana!";
                source.useMana(400);
                source.setAttackBonus(1.2);
                source.setStatusEffectTurns(3);
                return "TALONA'S MIGHT! +20% damage for 3 turns!";
            }
        });
    }
}