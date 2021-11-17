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
    var place: Dictionary<String, Any>?
    var places: Dictionary<String, Dictionary<String, Any>>?
    
    var selection: String?
    
    override func viewDidLoad() {
        tableView.dataSource = self
        tableView.delegate = self
        
        print("PlaceListingsController:\n\tviewDidLoad\n")
        // Do any additional setup after loading the view.
        
        // Read places.json in the Caches directory
        // Convert the places json object to a dictionary.
        if let url = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first {
            let file = url.appendingPathComponent("places").appendingPathExtension("json")
            
            do {
                places = try JSONSerialization.jsonObject(with: Data(contentsOf: file)) as? Dictionary<String, Dictionary<String, Any>>
            } catch {
                print("Error reading file.\n")
            }
            
            // Set the names String array with the keys from places used to set the cells in the table view
            for place in places! {
                names.append(place.key)
            }
        }
    }

    // This method is called when this view is transitioning into the foreground.
    // Action: Transition from Place Description to Place Listings.
    override func viewWillAppear(_ animated: Bool) {
        print("PlaceListingsController:\n\tviewWillAppear\n")
        
        // Find the name of the selected place in the places dictionary
        for p in places! {
            if p.key == place!["name"] as! String {
                // If the description of the place is not matching the description of the place in the places dictionary
                // update the description in the places.json file.
                if (places![p.key]!["description"]! as! String) != (place!["description"]! as! String) {
                    
                    // Set the description of the place in the places dictionary to the selected place's description
                    places![p.key]!["description"]! = place!["description"]!
                    
                    // Access places.json in the Caches directory
                    // Convert the places dictionary into a json object and write to the file.
                    do {
                        if let url = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first {
                            let file = url.appendingPathComponent("places").appendingPathExtension("json")
                            
                            try JSONSerialization.data(withJSONObject: places!, options: [.prettyPrinted])
                                .write(to: file, options: [.atomicWrite])
                            
                            print("Updated file.\n\t\(place!["name"] as! String): {description: \(place!["description"]! as! String)}\n")
                        }
                    } catch {
                        print("Error writing to file.\n")
                    }
                } else {
                    print("No changes detected.\n")
                }
                break
            }
        }
    }
    
    // This method is called when this view successfuly opens into the foreground.
    // Action: Transition from Place Description to Place Listings.
    override func viewDidAppear(_ animated: Bool) {
        print("PlaceListingsController:\n\tviewDidAppear\n")
    }
    
    // This method is called when this view is transitioning into the background.
    // Action: Transition to Place Description from Place Listings.
    override func viewWillDisappear(_ animated: Bool) {
        print("PlaceListingsController:\n\tviewWillDisappear\n")
    }
    
    // This method is called when this view sucessfully goes into the background.
    // Action: Transition to Place Description from Place Listings.
    override func viewDidDisappear(_ animated: Bool) {
        print("PlaceListingsController:\n\tviewDidDisappear\n")
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
                place = jsonPlace.value
                break
            }
        }
    }
    
    // Delete a cell on the TableView and from places.json file.
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let name = names.remove(at: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
            
            places!.removeValue(forKey: name)
            print("Deleted \(name).\n")
            
        } else if editingStyle == .insert {
            
        }
        
        // Access places.json in the Caches directory
        // Convert the places dictionary into a json object and write to the file.
        do {
            if let url = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first {
                let file = url.appendingPathComponent("places").appendingPathExtension("json")
                
                try JSONSerialization.data(withJSONObject: places!, options: [.prettyPrinted])
                    .write(to: file, options: [.atomicWrite])
            }
        } catch {
            print("Error writing to file.\n")
        }
    }
}
