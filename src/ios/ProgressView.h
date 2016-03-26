/****************************************
 *
 *  ProgressView.h
 *  Cordova ProgressView
 *
 *  Created by Sidney Bofah on 2014-11-20.
 *
 ****************************************/

#import <Cordova/CDV.h>
#import "MBProgressHUD.h"


@interface ProgressView: CDVPlugin {
    MBProgressHUD *_progressView;
    BOOL _isVisible;
    NSString* _labelDefault;
    CGFloat _progressDefault;
}

- (void)show:(CDVInvokedUrlCommand*)command;
- (void)update:(CDVInvokedUrlCommand*)command;
- (void)setProgress:(CDVInvokedUrlCommand*)command;
- (void)setLabel:(CDVInvokedUrlCommand*)command;
- (void)hide:(CDVInvokedUrlCommand*)command;

@end