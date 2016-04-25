package org.ponyelisium;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.server.FMLServerHandler;

@Mod(modid = DeathWatch.MODID, name = DeathWatch.NAME, version = DeathWatch.VERSION, acceptableRemoteVersions = "*")
public class DeathWatch {

    public static final String MODID = "deathwatch";
    public static final String NAME = "Death Watch";
    public static final String VERSION = "1.1";
    
    public static FileWriter file = null;
    public static File filepath = null;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss");
    
    public static FileWriter getWriter() throws IOException {
    	if (filepath == null) {
    		filepath = new File(DimensionManager.getCurrentSaveRootDirectory(),"deathwatch.csv");
    	}
    	if (file == null) {
    		file = new FileWriter(filepath,true);
    	}
    	return file;
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    	MinecraftForge.EVENT_BUS.register(new org.ponyelisium.DeathWatch());
    }
    
    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onLivingDeathEvent(LivingDeathEvent event) throws IOException {
    	if(!event.entity.worldObj.isRemote) {
	    	if(event.entity instanceof EntityPlayerMP) {
	    		EntityPlayerMP player = (EntityPlayerMP)event.entity;
	    		FileWriter output = this.getWriter();
	    		output.write(sdf.format(Calendar.getInstance().getTime())+","+player.getDisplayName()+","+player.dimension+","+player.posX+","+player.posY+","+player.posZ+","+event.source.damageType+"\n");
	    		output.flush();
	    		System.out.println(sdf.format(Calendar.getInstance().getTime())+","+player.getDisplayName()+","+player.dimension+","+player.posX+","+player.posY+","+player.posZ+","+event.source.damageType);
	    	}
    	}
    }
}
