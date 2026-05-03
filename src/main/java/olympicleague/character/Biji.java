package character;

public class Biji extends GameCharacter {
    public Biji() {
        super("Biji", "The Melodic Warrior...", 1600, 1200);

        this.addSkill(new Skill("Power Chord", 1, "380 Base Dmg. Cost: 200MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 200) return "Not enough mana!";
                source.useMana(200);
                return "POWER CHORD shreds! Dealt " + target.takeDamage(source.randomDamage(380, 25)) + " damage!";
            }
        });

        this.addSkill(new Skill("Healing Hymn", 3, "Heals 400 HP. Cost: 300MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 300) return "Not enough mana!";
                source.useMana(300);
                source.heal(400);
                return "HEALING HYMN played! Biji recovers 400 HP!";
            }
        });

        this.addSkill(new Skill("Symphony of Destruction", 5, "600 Dmg + Stun. Cost: 550MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 550) return "Not enough mana!";
                source.useMana(550);
                target.setStunned(true);
                return "SYMPHONY OF DESTRUCTION! Dealt " + target.takeDamage(source.randomDamage(600, 30)) + " damage and stunned the target!";
            }
        });
    }
}