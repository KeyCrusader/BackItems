package mod.keycrusader.backitems.client.util;

import mod.keycrusader.backitems.BackItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public enum ModelHandler {
    BACKPACK("backpack", 3),
    QUIVER("quiver", 5),
    PARACHUTE("parachute", 0),
    CHUTE("chute", 1),
    GLIDER("glider", 0),
    GLIDER_WINGS("glider_wings", 0),
    SASHIMONO("sashimono", 0);

    protected List<ResourceLocation> modelLocation = new ArrayList<>();
    protected int levels;
    private List<IBakedModel> cachedModel;

    ModelHandler(String modelName, int levels) {
        for (int l = 0; l <= levels; l++) {
            this.modelLocation.add(new ResourceLocation(BackItems.MODID, "curio/" + modelName+"_"+l));
        }
        this.levels = levels;
    }

    public IBakedModel getModel() {
        return getModel(0).get(0);
    }

    public List<IBakedModel> getModel(int level) {
        /** If the models haven't been loaded before, load and cache them */
        if (this.cachedModel == null) {
            this.cachedModel = new ArrayList<>();

            for (ResourceLocation resLoc : this.modelLocation) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(resLoc);
                if (model == Minecraft.getInstance().getModelManager().getMissingModel()) {
                    // TODO Handle missing model
                    BackItems.LOGGER.error("MISSING MODEL: "+resLoc);
                }
                else {
                    this.cachedModel.add(model);
                }
            }
        }

        List<IBakedModel> retVal = new ArrayList<IBakedModel>();
        for (int l = 0; l <= level; l++) {
            retVal.add(this.cachedModel.get(l));
        }

        return retVal;
    }
}
