/****************************************
 *
 *  ProgressView.m
 *  Cordova ProgressView
 *
 *  Created by Sidney Bofah on 2014-12-01.
 *
 ****************************************/

#import <Cordova/CDV.h>
#import "MBProgressHUD.h"
#import "ProgressView.h"
#import "AppDelegate.h"

@implementation ProgressView


/**
 *  MACRO
 */

#define SCREEN_WIDTH ((([UIApplication sharedApplication].statusBarOrientation == UIInterfaceOrientationPortrait) || ([UIApplication sharedApplication].statusBarOrientation == UIInterfaceOrientationPortraitUpsideDown)) ? [[UIScreen mainScreen] bounds].size.width : [[UIScreen mainScreen] bounds].size.height)
#define SCREEN_HEIGHT ((([UIApplication sharedApplication].statusBarOrientation == UIInterfaceOrientationPortrait) || ([UIApplication sharedApplication].statusBarOrientation == UIInterfaceOrientationPortraitUpsideDown)) ? [[UIScreen mainScreen] bounds].size.height : [[UIScreen mainScreen] bounds].size.width)

/**
 *  CONSTANT
 */

static NSString *const _PROGRESSVIEW_STYLE_HORIZONTAL = @"BAR";
static NSString *const _PROGRESSVIEW_STYLE_CIRCLE = @"CIRCLE";
static const double _PROGRESSVIEW_UPDATE_INTERVAL = 1.5;


/**
 *  INIT
 */

- (void)pluginInitialize
{
    NSLog (@"(Cordova ProgressView) (Init) OK");
};


/***************************
 *  PUBLIC METHODS
 ***************************/

/**
 *  Show Dialog
 */

-(void)show:(CDVInvokedUrlCommand *)command {

    // Get Arguments
    NSString* label = [command.arguments objectAtIndex:0];
    NSString* shape = [command.arguments objectAtIndex:1];
    BOOL isIndeterminate = [[command.arguments objectAtIndex:2] boolValue];

    // Set Style
    [self showView:label isIndeterminate:isIndeterminate isShape:shape isVisible:YES];

    // Callback
    CDVPluginResult* pluginResult;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"(Cordova ProgressView) (Show) OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    //}];
};


-(void)update:(CDVInvokedUrlCommand *)command {

    if (!_progressView) {
        CDVPluginResult* pluginResult;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"(Cordova ProgressView) (Show) OK"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];        
        return;
    }

    // Get Arguments
    NSString* label = [command.arguments objectAtIndex:0];
    NSString* shape = [command.arguments objectAtIndex:1];

    if ([label length] == 0) {
        label = _progressView.labelText;
    }

    [self removeView];

    // Set Style
    [self showView:label isIndeterminate:NO isShape:shape isVisible:YES];

    // Callback
    CDVPluginResult* pluginResult;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"(Cordova ProgressView) (Show) OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    //}];
};

/**
 *  Set Progress
 */

- (void)setProgress:(CDVInvokedUrlCommand*)command
{
    // Init
    CDVPluginResult* pluginResult = nil;

    // Get Arguments
    CGFloat progress = [[command.arguments objectAtIndex:0] floatValue];

    // Execute
    [self updateProgress:progress];

    // Callback
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"(Cordova ProgressView) (setProgress) OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};


/**
 *  Set Label
 */

- (void)setLabel:(CDVInvokedUrlCommand*)command
{
    // Init
    CDVPluginResult* pluginResult = nil;

    NSString* label = [command.arguments objectAtIndex:0];

    // Execute
    [self updateLabel:label];

    // Callback
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"(Cordova ProgressView) (setLabel) OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};


/**
 *  Hide
 */

- (void)hide:(CDVInvokedUrlCommand*)command
{
    // Init
    CDVPluginResult* pluginResult;

    [self removeView];

    // Callback
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"(Cordova ProgressView) (Hide) OK"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
};



/***************************
 *  PRIVATE METHODS
 ***************************/


- (void)updateProgress:(CGFloat)progress
{

    _progressView.progress = progress;
};


- (void)updateLabel:(NSString *)viewLabel
{

    _progressView.labelText = viewLabel;
};


- (void)showView:(NSString *)viewLabel isIndeterminate:(BOOL)isIndeterminate isShape:(NSString *)shape isVisible:(BOOL)isVisible
{

    if ([shape isEqualToString:_PROGRESSVIEW_STYLE_HORIZONTAL]){
        _progressView = [MBProgressHUD showHUDAddedTo:self.webView.superview animated:YES];
        _progressView.mode = MBProgressHUDModeDeterminateHorizontalBar;
    } else {
        _progressView = [MBProgressHUD showHUDAddedTo:self.webView.superview animated:YES];
        _progressView.mode = MBProgressHUDModeIndeterminate;
    }

    _progressView.labelText = viewLabel;
};


- (void)removeView
{
    [_progressView hide:YES];
    _progressView = nil;
};


@end