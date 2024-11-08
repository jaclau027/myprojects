import pandas as pd
from fastapi import FastAPI, Query, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse
from pydantic import BaseModel
from typing import List, Optional, Dict
import logging
import sys
from collections import defaultdict
import traceback
import os

# Set up logging
logging.basicConfig(level=logging.DEBUG, stream=sys.stdout,
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

app = FastAPI()

# Enable CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global variables to store processed data
processed_data = {}
unique_source_ips = []
anomaly_data = []

class PacketData(BaseModel):
    Timestamp: str
    Source_IP: str
    Destination_IP: str
    Classification: str

class AnomalyData(BaseModel):
    Timestamp: str
    Source_IP: str
    Classification: str
    Anomaly: int

def preprocess_data():
    global processed_data, unique_source_ips
    try:
        logger.info("Loading and preprocessing packet analysis CSV file")
        df = pd.read_csv("packet_analysis_combined.csv")


        df['Timestamp'] = pd.to_datetime(df['Timestamp'], unit='s')
        
        # Organize data by Source IP
        processed_data = defaultdict(list)
        for _, row in df.iterrows():
            source_ip = row['Source IP']
            destination_ip = row['Destination IP']
            classification = row['Classification']

            if (pd.isna(source_ip) or source_ip == "" or source_ip.lower() == "unknown" or
                pd.isna(destination_ip) or destination_ip == "" or destination_ip.lower() == "unknown" or
                pd.isna(classification) or classification == "" or classification.lower() == "unknown"):
                
                logger.warning(f"Skipping row due to invalid values: {row}")
                continue

            processed_data[source_ip].append({
                'Timestamp': row['Timestamp'].isoformat(),
                'Source_IP': source_ip,
                'Destination_IP': row['Destination IP'],
                'Classification': row['Classification'],
            })
        
        # Convert defaultdict to regular dict
        processed_data = dict(processed_data)
        
        # Get unique Source IPs
        unique_source_ips = list(processed_data.keys())
        
        logger.info(f"Data preprocessed successfully. {len(unique_source_ips)} unique Source IPs found.")
        logger.debug(f"Sample of processed data: {list(processed_data.values())[0][:2]}")
    except Exception as e:
        logger.error(f"Error preprocessing packet data: {e}")
        logger.error(traceback.format_exc())
        processed_data = {}
        unique_source_ips = []

def preprocess_anomaly_data():
    global anomaly_data
    try:
        logger.info("Loading and filtering synthetic anomaly detection results")
        df_anomalies = pd.read_csv("combined_anomalies_analysis.csv")
        logger.debug(f"Columns in the anomaly CSV: {df_anomalies.columns}")
        logger.debug(f"First few rows of the anomaly CSV:\n{df_anomalies.head()}")
        
        df_anomalies['Timestamp'] = pd.to_datetime(df_anomalies['Timestamp'], unit='s')
        
        # Filter for anomalies with value -1 and include specified columns
        filtered_df = df_anomalies[df_anomalies['Anomaly'] == -1]
        anomaly_data = filtered_df[['Timestamp', 'Source IP', 'Classification', 'Anomaly']].to_dict('records')
        
        # Convert Timestamp to ISO format string
        for item in anomaly_data:
            item['Timestamp'] = item['Timestamp'].isoformat()
            item['Source_IP'] = item.pop('Source IP')  # Rename 'Source IP' to 'Source_IP' for consistency

        logger.info(f"Filtered anomaly data loaded successfully. {len(anomaly_data)} anomalies with value -1 found.")
        logger.debug(f"Sample of processed anomaly data: {anomaly_data[:5]}")
    except Exception as e:
        logger.error(f"Error preprocessing anomaly data: {e}")
        logger.error(traceback.format_exc())
        anomaly_data = []

# Preprocess data on startup
preprocess_data()
preprocess_anomaly_data()

@app.get("/api/graph")
async def get_graph(classification: str = Query(None, description="Specific classification or 'combined' for all")):
    if classification is None:
        raise HTTPException(status_code=400, detail="Classification parameter is required")
    
    if classification.lower() == 'combined':
        graph_path = "scatter_plots/all_classifications_scatter_plot.png"
    else:
        graph_path = f"scatter_plots/{classification.replace(' ', '_').lower()}_scatter_plot.png"
    
    if not os.path.exists(graph_path):
        logger.error(f"Graph file not found: {graph_path}")
        # List contents of the scatter_plots directory
        try:
            dir_contents = os.listdir("scatter_plots")
            logger.info(f"Contents of scatter_plots directory: {dir_contents}")
        except Exception as e:
            logger.error(f"Error listing scatter_plots directory: {e}")
        raise HTTPException(status_code=404, detail=f"Graph not found: {graph_path}")
    
    return FileResponse(graph_path)

@app.get("/api/available-graphs")
async def get_available_graphs():
    try:
        if not os.path.exists("scatter_plots"):
            logger.error("scatter_plots directory not found")
            raise HTTPException(status_code=404, detail="Graphs directory not found")
        
        graph_files = os.listdir("scatter_plots")
        logger.info(f"Files in scatter_plots directory: {graph_files}")
        
        classifications = [f.replace("_scatter_plot.png", "").replace("_", " ").title() 
                           for f in graph_files if f.endswith("_scatter_plot.png") and f != "all_classifications_scatter_plot.png"]
        has_combined = "all_classifications_scatter_plot.png" in graph_files
        
        logger.info(f"Available classifications: {classifications}")
        logger.info(f"Combined graph available: {has_combined}")
        
        return {"classifications": classifications, "has_combined": has_combined}
    except Exception as e:
        logger.error(f"Error getting available graphs: {e}")
        raise HTTPException(status_code=500, detail=f"Error retrieving graph information: {str(e)}")

@app.on_event("startup")
async def startup_event():
    logger.info("Checking for graph files on startup")
    if not os.path.exists("scatter_plots"):
        logger.warning("scatter_plots directory not found")
    else:
        graph_files = os.listdir("scatter_plots")
        logger.info(f"Found graph files: {graph_files}")

@app.get("/api/packet-data", response_model=List[PacketData])
async def get_packet_data(source_ip: Optional[str] = Query(None)):
    if not processed_data:
        logger.error("Processed packet data is empty or not available")
        raise HTTPException(status_code=500, detail="Data not available")
    
    if source_ip:
        if source_ip not in processed_data:
            logger.warning(f"Requested Source IP {source_ip} not found")
            raise HTTPException(status_code=404, detail="Source IP not found")
        logger.info(f"Returning packet data for Source IP: {source_ip}")
        return processed_data[source_ip]
    else:
        # Return data for all IPs (be cautious with large datasets)
        logger.info("Returning packet data for all Source IPs")
        return [item for sublist in processed_data.values() for item in sublist]

@app.get("/api/unique-source-ips", response_model=List[str])
async def get_unique_source_ips():
    if not unique_source_ips:
        logger.error("Unique Source IPs list is empty or not available")
        raise HTTPException(status_code=500, detail="Data not available")
    logger.info(f"Returning {len(unique_source_ips)} unique Source IPs")
    return unique_source_ips

@app.get("/api/preprocessed-data-summary", response_model=Dict[str, List[PacketData]])
async def get_preprocessed_data_summary():
    if not processed_data:
        logger.error("Processed packet data is empty or not available")
        raise HTTPException(status_code=500, detail="Data not available")
    
    logger.info(f"Returning preprocessed data summary for {len(processed_data)} Source IPs")
    return processed_data

@app.get("/api/anomaly-data", response_model=List[AnomalyData])
async def get_anomaly_data():
    if not anomaly_data:
        logger.error("Anomaly data is empty or not available")
        raise HTTPException(status_code=500, detail="Anomaly data not available")
    logger.info(f"Returning {len(anomaly_data)} anomaly data entries")
    return anomaly_data

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8080)










# import pandas as pd
# from fastapi import FastAPI, Query, HTTPException
# from fastapi.middleware.cors import CORSMiddleware
# from fastapi.responses import FileResponse
# from pydantic import BaseModel
# from typing import List, Optional, Dict
# import logging
# import sys
# from collections import defaultdict
# import traceback
# import os

# # Set up logging
# logging.basicConfig(level=logging.DEBUG, stream=sys.stdout,
#                     format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
# logger = logging.getLogger(__name__)

# app = FastAPI()

# # Enable CORS
# app.add_middleware(
#     CORSMiddleware,
#     allow_origins=["*"],
#     allow_credentials=True,
#     allow_methods=["*"],
#     allow_headers=["*"],
# )

# # Global variables to store processed data
# processed_data = {}
# unique_source_ips = []
# anomaly_data = []

# class PacketData(BaseModel):
#     Timestamp: str
#     Source_IP: str
#     Destination_IP: str
#     Classification: str

# class AnomalyData(BaseModel):
#     Timestamp: str
#     Source_IP: str
#     Classification: str
#     Anomaly: int

# def preprocess_data():
#     global processed_data, unique_source_ips
#     try:
#         logger.info("Loading and preprocessing packet analysis CSV file")
#         df = pd.read_csv("packet_analysis_combined2.csv")
        
#         df['Timestamp'] = pd.to_datetime(df['Timestamp'], unit='s')
        
#        # df = df[df['Source IP'].notna() & (df['Source IP'] != '') & df['Destination IP'].notna() & (df['Destination IP'] != '')]

#         # Organize data by Source IP
#         processed_data = defaultdict(list)
#         for _, row in df.iterrows():
#             source_ip = row['Source IP']
#             processed_data[source_ip].append({
#                 'Timestamp': row['Timestamp'].isoformat(),
#                 'Source_IP': source_ip,
#                 'Destination_IP': row['Destination IP'],
#                 'Classification': row['Classification'],
#             })
        
#         # Convert defaultdict to regular dict
#         processed_data = dict(processed_data)
        
#         # Get unique Source IPs
#         unique_source_ips = list(processed_data.keys())
        
#         logger.info(f"Data preprocessed successfully. {len(unique_source_ips)} unique Source IPs found.")
#         logger.debug(f"Sample of processed data: {list(processed_data.values())[0][:2]}")
#     except Exception as e:
#         logger.error(f"Error preprocessing packet data: {e}")
#         logger.error(traceback.format_exc())
#         processed_data = {}
#         unique_source_ips = []

# def preprocess_anomaly_data():
#     global anomaly_data
#     try:
#         logger.info("Loading and filtering synthetic anomaly detection results")
#         df_anomalies = pd.read_csv("combined_anomalies_analysis.csv")
#         logger.debug(f"Columns in the anomaly CSV: {df_anomalies.columns}")
#         logger.debug(f"First few rows of the anomaly CSV:\n{df_anomalies.head()}")
        
#         df_anomalies['Timestamp'] = pd.to_datetime(df_anomalies['Timestamp'], unit='s')
        
#         # Filter for anomalies with value -1 and include specified columns
#         filtered_df = df_anomalies[df_anomalies['Anomaly'] == -1]
#         anomaly_data = filtered_df[['Timestamp', 'Source IP', 'Classification', 'Anomaly']].to_dict('records')
        
#         # Convert Timestamp to ISO format string
#         for item in anomaly_data:
#             item['Timestamp'] = item['Timestamp'].isoformat()
#             item['Source_IP'] = item.pop('Source IP')  # Rename 'Source IP' to 'Source_IP' for consistency

#         logger.info(f"Filtered anomaly data loaded successfully. {len(anomaly_data)} anomalies with value -1 found.")
#         logger.debug(f"Sample of processed anomaly data: {anomaly_data[:5]}")
#     except Exception as e:
#         logger.error(f"Error preprocessing anomaly data: {e}")
#         logger.error(traceback.format_exc())
#         anomaly_data = []

# # Preprocess data on startup
# preprocess_data()
# preprocess_anomaly_data()

# @app.get("/api/graph")
# async def get_graph(classification: str = Query(None, description="Specific classification or 'combined' for all")):
#     if classification is None:
#         raise HTTPException(status_code=400, detail="Classification parameter is required")
    
#     if classification.lower() == 'combined':
#         graph_path = "scatter_plots/all_classifications_scatter_plot.png"
#     else:
#         graph_path = f"scatter_plots/{classification.replace(' ', '_').lower()}_scatter_plot.png"
    
#     if not os.path.exists(graph_path):
#         logger.error(f"Graph file not found: {graph_path}")
#         # List contents of the scatter_plots directory
#         try:
#             dir_contents = os.listdir("scatter_plots")
#             logger.info(f"Contents of scatter_plots directory: {dir_contents}")
#         except Exception as e:
#             logger.error(f"Error listing scatter_plots directory: {e}")
#         raise HTTPException(status_code=404, detail=f"Graph not found: {graph_path}")
    
#     return FileResponse(graph_path)

# @app.get("/api/available-graphs")
# async def get_available_graphs():
#     try:
#         if not os.path.exists("scatter_plots"):
#             logger.error("scatter_plots directory not found")
#             raise HTTPException(status_code=404, detail="Graphs directory not found")
        
#         graph_files = os.listdir("scatter_plots")
#         logger.info(f"Files in scatter_plots directory: {graph_files}")
        
#         classifications = [f.replace("_scatter_plot.png", "").replace("_", " ").title() 
#                            for f in graph_files if f.endswith("_scatter_plot.png") and f != "all_classifications_scatter_plot.png"]
#         has_combined = "all_classifications_scatter_plot.png" in graph_files
        
#         logger.info(f"Available classifications: {classifications}")
#         logger.info(f"Combined graph available: {has_combined}")
        
#         return {"classifications": classifications, "has_combined": has_combined}
#     except Exception as e:
#         logger.error(f"Error getting available graphs: {e}")
#         raise HTTPException(status_code=500, detail=f"Error retrieving graph information: {str(e)}")

# @app.on_event("startup")
# async def startup_event():
#     logger.info("Checking for graph files on startup")
#     if not os.path.exists("scatter_plots"):
#         logger.warning("scatter_plots directory not found")
#     else:
#         graph_files = os.listdir("scatter_plots")
#         logger.info(f"Found graph files: {graph_files}")

# @app.get("/api/packet-data", response_model=List[PacketData])
# async def get_packet_data(source_ip: Optional[str] = Query(None)):
#     if not processed_data:
#         logger.error("Processed packet data is empty or not available")
#         raise HTTPException(status_code=500, detail="Data not available")
    
#     if source_ip:
#         if source_ip not in processed_data:
#             logger.warning(f"Requested Source IP {source_ip} not found")
#             raise HTTPException(status_code=404, detail="Source IP not found")
#         logger.info(f"Returning packet data for Source IP: {source_ip}")
#         return processed_data[source_ip]
#     else:
#         # Return data for all IPs (be cautious with large datasets)
#         logger.info("Returning packet data for all Source IPs")
#         return [item for sublist in processed_data.values() for item in sublist]

# @app.get("/api/unique-source-ips", response_model=List[str])
# async def get_unique_source_ips():
#     if not unique_source_ips:
#         logger.error("Unique Source IPs list is empty or not available")
#         raise HTTPException(status_code=500, detail="Data not available")
#     logger.info(f"Returning {len(unique_source_ips)} unique Source IPs")
#     return unique_source_ips

# @app.get("/api/preprocessed-data-summary", response_model=Dict[str, List[PacketData]])
# async def get_preprocessed_data_summary():
#     if not processed_data:
#         logger.error("Processed packet data is empty or not available")
#         raise HTTPException(status_code=500, detail="Data not available")
    
#     logger.info(f"Returning preprocessed data summary for {len(processed_data)} Source IPs")
#     return processed_data

# @app.get("/api/anomaly-data", response_model=List[AnomalyData])
# async def get_anomaly_data():
#     if not anomaly_data:
#         logger.error("Anomaly data is empty or not available")
#         raise HTTPException(status_code=500, detail="Anomaly data not available")
#     logger.info(f"Returning {len(anomaly_data)} anomaly data entries")
#     return anomaly_data

# if __name__ == "__main__":
#     import uvicorn
#     uvicorn.run(app, host="0.0.0.0", port=8080)