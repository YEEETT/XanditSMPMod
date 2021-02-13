package me.Fake_Name131.whatever;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;

import me.Fake_Name131.whatever.screen.LagpixelGUI;
import me.Fake_Name131.whatever.screen.XanditGUI;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoSettingsScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("whatever")
public class Whatever {
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public Whatever() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("HELLO FROM PREINIT");
		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("whatever", "helloworld", () -> {
			LOGGER.info("Hello world from the MDK");
			return "Hello world";
		});
	}

	Minecraft mc = Minecraft.getInstance();
	ClientPlayerEntity p = mc.player;
	boolean fullbright = true;
	String house1 = "";
	String house2 = "";

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}",
				event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// register a new block here
			LOGGER.info("HELLO from Register Block");
		}
	}

	@SubscribeEvent
	public void onReceiveChat(ClientChatReceivedEvent event) {
		if (event.getMessage().toString().contains("You own these")) {
			try {
				System.out.println();
				try {
					house1 = event.getMessage().toString().split("text='houses: \\[")[1].split("\\]'")[0]
							.split(", ")[0];
					house2 = event.getMessage().toString().split("text='houses: \\[")[1].split("\\]'")[0]
							.split(", ")[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("ArrayIndexOutOfBoundsException");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (mc.currentScreen instanceof XanditGUI) {
					event.setCanceled(true);
				}
			} catch (Exception e) {
				// No screen open
			}
		}
		if (((event.getMessage().toString().toLowerCase().contains("xandit"))
				&& (event.getMessage().toString().toLowerCase().contains("key='command.context.here'")))
				|| (event.getMessage().toString().toLowerCase().contains("key='command.unknown.command'"))) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onChat(ClientChatEvent event) {
		if (event.getOriginalMessage().toLowerCase().startsWith("/xandit")) {
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void openMenu(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof VideoSettingsScreen) {
			String thing = "Fullbright: ";
			try {
				if (fullbright) {
					thing += "§2TRUE";
				} else {
					thing += "§4FALSE";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Widget fullBright = new Button((event.getGui().width / 2) + 5, 286, 150, 20, new StringTextComponent(thing),
					new IPressable() {

						@Override
						public void onPress(Button arg0) {
							try {
								if (fullbright) {
									mc.gameSettings.gamma = 0;
									fullbright = false;
								} else {
									mc.gameSettings.gamma = 1000;
									fullbright = true;
								}
								mc.displayGuiScreen(event.getGui());
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
			event.addWidget(fullBright);
		} else if (event.getGui() instanceof IngameMenuScreen) {
			if (!mc.isSingleplayer()) {
				String server = "";
				if (mc.getCurrentServerData().serverIP.toLowerCase().contains("xandit")) {
					server = "Xandit SMP Warps";
					Widget serverFunctionality = new Button((event.getGui().width / 2) - 102,
							(event.getGui().height / 2) - 121, 205, 20, new StringTextComponent(server),
							new IPressable() {

								@Override
								public void onPress(Button arg0) {
									try {
										mc.displayGuiScreen((Screen) null);
										mc.player.sendChatMessage("/home");
										mc.displayGuiScreen(new XanditGUI(new StringTextComponent("Xandit SMP Warps"),
												house1, house2));
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
					event.addWidget(serverFunctionality);
				} else if (mc.getCurrentServerData().serverIP.toLowerCase().contains("marscraft")) {
					server = "Marscraft Login";
					Widget serverFunctionality = new Button((event.getGui().width / 2) - 102,
							(event.getGui().height / 2) - 121, 205, 20, new StringTextComponent(server),
							new IPressable() {

								@Override
								public void onPress(Button arg0) {
									try {
										mc.displayGuiScreen((Screen) null);
										mc.player.sendChatMessage("/login Real_Name131");
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
					event.addWidget(serverFunctionality);
				}
				if (mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
					server = "LAGPICKLE";
					Widget serverFunctionality = new Button((event.getGui().width / 2) - 102,
							(event.getGui().height / 2) - 121, 205, 20, new StringTextComponent(server),
							new IPressable() {

								@Override
								public void onPress(Button arg0) {
									try {
										mc.displayGuiScreen((Screen) null);
										mc.displayGuiScreen(new LagpixelGUI(new StringTextComponent("LAGPICKLE")));
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
					event.addWidget(serverFunctionality);
				}
			}
			String truefalse = "Sprint: ";
			if (mc.gameSettings.toggleSprint == true) {
				truefalse += "§2TOGGLE";
			} else {
				truefalse += "§4HOLD";
			}
			Widget toggle = new Button((event.getGui().width / 2) - 102, (event.getGui().height / 2) + 24, 102, 20,
					new StringTextComponent(truefalse), new IPressable() {

						@Override
						public void onPress(Button arg0) {
							if (mc.gameSettings.toggleSprint == true) {
								mc.gameSettings.toggleSprint = false;
							} else {
								mc.gameSettings.toggleSprint = true;
							}
							try {
								mc.displayGuiScreen(mc.currentScreen);
							} catch (Exception e) {
								mc.displayGuiScreen((Screen) null);
							}
						}
					});
			event.addWidget(toggle);
			truefalse = "Sneak: ";
			if (mc.gameSettings.toggleCrouch == true) {
				truefalse += "§2TOGGLE";
			} else {
				truefalse += "§4HOLD";
			}
			toggle = new Button((event.getGui().width / 2) + 2, (event.getGui().height / 2) + 24, 102, 20,
					new StringTextComponent(truefalse), new IPressable() {

						@Override
						public void onPress(Button arg0) {
							if (mc.gameSettings.toggleCrouch == true) {
								mc.gameSettings.toggleCrouch = false;
							} else {
								mc.gameSettings.toggleCrouch = true;
							}
							try {
								mc.displayGuiScreen(mc.currentScreen);
							} catch (Exception e) {
								mc.displayGuiScreen((Screen) null);
							}
						}
					});
			event.addWidget(toggle);
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		MatrixStack matrixStack = new MatrixStack();
		int[] y = { 10, 10, 20, 30, 40, 50 };
		if (!((mc.gameSettings.hideGUI) || (mc.gameSettings.showDebugInfo)
				|| (mc.currentScreen instanceof XanditGUI))) {
			try {
				String coords = mc.player.getPosition().getCoordinatesAsString();
				mc.fontRenderer.drawStringWithShadow(matrixStack, "XYZ: ", 10, y[3], 16727296);
				mc.fontRenderer.drawStringWithShadow(matrixStack, coords, 35, y[3], 65280);
				mc.fontRenderer.drawStringWithShadow(matrixStack, "NAME: ", 10, y[2], 16727296);
				mc.fontRenderer.drawStringWithShadow(matrixStack, mc.getConnection().getGameProfile().getName(), 40,
						y[2], 65280);
				if (mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
					mc.fontRenderer.drawStringWithShadow(matrixStack, "PERSPECTIVE: ", 10, y[1], 16727296);
					mc.fontRenderer.drawStringWithShadow(matrixStack, "FIRST_PERSON", 80, y[1], 65280);
				} else if (mc.gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_BACK) {
					mc.fontRenderer.drawStringWithShadow(matrixStack, "PERSPECTIVE: ", 10, y[1], 16727296);
					mc.fontRenderer.drawStringWithShadow(matrixStack, "THIRD_PERSON", 80, y[1], 65280);
				} else if (mc.gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_FRONT) {
					mc.fontRenderer.drawStringWithShadow(matrixStack, "PERSPECTIVE: ", 10, y[1], 16727296);
					mc.fontRenderer.drawStringWithShadow(matrixStack, "SECOND_PERSON", 80, y[1], 65280);
				}
				mc.fontRenderer.drawStringWithShadow(matrixStack, "FPS: ", 10, y[4], 16727296);
				mc.fontRenderer.drawStringWithShadow(matrixStack, mc.debug.split("fps")[0], 35, y[4], 65280);
			} catch (Exception e) {
				mc.fontRenderer.drawStringWithShadow(matrixStack, "FPS: ", 10, y[0], 16727296);
				mc.fontRenderer.drawStringWithShadow(matrixStack, mc.debug.split("fps")[0], 35, y[0], 65280);
			}

		}
	}
}
