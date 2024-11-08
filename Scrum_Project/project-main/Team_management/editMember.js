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

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

const urlParams = new URLSearchParams(window.location.search);
const memberID = urlParams.get("memberID");

const usernameInput = document.getElementById("username");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const confirmButton = document.getElementById("confirm-button");

const memberRef = ref(db, `addMember/${memberID}`);
get(memberRef).then((snapshot) => {
    if (snapshot.exists()) {
        const data = snapshot.val();
        usernameInput.value = data.username;
        emailInput.value = data.email;
        passwordInput.value = data.password;
    }
});

confirmButton.addEventListener("click", () => {
    const updatedUsername = usernameInput.value;
    const updatedEmail = emailInput.value;
    const updatedPassword = passwordInput.value;

    if (updatedUsername && updatedEmail && updatedPassword) {
        const updates = {
            [`addMember/${memberID}/username`]: updatedUsername,
            [`addMember/${memberID}/email`]: updatedEmail,
            [`addMember/${memberID}/password`]: updatedPassword
        };

        update(ref(db), updates)
            .then(() => {
                alert("Member details have been updated.");
                window.location.href = "profilepage.html";
            })
            .catch((error) => {
                console.error("Error updating member details:", error);
                alert("An error occurred while updating member details.");
            });
    } else {
        alert("Please fill in all the member's details.");
    }
});
