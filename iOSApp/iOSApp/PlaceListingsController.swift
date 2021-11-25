//
//  PlaceListingsController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/11/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved. The SER423 Instructional Team and Arizona State University
//  have the right to build and evaluate this software package for the purposes of grading and program assessment.
//
//  @author Ryan Meneses     mailto: rmenese1@asu.edu
//  @version 1.0
//  @since November 11, 2021
//

import UIKit

class PlaceListingsController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    var url: String?
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var contentView: UIView!
    
    // This variable is used to populate the cells in the TableView
    var names: [String] = []
    var isNewPlace: Bool = false
    
    // This variable is updated on prepare segue from PlaceDescriptionController
    var place: Dictionary<String, Any>?
    var places: Dictionary<String, Dictionary<String, Any>>?
    
    var selection: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
                
        NSLog("PlaceListingsController: viewDidLoad\n")
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
            
            // If isNewPlace is true add the place to places
            if isNewPlace {
                // Update image to ""; this version does not utilize images.
                place!["image"] = ""
                places!.updateValue(place!, forKey: place!["name"] as! String)
                
                // Access places.json in the Caches directory
                // Convert the places dictionary into a json object and write to the file.
                do {
                    if let url = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first {
                        let file = url.appendingPathComponent("places").appendingPathExtension("json")
                        
                        try JSONSerialization.data(withJSONObject: places!, options: [.prettyPrinted])
                            .write(to: file, options: [.atomicWrite])
                        
                        print("Updated file.\n\tAdded \(place!["name"] as! String) to places.\n")
                    }
                } catch {
                    print("Error writing to file.\n")
                }
                
                // Add and save on PlaceJsonRPCServer
                add()
                
                // Set isNewPlace to false once the place is added to places
                // Note this is only set to true from the PlaceAdder View
                isNewPlace = false
            }
            
            // Set the names String array with the keys from places used to set the cells in the table view
            for p in places! {
                names.append(p.key)
            }
        }
    }

    // This method is called when this view is transitioning into the foreground.
    // Action: Transition from Place Description to Place Listings.
    override func viewWillAppear(_ animated: Bool) {
        NSLog("PlaceListingsController: viewWillAppear\n")
        
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
                    
                    // Add and save on PlaceJsonRPCServer
                    add()
                    
                } else {
                    print("No description changes detected.\n")
                }
                break
            }
        }
    }
    
    // This method is called when this view successfuly opens into the foreground.
    // Action: Transition from Place Description to Place Listings.
    override func viewDidAppear(_ animated: Bool) {
        NSLog("PlaceListingsController: viewDidAppear\n")
    }
    
    // This method is called when this view is transitioning into the background.
    // Action: Transition to Place Description from Place Listings.
    override func viewWillDisappear(_ animated: Bool) {
        NSLog("PlaceListingsController: viewWillDisappear\n")
    }
    
    // This method is called when this view sucessfully goes into the background.
    // Action: Transition to Place Description from Place Listings.
    override func viewDidDisappear(_ animated: Bool) {
        NSLog("PlaceListingsController: viewDidDisappear\n")
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
            
            // Remove place from places object in memory
            places!.removeValue(forKey: name)
            
            // Remove and save on PlaceJsonRPCServer
            self.remove(name: name)
            
            print("Deleted \(name).\n")
            
        } else if editingStyle == .insert {
            // Insert functionality.
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
    
    // Helper method to package the name to be removed on PlaceJsonRPCServer
    func remove(name: String) {
        // Remove place from places.json on the PlaceJsonRPCServer
        print("Removing \(name) from PlaceJsonRPCServer...\n")
        let _: Bool = remove(name: name, callback: { (res: String, err: String?) -> Void in
            if err != nil {
                print(err!)
                
            } else {
                if let _: Data = res.data(using: String.Encoding.utf8) {
                    print("\"\(name)\": \(res)\n")
                }
            }
        })
        
        // Saving places on the PlaceJsonRPCServer
        print("Saving places on PlaceJsonRPCServer...\n")
        let _: Bool = saveToJsonFile(callback: { (res: String, err: String?) -> Void in
            if err != nil {
                print(err!)
                
            } else {
                print("\(res)\n")
            }
        })
    }
    
    // JsonRPC Delete request: Delete a place from the PlaceJsonRPCServer.
    // Adopted from StudentCollectioniOSJsonRPC example from Unit 7: iOS Networking and Asynchornous Activites Samples
    func remove(name: String, callback: @escaping (String, String?) -> Void) -> Bool {
        let placeDescriptionController = PlaceDescriptionController()
        
        do {
            let dict: [String: Any] = ["jsonrpc": "2.0", "method": "remove", "params": [name], "id": "3"]
            let reqData: Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
            placeDescriptionController.asyncHttpPostJSON(url: self.url!, data: reqData, completion: callback)
            
            return true
            
        } catch let error as NSError {
            print(error)
        }
        
        return false
    }
    
    // Helper method to setup a json object to be added on PlaceJsonRPCServer
    func add() {
        // Adding a place on the PlaceJsonRPCServer
        print("Adding \(place!["name"] as! String) to PlaceJsonRPCServer...\n")
        let _: Bool = add(place: place!, callback: { (res: String, err: String?) -> Void in
            if err != nil {
                print(err!)
                
            } else {
                if let data: Data = res.data(using: String.Encoding.utf8) {
                    do {
                        let p = try JSONSerialization.jsonObject(with: data,options: .mutableContainers) as?[String: AnyObject]
                        print("\"\(self.place!["name"] as! String)\": \(p!["result"]! as Any)\n")
                        
                    } catch {
                        print("Error: Could not convert json object to dictionary.")
                    }
                }
            }
        })
        
        // Saving places on the PlaceJsonRPCServer
        print("Saving places on PlaceJsonRPCServer...\n")
        let _: Bool = saveToJsonFile(callback: { (res: String, err: String?) -> Void in
            if err != nil {
                print(err!)
                
            } else {
                print("\(res)\n")
            }
        })
    }
    
    // JsonRPC Add request: Add a place to the PlaceJsonRPCServer.
    // Adopted from StudentCollectioniOSJsonRPC example from Unit 7: iOS Networking and Asynchornous Activites Samples
    func add(place: Dictionary<String, Any>, callback: @escaping (String, String?) -> Void) -> Bool {
        let placeDescriptionController = PlaceDescriptionController()
        self.url = placeDescriptionController.setURL()
        
        do {
            let dict: [String: Any] = ["jsonrpc": "2.0", "method": "add", "params": [place], "id": "3"]
            let reqData: Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
            placeDescriptionController.asyncHttpPostJSON(url: self.url!, data: reqData, completion: callback)
            
            return true
            
        } catch let error as NSError {
            print(error)
        }
        
        return false
    }
    
    // JsonRPC Save request: Save the places on the PlaceJsonRPCServer to a file.
    // Adopted from StudentCollectioniOSJsonRPC example from Unit 7: iOS Networking and Asynchornous Activites Samples
    func saveToJsonFile(callback: @escaping (String, String?) -> Void) -> Bool {
        let placeDescriptionController = PlaceDescriptionController()
        
        do {
            let dict: [String: Any] = ["jsonrpc": "2.0", "method": "saveToJsonFile", "id": "3"]
            let reqData: Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
            placeDescriptionController.asyncHttpPostJSON(url: self.url!, data: reqData, completion: callback)
            
            return true
            
        } catch let error as NSError {
            print(error)
        }
        
        return false
    }
}
