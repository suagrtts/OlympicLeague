package character;

public class Selwyn extends GameCharacter {
    public Selwyn() {
        super("Selwyn", "The Digital Overlord...", 1700, 1100);

        this.addSkill(new Skill("Rage Bait", 1, "420 Base Dmg. Cost: 220MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 220) return "Not enough mana!";
                source.useMana(220);
                return "RAGE BAIT! Keyboard smashing deals " + target.takeDamage(source.randomDamage(420, 30)) + " damage!";
            }
        });

        this.addSkill(new Skill("Respawn Shield", 3, "Reduce next hit by 60%. Cost: 280MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 280) return "Not enough mana!";
                source.useMana(280);
                source.setDefenseBonus(0.4);
                source.setStatusEffectTurns(1);
                return "RESPAWN SHIELD active! Next incoming damage reduced by 60%!";
            }
        });

        this.addSkill(new Skill("Loki's Hack", 5, "450 Dmg ignoring defenses. Cost: 480MP") {
            @Override public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 480) return "Not enough mana!";
                source.useMana(480);
                return "LOKI'S HACK exploits reality! Dealt " + target.takeTrueDamage(source.randomDamage(450, 25)) + " True Damage!";
            }
        });
    }

    @Override
    public int takeDamage(int damage) {
        boolean hadShield = (this.getStatusEffectTurns() > 0 && this.getDefenseBonus() == 0.4);
        int actual = super.takeDamage(damage);
        if (hadShield && actual > 0) {
            this.setDefenseBonus(1.0);
            this.setStatusEffectTurns(0);
        }
        return actual;
    }
}