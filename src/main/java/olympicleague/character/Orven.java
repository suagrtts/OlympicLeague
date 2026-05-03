package character;

public class Orven extends GameCharacter {
    public Orven() {
        super("Orven", "A demigod son of Hermes...", 1600, 700);

        this.addSkill(new Skill("Swift Strike", 1, "250 Base Dmg. Cost: 140MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 140) return "Not enough mana!";
                source.useMana(140);
                return "SWIFT STRIKE hits for " + target.takeDamage(source.randomDamage(250, 20)) + " damage!";
            }
        });

        this.addSkill(new Skill("Vanish", 3, "Untargetable next turn. Cost: 120MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 120) return "Not enough mana!";
                source.useMana(120);
                source.setUntargetable(true);
                source.setStatusEffectTurns(1);
                return "VANISH! Orven becomes untargetable!";
            }
        });

        this.addSkill(new Skill("Hermes' Speed", 5, "Attack twice. Cost: 200MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 200) return "Not enough mana!";
                source.useMana(200);
                StringBuilder log = new StringBuilder("HERMES' SPEED! Orven attacks twice!\n");
                log.append("Strike 1: ").append(target.takeDamage(source.randomDamage(250, 20))).append(" dmg!\n");
                if (target.isAlive()) {
                    log.append("Strike 2: ").append(target.takeDamage(source.randomDamage(250, 20))).append(" dmg!");
                }
                return log.toString();
            }
        });
    }
}