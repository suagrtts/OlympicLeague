package olympicleague.character;

public class Skeleton extends GameCharacter {

    private boolean reassembledThisRound = false;

    public Skeleton() {
        super("Skeleton",
                "Once a proud soldier of the Olympian wars, now an eternal servant " +
                        "of Hades. The Skeleton was cursed to fight forever, unable to die " +
                        "while the underworld still has use for it. It remembers nothing " +
                        "but the clash of bone against steel.",
                1900, 700);

        this.addSkill(new Skill("Bone Shatter", 0, "280 TRUE Dmg (ignores defense). Cost: 140MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 140) return "Not enough mana for Bone Shatter!";
                source.useMana(140);
                int dmg = target.takeTrueDamage(source.randomDamage(280, 25));
                return "BONE SHATTER! " + source.getName() + "'s brittle arm cracks with force — "
                        + dmg + " TRUE damage to " + target.getName() + "!";
            }
        });

        this.addSkill(new Skill("Undying Resolve", 3, "–45% damage taken for 2 turns. Cost: 200MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 200) return "Not enough mana for Undying Resolve!";
                source.useMana(200);
                source.setDefenseBonus(0.55);
                source.setStatusEffectTurns(2);
                return "UNDYING RESOLVE! " + source.getName() +
                        "'s bones knit together — damage reduced by 45% for 2 turns!";
            }
        });

        this.addSkill(new Skill("Hades' Judgment", 6, "500 Dmg + STUN target. Cost: 500MP") {
            @Override
            public String execute(GameCharacter source, GameCharacter target) {
                if (source.getMana() < 500) return "Not enough mana for Hades' Judgment!";
                source.useMana(500);
                int dmg = target.takeDamage(source.randomDamage(500, 30));
                target.setStunned(true);
                return "HADES' JUDGMENT! The underworld reaches out through " + source.getName()
                        + " — " + dmg + " damage and " + target.getName() + " is STUNNED!";
            }
        });
    }

    @Override
    public int takeDamage(int damage) {
        int actual = super.takeDamage(damage);
        triggerRevivalIfNeeded();
        return actual;
    }

    @Override
    public int takeTrueDamage(int damage) {
        int actual = super.takeTrueDamage(damage);
        triggerRevivalIfNeeded();
        return actual;
    }

    /**
     * On first death per round, Skeleton reassembles at 50% HP.
     * Returns a log message if revival triggered, or null if not.
     */
    public String triggerRevivalIfNeeded() {
        if (!this.isAlive() && !reassembledThisRound) {
            reassembledThisRound = true;
            this.revive(this.getMaxHealth() / 2);
            return "💀 " + getName() + "'s bones REASSEMBLE! Revived at " + getHealth() + " HP!";
        }
        return null;
    }

    @Override
    public void resetForNewRound() {
        super.resetForNewRound();
        reassembledThisRound = false;
    }
}