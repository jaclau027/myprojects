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

// taskManager.js for productBacklog.html
const addTaskButton = document.getElementById("add-button");
const taskList = document.getElementById("card-views-container");
const NewToOld = document.getElementById("Newest-To-Oldest")
const OldToNew = document.getElementById("Oldest-To-Newest")
const UrgentToLow = document.getElementById("Urgent-To-Low")
const LowToUrgent = document.getElementById("Low-To-Urgent")
const savedTasks = [];
const originalTasks = [];

// const
//     FE = document.getElementById("FE"),
//     BE = document.getElementById("BE"),
//     API = document.getElementById("API"),
//     DB = document.getElementById("DB"),
//     FW = document.getElementById("FW"),
//     TEST = document.getElementById("TEST"),
//     UI = document.getElementById("UI"),
//     UX = document.getElementById("UX"),
//     tagDisplayList = [FE,BE,API,DB,FW,TEST,UI,UX]

document.addEventListener("DOMContentLoaded", function(){

    const app = initializeApp(firebaseConfig);

    const db = getDatabase(app);
    const que = query(ref(db, "productBacklog/"), orderByChild("dateAdded"));
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
    .catch((error) => {
        console.error("Error getting data:", error);
    });
    console.log(savedTasks)
    })


    // DISPLAY
    function displayTasks(taskData) {
        if (taskList !== null){
            taskList.innerHTML = "";
            taskData.forEach(function (task, index) {
    
                const taskCard = document.createElement("div");
                taskCard.classList.add("cardview");
                taskCard.id = `${index}`;
    
    
                const taskName = document.createElement("div");
                taskName.className = "task-name";
                taskName.textContent = task.taskName;
    
                const priority = document.createElement("div");
                priority.className = "priority";
                priority.textContent = `Priority: ${task.taskPriority}`;
    
    
                const storyPoints = document.createElement("div");
                storyPoints.className = "story-points";
                storyPoints.textContent = `Story Points: ${task.taskStoryPoints}`;


                // testing displaying tags
                // const tags = document.createElement("div");
                // tags.className = "tags";
                // tags.textContent = `Tags: ${task.taskTags}`;

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

                const deletecheckbox = document.createElement("div")
                deletecheckbox.classList.add("custom-checkbox")
                deletecheckbox.classList.add("hidden-checkbox")
                const checkboxinput = document.createElement("input")
                checkboxinput.classList.add("delete-checkbox")
                checkboxinput.type = "checkbox"
                const checkmark = document.createElement("span")
                checkmark.classList.add("checkmark")
                deletecheckbox.appendChild(checkboxinput)
                deletecheckbox.appendChild(checkmark)
    
                taskCard.appendChild(taskName)
                taskCard.appendChild(priority)
                taskCard.appendChild(storyPoints)
                taskCard.appendChild(tagsContainer)
                taskCard.appendChild(deletecheckbox)
                taskCard.addEventListener("click",function (){
                window.location.href = `task.html?taskId=${taskName.textContent}`})
                taskList.appendChild(taskCard);    
            });
        }
        return taskData;
    }


    // ADD
    if (addTaskButton !== null){    
        addTaskButton.addEventListener("click", function () {
        window.location.href = "addtask.html"; 
    });
}




    // SORTING
    const displayPriority = {
        "Low":1,
        "Medium":2,
        "Important":3,
        "Urgent":4
    }

    if (NewToOld !== null && OldToNew !== null && UrgentToLow !== null && LowToUrgent !== null){
    NewToOld.addEventListener("click",function(){
        savedTasks.sort((a, b) => {
            const dateA = new Date(a.dateAdded);
            const dateB = new Date(b.dateAdded);
            return dateB - dateA;
        });
        displayTasks(savedTasks)
        savedTasks.reverse()
    })
    OldToNew.addEventListener("click",function(){
        originalTasks.sort((a, b) => {
            const dateA = new Date(a.dateAdded);
            const dateB = new Date(b.dateAdded);
            return dateA - dateB;
        });
        displayTasks(originalTasks)
    })

    UrgentToLow.addEventListener("click",function(){
        const newTaskArray = savedTasks.sort((a,b)=>{
            const priorityA = displayPriority[a.taskPriority]
            const priorityB = displayPriority[b.taskPriority]
            return priorityB - priorityA
        })
        displayTasks(newTaskArray)
    })
    LowToUrgent.addEventListener("click",function(){
        const newTaskArray = savedTasks.sort((a,b)=>{
            const priorityA = displayPriority[a.taskPriority]
            const priorityB = displayPriority[b.taskPriority]
            return priorityA - priorityB
        })
        displayTasks(newTaskArray)
    })
};

// export const displayedTasks = displayTasks(savedTasks);

