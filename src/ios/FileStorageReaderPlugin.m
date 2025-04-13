/********* FileStorageReaderPlugin.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <WebKit/WebKit.h>

@interface FileStorageReaderPlugin : CDVPlugin <WKNavigationDelegate>
@property (nonatomic, strong) WKWebView* hiddenWebView;
@property (nonatomic, copy) NSString* callbackId;
@end

@implementation FileStorageReaderPlugin

- (void)readFileStorage:(CDVInvokedUrlCommand*)command {
    self.callbackId = command.callbackId;
    
    WKWebViewConfiguration* config = [[WKWebViewConfiguration alloc] init];
    config.preferences.javaScriptEnabled = YES;

    self.hiddenWebView = [[WKWebView alloc] initWithFrame:CGRectMake(0, 0, 1, 1) configuration:config];
    self.hiddenWebView.navigationDelegate = self;

    NSURL *url = [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"www/index" ofType:@"html"]];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];

    dispatch_async(dispatch_get_main_queue(), ^{
        [self.hiddenWebView loadRequest:request];
        [self.hiddenWebView setHidden:YES];
        [self.hiddenWebView setAlpha:0.01];
        [self.viewController.view addSubview:self.hiddenWebView];
    });
}

- (void)webView:(WKWebView*)webView didFinishNavigation:(WKNavigation*)navigation {
    NSString* js = @"(function() {"
                    "var out = {};"
                    "for (var i = 0; i < localStorage.length; i++) {"
                    "  var key = localStorage.key(i);"
                    "  out[key] = localStorage.getItem(key);"
                    "}"
                    "return JSON.stringify(out);"
                  "})();";

    [webView evaluateJavaScript:js completionHandler:^(id result, NSError* error) {
        if (error) {
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
        } else {
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
        }
        [webView removeFromSuperview];
    }];
}

@end