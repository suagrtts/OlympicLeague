package olympicleague.character;

/**
 * EvilWizard — The Dark Archmage.
 * A twisted sorcerer whose obsession with forbidden magic has warped both
 * body and soul. The Evil Wizard commands devastating arcane forces and can
 * drain the very life from opponents. Based on the EVil_Wizard_2 sprite set.
 * God: Hecate, Goddess of Magic and Witchcraft.
 */
public class EvilWizard extends GameCharacter {

    public EvilWizard() {
        super("EvilWizard",
              "Once a scholar at the Temple of Hecate, the Evil Wizard descended " +
              "into forbidden arts after losing everything. Now they wield shadow " +
              "and flame with terrifying precision, seeking power above all else. " +
              "None who face them leave unscathed.",
              1400, 1400);

        // Skill 1 — Dark Bolt (high mana cost, powerful damage)
        this.addSkill(new Skill("Dark Bolt", 1, "420 Base Dmg. Cost: 210MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 210) return "Not enough mana for Dark Bolt!";
                source.useMana(210);
                int dmg = target.takeDamage(source.randomDamage(420, 30));
                return "DARK BOLT! " + source.getName() + " channels forbidden energy — "
                       + dmg + " damage rips through " + target.getName() + "!";
            }
        });

        // Skill 2 — Soul Drain (heals caster, moderate damage)
        this.addSkill(new Skill("Soul Drain", 3, "Drain 300 HP from target. Cost: 300MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 300) return "Not enough mana for Soul Drain!";
                source.useMana(300);
                int drained = target.takeTrueDamage(300);
                source.heal(drained);
                return "SOUL DRAIN! " + source.getName() + " tears " + drained + " HP from "
                       + target.getName() + "'s soul and absorbs it!";
            }
        });

        // Skill 3 — Arcane Supremacy (huge attack bonus for 2 turns)
        this.addSkill(new Skill("Arcane Supremacy", 5, "+70% ATK, true power unleashed. Cost: 700MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 700) return "Not enough mana for Arcane Supremacy!";
                source.useMana(700);
                source.setAttackBonus(1.70);
                source.setStatusEffectTurns(2);
                return "ARCANE SUPREMACY! " + source.getName() +
                       " unlocks their true dark power — +70% attack for 2 turns! The arena trembles!";
            }
        });
    }
}
