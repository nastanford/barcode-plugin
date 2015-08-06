/* 
 *  Copyright (C) 2015, Marcus Fernstrom
 *  
 *  License: GPL V3, full license on Github.
 *  
 *  Wrapper plugin for creating barcodes using Barcode4J and ZXING.
 *  
 *  I'm new to both Java and creating plugins, use at your own risk.
 */

package com.marcusfernstrom.openbd.plugin.barcodeGen;

import com.naryx.tagfusion.cfm.engine.cfArgStructData;
import com.naryx.tagfusion.cfm.engine.cfBooleanData;
import com.naryx.tagfusion.cfm.engine.cfData;
import com.naryx.tagfusion.cfm.engine.cfSession;
import com.naryx.tagfusion.cfm.engine.cfmRunTimeException;
import com.naryx.tagfusion.expression.function.functionBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Hashtable;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class MainFunction extends functionBase {
	private static final long serialVersionUID = 1L;
	
	
	
	// 4 variables, resolution is optional
	public MainFunction() {
		min = 3;
		max = 4;
		setNamedParams( new String[]{ "path","type","string","resolution"} );
	}
	
	
	
	public java.util.Map getInfo(){
		return makeInfo(
				"Barcode Generator", 
				"Uses Barcode4J to generate barcode images.",
				ReturnType.BOOLEAN );
	}
	
	
	
	@Override
	public cfData execute( cfSession _session, cfArgStructData argStruct ) throws cfmRunTimeException {
		// Grab passed in arguments, and set up defaults
		String thePath  	= getNamedStringParam( argStruct, "path" , "" );
		String theType 		= getNamedStringParam( argStruct, "type" , "" );
		String theString 	= getNamedStringParam( argStruct, "string" , "" );
		int	resolution		= getNamedIntParam( argStruct, "resolution", 200 );
		
		try( OutputStream fout = new FileOutputStream(thePath) ) {
			if( theType.equals( "qr") ){
			  String qrCodeText = theString;
			  String filePath 	= thePath;
			  String fileType 	= "jpg";
			  File qrFile 			= new File(filePath);
			  createQRImage(qrFile, qrCodeText, resolution, fileType);
			} else {
				// Initialize Barcode4J things
				BarcodeUtil util = BarcodeUtil.getInstance();
		    BarcodeGenerator gen;
	    
	  		// Create barcode
				gen = util.createBarcodeGenerator( buildCfg(theType) );
		    BitmapCanvasProvider canvas = new BitmapCanvasProvider( fout, "image/jpeg", resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0 );
		    gen.generateBarcode( canvas, theString );
		    canvas.finish();
			}
			
		} catch( Exception e) {
			throwException( _session, "An error occured when trying to generate barcode: " + e.getMessage() );
		}
		
		// If we get here, all is well and we just return a boolean true
		return cfBooleanData.getcfBooleanData(true);
	}
	
	
	
	// This function totally stolen from example at http://bethecoder.com/applications/tutorials/barcodes/barcode4j/datamatrix-barcode.html
	private static Configuration buildCfg(String type) {
    DefaultConfiguration cfg = new DefaultConfiguration("barcode");

    //Bar code type
    DefaultConfiguration child = new DefaultConfiguration(type);
      cfg.addChild(child);
    
      //Human readable text position
      DefaultConfiguration attr = new DefaultConfiguration("human-readable");
      DefaultConfiguration subAttr = new DefaultConfiguration("placement");
        subAttr.setValue("bottom");
        attr.addChild(subAttr);
        
        child.addChild(attr);
    return cfg;
  }
	
	
	
	private static void createQRImage(File qrFile, String qrCodeText, int size,
		   String fileType) throws WriterException, IOException {
		  // Create the ByteMatrix for the QR-Code that encodes the given String
		  Hashtable hintMap = new Hashtable();
		  hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		  QRCodeWriter qrCodeWriter = new QRCodeWriter();
		  BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
		    BarcodeFormat.QR_CODE, size, size, hintMap);
		  // Make the BufferedImage that are to hold the QRCode
		  int matrixWidth = byteMatrix.getWidth();
		  BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
		    BufferedImage.TYPE_INT_RGB);
		  image.createGraphics();

		  Graphics2D graphics = (Graphics2D) image.getGraphics();
		  graphics.setColor(Color.WHITE);
		  graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		  // Paint and save the image using the ByteMatrix
		  graphics.setColor(Color.BLACK);

		  for (int i = 0; i < matrixWidth; i++) {
		   for (int j = 0; j < matrixWidth; j++) {
		    if (byteMatrix.get(i, j)) {
		     graphics.fillRect(i, j, 1, 1);
		    }
		   }
		  }
		  ImageIO.write(image, fileType, qrFile);
		 }
}