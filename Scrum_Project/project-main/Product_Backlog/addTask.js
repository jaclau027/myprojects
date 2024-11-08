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
  // Initialize Firebase
  const app = initializeApp(firebaseConfig);
  
  const db = getDatabase(app);
  
  const decrementButton = document.querySelector(".decrement-button");
  const incrementButton = document.querySelector(".increment-button");
  const storyPointsElement = document.getElementById("StoryPoints");
  decrementButton.addEventListener("click", function () {
    const currentValue = parseInt(storyPointsElement.value);
    if (currentValue > 1) {
      storyPointsElement.value = currentValue - 1;
    }
  });
  
  incrementButton.addEventListener("click", function () {
    const currentValue = parseInt(storyPointsElement.value);
    if (currentValue < 10) {
      storyPointsElement.value = currentValue + 1;
    }
  });
  // Tags
  const
    taskTagsElement = document.getElementById("tags-container"),
    tagsOptionsElement = document.getElementById("tag-menu"),
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
    tagDisplayList = [FE,BE,API,DB,FW,TEST,UI,UX]
  
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
  
  document.getElementById("save-button").addEventListener('click', function(e){
            
    e.preventDefault();
  
    const dateAdded = new Date();
    // console.log(dateAdded.toLocaleString());
    const taskName = document.getElementById("task-name").value;
    const taskDescription = document.getElementById("task-description").value;
    const taskPriority = document.getElementById("priorities").value;
  
    const taskAssignee = document.getElementById("AssigneeList").value;
    const taskStage = document.getElementById("SOT").value;
  
    const radioNotStarted = document.getElementById("Not Started");
    const radioInProgress = document.getElementById("In Progress");
    const radioCompleted = document.getElementById("Completed");
    let taskStatus = ""
    // Determine and select Status
    if (radioNotStarted.checked){
        taskStatus = radioNotStarted.value;
    } else if (radioInProgress.checked){
        taskStatus = radioInProgress.value;
    } else if (radioCompleted.checked){
        taskStatus = radioCompleted.value;
    }
  
    const story = document.getElementById("Story");
    const bug = document.getElementById("Bug");
    let taskCategory = ""
    if (story.checked){
        taskCategory = story.value;
    }
    else if (bug.checked){
        taskCategory= bug.value;
    }
  
    let taskTags = [];
          tagOptionList.forEach((tag) => {
              if (tag.checked){
                  taskTags.push(tag.value)
              }
          })
  
  
    // Create an empty array to store selected status options
    // const selectedStatus = [];
    // const statusCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');
    // console.log(statusCheckboxes);
    // for(let i = 0; i < statusCheckboxes.length; i++){
    //     selectedStatus.push(statusCheckboxes[i].value)
    // }
    if (taskName.trim() !== "" && taskAssignee!== "" && taskPriority!== "" && taskPriority!== "" && taskStage!== ""
    && storyPointsElement!== 0 && taskStatus!== "" && taskCategory!== "" && taskDescription!== "") {
  
      set(ref(db, 'productBacklog/' + document.getElementById("task-name").value),
      {
          dateAdded : dateAdded.toLocaleString("zh-CN"),
          taskName : taskName,
          taskAssignee : taskAssignee,
          taskPriority : taskPriority,
          taskStage : taskStage,
          taskStoryPoints : storyPointsElement.value,
          taskStatus : taskStatus,      
          taskCategory : taskCategory,
          taskDescription : taskDescription,
          taskTags : taskTags
  
      })
      .then(() => {window.location.href = "prodBacklog.html"});
    }
  });
  
  // To receive the input and display it
  // const decrementButton = document.querySelector(".decrement-button");
  // const incrementButton = document.querySelector(".increment-button");
  // const 
    // taskNameElement = document.getElementById("task-name"),
    // taskDescriptionElement = document.getElementById("task-description"),
    // taskPriorityElement = document.getElementById("priorities"),
    // storyPointsElement = document.getElementById("StoryPoints"),
    // AssigneeListElement = document.getElementById("AssigneeList"),
  
    // radioNotStarted = document.getElementById("Not Started"),
    // radioInProgress = document.getElementById("In Progress"),
    // radioCompleted = document.getElementById("Completed"),
  
    // taskStageElement = document.getElementById("SOT"),
    // taskTagsElement = document.getElementById("tags-container"),
    // tagsOptionsElement = document.getElementById("tag-menu"),
    // savebutton = document.getElementById("save-button")
    // FEoption = document.getElementById("FEoption"),
    // BEoption = document.getElementById("BEoption"),
    // APIoption = document.getElementById("APIoption"),
    // DBoption = document.getElementById("DBoption"),
    // FWoption = document.getElementById("FWoption"),
    // TESToption = document.getElementById("TESToption"),
    // UIoption = document.getElementById("UIoption"),
    // UXoption = document.getElementById("UXoption"),
    // tagOptionList = [FEoption,BEoption,APIoption,DBoption,FWoption,TESToption,UIoption,UXoption],
    // FE = document.getElementById("FE"),
    // BE = document.getElementById("BE"),
    // API = document.getElementById("API"),
    // DB = document.getElementById("DB"),
    // FW = document.getElementById("FW"),
    // TEST = document.getElementById("TEST"),
    // UI = document.getElementById("UI"),
    // UX = document.getElementById("UX"),
    // increment = document.getElementById("increment"),
    // decrement = document.getElementById("decrement"),
    // tagDisplayList = [FE,BE,API,DB,FW,TEST,UI,UX]
    // story = document.getElementById("Story"),
    // bug = document.getElementById("Bug")
  // decrementButton.addEventListener("click", function () {
  //   const currentValue = parseInt(storyPointsElement.value);
  //   if (currentValue > 1) {
  //     storyPointsElement.value = currentValue - 1;
  //   }
  // });
  
  // incrementButton.addEventListener("click", function () {
  //   const currentValue = parseInt(storyPointsElement.value);
  //   if (currentValue < 10) {
  //     storyPointsElement.value = currentValue + 1;
  //   }
  // });
  // function checkTag() {
  //     tagOptionList.forEach(tag => {
  //       if (tag.checked == true){
  //         tagDisplayList[tagOptionList.indexOf(tag)].style.display = "block"
  //       }
  //       else {
  //         tagDisplayList[tagOptionList.indexOf(tag)].style.display = "none"
  //       }
  //     })}
  //   const check = setInterval(checkTag,10)
  
  document.addEventListener("DOMContentLoaded", function () {
      const saveTaskButton = document.getElementById("save-button");
      const taskForm = document.getElementById("taskForm")
  
      // Click event for "Save" button
      saveTaskButton.addEventListener("click", function (event) {
          event.preventDefault();
          // Get task inputs
          const taskName = taskNameElement.value;
          const taskDescription = taskDescriptionElement.value;
          const taskPriority = taskPriorityElement.value;
          const taskStage = taskStageElement.value;
          const taskAssignee = AssigneeListElement.value;
          const storyPoints = storyPointsElement.value;
          // nned modify
          //let taskTags = taskTagsInput.value;
          let taskTags = [];
          tagOptionList.forEach((tag) => {
              if (tag.checked){
                  taskTags.push(tag.value)
              }
          })
   
          let taskStatus = ""
          // Determine and select Status
          if (radioNotStarted.checked){
              taskStatus = radioNotStarted.value
          } else if (radioInProgress.checked){
              taskStatus = radioInProgress.value
          } else if (radioCompleted.checked){
              taskStatus = radioCompleted.value
          }
          let taskCategory = ""
          if (story.checked){
              taskCategory = story.value
          }
          else if (bug.checked){
              taskCategory= bug.value
          }
  
          // Add the new task to the savedTasks array
          if (taskName.trim() !== "" && taskStatus!== "") {
              // Load existing tasks from local storage
              const savedTasks = JSON.parse((localStorage.getItem("tasks"))) || [];
              
              // Add the new task
              savedTasks.push({ name: taskName, 
                  description: taskDescription, 
                  priority: taskPriority, 
                  stage: taskStage, 
                  assignee: taskAssignee, 
                  status: taskStatus,
                  storyPoints: storyPoints, 
                  tags: taskTags,//taskTags  here should be a arraylist now is undefined/null
                  category:taskCategory
                  
              });
              // // Function to display tasks
              // function displayTasks() {
              //     taskList.innerHTML = "";
              //     savedTasks.forEach(function (task, index) {
              //             // Create a task card with a link to view details
              //             const taskCard = document.createElement("div");
              //             taskCard.classList.add("cardview");
  
              //             taskCard.innerHTML = `<a href="task.html?id=${index}"><strong>${task.name}</strong>
              //                                     <br>Priority: ${task.priority}
              //                                     <br>Story Points: ${task.storyPoints}
              //                                     <br>Tags: ${task.tags}`;
              //             taskList.appendChild(taskCard);
  
  
              //     });
              // }
  
              // Save the updated tasks array to local storage
              localStorage.setItem("tasks", JSON.stringify(savedTasks));
  
              // Navigate back to the index.html page
              window.location.href = "prodBacklog.html";
          }
      });
  });