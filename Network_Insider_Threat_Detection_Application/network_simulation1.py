import subprocess
from pymongo import MongoClient
import gridfs
import sys

def retrieve_pcap_from_mongo():
    client = MongoClient("mongodb://localhost:27017/")
    db = client['injected_pcap']
    fs = gridfs.GridFS(db)
    pcap_data = fs.find_one({'filename': 'merged.pcap'})
    # pcap_data = fs.find_one()
    # print the filename of the stored pcap file
    print("retrived file: " ,pcap_data.filename)
    return pcap_data.read()

def simulate_and_capture(pcap_data):
    with open('temp.pcap', 'wb') as f:
        f.write(pcap_data)
    # subprocess.run(["tcpreplay", "--intf1=en1", "temp.pcap"], check=True)
    subprocess.run(["tshark", "-i", "en1", "-a", "duration:30", "-w", "captured.pcap"], check=True)
    # subprocess.run(["tcpreplay", "--intf1=en1", "--topspeed"], check=True)

    subprocess.run(["tcpreplay", "--intf1=en1", "--topspeed", "temp.pcap"], check=True)
    # subprocess.run(["tshark", "-i en1", "-w captured.pcap"], check=True)

    # tcpreplay --intf1=en1 --mtu=1500 --truncate temp.pcap
    # subprocess.run(["tcpreplay", "--intf1=en1", "--mtu=1500", "--truncate", "temp.pcap"], check=True)
    # print('running tcpreplay')
    # make it stop after a few seconds to capture the packets
    # subprocess.run(["tshark", "-i", "en1", "-w captured.pcap"], check=True)
    # subprocess.run(["tshark", "-i", "en1", "-a", "duration:5", "-w", "captured.pcap"], check=True)
    # print('running tshark')

def decode_and_analyze():
    subprocess.run(["python3", "decoder.py", "captured.pcap"], check=True)
    # subprocess.run(["python3", "analyze.py"], check=True)

def main():
    pcap_data = retrieve_pcap_from_mongo()
    simulate_and_capture(pcap_data)
    # decode_and_analyze()

# import subprocess
# import threading

# def run_tshark(interface, output_file, duration):
#     # Start packet capture using tshark with a time limit
#     command = ["tshark", "-i", interface, "-a", f"duration:{duration}", "-w", output_file]
#     subprocess.run(command, check=True)

# def run_tcpreplay(interface, pcap_file):
#     # Replay pcap file at top speed
#     command = ["tcpreplay", "--intf1=" + interface, "--topspeed", pcap_file]
#     subprocess.run(command, check=True)

# def simulate_and_capture(pcap_data):
#     with open('temp.pcap', 'wb') as f:
#         f.write(pcap_data)

#     interface = "en1"
#     pcap_file = "temp.pcap"
#     output_file = "captured.pcap"
#     capture_duration = 30  # Duration in seconds for how long tshark should capture

#     # Start the tshark capture in a separate thread
#     tshark_thread = threading.Thread(target=run_tshark, args=(interface, output_file, capture_duration))
#     tshark_thread.start()

#     # Wait a brief moment to ensure tshark is fully initialized and capturing
#     threading.Event().wait(1)

#     # Start the replay
#     run_tcpreplay(interface, pcap_file)

#     # Wait for the tshark thread to complete
#     tshark_thread.join()

#     print("Replay and capture completed.")

# Example usage with dummy pcap data

if __name__ == "__main__":
    # simulate_and_capture(b'your_pcap_data_here')
    main()