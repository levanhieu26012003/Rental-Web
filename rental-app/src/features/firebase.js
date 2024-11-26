// src/firebase.js
import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";
// import { getAnalytics } from "firebase/analytics"; // Chỉ sử dụng nếu bạn cần Analytics

const firebaseConfig = {
  apiKey: "AIzaSyA-NVTDhRJxjOdpS0vC1--jVNuoLrYZnOs",
  authDomain: "hieukhoa-f4b08.firebaseapp.com",
  projectId: "hieukhoa-f4b08",
  storageBucket: "hieukhoa-f4b08.appspot.com",
  messagingSenderId: "213470038105",
  appId: "1:213470038105:web:d22f27f6244919b1130948",
  measurementId: "G-0D5EBLDJ1W"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// const analytics = getAnalytics(app); // Chỉ sử dụng nếu bạn cần Analytics

// Initialize Firestore and Auth
const firestore = getFirestore(app);
const authFirebase = getAuth(app);

export { firestore, authFirebase };
