import numpy as np
import pandas as pd
from sklearn.svm import OneClassSVM
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
import os

# Load your training dataset
clean_ds = 'clean_packet_analysis.csv'
train_data = pd.read_csv(clean_ds)

# Use 'Source Port' and 'Destination Port' as features for training
train_features = train_data[['Source Port', 'Destination Port']]

# Drop any rows with missing values in these features
train_features = train_features.dropna()

# Train the One-Class SVM model using the training data
scaler = StandardScaler()
train_data_scaled = scaler.fit_transform(train_features)
model = OneClassSVM(nu=0.05, gamma='scale')
model.fit(train_data_scaled)

# --- Read test data from the 'bulk04ThreatDecode.csv' file ---
thread = 'packet_analysis_combined.csv'
test_data = pd.read_csv(thread)

# Use 'Source Port', 'Destination Port', and 'Classification' as features for testing
test_features = test_data[['Source Port', 'Destination Port', 'Classification']]

# Drop any rows with missing values in these features
test_features = test_features.dropna()

# Create a directory to save plots
if not os.path.exists('scatter_plots'):
    os.makedirs('scatter_plots')

# Standardize the test data using the same scaler as the training data
test_data_scaled = scaler.transform(test_features[['Source Port', 'Destination Port']])

# Predict anomalies in the test data
predictions = model.predict(test_data_scaled)

# Create a DataFrame for test data and add the predictions
test_data_df = test_features.copy()
test_data_df['Prediction'] = predictions
# Remove data points with "unknown" Classification
test_data_df = test_data_df[test_data_df['Classification'].str.lower() != 'unknown']
# Get unique classifications from the test data
unique_test_classes = test_data_df['Classification'].unique()

# Define a color map for normal data points based on classification
color_map = {
    'Web Activity': 'blue',
    'File Activity': 'green',
    'Software Activity': 'orange',
}

# Loop through each unique classification and create a separate scatter plot for test data
for classification in unique_test_classes:
    plt.figure(figsize=(10, 6))
    class_data = test_data_df[test_data_df['Classification'] == classification]
    
    # Plot normal data points (Prediction = 1) with specific color
    plt.scatter(class_data[class_data['Prediction'] == 1]['Source Port'],
                class_data[class_data['Prediction'] == 1]['Destination Port'],
                color=color_map[classification], alpha=0.5, label='Normal Data')
    
    # Plot abnormal data points (Prediction = -1)
    plt.scatter(class_data[class_data['Prediction'] == -1]['Source Port'],
                class_data[class_data['Prediction'] == -1]['Destination Port'],
                color='red', alpha=0.5, label='Abnormal Data')
    
    plt.title(f'Test Data Distribution - {classification}')
    plt.xlabel('Source Port')
    plt.ylabel('Destination Port')
    plt.grid(True)
    plt.legend()
    
    # Save each plot as a PNG file
    plt.savefig(f'scatter_plots/{classification}_scatter_plot.png')
    plt.close()

# Create a combined scatter plot for all classifications in test data
plt.figure(figsize=(10, 6))

for classification in unique_test_classes:
    class_data = test_data_df[test_data_df['Classification'] == classification]
    
    # Plot normal data points (Prediction = 1) with specific color
    plt.scatter(class_data[class_data['Prediction'] == 1]['Source Port'],
                class_data[class_data['Prediction'] == 1]['Destination Port'],
                color=color_map[classification], label=f'{classification} - Normal', alpha=0.5) 
                # color='blue', label='Normal Data'
    
    # Plot abnormal data points (Prediction = -1)
    plt.scatter(class_data[class_data['Prediction'] == -1]['Source Port'],
                class_data[class_data['Prediction'] == -1]['Destination Port'],
                color='red', label='Abnormal Data', alpha=0.5)

plt.title('Test Data Distribution - All Classifications')
plt.xlabel('Source Port')
plt.ylabel('Destination Port')
plt.legend()
plt.grid(True)

# Save the combined plot as a PNG file
plt.savefig('scatter_plots/all_classifications_scatter_plot.png')
plt.close()

# Save the results to a new CSV file
output_file = 'synthetic_anomaly_detection_results.csv'
test_data_df.to_csv(output_file, index=False)
