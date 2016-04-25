package com.kitsuneindustries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = DeathWatch.MODID, name = DeathWatch.NAME, version = DeathWatch.VERSION, acceptableRemoteVersions = "*")
public class DeathWatch {

    public static final String MODID = "deathwatch";
    public static final String NAME = "Death Watch";
    public static final String VERSION = "2.1";
    
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
    
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    	MinecraftForge.EVENT_BUS.register(new com.kitsuneindustries.DeathWatch());
    }
    
    @SubscribeEvent(priority=EventPriority.NORMAL)
    public synchronized void onLivingDeathEvent(LivingDeathEvent event) throws IOException {
    	if(!event.entity.worldObj.isRemote) {
	    	if(event.entity instanceof EntityPlayerMP) {
	    		EntityPlayerMP player = (EntityPlayerMP)event.entity;
	    		Entity killer = event.source.getEntity(); // If there is a killer
	    		FileWriter output = this.getWriter();
	    		
	    		output.write(sdf.format(Calendar.getInstance().getTime())+","+player.getDisplayNameString()+","+player.dimension
	    				+","+player.posX+","+player.posY+","+player.posZ+","+event.source.damageType
	    				+(killer != null ? ","+killer.getName():"")
	    				+"\n");
	    		output.flush();
	    		System.out.println(sdf.format(Calendar.getInstance().getTime())+","+player.getDisplayNameString()+","+player.dimension+","+player.posX+","+player.posY+","+player.posZ+","+event.source.damageType);
	    	}
    	}
    }
}
