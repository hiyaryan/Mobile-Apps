//
//  ViewController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/1/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    // Name Text Field
    @IBOutlet weak var nameTextField: UITextField!
    
    // Description Text View
    @IBOutlet weak var descriptionTextView: UITextView!
    
    // Category Text Field
    @IBOutlet weak var categoryTextField: UITextField!
    
    // Title Text View
    @IBOutlet weak var titleTextView: UITextField!
    
    // Street Text View
    @IBOutlet weak var streetTextView: UITextView!
    
    // Elevation Text Field
    @IBOutlet weak var elevationTextField: UITextField!
    
    // Latitude Text Field
    @IBOutlet weak var latitudeTextField: UITextField!
    
    // Longitude Text Field
    @IBOutlet weak var longitudeTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        // EXAMPLE
        // To update tect in a text field:
        // nameTextField.text = "HELLO"
        // To print text from text field:
        // print(nameTextField.text!)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // This method is called when this view is transitioning into the background.
    // Action: Transition to Console from Place Description.
    override func viewWillDisappear(_ animated: Bool) {
        print("\nAppDelegate:\n\tViewController-viewWillDisappear\n")
    }
    
    // This method is called when this view sucessfully goes into the background.
    // Action: Transition to Console from Place Description.
    override func viewDidDisappear(_ animated: Bool) {
        print("\nAppDelegate:\n\tViewController-viewDidDisappear\n")
    }
}
