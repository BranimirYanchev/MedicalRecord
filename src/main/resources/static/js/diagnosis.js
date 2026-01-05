import { API } from "./core/api.js";

document.addEventListener("DOMContentLoaded", loadDiagnosis);

async function loadDiagnosis() {
    const visits = await API.get("/visit/all");
    const diagnosis = await API.get("/diagnosis/all");

    const select = document.getElementById("dVisit");
    const table = document.getElementById("diagnosisTable");

    // ❗ FIX: изчистваме dropdown-а преди да добавяме опции
    select.innerHTML = `<option value="">-- Изберете посещение --</option>`;

    visits.forEach(v => {
        select.innerHTML += `<option value="${v.id}">Посещение #${v.id} (${v.visitDate})</option>`;
    });

    // Изчистваме таблицата
    table.innerHTML = "";

    diagnosis.forEach(d => {
        table.innerHTML += `
            <tr>
                <td>${d.id}</td>
                <td>${d.name}</td>
                <td>${d.visit?.id ?? "—"}</td>
                <td>${d.visit?.doctor?.name ?? "—"}</td>
                <td>${d.visit?.patient?.name ?? "—"}</td>
                <td>
                    <button class="btn-small btn-primary" onclick="editDiagnosis(${d.id})">Редактирай</button>
                    <button class="btn-small btn-danger" onclick="deleteEntity('diagnosis', ${d.id})">Изтрий</button>
                </td>
            </tr>
        `;
    });
}


// ========== EDIT ==========
window.editDiagnosis = async function(id) {
    const d = await API.get(`/diagnosis/${id}`);

    document.getElementById("editId").value = id;
    document.getElementById("dName").value = d.name;
    document.getElementById("dVisit").value = d.visit.id;

    // Променяме текста на бутона
    document.querySelector("#addDiagnosisForm button").textContent = "Запази";
};


// ========== ADD / UPDATE ==========
document.getElementById("addDiagnosisForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("editId").value;
    const visitId = document.getElementById("dVisit").value;
    const body = { name: document.getElementById("dName").value.trim() };

    if (!body.name) return alert("Моля въведете име на диагнозата.");

    try {
        if (id) {
            // UPDATE — поддържаме optional visitId
            let url = `/diagnosis/update/${id}`;
            if (visitId) url += `?visitId=${visitId}`;

            await API.put(url, body);
        } else {
            // CREATE — visitId е задължителен
            if (!visitId) return alert("Моля изберете посещение.");

            await API.post(`/diagnosis/add?visitId=${visitId}`, body);
        }

        resetForm();
        loadDiagnosis();

    } catch (e) {
        console.error(e);
        alert("Грешка при записването на диагнозата.");
    }
});


// ========== RESET FORM ==========
function resetForm() {
    document.getElementById("addDiagnosisForm").reset();
    document.getElementById("editId").value = "";
    document.querySelector("#addDiagnosisForm button").textContent = "Добави диагноза";
}


// ========== DELETE (универсално) ==========
window.deleteEntity = async function(type, id) {
    if (!confirm("Наистина ли искате да изтриете този запис?")) return;

    try {
        await API.delete(`/${type}/delete/${id}`);
        loadDiagnosis();
    } catch (e) {
        console.error(e);
        alert("Грешка при изтриването!");
    }
};
