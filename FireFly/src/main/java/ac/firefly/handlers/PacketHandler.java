package ac.firefly.handlers;

import ac.firefly.Firefly;
import ac.firefly.data.PlayerData;
import ac.firefly.events.packet.*;
import ac.firefly.managers.PluginManager;
import com.comphenix.packetwrapper.WrapperPlayClientBlockDig;
import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class PacketHandler {
    public static Map<UUID, Integer> movePackets;
    private static final List<String> INSTANT_BREAK_BLOCKS = Arrays.asList(
            "reeds", "waterlily", "deadbush", "flower", "doubleplant", "tallgrass"
    );

    public static void init() {
        movePackets = new HashMap<>();
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Firefly.instance, PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        Player player = event.getPlayer();
                        if (player == null) {
                            return;
                        }

                        EnumWrappers.EntityUseAction type;
                        try {
                            type = packet.getEntityUseActions().read(0);
                        } catch (Exception ex) {
                            return;
                        }

                        Entity entity = event.getPacket().getEntityModifier(player.getWorld()).read(0);

                        if (entity == null) {
                            return;
                        }

                        Bukkit.getServer().getPluginManager().callEvent(new PacketUseEntityEvent(type, player, entity, packet));

                        if (type == EnumWrappers.EntityUseAction.ATTACK) {
                            Bukkit.getServer().getPluginManager().callEvent(new PacketAttackEvent(player, entity, PacketTypes.USE));
                            PluginManager.instance.getDataManager().getData(event.getPlayer()).setLastAttackPacket(System.currentTimeMillis());
                        }
                    }
                });
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Firefly.instance, PacketType.Play.Client.ARM_ANIMATION) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        Player player = event.getPlayer();
                        if (player == null) {
                            return;
                        }

                        EnumWrappers.EntityUseAction type;
                        try {
                            type = packet.getEntityUseActions().read(0);
                        } catch (Exception ex) {
                            return;
                        }

                        Entity entity = event.getPacket().getEntityModifier(player.getWorld()).read(0);

                        if (entity == null) {
                            return;
                        }

                        Bukkit.getServer().getPluginManager().callEvent(new PacketUseEntityEvent(type, player, entity, packet));

                        if (type == EnumWrappers.EntityUseAction.ATTACK) {
                            Bukkit.getServer().getPluginManager().callEvent(new PacketAttackEvent(player, entity, PacketTypes.USE));
                        }
                    }
                });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance,
                PacketType.Play.Client.ARM_ANIMATION) {
            @Override
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketSwingArmEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance,
                PacketType.Play.Server.KEEP_ALIVE) {
            @Override
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketServerKeepAliveEvent(player));
            }

            @Override
            public void onPacketSending(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                PlayerData data = PluginManager.instance.getDataManager().getData(player);
                data.lastServerKP = System.currentTimeMillis();
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance,
                PacketType.Play.Client.KEEP_ALIVE) {
            @Override
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                PlayerData data = PluginManager.instance.getDataManager().getData(player);
                Bukkit.getServer().getPluginManager().callEvent(new PacketClientKeepAliveEvent(player));

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                    data.setLastPacketClass(event.getPacket());
                    data.ping = System.currentTimeMillis() - data.lastServerKP;
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), packetEvent.getPacket().getFloat().read(0), packetEvent.getPacket().getFloat().read(1), PacketTypes.LOOK));

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                    data.setLastPacketClass(packetEvent.getPacket());
                }
            }

        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.POSITION) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, packetEvent.getPacket().getDoubles().read(0), packetEvent.getPacket().getDoubles().read(1), packetEvent.getPacket().getDoubles().read(2), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.POSITION));

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, packetEvent.getPacket().getDoubles().read(0), packetEvent.getPacket().getDoubles().read(1), packetEvent.getPacket().getDoubles().read(2), packetEvent.getPacket().getFloat().read(0), packetEvent.getPacket().getFloat().read(1), PacketTypes.POSLOOK));

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastKillauraPitch(packetEvent.getPacket().getFloat().read(1));
                    data.setLastKillauraYaw(packetEvent.getPacket().getFloat().read(0));
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.FLYING) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.FLYING));

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.POSITION) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPositionEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.FLYING) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                boolean look =  packetEvent.getClass().getName().contains("Look");

                Bukkit.getServer().getPluginManager().callEvent(new PacketFlyingEvent(packetEvent.getPacket(), player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), look));

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                PlayerData data = PluginManager.instance.getDataManager().getData(player);
                WrapperPlayClientBlockDig wrappedPacket = new WrapperPlayClientBlockDig(packetEvent.getPacket());
                final EnumWrappers.PlayerDigType digType = wrappedPacket.getStatus();

                if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                    Block block = wrappedPacket.getLocation().toLocation(player.getWorld()).getBlock();

                    String tile = block.getType().toString().replace("tile.", "");

                    if (INSTANT_BREAK_BLOCKS.contains(tile)) {
                        data.setInstantBreakDigging(true);
                    } else {
                        data.setInstantBreakDigging(false);
                    }

                    data.setDigging(true);
                } else if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK ||
                        digType == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                    data.setInstantBreakDigging(false);
                    data.setDigging(false);
                }


                WrapperPlayClientBlockDig digPacket = new WrapperPlayClientBlockDig(packetEvent.getPacket());
                Bukkit.getPluginManager().callEvent(new PacketDigEvent(player, digPacket.getLocation().toLocation(player.getWorld())));

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Server.ENTITY_VELOCITY) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                WrapperPlayServerEntityVelocity vPacket = new WrapperPlayServerEntityVelocity(packetEvent.getPacket());
                Bukkit.getPluginManager().callEvent(new PacketVelocityEvent(packetEvent.getPlayer(), vPacket.getVelocityX(), vPacket.getVelocityY(), vPacket.getVelocityZ()));

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }

            @Override
            public void onPacketSending(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                WrapperPlayServerEntityVelocity vPacket = new WrapperPlayServerEntityVelocity(packetEvent.getPacket());
                Bukkit.getPluginManager().callEvent(new PacketVelocityEvent(packetEvent.getPlayer(), vPacket.getVelocityX(), vPacket.getVelocityY(), vPacket.getVelocityZ()));
            }
        });

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.BLOCK_PLACE) {
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                PlayerData data = PluginManager.instance.getDataManager().getData(player);

                data.setLastPlace(System.currentTimeMillis());

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
    }
}


