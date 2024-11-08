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
    const newsprintRef = ref(db, "newsprint/");

    // data array
    const sprintArray = [];
    const sprintList = document.querySelector(".vertical-scroll");
    console.log(sprintList.innerHTML)
    // Retrieve data from the database
    get(newsprintRef).then((snapshot) => {
        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const childData = childSnapshot.val();
                sprintArray.push(childData)
            });
            sprintList.innerHTML = "";
            sprintArray.forEach((sprintData, index) => {
                const cardView = document.createElement("div");
                cardView.classList.add("cardview");
                cardView.id = `sprint${index + 1}`;

                const anchor = document.createElement("a");
                anchor.href = `sprint.html?sprintId=${sprintData.sprint}`;

                const sprintName = document.createElement("div");
                sprintName.classList.add("sprint-name");
                sprintName.textContent = sprintData.sprint;

                const startDate = document.createElement("div");
                startDate.classList.add("startdate");
                startDate.textContent = `Start date: ${sprintData.startDate}`;

                const endDate = document.createElement("div");
                endDate.classList.add("enddate");
                endDate.textContent = `End date: ${sprintData.endDate}`;
                
                const status = document.createElement("div");
                status.classList.add("status");
                status.textContent = `Status: ${sprintData.selectedStatus}`;

                anchor.appendChild(sprintName);
                anchor.appendChild(startDate);
                anchor.appendChild(endDate);
                anchor.appendChild(status);

                cardView.appendChild(anchor);
                sprintList.appendChild(cardView);
            })
        } else {
            console.log("No data available");
        }
    }).catch((error) => {
        console.error("Error getting data:", error);
    });

    // console.log(sprintList)
    // console.log(sprintArray)

})


