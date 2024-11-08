import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import { getDatabase, ref, set, get, child, query, orderByChild ,push} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-database.js";
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

// taskManager.js for productBacklog.html
const addTaskButton = document.getElementById("add-button-test");
const taskList = document.getElementById("card-views-container");
const NewToOld = document.getElementById("Newest-To-Oldest")
const OldToNew = document.getElementById("Oldest-To-Newest")
const UrgentToLow = document.getElementById("Urgent-To-Low")
const LowToUrgent = document.getElementById("Low-To-Urgent")
const discardButton = document.getElementById("discard-button-test");
const selectedTasks = [];
const savedTasks = [];
const originalTasks = [];

document.addEventListener("DOMContentLoaded", function(){


    // const app = initializeApp(firebaseConfig);

    // const db = getDatabase(app);

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
            console.log(taskData)
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
                // tags.className = "tags"
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

                
                const deletecheckbox = document.createElement("label")
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
                taskCard.appendChild(deletecheckbox)
                taskCard.appendChild(storyPoints)
                taskCard.appendChild(tagsContainer)
                taskList.appendChild(taskCard);    

            });
        }
        return taskData;
    }


    if (addTaskButton !== null) {
        addTaskButton.addEventListener("click", function (e) {
            e.preventDefault();
            const urlParams = new URLSearchParams(window.location.search),
            sprintId = urlParams.get("sprintId")
    
            const checkboxes = document.querySelectorAll(".delete-checkbox");
    
            checkboxes.forEach(function (checkbox) {
                if (checkbox.checked) {
                    const taskName = checkbox.parentElement.parentElement.querySelector(".task-name").textContent; // Assuming task name is in an element with class "task-name"
                    const selectedTask = savedTasks.find(task => task.taskName === taskName);
    
                    if (selectedTask) {
                        // Initialize Firebase
                        const app = initializeApp(firebaseConfig);
                        const db = getDatabase(app);
    
                        // Create a reference for the task name
                        const sprintTaskRef = ref(db, `newsprint/${sprintId}/selectedTasks/${taskName}`);
    
                        // Set the value of the reference to the task's details
                        set(sprintTaskRef, selectedTask)
                            .then(() => {
                                console.log(`Task '${taskName}' saved to Firebase`);
                                window.location.href = `sprint.html?sprintId=${sprintId}`;
                            })
                            .catch((error) => {
                                console.error(`Error saving task '${taskName}' to Firebase:`, error);
                            });
                    }
                }
            });
        });
    }


    if (discardButton !== null) {
        discardButton.addEventListener("click", function () {
            console.log("Discard button clicked");
            selectedTasks.forEach((selectedTask) => {
                const index = savedTasks.indexOf(selectedTask);
                if (index !== -1) {
                    savedTasks.splice(index, 1);
                }
            });
            selectedTasks.length = 0;
            displayTasks(savedTasks);
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
    }
    //;




