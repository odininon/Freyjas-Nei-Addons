package com.aesireanempire.neiaddons

import codechicken.nei.NEIClientConfig
import codechicken.nei.guihook.{GuiContainerManager, IContainerInputHandler}
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack

class MaterialInputHandler extends IContainerInputHandler {
    override def keyTyped(gui: GuiContainer, keyChar: Char, keyCode: Int): Boolean = false

    override def lastKeyTyped(gui: GuiContainer, keyChar: Char, keyID: Int): Boolean = {
        val stackover: ItemStack = GuiContainerManager.getStackMouseOver(gui)

        if (stackover == null) {
            return false
        }

        if (keyID == NEIClientConfig.getKeyBinding("gui.materials_list")) {
            return GuiMaterialList.openGui("item", stackover.copy)
        }

        false
    }

    override def mouseClicked(gui: GuiContainer, mousex: Int, mousey: Int, button: Int): Boolean = false

    override def onMouseDragged(gui: GuiContainer, mousex: Int, mousey: Int, button: Int, heldTime: Long): Unit = {}

    override def onMouseClicked(gui: GuiContainer, mousex: Int, mousey: Int, button: Int): Unit = {}

    override def onMouseScrolled(gui: GuiContainer, mousex: Int, mousey: Int, scrolled: Int): Unit = {}

    override def mouseScrolled(gui: GuiContainer, mousex: Int, mousey: Int, scrolled: Int): Boolean = false

    override def onKeyTyped(gui: GuiContainer, keyChar: Char, keyID: Int): Unit = {}

    override def onMouseUp(gui: GuiContainer, mousex: Int, mousey: Int, button: Int): Unit = {}
}
