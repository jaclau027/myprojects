from pymongo import MongoClient
import gridfs
import os

def store_pcap(file_path, fs):
    """
    Store a PCAP file in MongoDB
    """
    with open(file_path, 'rb') as f:
        contents = f.read()
        # Storing the file with its basename as the filename in MongoDB
        fs.put(contents, filename=os.path.basename(file_path))
        print(f"File {file_path} has been stored in MongoDB")

def connect_mongodb(local_path, uri, db_name):
    """
    This function connects to MongoDB and stores all PCAP files in the specified directory
    """
    # uri ="mongodb+srv://fyp2024:
    # client = MongoClient('mongodb://localhost:27017/')  # Update the URI according to your configuration
    # db = client['fyp_pcap']
    # for file in os.listdir('pcap_web'):
    # if file.endswith('.pcap'):
    # store_pcap('pcap_web/' + file)
    client = MongoClient(uri)
    db = client[db_name]
    fs = gridfs.GridFS(db)

    files_count = 0 # Count the number of files stored
    for file in os.listdir(local_path):
        files_count += 1
        if file.endswith('.pcap'):
            store_pcap(local_path + file, fs)
    print(f"{files_count} files have been stored in MongoDB")

# # Example: Store a PCAP file
# store_pcap('bulk_xs_04.pcap')
# # go to the pcap folder that stores all the pcap files
# # run the following command to store all the pcap files

# # Example: Store a PCAP file
# local_file_path = 'injected_file/'
local_file_path = 'clean_file/'
uri = "mongodb://localhost:27017/"
db_name = 'fyp_pcap'
connect_mongodb(local_file_path, uri, db_name)