# from flask import Flask, request
# import subprocess
# import os

# app = Flask(__name__)

# @app.route('/trigger_simulation', methods=['POST'])
# def trigger_simulation():
#     subprocess.run(["python3", "network_simulation.py"], check=True)
#     return "Simulation initiated successfully", 200

# if __name__ == "__main__":
#     app.run(host='0.0.0.0', port=5000)

# from flask import Flask, request
# from flask_socketio import SocketIO, emit
# import subprocess
# import os
# import logging

# app = Flask(__name__)
# socketio = SocketIO(app)
# # Setup basic logging
# logging.basicConfig(level=logging.DEBUG)

# @app.route('/trigger_simulation', methods=['POST'])
# def trigger_simulation():
#     try:
#         # Log the attempt to run the simulation script
#         logging.info("Attempting to trigger the simulation...")
        
#         # Execute the simulation script
#         subprocess.run(["python3", "network_simulation.py"], check=True, stderr=subprocess.PIPE)
#         logging.info("Simulation initiated.")

#         subprocess.run(["python3", "decode.py"], check=True)
#         logging.info("Decoding complete.")

#         subprocess.run(["python3", "analyze.py"], check=True)
        
#         # If successful, log and return success message
#         return "Simulation initiated successfully", 200
   
#     except subprocess.CalledProcessError as e:
#         # Log error details and return a failure message
#         logging.error(f"Failed to initiate simulation: {e}")
#         return f"Error: {str(e)}", 500

# if __name__ == "__main__":
#     # app.run(host='0.0.0.0', port=8000)
#     socketio.run(app, host='0.0.0.0', port=8000)

from flask import Flask
from flask_socketio import SocketIO, emit
import subprocess
import logging

app = Flask(__name__)
socketio = SocketIO(app, cors_allowed_origins="*")

@app.route('/trigger_simulation', methods=['POST'])
def trigger_simulation():
    try:
        logging.info("Triggering network simulation...")
        emit('update', {'message': 'Starting network simulation...'}, namespace='/simulation')
        subprocess.run(["python3", "network_simulation.py"], check=True)
        emit('update', {'message': 'Network simulation complete. Decoding...'})

        subprocess.run(["python3", "decode.py"], check=True)
        emit('update', {'message': 'Decoding complete. Analyzing data...'}, namespace='/simulation')

        subprocess.run(["python3", "analyze.py"], check=True)
        emit('update', {'message': 'Data analysis complete.'})

        return "Simulation initiated successfully", 200
    except subprocess.CalledProcessError as e:
        logging.error(f"Simulation failed: {e}")
        emit('update', {'message': f"Error: {str(e)}"})
        return f"Error: {str(e)}", 500

if __name__ == "__main__":
    socketio.run(app, host='0.0.0.0', port=8000)