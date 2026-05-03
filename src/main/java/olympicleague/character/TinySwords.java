package olympicleague.character;

/**
 * TinySwords — The Diminutive Duelist.
 * Don't let the name fool you: this tiny warrior's twin blades
 * strike with blinding speed and lethal precision.
 * God: Athena, Goddess of Wisdom and Warfare.
 *
 * Replaces: Vor (deprecated)
 */
public class TinySwords extends GameCharacter {

    public TinySwords() {
        super("TinySwords",
              "Born small, forged mighty. TinySwords proved every doubter wrong " +
              "in the arenas of Athena, mastering dual-blade techniques no " +
              "full-sized warrior could replicate. Small body, infinite fury.",
              1650, 900);

        // Skill 1 — Twin Slash (fast, cheap, low cooldown)
        this.addSkill(new Skill("Twin Slash", 1, "350 Base Dmg × 2 hits. Cost: 160MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 160) return "Not enough mana for Twin Slash!";
                source.useMana(160);
                int hit1 = target.takeDamage(source.randomDamage(175, 15));
                int hit2 = target.takeDamage(source.randomDamage(175, 15));
                return source.getName() + " unleashes TWIN SLASH! Hit 1: " + hit1 + " | Hit 2: " + hit2
                       + " | Total: " + (hit1 + hit2) + " damage!";
            }
        });

        // Skill 2 — Blade Veil (evade + counter window)
        this.addSkill(new Skill("Blade Veil", 3, "Become untargetable for 1 turn. Cost: 280MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 280) return "Not enough mana for Blade Veil!";
                source.useMana(280);
                source.setUntargetable(true);
                source.setStatusEffectTurns(1);
                return "BLADE VEIL! " + source.getName() + " vanishes between sword flashes — untargetable next hit!";
            }
        });

        // Skill 3 — Athena's Fury (massive damage burst, high cooldown)
        this.addSkill(new Skill("Athena's Fury", 5, "+60% ATK for 2 turns. Cost: 450MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 450) return "Not enough mana for Athena's Fury!";
                source.useMana(450);
                source.setAttackBonus(1.60);
                source.setStatusEffectTurns(2);
                return "ATHENA'S FURY descends upon " + source.getName() +
                       "! Attack power surges +60% for 2 turns!";
            }
        });
    }
}
