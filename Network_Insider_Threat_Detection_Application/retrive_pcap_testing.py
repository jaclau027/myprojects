from pymongo import MongoClient
import gridfs
import os

# Setup MongoDB connection
# client = MongoClient('mongodb://localhost:27017/')
# MongoDB Atlas connection string
uri ="mongodb+srv://fyp2024:fyp2024@clusterfyp.x39ss.mongodb.net/"
client = MongoClient(uri)
db = client['fyp_pcap']
fs = gridfs.GridFS(db)

def retrieve_pcap():
    # retieve all files
    num = 0
    
    for file in fs.find():
        filename = file.filename
        save_path = str(num) + '_testing.pcap'
        with open(save_path, 'wb') as f:
            f.write(file.read())
        print(f"File {filename} has been written to {save_path}")
        num += 1

retrieve_pcap()