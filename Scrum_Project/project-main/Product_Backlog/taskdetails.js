import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import { getDatabase, ref, set, get, child, query, orderByChild } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-database.js";
const firebaseConfig = {
    apiKey: "AIzaSyAk2H_8opCo31ebK1Ce_hZ5G36XNkydR1s",
    authDomain: "project-2782373696466964042.firebaseapp.com",
    databaseURL: "https://project-2782373696466964042-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "project-2782373696466964042",
    storageBucket: "project-2782373696466964042.appspot.com",
    messagingSenderId: "971400388443",
    appId: "1:971400388443:web:fc495758d4109f4a1f847e"
};

// taskdetails.js (for taskdetails.html)
const
FE = document.getElementById("FE"),
BE = document.getElementById("BE"),
API = document.getElementById("API"),
DB = document.getElementById("DB"),
FW = document.getElementById("FW"),
TEST = document.getElementById("TEST"),
UI = document.getElementById("UI"),
UX = document.getElementById("UX"),
tagDisplayList = [FE,BE,API,DB,FW,TEST,UI,UX],
category = document.getElementById('category'),
savedTasks = []
const taskNameElement = document.getElementById("taskNameHeader");
const taskDescriptionElement = document.getElementById("task-description");
const taskPriorityElement = document.getElementById("taskPriority");
const storyPointsElement = document.getElementById("storyPoints");
const taskAssigneeElement = document.getElementById("asignees");
const taskStageElement = document.getElementById("stages");
const taskStatusElement = document.getElementById("status");
const taskTagsElement = document.getElementById("tags");
const editbutton = document.getElementById("edit-button")
const saveTaskButton = document.getElementById("saveTaskButton");

//----------------------------------------------get task id------------------------------------------------------------------
const urlParams = new URLSearchParams(window.location.search);
const taskId = urlParams.get("taskId");
document.addEventListener("DOMContentLoaded", function () {

//----------------------------------------------get data------------------------------------------------------------------
    const app = initializeApp(firebaseConfig);
    const db = getDatabase(app);
    const dataRef = ref(db, "productBacklog/" + taskId);

    get(dataRef).then((snapshot) => {
        if (snapshot.exists()) {
            console.log(snapshot.val())
            snapshot.forEach((childSnapshot) => {
                const childData = childSnapshot.val();
                savedTasks.push(childData)
            });
    }}
    )
    .then(()=> console.log(savedTasks))
    .then(()=> displayTaskDetails(savedTasks));

//----------------------------------------------display task detail------------------------------------------------------------------
function displayTaskDetails(savedTasks){
    if (taskId !== null && taskId.length >= 0) {
        const task = {
            "dateAdded": savedTasks[0],
            "taskAssignee": savedTasks[1],
            "taskCategory": savedTasks[2],
            "taskDescription": savedTasks[3],
            "taskName": savedTasks[4],
            "taskPriority": savedTasks[5],
            "taskStage": savedTasks[6],
            "taskStatus": savedTasks[7],
            "taskStoryPoints": savedTasks[8],
            "taskTags": savedTasks[9]
        } 
        taskNameElement.textContent = task["taskName"];
        taskDescriptionElement.textContent = task["taskDescription"];
        taskPriorityElement.textContent = task["taskPriority"];
        storyPointsElement.textContent = task["taskStoryPoints"];
        taskStageElement.textContent = task["taskStage"];
        taskAssigneeElement.textContent = task["taskAssignee"];
        taskStatusElement.textContent = task["taskStatus"];
        category.textContent = task["taskCategory"];
        try{
            task["taskTags"].forEach((taskTag)=>{
                if (taskTag == null){
                  return;
                }
                else {
                    for (const tag of tagDisplayList){
                        if (taskTag == tag.textContent){
                            console.log(tag.textContent)
                            tag.style.display = "block";
                            break;
                        }
                    }
                }
              })
        }catch{console.log("Empty")}
    } else {
        taskNameElement.textContent = "Task not found.";
    }
}

//----------------------------------------------edit button------------------------------------------------------------------

    editbutton.addEventListener("click",function(event){
        event.preventDefault()
        window.location.href=`edittask.html?taskId=${taskId}`
    })
});