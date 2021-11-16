//
//  PlaceDescriptionController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/1/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved.
//

import UIKit

class PlaceDescriptionController: UIViewController, UITextViewDelegate {
    
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
    
    // Name Text Field
    @IBOutlet weak var nameButton: UIButton!
    
    // Description Text View
    @IBOutlet weak var descriptionTextView: UITextView!
    
    // Category Text Field
    @IBOutlet weak var categoryTextField: UITextField!
    
    // Title Text Field
    @IBOutlet weak var titleTextView: UITextField!
    
    // Street Text View
    @IBOutlet weak var streetTextView: UITextView!
    
    // Elevation Text Field
    @IBOutlet weak var elevationTextField: UITextField!
    
    // Latitude Text Field
    @IBOutlet weak var latitudeTextField: UITextField!
    
    // Longitude Text Field
    @IBOutlet weak var longitudeTextField: UITextField!
    
    // Anytime the view is loaded each place field is updated
    override func viewDidLoad() {
        super.viewDidLoad()
        descriptionTextView.delegate = self
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // This function updates the Description field
    func textViewDidChange(_ textView: UITextView) {
        // print(textView.text!)
        place["description"] = textView.text!
    }
    
    // This method is called when this view is transitioning into the foreground.
    // Action: Transition from Place Listings to Place Description.
    override func viewWillAppear(_ animated: Bool) {
        print("\nPlaceDescriptionController:\n\tviewWillAppear\n")
        
        let name: String? = (place["name"] as! String)
        nameButton.setTitle(name!, for: .normal)
        
        let description: String? = (place["description"] as! String)
        descriptionTextView.text = description!
        
        let category: String? = (place["category"] as! String)
        categoryTextField.text = category!
        
        let title: String? = (place["address-title"] as! String)
        titleTextView.text = title!
        
        let street: String? = (place["address-street"] as! String)
        streetTextView.text = street!
        
        let elevation: Double? = (place["elevation"] as! Double)
        elevationTextField.text = String(elevation!)
        
        let latitude: Double? = (place["latitude"] as! Double)
        latitudeTextField.text = String(latitude!)
        
        let longitude: Double? = (place["longitude"] as! Double)
        longitudeTextField.text = String(longitude!)
    }
    
    // This method is called when this view successfuly opens into the foreground.
    // Action: Transition from Place Listings to Place Description.
    override func viewDidAppear(_ animated: Bool) {
        print("\nPlaceDescriptionController:\n\tviewDidAppear\n")
    }
    
    // This method is called when this view is transitioning into the background.
    // Action: Transition to Place Listings from Place Description.
    override func viewWillDisappear(_ animated: Bool) {
        print("\nPlaceDescriptionController:\n\tviewWillDisappear\n")
        
    }
    
    // This method is called when this view sucessfully goes into the background.
    // Action: Transition to Place Listings from Place Description.
    override func viewDidDisappear(_ animated: Bool) {
        print("\nPlaceDescriptionController:\n\tviewDidDisappear\n")
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let placeListings = segue.destination as? PlaceListingsController {
            placeListings.place = self.place
        }
    }
    
    @IBAction func savePlace(segue: UIStoryboardSegue) {
        let placeListingsController = segue.source as! PlaceListingsController        
        place = placeListingsController.place!
    }
}
