//
//  PlaceAdderController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/17/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved. The SER423 Instructional Team and Arizona State University
//  have the right to build and evaluate this software package for the purposes of grading and program assessment.
//
//  @author Ryan Meneses     mailto: rmenese1@asu.edu
//  @version 1.0
//  @since November 17, 2021
//

import UIKit

class PlaceAdderController: UIViewController, UITextViewDelegate, UITextFieldDelegate {

    // This dict represents the data structure of a place object.
    var place: [String: Any] = [
        "address-title": "ASU Software Engineering",
        "address-street": "7171 E varoran Arroyo Mall\nPeralta Hall 230\nMesa AZ 85212",
        "elevation": 1384.0,
        "latitude": 33.306388,
        "longitude": -111.679121,
        "name": "ASU-Poly",
        "image": "asuwest",
        "description": "Home of ASU's Software Engineering Programs",
        "category": "School"
    ]
    
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var categoryTextField: UITextField!
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var streetTextView: UITextView!
    @IBOutlet weak var elevationTextField: UITextField!
    @IBOutlet weak var latitudeTextField: UITextField!
    @IBOutlet weak var longitudeTextField: UITextField!
    
    // Setup the text fields and views with with event listeners and accessibility labels
    override func viewDidLoad() {
        super.viewDidLoad()
        
        nameTextField.delegate = self
        nameTextField.accessibilityLabel = "name"
        
        descriptionTextView.delegate = self
        descriptionTextView.accessibilityLabel = "description"
        
        categoryTextField.delegate = self
        categoryTextField.accessibilityLabel = "category"
        
        titleTextField.delegate = self
        titleTextField.accessibilityLabel = "title"
        
        streetTextView.delegate = self
        streetTextView.accessibilityLabel = "street"
        
        elevationTextField.delegate = self
        elevationTextField.accessibilityLabel = "elevation"
        
        latitudeTextField.delegate = self
        latitudeTextField.accessibilityLabel = "latitude"
        
        longitudeTextField.delegate = self
        longitudeTextField.accessibilityLabel = "longitude"
        
        NSLog("PlaceAdderController: viewDidLoad\n")
        // Do any additional setup after loading the view.
    }
    
    // Images are not used in this project, before view disappears set the image
    // element to N/A.
    override func viewWillDisappear(_ animated: Bool) {
        NSLog("PlaceAdderController: viewWillDisappear\n")
    }
    
    // This function updates the Description field
    func textViewDidChange(_ textView: UITextView) {
        switch textView.accessibilityLabel {
        case "description":
            // print("DESCRIPTION:\n\t\(textView.text!)")
            place["description"] = textView.text!

        case "street":
            // print("STREET:\n\t\(textView.text!)")
            place["address-street"] = textView.text!

        default:
            print("No text view selected.")
        }
    }
    
    // Set the place elements while the user updates the text fields
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        switch textField.accessibilityLabel {
        case "name":
            // print("NAME:\n\t\(textField.text!)")
            place["name"] = textField.text!
            
        case "category":
            // print("CATEGORY:\n\t\(textField.text!)")
            place["category"] = textField.text!
            
        case "title":
            // print("TITLE:\n\t\(textField.text!)")
            place["address-title"] = textField.text!
            
        case "elevation":
            // print("ELEVATION:\n\t\(textField.text!)")
            if Double(textField.text!) != nil  {
                place["elevation"] = Double(textField.text!)
            } else {
                place["elevation"] = 0.00
            }
            
        case "latitude":
            // print("LATITUDE:\n\t\(textField.text!)")
            if Double(textField.text!) != nil  {
                place["latitude"] = Double(textField.text!)
            } else {
                place["latitude"] = 0.00
            }

        case "longitude":
            // print("LONGITUDE:\n\t\(textField.text!)")
            if Double(textField.text!) != nil  {
                place["longitude"] = Double(textField.text!)
            } else {
                place["longitude"] = 0.00
            }
            
        default:
            print("No text field selected.")
            return false
        }
        
        return true
    }

    
    // Prepare for segue from this (PlaceDescription) View to PlaceListings View
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let placeListings = segue.destination as? PlaceListingsController {
            placeListings.place = self.place
            placeListings.isNewPlace = true
        }
    }
}
