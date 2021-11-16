//
//  PlaceListingsController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/11/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved.
//

import UIKit

class PlaceListingsController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var contentView: UIView!
    
    // This variable is used to populate the cells in the TableView
    var names: [String] = []
    
    // This variable is updated on prepare segue from PlaceDescriptionController
    var place: [String: Any]?
    var places: Dictionary<String, Any>?
    
    var selection: String?
    
    override func viewDidLoad() {
        tableView.dataSource = self
        tableView.delegate = self
        
        print("\nPlaceListingsController:\n\tviewDidLoad\n")
        // Do any additional setup after loading the view.
        
        if let path = Bundle.main.path(forResource: "places", ofType: "json") {
            do {
                let data = try Data(contentsOf: URL(fileURLWithPath: path))
                places = try JSONSerialization.jsonObject(with: data) as? Dictionary<String, Any>
                
                for place in places! {
                    names.append(place.key)
                }
            } catch {
                print("Error parsing file.")
            }
        } else {
            print("File not found.")
        }
    }

    // This method is called when this view is transitioning into the foreground.
    // Action: Transition from Place Description to Place Listings.
    override func viewWillAppear(_ animated: Bool) {
        print("\nPlaceListingsController:\n\tviewWillAppear\n")
        print(place?["description"] as! String)
    }
    
    // This method is called when this view successfuly opens into the foreground.
    // Action: Transition from Place Description to Place Listings.
    override func viewDidAppear(_ animated: Bool) {
        print("\nPlaceListingsController:\n\tviewDidAppear\n")
    }
    
    // This method is called when this view is transitioning into the background.
    // Action: Transition to Place Description from Place Listings.
    override func viewWillDisappear(_ animated: Bool) {
        print("\nPlaceListingsController:\n\tviewWillDisappear\n")
    }
    
    // This method is called when this view sucessfully goes into the background.
    // Action: Transition to Place Description from Place Listings.
    override func viewDidDisappear(_ animated: Bool) {
        print("\nPlaceListingsController:\n\tviewDidDisappear\n")
    }
    
    // Receives the number of rows in the TableView
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return names.count
    }
    
    // Fills out the TableView with names of places
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PlaceCell", for: indexPath)
        cell.textLabel?.text = names[indexPath.row]
        return cell
    }
    
    // Extracts the text from the cells in the TableView that the user has selected
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = tableView.cellForRow(at: indexPath)?.textLabel?.text
        selection = data!
        
        for jsonPlace in places! {
            if jsonPlace.key == selection {
                place = jsonPlace.value as? Dictionary<String, Any>
                break
            }
        }
    }
}
