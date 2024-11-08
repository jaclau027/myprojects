// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import { getDatabase, ref, set, get, child } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-database.js";
const firebaseConfig = {
    apiKey: "AIzaSyAk2H_8opCo31ebK1Ce_hZ5G36XNkydR1s",
    authDomain: "project-2782373696466964042.firebaseapp.com",
    databaseURL: "https://project-2782373696466964042-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "project-2782373696466964042",
    storageBucket: "project-2782373696466964042.appspot.com",
    messagingSenderId: "971400388443",
    appId: "1:971400388443:web:fc495758d4109f4a1f847e"
};
document.addEventListener("DOMContentLoaded",function(){
// Initialize Firebase
const app = initializeApp(firebaseConfig);

const db = getDatabase(app);

document.getElementById("save-button").addEventListener('click', function(e){
          
    e.preventDefault();

    const sprint = document.getElementById("sprint").value;
    const startDate = document.getElementById("start-date").value;
    const endDate = document.getElementById("end-date").value;

    // Create an empty array to store selected status options
    const selectedStatus = [];
    const statusCheckboxes = document.querySelectorAll('input[type="radio"]:checked');

    for(let i = 0; i < statusCheckboxes.length; i++){
        selectedStatus.push(statusCheckboxes[i].value)
    }
    if(sprint.length !== 0 && startDate.length !== 0 && endDate.length !== 0 && selectedStatus.length !== 0){
        if(startDate <= endDate){
            set(ref(db, 'newsprint/' + document.getElementById("sprint").value),
            {
                sprint : sprint,
                startDate : startDate,
                endDate : endDate,
                selectedStatus : selectedStatus,
                selectedTasks : []
            })
            .then(() => {window.location.href = "scrumboard.html"})  
        }else{
            alert("Start date must be smaller than or equal to the end date.")
        }
    }
    else{
        alert("Please enter all the sprint details.")
    }
  });
})


