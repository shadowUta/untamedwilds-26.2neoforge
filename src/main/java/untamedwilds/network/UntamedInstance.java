package untamedwilds.network;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import untamedwilds.UntamedWilds;

public class UntamedInstance {

    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        UntamedWilds.LOGGER.info("Registering Packets!");
        event.registrar("1").playToClient(SyncTextureData.TYPE, SyncTextureData.STREAM_CODEC, SyncTextureData::handle);
    }

    public static void sendToClient(SyncTextureData packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}
