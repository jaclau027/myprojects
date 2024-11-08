from scapy.all import *
import pandas as pd
from scapy.layers.http import HTTPRequest, HTTPResponse
from scapy.layers.dns import DNSQR, DNSRR, DNS
from scapy.layers.inet import IP, TCP, UDP
import csv
import json

def identify_activity(pkt):
    if pkt.haslayer(TCP):
        dport = pkt[TCP].dport
        sport = pkt[TCP].sport

        # identify web activty 
        if dport in [80, 81, 443, 444, 8080, 8443] or sport in [80, 81, 443, 444, 8080, 8443]:
            return "Web Activity"
        elif dport in [21, 445, 22, 2049, 20, 1, 80, 443, 8080] or sport in [21, 445, 22, 2049, 20, 1, 80, 443, 8080]:
            return "File Activity"
        else: 
            return "Unknown"
        
    elif pkt.haslayer(UDP):
        dport = pkt[UDP].dport
        sport = pkt[UDP].sport
        if pkt.haslayer(DNSQR) or pkt.haslayer(DNS):
            return "Software Activity"
        elif dport in [3389, 5900, 1433, 22, 23, 5060, 5061, 53] or sport in [3389, 5900, 1433, 22, 23, 5060, 5061, 53]:
            return "Software Activity"
        
        return "Unknown"
    return "Unknown"

def decode_raw_payload(raw_data):
    try:
        # Attempt to decode raw data to a string
        decoded_data = raw_data.decode('utf-8', errors='ignore')
        return decoded_data
    except Exception as e:
        return f"Decoding error: {e}"
    
# pcap_files = ['01Captura1s0i0.pcap', 'Google_Meet_1.pcap', 'web_dataset.pcap']
pcap_files = ['captured.pcap']

with open('packet_analysis_combined.csv', 'w', newline='') as csv_file:
    pass

def decode(pcap):
    pkts = rdpcap(pcap)
        
    data = []

    for packet in pkts:
        packet_info = {
            'Timestamp': packet.time,
            'Source IP': '',
            'Destination IP': '',
            'Source Port': '',
            'Destination Port': '',
            "Packet Length": len(packet),
            "Protocol": packet.payload.name,
            "Classification": identify_activity(packet),
        }

        try: 
            if packet.haslayer(IP):
                ip_layer = packet[IP]
                packet_info['Source IP'] = ip_layer.src
                packet_info['Destination IP'] = ip_layer.dst

            if packet.haslayer(TCP):
                    transport_layer = packet[TCP]
                    packet_info['Source Port'] = transport_layer.sport
                    packet_info['Destination Port'] = transport_layer.dport

            if packet.haslayer(HTTPRequest):
                    http_layer = packet[HTTPRequest]
                    packet_info['Activity'] = 'HTTP Request'
                    packet_info['HTTP Method'] = http_layer.Method.decode(errors='ignore')
                    packet_info['HTTP Host'] = http_layer.Host.decode(errors='ignore')
                    packet_info['HTTP Path'] = http_layer.Path.decode(errors='ignore')

            if packet.haslayer(HTTPResponse):
                    http_layer = packet[HTTPResponse]
                    packet_info['Activity'] = 'HTTP Response'
                    packet_info['Activity'] = 'HTTP Response'
                    packet_info['HTTP Status Code'] = http_layer.Status_Code.decode(errors='ignore')
                    packet_info['HTTP Reason Phrase'] = http_layer.Reason_Phrase.decode(errors='ignore')


            if packet.haslayer(Raw):
                raw_p = packet[Raw].load
                packet_info["Raw Payload"] = packet[Raw].load.decode(errors='ignore')
                packet_info["Decoded Payload"] = decode_raw_payload(raw_p)


            

        except Exception as e:
                print(f"Error processing packet in {pcap}: {e}")
                continue

        data.append(packet_info)
        
    return data

all = []
for pcap in pcap_files:
    # Check if the PCAP file exists
    if os.path.exists(pcap):  
        print(f"Processing {pcap}...")
        # Collect the data from each activity pcap files
        data = decode(pcap)  
        # append into one data file
        all.extend(data) 
    else:
        print(f"File {pcap} does not exist.")

if all:  # Only write to CSV if there is data
    dataframe = pd.DataFrame(all)
    dataframe.to_csv('packet_analysis_combined.csv', index=False)
    print(f"Decoded PCAP data saved to 'packet_analysis_combined.csv'")
else:
    print("No data to save.")


pcap_test = 'temp_test.pcap'
clean_decode = decode(pcap_test)
dataframe = pd.DataFrame(clean_decode)
dataframe.to_csv('clean_packet_analysis.csv', index=False)
print(f"Decoded clean PCAP data saved to 'clean_packet_analysis.csv'")