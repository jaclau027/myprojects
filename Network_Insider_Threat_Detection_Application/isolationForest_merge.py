import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import IsolationForest
import os

# Path to the merged CSV file
merged_csv = 'packet_analysis_combined.csv'

# Desired features to analyze (will be filtered based on availability)
desired_features = ['Source Port', 'Destination Port', 'Packet Length']

def process_isolation_forest(df, desired_features, contamination=0.01):
    # Check which desired features are actually present in the DataFrame
    available_features = [f for f in desired_features if f in df.columns]
    
    if len(available_features) < 2:
        raise ValueError("At least two of the desired features must be present in the DataFrame")
    
    print(f"Using features: {available_features}")
    
    # Handle NaN values
    df_clean = df[available_features].copy()
    
    # For numeric columns, replace NaN with median
    for col in df_clean.select_dtypes(include=[np.number]).columns:
        df_clean[col] = df_clean[col].fillna(df_clean[col].median())
    
    # For non-numeric columns, replace NaN with mode (most frequent value)
    for col in df_clean.select_dtypes(exclude=[np.number]).columns:
        df_clean[col] = df_clean[col].fillna(df_clean[col].mode()[0])
    
    # Convert features to numerical values
    X = df_clean.values
    
    # Standardize the features
    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)
    
    # Initialize and fit the Isolation Forest
    iso_forest = IsolationForest(contamination=contamination, random_state=42)
    iso_forest.fit(X_scaled)
    
    # Predict anomalies (-1 for anomalies, 1 for normal)
    df['Anomaly'] = iso_forest.predict(X_scaled)

    return df

# Load the merged CSV file into a DataFrame
df_merged = pd.read_csv(merged_csv)

# Remove data points with "unknown" Classification
df_merged = df_merged[df_merged['Classification'].str.lower() != 'unknown']

# Display available columns and unique classifications
print("Available columns in the DataFrame:")
print(df_merged.columns.tolist())
print("\nUnique classifications:")
print(df_merged['Classification'].unique())

# Process the entire dataset with Isolation Forest
df_result = process_isolation_forest(df_merged, desired_features, contamination=0.01)

# Create a directory to save plots
if not os.path.exists('scatter_plots'):
    os.makedirs('scatter_plots')

# Define a color map for different classifications
color_map = {
    'Web Activity': 'blue',
    'Software Activity': 'orange',
    'File Activity': 'green',
}

# Get unique classifications
unique_classifications = df_result['Classification'].unique()

# Create separate plots for each classification
for classification in unique_classifications:
    plt.figure(figsize=(10, 6))
    class_data = df_result[df_result['Classification'] == classification]
    
    plt.scatter(class_data[class_data['Anomaly'] == 1]['Source Port'],
                class_data[class_data['Anomaly'] == 1]['Destination Port'],
                color=color_map.get(classification, 'purple'), alpha=0.5, label='Normal')
    
    plt.scatter(class_data[class_data['Anomaly'] == -1]['Source Port'],
                class_data[class_data['Anomaly'] == -1]['Destination Port'],
                color='red', alpha=0.5, label='Anomaly')
    
    plt.title(f'Isolation Forest Anomaly Detection - {classification}')
    plt.xlabel('Source Port')
    plt.ylabel('Destination Port')
    plt.legend()
    plt.grid(True)
    
    plt.savefig(f'scatter_plots/{classification.replace(" ", "_").lower()}_scatter_plot.png')
    plt.close()

# Create a combined plot for all classifications
plt.figure(figsize=(12, 8))

for classification in unique_classifications:
    class_data = df_result[df_result['Classification'] == classification]
    
    plt.scatter(class_data[class_data['Anomaly'] == 1]['Source Port'],
                class_data[class_data['Anomaly'] == 1]['Destination Port'],
                color=color_map.get(classification, 'purple'), alpha=0.5, label=f'{classification} - Normal')
    
    plt.scatter(class_data[class_data['Anomaly'] == -1]['Source Port'],
                class_data[class_data['Anomaly'] == -1]['Destination Port'],
                color='red', alpha=0.5, label=f'{classification} - Anomaly')

plt.title('Isolation Forest Anomaly Detection - All Classifications')
plt.xlabel('Source Port')
plt.ylabel('Destination Port')
plt.legend()
plt.grid(True)

plt.savefig('scatter_plots/all_classifications_scatter_plot.png')
plt.close()

# Save the final results to a new CSV file
final_output_path = 'combined_anomalies_analysis.csv'
df_result.to_csv(final_output_path, index=False)

print("Processing complete. Results saved to 'combined_anomalies_analysis.csv'")
print("Scatter plots saved in the 'scatter_plots' directory")

# Print summary of classifications in the output
print("\nClassifications in the output:")
print(df_result['Classification'].value_counts())


















# import matplotlib.pyplot as plt
# import pandas as pd
# import numpy as np
# from sklearn.preprocessing import StandardScaler
# from sklearn.ensemble import IsolationForest
# import os
# import ipaddress

# # Path to the merged CSV file
# merged_csv = 'packet_analysis_combined.csv'

# # Desired features to analyze
# desired_features = ['Source IP', 'Destination IP', 'Packet Length']

# def ip_to_int(ip):
#     try:
#         return int(ipaddress.ip_address(ip))
#     except ValueError:
#         return np.nan

# def process_isolation_forest(df, desired_features, contamination=0.01):
#     # Check which desired features are actually present in the DataFrame
#     available_features = [f for f in desired_features if f in df.columns]
    
#     if len(available_features) < 2:
#         raise ValueError("At least two of the desired features must be present in the DataFrame")
    
#     print(f"Using features: {available_features}")
    
#     # Handle NaN values and convert IP addresses
#     df_clean = df[available_features].copy()
    
#     for col in df_clean.columns:
#         if 'IP' in col:
#             df_clean[col] = df_clean[col].apply(ip_to_int)
#         if df_clean[col].dtype == object:
#             df_clean[col] = pd.to_numeric(df_clean[col], errors='coerce')
    
#     # Replace remaining NaN with median for each column
#     df_clean = df_clean.fillna(df_clean.median())
    
#     # Convert features to numerical values
#     X = df_clean.values
    
#     # Standardize the features
#     scaler = StandardScaler()
#     X_scaled = scaler.fit_transform(X)
    
#     # Initialize and fit the Isolation Forest
#     iso_forest = IsolationForest(contamination=contamination, random_state=42)
#     iso_forest.fit(X_scaled)
    
#     # Predict anomalies (-1 for anomalies, 1 for normal)
#     df['Anomaly'] = iso_forest.predict(X_scaled)

#     return df

# # Load the merged CSV file into a DataFrame
# df_merged = pd.read_csv(merged_csv)

# # Remove data points with "unknown" Classification
# df_merged = df_merged[df_merged['Classification'].str.lower() != 'unknown']

# # Display available columns and unique classifications
# print("Available columns in the DataFrame:")
# print(df_merged.columns.tolist())
# print("\nUnique classifications:")
# print(df_merged['Classification'].unique())

# # Process the entire dataset with Isolation Forest
# df_result = process_isolation_forest(df_merged, desired_features, contamination=0.01)

# # Create a directory to save plots
# if not os.path.exists('scatter_plots'):
#     os.makedirs('scatter_plots')

# # Define a color map for different classifications
# color_map = {
#     'Web Activity': 'blue',
#     'Software Activity': 'orange',
#     'File Activity': 'green',
# }

# # Get unique classifications
# unique_classifications = df_result['Classification'].unique()

# # Create separate plots for each classification
# for classification in unique_classifications:
#     plt.figure(figsize=(10, 6))
#     class_data = df_result[df_result['Classification'] == classification]
    
#     plt.scatter(class_data[class_data['Anomaly'] == 1]['Source IP'].apply(ip_to_int),
#                 class_data[class_data['Anomaly'] == 1]['Destination IP'].apply(ip_to_int),
#                 color=color_map.get(classification, 'purple'), alpha=0.5, label='Normal')
    
#     plt.scatter(class_data[class_data['Anomaly'] == -1]['Source IP'].apply(ip_to_int),
#                 class_data[class_data['Anomaly'] == -1]['Destination IP'].apply(ip_to_int),
#                 color='red', alpha=0.5, label='Anomaly')
    
#     plt.title(f'Isolation Forest Anomaly Detection - {classification}')
#     plt.xlabel('Source IP (numeric)')
#     plt.ylabel('Destination IP (numeric)')
#     plt.legend()
#     plt.grid(True)
    
#     plt.savefig(f'scatter_plots/{classification.replace(" ", "_").lower()}_scatter_plot.png')
#     plt.close()

# # Create a combined plot for all classifications
# plt.figure(figsize=(12, 8))

# for classification in unique_classifications:
#     class_data = df_result[df_result['Classification'] == classification]
    
#     plt.scatter(class_data[class_data['Anomaly'] == 1]['Source IP'].apply(ip_to_int),
#                 class_data[class_data['Anomaly'] == 1]['Destination IP'].apply(ip_to_int),
#                 color=color_map.get(classification, 'purple'), alpha=0.5, label=f'{classification} - Normal')
    
#     plt.scatter(class_data[class_data['Anomaly'] == -1]['Source IP'].apply(ip_to_int),
#                 class_data[class_data['Anomaly'] == -1]['Destination IP'].apply(ip_to_int),
#                 color='red', alpha=0.5, label=f'{classification} - Anomaly')

# plt.title('Isolation Forest Anomaly Detection - All Classifications')
# plt.xlabel('Source IP (numeric)')
# plt.ylabel('Destination IP (numeric)')
# plt.legend()
# plt.grid(True)

# plt.savefig('scatter_plots/all_classifications_scatter_plot.png')
# plt.close()

# # Save the final results to a new CSV file
# final_output_path = 'combined_anomalies_analysis.csv'
# df_result.to_csv(final_output_path, index=False)

# print("Processing complete. Results saved to 'combined_anomalies_analysis.csv'")
# print("Scatter plots saved in the 'scatter_plots' directory")

# # Print summary of classifications in the output
# print("\nClassifications in the output:")
# print(df_result['Classification'].value_counts())