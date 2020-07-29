package mod.keycrusader.backitems.common.capability;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import mod.keycrusader.backitems.client.models.SashimonoModel;
import mod.keycrusader.backitems.client.util.ModelHandler;
import mod.keycrusader.backitems.client.util.RenderHandler;
import mod.keycrusader.backitems.common.util.Helpers;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import top.theillusivec4.curios.api.capability.ICurio;

import java.util.List;

import static net.minecraft.tileentity.BannerTileEntity.func_230138_a_;

public class CurioSashimono implements ICurio {
    private SashimonoModel model;
    private final double animationOffset = Math.random();

    /** Base colour of the banner (e.g. White, Black, Blue... */
    private DyeColor baseColor = DyeColor.WHITE;
    /** A list of all the banner patterns. */
    private ListNBT patterns;
    private boolean patternDataSet;
    /** A list of all patterns stored on this banner. */
    private List<Pair<BannerPattern, DyeColor>> patternList;

    private ItemStack cachedBannerStack = null;
    private float prevRotationYaw = 0;

    @Override
    public boolean canRightClickEquip() {
        return true;
    }

    @Override
    public void playEquipSound(LivingEntity entityLivingBase) {
        entityLivingBase.world.playSound(null, entityLivingBase.getPosition(),
                SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.NEUTRAL,
                1.0F, 1.0F);
    }

    @Override
    public boolean hasRender(String identifier, LivingEntity livingEntity) {
        return true;
    }

    private ListNBT readTagsFromItemStack(ItemStack stack) {
        ListNBT listnbt = null;
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null && compoundnbt.contains("Patterns", 9)) {
            listnbt = compoundnbt.getList("Patterns", 10).copy();
        }

        return listnbt;
    }

    private void loadFromItemStack(ItemStack stack) {
        this.patterns = readTagsFromItemStack(stack);
        this.patternList = null;
        this.patternDataSet = true;
        this.baseColor = ((BannerItem) stack.getItem()).getColor();
        this.cachedBannerStack = stack;
    }

    private List<Pair<BannerPattern, DyeColor>> getPatternList() {
        if (this.patternList == null && this.patternDataSet) {
            this.patternList = func_230138_a_(this.baseColor, this.patterns);
        }

        return this.patternList;
    }

    @Override
    public void render(String identifier, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) livingEntity;

        if (this.model == null) {
            this.model = new SashimonoModel();
        }

        matrixStack.push();
        RenderHandler.alignToBack(matrixStack, playerEntity);

        ItemStack stack = Helpers.getItem(livingEntity, identifier);
        if (!stack.equals(cachedBannerStack)) {
            loadFromItemStack(stack);
        }

        matrixStack.push();
        // TODO lerp animation type thing here
        //((ModelSashimono)this.model).sashimonoSlate.rotateAngleY = (((livingEntity.rotationYaw - this.prevRotationYaw)*10) -90) * (float)Math.PI / 180;
        this.prevRotationYaw = livingEntity.rotationYaw;

        List<Pair<BannerPattern, DyeColor>> list = getPatternList();

        for(int i = 0; i < 17 && i < list.size(); ++i) {
            Pair<BannerPattern, DyeColor> pair = list.get(i);
            float[] afloat = pair.getSecond().getColorComponentValues();
            Material material = new Material(Atlases.BANNER_ATLAS, pair.getFirst().func_226957_a_(true));
            this.model.render(matrixStack, material.getBuffer(renderTypeBuffer, RenderType::getEntityCutout), light, 0 | 10 << 16, afloat[0], afloat[1], afloat[2], 1.0F);
        }
        matrixStack.pop();

        RenderHandler.renderColouredModel(ModelHandler.SASHIMONO.getModel(), matrixStack, renderTypeBuffer, this.baseColor.getColorValue(), light);
        matrixStack.pop();
    }
}
