package com.aesireanempire.neiaddons

import codechicken.nei.NEIClientUtils
import codechicken.nei.recipe.{GuiCraftingRecipe, ICraftingHandler}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConverters._
import scala.collection.mutable

object GuiMaterialList {
    def openGui(outputId: String, stack: ItemStack): Boolean = {
        val mc: Minecraft = NEIClientUtils.mc

        mc.displayGuiScreen(new GuiMaterialList(stack))
        true
    }
}

class GuiMaterialList(itemStack: ItemStack) extends GuiScreen {
    var materialStacks = mutable.HashMap.empty[String, Int]

    getRawMaterials(itemStack)

    def getRawMaterials(itemStack: ItemStack): Unit = {
        val handlers: Array[ICraftingHandler] = getCraftingHandlers("item", itemStack.copy())

        if (handlers.nonEmpty) {
            val handler = handlers.head
            val stacks = handler.getIngredientStacks(0)

            for (stack <- stacks.asScala) {
                if (!rawMaterial(stack.item)) {
                    getRawMaterials(stack.item)
                } else {
                    addStackToList(stack.item)
                }
            }
        } else {
            addStackToList(itemStack)
        }
    }

    private def addStackToList(itemStack: ItemStack) = {
        var name = itemStack.getUnlocalizedName
        val oreIds = OreDictionary.getOreIDs(itemStack)
        if (oreIds.length > 0) {
            name = OreDictionary.getOreName(oreIds(0))
        }
        materialStacks.put(name, materialStacks.getOrElse(name, 0) + 1)
    }

    def rawMaterial(itemStack: ItemStack): Boolean = {
        for (id <- OreDictionary.getOreIDs(itemStack)) {
            val dustName = OreDictionary.getOreName(id)
            val oreName = dustName.replace("dust", "ore").replace("ingot", "ore")

            if (OreDictionary.getOreID(oreName) != -1) return true
        }
        false
    }

    def getCraftingHandlers(outputId: String, stack: ItemStack): Array[ICraftingHandler] = {
        var handlers: Array[ICraftingHandler] = Array.empty[ICraftingHandler]

        for (craftinghandler <- GuiCraftingRecipe.craftinghandlers.asScala) {
            val handler: ICraftingHandler = craftinghandler.getRecipeHandler(outputId, stack)
            if (handler.numRecipes > 0) {
                handlers = handlers :+ handler
            }
        }
        handlers
    }

    override def drawScreen(p_73863_1_ : Int, p_73863_2_ : Int, p_73863_3_ : Float) {
        val itemStackName = getLocalizedName(itemStack.getUnlocalizedName)

        drawCenteredString(mc.fontRenderer, "Raw materials needed to construct: " + itemStackName, width / 2, 10, 0xffff0000)

        var index = 0
        for (stack <- materialStacks.keysIterator) {
            drawCenteredString(mc.fontRenderer, materialStacks.get(stack).get + " " + getLocalizedName(stack), width / 2, 30 + 10 * index, 0xffff0000)
            index = index + 1
        }
    }

    def getLocalizedName(name: String): String = {
        if (name.contains(".")) {
            return ("" + StatCollector.translateToLocal(name + ".name")).trim
        }
        name
    }
}
