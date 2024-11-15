/* SPDX-License-Identifier: MIT */

package li.cil.oc2r.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import li.cil.oc2r.client.gui.terminal.TerminalInput;
import li.cil.oc2r.common.container.AbstractMachineTerminalContainer;
import li.cil.oc2r.common.vm.Terminal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

@OnlyIn(Dist.CLIENT)
public final class MachineTerminalWidget {
    private static final int TERMINAL_WIDTH = Terminal.WIDTH * Terminal.CHAR_WIDTH / 2;
    private static final int TERMINAL_HEIGHT = Terminal.HEIGHT * Terminal.CHAR_HEIGHT / 2;

    private static final int MARGIN_SIZE = 8;
    private static final int TERMINAL_X = MARGIN_SIZE;
    private static final int TERMINAL_Y = MARGIN_SIZE;

    public static final int WIDTH = Sprites.TERMINAL_SCREEN.width;
    public static final int HEIGHT = Sprites.TERMINAL_SCREEN.height;

    ///////////////////////////////////////////////////////////////////

    private final AbstractMachineTerminalScreen<?> parent;
    private final AbstractMachineTerminalContainer container;
    private final Terminal terminal;
    private int leftPos, topPos;
    private boolean isMouseOverTerminal;
    private Terminal.RendererView rendererView;

    ///////////////////////////////////////////////////////////////////

    public MachineTerminalWidget(final AbstractMachineTerminalScreen<?> parent) {
        this.parent = parent;
        this.container = this.parent.getMenu();
        this.terminal = this.container.getTerminal();
    }

    public void renderBackground(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        isMouseOverTerminal = isMouseOverTerminal(mouseX, mouseY);

        Sprites.TERMINAL_SCREEN.draw(graphics, leftPos, topPos);

        if (shouldCaptureInput()) {
            Sprites.TERMINAL_FOCUSED.draw(graphics, leftPos, topPos);
        }
    }

    public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, @Nullable final Component error) {
        if (container.getVirtualMachine().isRunning()) {
            final PoseStack terminalStack = new PoseStack();
            terminalStack.translate(leftPos + TERMINAL_X, topPos + TERMINAL_Y, 0);
            terminalStack.scale(TERMINAL_WIDTH / (float) terminal.getWidth(), TERMINAL_HEIGHT / (float) terminal.getHeight(), 1f);

            if (rendererView == null) {
                rendererView = terminal.getRenderer();
            }

            //final Matrix4f projectionMatrix = orthographic(0, parent.width, 0, parent.height, -10, 10f);
            final Matrix4f projectionMatrix = (new Matrix4f()).setOrtho(0, parent.width, parent.height, 0, -10f, 10f);
            rendererView.render(terminalStack, projectionMatrix);
        } else {
            final Font font = getClient().font;
            if (error != null) {
                final int textWidth = font.width(error);
                final int textOffsetX = (TERMINAL_WIDTH - textWidth) / 2;
                final int textOffsetY = (TERMINAL_HEIGHT - font.lineHeight) / 2;
                drawShadow(
                    font,
                    graphics,
                    error,
                    leftPos + TERMINAL_X + textOffsetX,
                    topPos + TERMINAL_Y + textOffsetY,
                    0xEE3322
                );
            }
        }
    }

    private void drawShadow(Font font, GuiGraphics graphics, Component text, float x, float y, int color) {
        var batch = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(text, x, y, color, true, graphics.pose().last().pose(), batch, Font.DisplayMode.NORMAL, 0, 15728880);
        batch.endBatch();
    }

    private static Matrix4f orthographic(float pMinX, float pMaxX, float pMinY, float pMaxY, float pMinZ, float pMaxZ) {
        Matrix4f matrix4f = new Matrix4f();
        float f = pMaxX - pMinX;
        float f1 = pMinY - pMaxY;
        float f2 = pMaxZ - pMinZ;
        matrix4f.set(
            2.0F / f, 0, 0, -(pMaxX + pMinX) / f,
            0, 2.0F / f1, 0, -(pMinY + pMaxY) / f1,
            0, 0, -2.0F / f2, -(pMaxZ + pMinZ) / f2,
            0, 0, 0, 1.0F
        );
        return matrix4f;
    }

    public void tick() {
        final ByteBuffer input = terminal.getInput();
        if (input != null) {
            container.sendTerminalInputToServer(input);
        }
    }
    
    public boolean mouseClicked( double x, double y, int button) {
        int mx = (int) x;
        int my = (int) y;
        int sx = (int)((x - MachineTerminalWidget.TERMINAL_X) / MachineTerminalWidget.TERMINAL_WIDTH * Terminal.WIDTH);
        int sy = (int)((y - MachineTerminalWidget.TERMINAL_Y) / MachineTerminalWidget.TERMINAL_HEIGHT * Terminal.HEIGHT);
        boolean overTerminal = isMouseOverTerminal(mx, my);
        if (overTerminal && shouldCaptureInput()) {
            terminal.putInput((byte) 0x1b);
            terminal.putInput((byte) '[');
            terminal.putInput((byte) button);
            terminal.putInput((byte) sx);
            terminal.putInput((byte) sy);
            return true;
        }
        return false;
    }
    
    public boolean mouseReleased( double x, double y, int button) {
        int mx = (int) x;
        int my = (int) y;
        int sx = (int)((x - MachineTerminalWidget.TERMINAL_X) / MachineTerminalWidget.TERMINAL_WIDTH * Terminal.WIDTH);
        int sy = (int)((y - MachineTerminalWidget.TERMINAL_Y) / MachineTerminalWidget.TERMINAL_HEIGHT * Terminal.HEIGHT);
        boolean overTerminal = isMouseOverTerminal(mx, my);
        if (overTerminal && shouldCaptureInput()) {
            terminal.putInput((byte) 0x1b);
            terminal.putInput((byte) '[');
            terminal.putInput((byte) 3);
            terminal.putInput((byte) sx);
            terminal.putInput((byte) sy);
            return true;
        }
        return false;
    }

    public boolean charTyped(final char ch, final int modifier) {
        if (modifier == 0 || modifier == GLFW.GLFW_MOD_SHIFT) {
            terminal.putInput((byte) ch);
        }
        return true;
    }

    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (!shouldCaptureInput() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            return false;
        }

        if ((modifiers & GLFW.GLFW_MOD_CONTROL) != 0 && keyCode == GLFW.GLFW_KEY_V) {
            final String value = getClient().keyboardHandler.getClipboard();
            for (final char ch : value.toCharArray()) {
                terminal.putInput((byte) ch);
            }
        } else {
            final byte[] sequence = TerminalInput.getSequence(keyCode, modifiers);
            if (sequence != null) {
                for (final byte b : sequence) {
                    terminal.putInput(b);
                }
            }
        }

        return true;
    }

    public void init() {
        this.leftPos = (parent.width - WIDTH) / 2;
        this.topPos = (parent.height - HEIGHT) / 2;

        //getClient().keyboardHandler.setSendRepeatsToGui(true);
    }

    public void onClose() {
        //getClient().keyboardHandler.setSendRepeatsToGui(false);
        if (rendererView != null) {
            terminal.releaseRenderer(rendererView);
            rendererView = null;
        }
    }

    ///////////////////////////////////////////////////////////////////

    private Minecraft getClient() {
        return parent.getMinecraft();
    }

    private boolean shouldCaptureInput() {
        return isMouseOverTerminal && AbstractMachineTerminalScreen.isInputCaptureEnabled() &&
            container.getVirtualMachine().isRunning();
    }

    private boolean isMouseOverTerminal(final int mouseX, final int mouseY) {
        return parent.isMouseOver(mouseX, mouseY,
            MachineTerminalWidget.TERMINAL_X, MachineTerminalWidget.TERMINAL_Y,
            MachineTerminalWidget.TERMINAL_WIDTH, MachineTerminalWidget.TERMINAL_HEIGHT);
    }
}
