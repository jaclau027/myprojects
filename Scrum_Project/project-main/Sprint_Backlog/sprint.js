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


const app = initializeApp(firebaseConfig);

const db = getDatabase(app);

const taskList = document.getElementById("card-views-container");
const savedTasks = [];
const originalTasks = [];
document.addEventListener("DOMContentLoaded", function(){
    const urlParams = new URLSearchParams(window.location.search),
sprintId = urlParams.get("sprintId")

    const que = query(ref(db, `newsprint/${sprintId}/selectedTasks`));

    const lightBlueFrames = document.querySelectorAll('.kanban-column'); //.light-blue-frame
    lightBlueFrames.forEach((frame) => {
        frame.addEventListener("dragover", allowDrop);
        frame.addEventListener("drop", drop);
    });
   
    get(que).then((snapshot) => {
        if (snapshot.exists()) {
            snapshot.forEach((childSnapshot) => {
                const childData = childSnapshot.val();
                savedTasks.push(childData)
                originalTasks.push(childData)
            });
    }}
    
    )
    .then(() => {console.log(savedTasks)})
    .then(() => displayTasks(savedTasks))
    .then(() => addDraggableAttributes())
    .catch((error) => {
        console.error("Error getting data:", error);
    });
    console.log(savedTasks)
    })


    // DISPLAY
    function displayTasks(taskData) {
        if (taskList !== null){
            console.log(taskData)
            taskList.innerHTML = "";
            taskData.forEach(function (task, index) {
    
                const taskCard = document.createElement("div");
                taskCard.classList.add("cardview");
                taskCard.id = `${index}`;
                taskCard.draggable = true;
    
                const taskName = document.createElement("div");
                taskName.className = "task-name";
                taskName.textContent = task.taskName;
    
                const priority = document.createElement("div");
                priority.className = "priority";
                priority.textContent = `Priority: ${task.taskPriority}`;
    
    
                const storyPoints = document.createElement("div");
                storyPoints.className = "story-points";
                storyPoints.textContent = `Story Points: ${task.taskStoryPoints}`;


                const tagsPair = ['Frontend','Backend', 'API','Database','Framework','Testing','UI','UX']
                const tagsContainer = document.createElement("div");
                tagsContainer.className = "tags-container";
                if (typeof(task.taskTags) !== "undefined"){
                    task.taskTags.forEach((tag) => {
                        if (tagsPair.includes(tag)){
                            const tagElement = document.createElement("div");
                            tagElement.classList.add("tags",`tags${tagsPair.indexOf(tag)+1}`);
                            tagsContainer.appendChild(tagElement);
                        }
                    });   
                }

                taskCard.appendChild(taskName)
                taskCard.appendChild(priority)
                taskCard.appendChild(storyPoints)
                taskCard.appendChild(tagsContainer)
                taskCard.addEventListener("click",function (){
                window.location.href = `task.html?taskId=${taskName.textContent}`})
                taskList.appendChild(taskCard); 
            });
        }
        return taskData;
    }

function addDraggableAttributes() {
    const taskCards = document.querySelectorAll('.cardview');
    taskCards.forEach((taskCard) => {
        taskCard.draggable = true;
        taskCard.addEventListener("dragstart", function (event) {
            onDragStart(event);
        });
    });
}

// Function to allow dropping elements (Event Handler)
function allowDrop(event) {
    event.preventDefault();
}

// Function to handle the drop event (Event Handler)
function drop(event) {
    event.preventDefault();
    const taskId = event.dataTransfer.getData("text/plain");
    const taskCard = document.getElementById(taskId);
    
    // Find the target light blue frame
    const targetLightBlueFrame = event.target.closest('.kanban-column');
    
    if (targetLightBlueFrame && taskCard) {
        // Append the taskCard to the target light blue frame
        targetLightBlueFrame.querySelector('.card-views').appendChild(taskCard);
        
        // Update the task status (To do, In Progress, Done) based on the target light blue frame
        const status = targetLightBlueFrame.querySelector('.kanban-card').id;
        // You can update the task status in your savedTasks or database here
        
    }
}


// Function to handle the drag start event (Event Handler)
function onDragStart(event) {
    event.dataTransfer.setData("text/plain", event.target.id);
}

