package com.aesireanempire.neiaddons

import codechicken.nei.api.{API, IConfigureNEI}
import codechicken.nei.guihook.GuiContainerManager
import org.lwjgl.input.Keyboard

class NEIConfig extends IConfigureNEI {
    override def loadConfig() = {
        API.addKeyBind("gui.materials_list", Keyboard.KEY_M)
        GuiContainerManager.addInputHandler(new MaterialInputHandler)
    }

    override def getName: String = "nei_useful_addons"

    override def getVersion: String = "1.0"
}
