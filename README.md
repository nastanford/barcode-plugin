# barcode-plugin
OpenBD plugin for generating barcodes and QR codes

## Usage
barcode( path, type, string, resolution );
Resolution is optional, and defaults to 200
  
## Example
barcode( expandPath('myQRcode.jpg'), 'qr', 'http://openbd.org/', 200);

## About
This plugin uses Barcode4J and ZXing to generate barcodes, it saves the file to disk and returns a boolean true if success. If there's an error, it throws an exception which includes the error cause.

## Barcode types
I haven't tested them all, but it should support all types that Barcode4J supports, as well as QR codes.

I'll create a list here with the known supported types.

## Installation
Just drop the .jar files into your WEB-INF/lib directory and restart your server.

openbdplugin-barcode.jar
zxing-core-3.2.0.jar
avalon-framework-4.2.0.jar
barcode4j.jar
