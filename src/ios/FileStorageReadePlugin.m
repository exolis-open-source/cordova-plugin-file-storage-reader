/********* FileStorageReaderPlugin.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface FileStorageReaderPlugin : CDVPlugin {
  // Member variables go here.
}

- (void)readFileStorage:(CDVInvokedUrlCommand*)command;
@end

@implementation FileStorageReaderPlugin

- (void)flush:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end