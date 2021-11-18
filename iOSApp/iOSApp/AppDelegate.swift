//
//  AppDelegate.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/1/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved.
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    // On launch, copy places.json from bundle to Caches directory.
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        // This method is called when the app is launched.
        // Action: Launch app from the home screen
        NSLog("AppDelegate: didFinishLaunchingWithOptions\n")
        
        // Set places url to places.json resource in bundle
        let places = Bundle.main.url(forResource: "places", withExtension: "json")!
        
        // Set url to Caches directory where places.json will be copied to
        let url = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first!
            .appendingPathComponent("places.json", isDirectory: false)
        
        // If places.json already exists, it will not be overwritten
        do {
            try FileManager.default.copyItem(at: places, to: url)
            print("Successfully copied file to Caches directory.\n")
        }
        catch {
            print("File exists in Caches directory.\n")
        }
        
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
        
        // This method is called at the instant the app undergoes entry into the background or is disrupted.
        // Action: Press the home button.
        NSLog("AppDelegate: applicationWillResignActive\n")
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        
        // This method is called when the app enters into the background.
        // Action: Press the home button to leave the app.
        NSLog("AppDelegate: applicationDidEnterBackground\n")
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
        
        // This method is called when the app is reopened from the background into the foreground.
        // Action: Double click the home button and reopen app.
        NSLog("AppDelegate: applicationWillEnterForeground\n")
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        
        // This method is called at the instant the app enters into the foreground.
        // Action: Launch or open the app.
        NSLog("AppDelegate: applicationDidBecomeActive\n")
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        
        // This method is called when the application is terminated.
        // Action: Double click the home button and swipe up on the app to close it.
        NSLog("AppDelegate: applicationWillTerminate\n")
    }
}
