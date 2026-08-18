package untamedwilds.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityDataHolderClient;
import untamedwilds.util.EntityUtils;

import java.util.HashMap;

public class SyncTextureData implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncTextureData> TYPE =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(UntamedWilds.MOD_ID, "sync_texture_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTextureData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.map(Identifier::parse, Identifier::toString), SyncTextureData::getEntityName,
        ByteBufCodecs.STRING_UTF8, SyncTextureData::getSpeciesName,
        ByteBufCodecs.INT, SyncTextureData::getSkinsData,
        ByteBufCodecs.INT, SyncTextureData::getId,
        SyncTextureData::new);

    private final Identifier entityName;
    private final String speciesName;
    private final Integer skinsData;
    private final Integer id;

    public SyncTextureData(Identifier str, String species_name, Integer skins, Integer id) {
        this.entityName = str;
        this.speciesName = species_name;
        this.skinsData = skins;
        this.id = id;
    }

    private Identifier getEntityName() { return entityName; }
    private String getSpeciesName() { return speciesName; }
    private int getSkinsData() { return skinsData; }
    private int getId() { return id; }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Handling texture data for entity: " + entityName + " with species: " + speciesName);
            }
            var found = BuiltInRegistries.ENTITY_TYPE.get(entityName);
            EntityType<?> type = found.isPresent() ? (EntityType<?>) found.get().value() : null;
            if (type == null) {
                return;
            }
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(type)) {
                ComplexMob.CLIENT_DATA_HASH.put(type, new EntityDataHolderClient(new HashMap<>(), new HashMap<>()));
            }
            EntityUtils.buildSkinArrays(entityName.getPath(), speciesName, skinsData, id, ComplexMob.TEXTURES_COMMON, ComplexMob.TEXTURES_RARE);
            ComplexMob.CLIENT_DATA_HASH.get(type).addSpeciesName(id, speciesName);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}