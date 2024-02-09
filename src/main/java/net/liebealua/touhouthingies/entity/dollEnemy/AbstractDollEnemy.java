package net.liebealua.touhouthingies.entity.dollEnemy;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class AbstractDollEnemy extends Monster {
    protected AbstractDollEnemy(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
}
