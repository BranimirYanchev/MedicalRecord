import { API } from "./core/api.js";

document.addEventListener("DOMContentLoaded", loadVisits);

const role = localStorage.getItem("role");
const loggedDoctorId = localStorage.getItem("doctorId");

if (localStorage.getItem("role") === "DOCTOR") {
    document.getElementById("vDoctor").disabled = true;
}


// ================================
// LOAD VISITS + DOCTORS + PATIENTS
// ================================
async function loadVisits() {
    const visits   = await API.get("/visit/all");
    const doctors  = await API.get("/doctor/all");
    const patients = await API.get("/patient/all");

    const table        = document.getElementById("visitsTable");
    const doctorSelect = document.getElementById("vDoctor");
    const patientSelect= document.getElementById("vPatient");

    // Dropdowns reset
    doctorSelect.innerHTML  = `<option value="">-- Изберете лекар --</option>`;
    patientSelect.innerHTML = `<option value="">-- Изберете пациент --</option>`;

    doctors.forEach(d => {
        doctorSelect.innerHTML += `<option value="${d.id}">${d.name} (${d.specialty})</option>`;
    });

    patients.forEach(p => {
        patientSelect.innerHTML += `<option value="${p.id}">${p.name} (${p.egn})</option>`;
    });

    // Table reset
    table.innerHTML = "";

    visits.forEach(v => {
        table.innerHTML += `
            <tr>
                <td>${v.id}</td>
                <td>${v.visitDate}</td>
                <td>${v.doctor?.name ?? "—"}</td>
                <td>${v.patient?.name ?? "—"}</td>
                <td>${v.diagnosis?.name ?? "—"}</td>
                <td>${v.medicalLeave ? "Да" : "Не"}</td>
               <td>
    ${
            role === "ADMIN" ||
            (role === "DOCTOR" && v.doctor && String(v.doctor.id) === loggedDoctorId)
                ? `
            <button class="btn-small btn-primary" onclick="editVisit(${v.id})">
                Редактирай
            </button>
            <button class="btn-small btn-danger" onclick="deleteEntity('visit', ${v.id})">
                Изтрий
            </button>
        `
                : ""
        }
</td>

            </tr>
        `;
    });

    // 🔥 след като редовете вече ги има в DOM – прилагаме role guard
    if (window.applyRoleGuard) {
        window.applyRoleGuard();
    }
}

document.getElementById("addVisitForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const id        = document.getElementById("editId").value;
    const visitDate = document.getElementById("vDate").value;
    const doctorId  = document.getElementById("vDoctor").value;
    const patientId = document.getElementById("vPatient").value;

    if (!visitDate) return alert("Моля изберете дата.");
    if (!doctorId)  return alert("Моля изберете лекар.");
    if (!patientId) return alert("Моля изберете пациент.");

    const body = { visitDate };

    try {
        if (id) {
            // UPDATE
            await API.put(
                `/visit/update/${id}?doctorId=${doctorId}&patientId=${patientId}`,
                body
            );
        } else {
            // CREATE
            await API.post(
                `/visit/add?doctorId=${doctorId}&patientId=${patientId}`,
                body
            );
        }

        resetForm();
        loadVisits();

    } catch (err) {
        console.error(err);
        alert("Грешка при запис на посещението.");
    }
});

function resetForm() {
    document.getElementById("addVisitForm").reset();
    document.getElementById("editId").value = "";
    document.querySelector("#addVisitForm button").textContent = "Добави посещение";
}

// ================================
// EDIT VISIT → POPULATE FORM
// ================================
window.editVisit = async function (id) {
    try {
        const v = await API.get(`/visit/${id}`);

        // hidden id → означава UPDATE
        document.getElementById("editId").value = v.id;

        // попълване на полетата
        document.getElementById("vDate").value = v.visitDate;
        document.getElementById("vDoctor").value = v.doctor?.id ?? "";
        document.getElementById("vPatient").value = v.patient?.id ?? "";

        // сменяме текста на бутона
        document.querySelector("#addVisitForm button[type='submit']")
            .textContent = "Запази";

        // (по желание) скрол към формата
        document.getElementById("addVisitForm")
            .scrollIntoView({ behavior: "smooth" });

    } catch (err) {
        console.error("Edit visit error:", err);
        alert("Грешка при зареждане на посещението.");
    }
};

