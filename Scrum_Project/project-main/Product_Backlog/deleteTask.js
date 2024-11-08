import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import { getDatabase, ref, set, get, child, query, orderByChild,remove } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-database.js";
const firebaseConfig = {
    apiKey: "AIzaSyAk2H_8opCo31ebK1Ce_hZ5G36XNkydR1s",
    authDomain: "project-2782373696466964042.firebaseapp.com",
    databaseURL: "https://project-2782373696466964042-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "project-2782373696466964042",
    storageBucket: "project-2782373696466964042.appspot.com",
    messagingSenderId: "971400388443",
    appId: "1:971400388443:web:fc495758d4109f4a1f847e"
};
document.addEventListener("DOMContentLoaded", function () {
    const deleteButton = document.getElementById("delete-button");
    var display = false;
    const app = initializeApp(firebaseConfig);

    const db = getDatabase(app);

    deleteButton.addEventListener("click", function (e) {
      const checkboxes = document.querySelectorAll(".custom-checkbox");
      e.preventDefault(); // Prevent the default behavior of the anchor tag
      checkboxes.forEach(function (checkbox) {
        checkbox.style.display = "block"; // Set display to "block" to show the checkboxes
      });
  
      // Add a click event listener to the "Delete" button on the task cards
      checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener("click", function (e) {
          e.stopPropagation(); // Prevent card click event from firing
  
          // Check if the checkbox is checked
          if (checkbox.querySelector("input[type='checkbox']").checked) {
            // Remove the task card
            const taskCard = checkbox.closest(".cardview");
            if (taskCard) {
              const taskName = taskCard.children[0].textContent;
              console.log(taskName)
              taskToRemove = ref(db,`ProductBacklog/${taskName}`);
              remove(taskToRemove)
            }
          }
        });
      });
    });
  });

