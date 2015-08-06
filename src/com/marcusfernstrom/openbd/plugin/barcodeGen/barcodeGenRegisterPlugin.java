package com.marcusfernstrom.openbd.plugin.barcodeGen;

import com.bluedragon.plugin.Plugin;
import com.bluedragon.plugin.PluginManagerInterface;
import com.naryx.tagfusion.xmlConfig.xmlCFML;

public class barcodeGenRegisterPlugin  implements Plugin {

	public String getPluginDescription() {
		return "BarcodeGen";
	}

	public String getPluginName() {
		return "BarcodeGen";
	}

	public String getPluginVersion() {
		return "0.1";
	}

	public void pluginStart(PluginManagerInterface manager, xmlCFML systemParameters) {
		manager.registerFunction( "barcode", "com.marcusfernstrom.openbd.plugin.barcodeGen.MainFunction" );
	}

	public void pluginStop(PluginManagerInterface manager) {}
}