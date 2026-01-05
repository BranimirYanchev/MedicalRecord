import { API } from "./core/api.js";


document.addEventListener("DOMContentLoaded", loadPatients);

async function loadPatients() {
    const patients = await API.get("/patient/all");
    const doctors = await API.get("/doctor/all");

    const table = document.getElementById("patientsTable");
    const select = document.getElementById("pDoctor");

    // Изчистваме dropdown-а и добавяме default
    select.innerHTML = `<option value="">— Няма личен лекар —</option>`;

    doctors.forEach(d => {
        select.innerHTML += `<option value="${d.id}">${d.name}</option>`;
    });

    table.innerHTML = "";

    patients.forEach(p => {
        table.innerHTML += `
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.egn}</td>
                <td>${p.insurancePaid ? "Да" : "Не"}</td>
                <td>${p.personalDoctor?.name ?? "—"}</td>
                <td>
                    <button class="btn-small btn-primary" data-show="ADMIN" onclick="editPatient(${p.id})">Редактирай</button>
                    <button class="btn-small btn-danger" data-show="ADMIN" onclick="deleteEntity('patient', ${p.id})">Изтрий</button>
                </td>
            </tr>
        `;
    });
}

// ===== EDIT PATIENT =====
window.editPatient = async function(id) {
    const p = await API.get(`/patient/${id}`);

    document.getElementById("pName").value = p.name;
    document.getElementById("pEgn").value = p.egn;
    document.getElementById("pInsurance").checked = p.insurancePaid;
    document.getElementById("pDoctor").value = p.personalDoctor?.id ?? "";

    // Скрито поле – държи ID-то при UPDATE
    let hidden = document.getElementById("editId");
    if (!hidden) {
        hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.id = "editId";
        document.getElementById("addPatientForm").appendChild(hidden);
    }

    hidden.value = p.id;

    document.querySelector("#addPatientForm button").textContent = "Запази";
};


// ===== ADD + UPDATE PATIENT =====
document.getElementById("addPatientForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const idElement = document.getElementById("editId");
    const id = idElement ? idElement.value : null;

    const body = {
        name: document.getElementById("pName").value.trim(),
        egn: document.getElementById("pEgn").value.trim(),
        insurancePaid: document.getElementById("pInsurance").checked
    };

    const personalDoctorId = document.getElementById("pDoctor").value || null;

    try {
        if (id) {
            // UPDATE
            await API.put(`/patient/update/${id}?personalDoctorId=${personalDoctorId}`, body);
            alert("Промените са запазени!");
        } else {
            // CREATE
            await API.post(`/patient/add?personalDoctorId=${personalDoctorId}`, body);
            alert("Пациентът е добавен успешно!");
        }

        resetForm();
        loadPatients();

    } catch (err) {
        console.error(err);
        alert("Грешка при записването!");
    }
});


// ===== RESET FORM AFTER SAVE =====
function resetForm() {
    document.getElementById("addPatientForm").reset();

    const idField = document.getElementById("editId");
    if (idField) idField.remove();

    document.querySelector("#addPatientForm button").textContent = "Добави";
}


// ===== DELETE (универсално, вече го имаш при докторите) =====
window.deleteEntity = async function(type, id) {
    if (!confirm("Наистина ли искате да изтриете този запис?")) return;

    try {
        await API.delete(`/${type}/delete/${id}`);
        loadPatients();
    } catch (e) {
        alert("Грешка при изтриването!");
        console.error(e);
    }
};
