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
  // Initialize Firebase
  const app = initializeApp(firebaseConfig);
  const db = getDatabase(app);

  // Add event listener to the delete button
  document.getElementById("delete-button").addEventListener("click", function (e) {
    const selectedMembers = document.querySelectorAll('.details-frame input[type="checkbox"]:checked');

    if (selectedMembers.length === 0) {
      alert("Please select at least one team member to delete.");
    } else {
      if (confirm("Are you sure you want to delete the selected team members?")) {
        const memberIdsToDelete = Array.from(selectedMembers).map(checkbox => checkbox.getAttribute("data-member-id"));
        
        // Delete the selected members from the Firebase database
        memberIdsToDelete.forEach(memberId => {
          const memberRef = ref(db, `addMember/${memberId}`);
          remove(memberRef)
            .then(() => {
              alert(`Member with ID ${memberId} has been deleted.`);
              window.location.reload();
            })
            .catch(error => {
              console.error("Error deleting member:", error);
            });
        });
      }
    }
  });

  document.getElementById("cancel-button").addEventListener("click", function (e) {
    window.location.href = "profilepage.html";
  });

  document.querySelector(".previous-page a").addEventListener("click", function (e) {
    e.preventDefault(); 
    window.location.href = "profilepage.html";
  });
});