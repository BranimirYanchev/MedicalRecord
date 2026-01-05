import { API } from "./core/api.js";

const statDoctors  = document.querySelector(".stat-card:nth-child(1) .stat-card__value");
const statPatients = document.querySelector(".stat-card:nth-child(2) .stat-card__value");
const statVisits   = document.querySelector(".stat-card:nth-child(3) .stat-card__value");

async function loadDashboardStats() {
    try {
        const [doctors, patients, visits] = await Promise.all([
            API.get("/doctor/all"),
            API.get("/patient/all"),
            API.get("/visit/all")
        ]);

        statDoctors.textContent  = doctors.length;
        statPatients.textContent = patients.length;
        statVisits.textContent   = visits.length;

    } catch (err) {
        console.error("Dashboard error:", err);
        statDoctors.textContent = "—";
        statPatients.textContent = "—";
        statVisits.textContent = "—";
    }
}

document.addEventListener("DOMContentLoaded", loadDashboardStats);
