import json
import os

# Directory where your JSON files are stored
directory = r"src\main\resources\messageTemplates\userManagement"

# Loop over each file in the directory
for filename in os.listdir(directory):
    if filename.endswith(".json"):
        # Construct the full file path
        file_path = os.path.join(directory, filename)
        
        # Open and read the JSON file
        with open(file_path, 'r') as file:
            json_data = json.load(file)
        
        request_name = filename[:-5]  # Remove the last 5 characters '.json'
        
        # Add the "request" key to the JSON object
        json_data["request"] = request_name
        
        # Write the modified JSON object back to the file
        with open(file_path, 'w') as file:
            json.dump(json_data, file, indent=4)

print("All files have been updated with the 'request' key.")
