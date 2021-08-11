package supercoder79.survivalgames.mixin;

import com.mojang.datafixers.util.Either;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.Identifier;

@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {
	@Accessor(value = "location")
	Either<Identifier, Structure> getLocation();
}
