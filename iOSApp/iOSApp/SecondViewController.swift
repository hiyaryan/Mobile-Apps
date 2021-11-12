//
//  SecondViewController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/11/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved.
//

import UIKit

class SecondViewController: UIViewController {

    @IBOutlet weak var consoleTextView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    // This method is called when this view is transitioning into the foreground.
    // Action: Transition from Place Description to Console.
    override func viewWillAppear(_ animated: Bool) {
        print("\nAppDelegate:\n\tSecondViewController-viewWillAppear\n")
    }
    
    // This method is called when this view successfuly opens into the foreground.
    // Action: Transition from Place Description to Console.
    override func viewDidAppear(_ animated: Bool) {
        print("\nAppDelegate:\n\tSecondViewController-viewDidAppear\n")
    }
}
