// import { displayedTasks } from "./taskManager.js";
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import { getDatabase, ref, set, get, child, query, update } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-database.js";
const firebaseConfig = {
    apiKey: "AIzaSyAk2H_8opCo31ebK1Ce_hZ5G36XNkydR1s",
    authDomain: "project-2782373696466964042.firebaseapp.com",
    databaseURL: "https://project-2782373696466964042-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "project-2782373696466964042",
    storageBucket: "project-2782373696466964042.appspot.com",
    messagingSenderId: "971400388443",
    appId: "1:971400388443:web:fc495758d4109f4a1f847e"
};

const 
  FEoption = document.getElementById("FEoption"),
  BEoption = document.getElementById("BEoption"),
  APIoption = document.getElementById("APIoption"),
  DBoption = document.getElementById("DBoption"),
  FWoption = document.getElementById("FWoption"),
  TESToption = document.getElementById("TESToption"),
  UIoption = document.getElementById("UIoption"),
  UXoption = document.getElementById("UXoption"),
  tagOptionList = [FEoption,BEoption,APIoption,DBoption,FWoption,TESToption,UIoption,UXoption],
  FE = document.getElementById("FE"),
  BE = document.getElementById("BE"),
  API = document.getElementById("API"),
  DB = document.getElementById("DB"),
  FW = document.getElementById("FW"),
  TEST = document.getElementById("TEST"),
  UI = document.getElementById("UI"),
  UX = document.getElementById("UX"),
  increment = document.getElementById("increment"),
  decrement = document.getElementById("decrement"),
  tagDisplayList = [FE,BE,API,DB,FW,TEST,UI,UX],
  urlParams = new URLSearchParams(window.location.search),
  taskId = urlParams.get("taskId"),
  previousPage = document.getElementById("previous-page")
  const taskNameElement = document.getElementById("task-name");
const taskDescriptionElement = document.getElementById("task-description");
const taskPriorityElement = document.getElementById("priorities");
const storyPointsElement = document.getElementById("storyPoints");
const taskAssigneeElement = document.getElementById("AssigneeList");
const taskStageElement = document.getElementById("SOT");
const notStarted = document.getElementById("Not Started"),
  inProgress = document.getElementById("In Progress"),
  completed = document.getElementById("Completed"),
  story = document.getElementById("Story"),
  bug = document.getElementById("Bug")
const deletebutton = document.getElementById("delete-button")
const savebutton = document.getElementById("save-button");


  const app = initializeApp(firebaseConfig);
  const db = getDatabase(app);
  let tags = ["Frontend","Backend","API","Database","Framework","Testing","UI","UX"]

document.addEventListener("DOMContentLoaded", function () {
  const dataRef = ref(db, "productBacklog/" + taskId);
  const savedTasks = []

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
        taskPriorityElement.value = task["taskPriority"];
        storyPointsElement.value = task["taskStoryPoints"];
        taskStageElement.value = task["taskStage"];
        taskAssigneeElement.value = task["taskAssignee"];
        if (task["taskStatus"] == "Not Started"){
          notStarted.checked = true
        }
        else if (task["taskStatus"] == "In Progress"){
          inProgress.checked = true
        }
        else if (task["taskStatus"] == "Completed"){
          completed.checked=true
        }
        if (task["taskCategory"] == "Story"){
          story.checked = true
        }
        else if (task["taskCategory"] == "Bug"){
          bug.checked = true
        }
        try{
            task["taskTags"].forEach((taskTag)=>{
                if (taskTag == null){
                  return;
                }
                else {
                  const tagsPair = ['Frontend','Backend', 'API','Database','Framework','Testing','UI','UX']
                    if (tagsPair.includes(taskTag)){
                      tagDisplayList[tagsPair.indexOf(taskTag)].style.display = "block";
                      tagOptionList[tagsPair.indexOf(taskTag)].checked = true
                          }
                      };   
      
                        })
                    
                
              
        }catch{console.log("Empty")}
    } else {
        taskNameElement.textContent = "Task not found.";
    }
}

//--------------------------------------------------story point-----------------------------------------------------
increment.addEventListener("click",function(){
  if (storyPointsElement.value != storyPointsElement.max){
    storyPointsElement.value = (parseInt(storyPointsElement.value)+1).toString()
  }
})
decrement.addEventListener("click",function(){
  if (storyPointsElement.value != storyPointsElement.min){
    storyPointsElement.value = (parseInt(storyPointsElement.value)-1).toString()
  }
})

//--------------------------------------------------check tag function-----------------------------------------------------
function checkTag() {
  tagOptionList.forEach(tag => {
    if (tag.checked == true){
      tagDisplayList[tagOptionList.indexOf(tag)].style.display = "block"
    }
    else {
      tagDisplayList[tagOptionList.indexOf(tag)].style.display = "none"
    }
  })}
const check = setInterval(checkTag,10)

//--------------------------------------------------save button-----------------------------------------------------
savebutton.addEventListener("click",function(event){
  event.preventDefault();
  let taskTag = [];
        tagOptionList.forEach((tag) => {
            if (tag.checked){
                taskTag.push(tag.value)
            }
        })

  tags = tags.filter(tag => taskTag.includes(tag));
//--------------------------------------------------update data-----------------------------------------------------
    update(ref(db, 'productBacklog/' + taskId),
    {
      taskAssignee :document.querySelector("#AssigneeList").value,
      taskPriority :document.querySelector("#priorities").value,
      taskStage : document.querySelector("#SOT").value,
      taskStoryPoints : document.querySelector("#storyPoints").value,
      taskStatus : document.querySelector('input[name="status"]:checked').value,      
      taskCategory : document.querySelector('input[name="category"]:checked').value,
      taskDescription : document.querySelector("#task-description").value,
      taskTags : taskTag
    })

    .then(() => { window.location.href = `task.html?taskId=${taskId}`});

})
previousPage.addEventListener("click",function(){
  window.location.href = `task.html?taskId=${taskId}`
})
}
);