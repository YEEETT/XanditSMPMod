package me.Fake_Name131.whatever.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class XanditGUI extends Screen {

	String h1 = "";
	String h2 = "";
	
	public XanditGUI(ITextComponent titleIn, String house1, String house2) {
		super(titleIn);
		h1 = house1;
		h2 = house2;
		
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		XanditGUI.drawCenteredString(ms, this.font, this.title, this.width / 2, 40, 0xFFFFFF);
		if (h1 == "") {
			this.drawCenteredString(ms, this.font, "Homes have not loaded properly! Please reopen this GUI!", this.width / 2, 10, 0xFF0000);
		}
		super.render(ms, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		Widget button = new Button((this.width / 2) - 105, (this.height / 2) - 75, 100, 20,
				new StringTextComponent("Sell"), new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.player.sendChatMessage(":sell");
						minecraft.player.rotationYaw = 45.0F;
						minecraft.player.rotationPitch = 0.0F;
						minecraft.displayGuiScreen((Screen) null);
					}
				});
		this.addButton(button);
		button = new Button((this.width / 2) + 5, (this.height / 2) - 75, 100, 20, new StringTextComponent("Shop"),
				new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.player.sendChatMessage(":shop");
						minecraft.player.rotationYaw = -45.0F;
						minecraft.player.rotationPitch = 0.0F;
						minecraft.displayGuiScreen((Screen) null);
					}
				});
		this.addButton(button);
		button = new Button((this.width / 2) - 105, (this.height / 2) - 50, 100, 20, new StringTextComponent("Spawn"),
				new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.player.sendChatMessage(":spawn");
						minecraft.player.rotationYaw = 180.0F;
						minecraft.player.rotationPitch = 0.0F;
						minecraft.displayGuiScreen((Screen) null);
					}
				});
		this.addButton(button);
		button = new Button((this.width / 2) + 5, (this.height / 2) - 50, 100, 20, new StringTextComponent("PvP"),
				new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.player.sendChatMessage(":pvp");
						minecraft.displayGuiScreen((Screen) null);
					}
				});
		this.addButton(button);
		button = new Button((this.width / 2) - 105, (this.height / 2) - 25, 100, 20,
				new StringTextComponent(h1), new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.player.sendChatMessage("/home " + h1);
						minecraft.displayGuiScreen((Screen) null);
					}
				});
		this.addButton(button);
		button = new Button((this.width / 2) + 5, (this.height / 2) - 25, 100, 20,
				new StringTextComponent(h2), new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.player.sendChatMessage("/home " + h2);
						minecraft.displayGuiScreen((Screen) null);
					}
				});
		this.addButton(button);
		button = new Button((this.width / 2) + -105, (this.height / 2), 210, 20, new StringTextComponent("Done"),
				new IPressable() {

					@Override
					public void onPress(Button arg0) {
						minecraft.displayGuiScreen(new IngameMenuScreen(true));
					}
				});
		this.addButton(button);
	}

}
