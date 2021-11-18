//
//  PlaceDescriptionController.swift
//  iOSApp
//
//  Created by Ryan Meneses on 11/1/21.
//  Copyright © 2021 Ryan Meneses. All rights reserved.
//

import UIKit

class PlaceDescriptionController: UIViewController, UITextViewDelegate {
    
    var url: String?
    
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
    
    // Setup the URL used to access places resources at the PlaceJsonRPCServer.
    // Adopted from StudentCollectioniOSJsonRPC example from Unit 7: iOS Networking and Asynchornous Activites Samples
    func setURL () -> String {
        var host:String = "localhost"
        var port:String = "8080"
        var http:String = "http"
        
        if let path = Bundle.main.path(forResource: "ServerInfo", ofType: "plist") {
            if let dict = NSDictionary(contentsOfFile: path) as? [String:AnyObject] {
                host = (dict["server_host"] as? String)!
                port = (dict["jsonrpc_port"] as? String)!
                http = (dict["server_protocol"] as? String)!
            }
        }
        
        print("Connecting to PlaceJsonRPCServer at: \(http)://\(host):\(port)\n")
        return "\(http)://\(host):\(port)"
    }
    
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
        NSLog("PlaceDescriptionController: viewWillAppear\n")
        
        print("Requesting \(place["name"] as! String) from PlaceJsonRPCServer...\n")
        self.url = self.setURL()
        let _: Bool = get(name: place["name"] as! String, callback: { (res: String, err: String?) -> Void in
            if err != nil {
                print(err!)
                
            } else {
                print("\(res)\n")
                
                if let data: Data = res.data(using: String.Encoding.utf8) {
                    do {
                        let p = try JSONSerialization.jsonObject(with: data,options: .mutableContainers) as?[String: AnyObject]
                        print("\"\(self.place["name"] as! String)\": \(p!["result"]! as Any)\n")
                        
                    } catch {
                        print("Error: Could not convert json object to dictionary.")
                    }
                }
            }
        })
        
        // If the server is running this will be true, if this is not true, the application will use
        // the places.json object it is bundled with to set the following place attributes.
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
        NSLog("PlaceDescriptionController: viewDidAppear\n")
    }
    
    // This method is called when this view is transitioning into the background.
    // Action: Transition to Place Listings from Place Description.
    override func viewWillDisappear(_ animated: Bool) {
        NSLog("PlaceDescriptionController: viewWillDisappear\n")
        
    }
    
    // This method is called when this view sucessfully goes into the background.
    // Action: Transition to Place Listings from Place Description.
    override func viewDidDisappear(_ animated: Bool) {
        NSLog("PlaceDescriptionController: viewDidDisappear\n")
    }
    
    // Prepare for segue from this (PlaceDescription) View to PlaceListings View
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let placeListings = segue.destination as? PlaceListingsController {
            placeListings.place = self.place
            placeListings.url = self.url
        }
    }
    
    // Prepare for segue from PlaceListings View to this (PlaceDescription) View
    @IBAction func savePlace(segue: UIStoryboardSegue) {
        let placeListingsController = segue.source as! PlaceListingsController        
        place = placeListingsController.place!
    }
    
    // Helper method to make an asynchornous request to PlaceJsonRPCServer.
    // Adopted from StudentCollectioniOSJsonRPC example from Unit 7: iOS Networking and Asynchornous Activites Samples
    func asyncHttpPostJSON(url: String,  data: Data, completion: @escaping (String, String?) -> Void) {
        let request = NSMutableURLRequest(url: NSURL(string: url)! as URL)
        
        request.httpMethod = "POST"
        request.addValue("application/json",forHTTPHeaderField: "Content-Type")
        request.addValue("application/json",forHTTPHeaderField: "Accept")
        request.httpBody = data as Data

        let task: URLSessionDataTask = URLSession.shared.dataTask(with: request as URLRequest, completionHandler: {
            (data, response, error) -> Void in
            
            if (error != nil) {
                DispatchQueue.main.async(execute: {
                    completion("Error in URL Session", error!.localizedDescription)
                })
                
            } else {
                DispatchQueue.main.async(execute: {
                    completion(NSString(data: data!, encoding: String.Encoding.utf8.rawValue)! as String, nil)
                })
            }
        })
        
        task.resume()
    }
    
    // JsonRPC Get request: Get a place from the PlaceJsonRPCServer.
    // Adopted from StudentCollectioniOSJsonRPC example from Unit 7: iOS Networking and Asynchornous Activites Samples
    func get(name: String, callback: @escaping (String, String?) -> Void) -> Bool {
        do {
            let dict: [String: Any] = ["jsonrpc": "2.0", "method": "get", "params": [name], "id": "3"]
            let reqData: Data = try JSONSerialization.data(withJSONObject: dict, options: JSONSerialization.WritingOptions(rawValue: 0))
            self.asyncHttpPostJSON(url: self.url!, data: reqData, completion: callback)
            
            return true
            
        } catch let error as NSError {
            print(error)
        }
        
        return false
    }
}
