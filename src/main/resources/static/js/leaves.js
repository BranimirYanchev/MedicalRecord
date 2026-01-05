import { API } from "./core/api.js";

document.addEventListener("DOMContentLoaded", loadLeaves);

// ============================
// LOAD LEAVES
// ============================
async function loadLeaves() {
    const leaves = await API.get("/leave/all");
    const visits = await API.get("/visit/all");

    const select = document.getElementById("lVisit");
    const table = document.getElementById("leavesTable");

    // dropdown
    select.innerHTML = `<option value="">-- Изберете посещение --</option>`;
    visits.forEach(v => {
        select.innerHTML += `<option value="${v.id}">Посещение #${v.id} (${v.visitDate})</option>`;
    });

    // таблица
    table.innerHTML = "";
    leaves.forEach(l => {
        table.innerHTML += `
            <tr>
                <td>${l.id}</td>
                <td>${l.visit?.patient?.name ?? "—"}</td>
                <td>${l.visit?.doctor?.name ?? "—"}</td>
                <td>${l.visit?.id ?? "—"}</td>
                <td>${l.startDate?.substring(0,10) ?? "—"}</td>
                <td>${l.days}</td>
                <td>
                    <button class="btn-small btn-primary" onclick="editLeave(${l.id})">Редактирай</button>
                    <button class="btn-small btn-danger" onclick="deleteEntity('leave', ${l.id})">Изтрий</button>
                </td>
            </tr>
        `;
    });
}


// ============================
// EDIT
// ============================
window.editLeave = async function (id) {
    const l = await API.get(`/leave/${id}`);

    document.getElementById("editId").value = id;
    document.getElementById("lVisit").value = l.visit.id;
    document.getElementById("lStart").value = l.startDate.substring(0, 10);
    document.getElementById("lDays").value = l.days;

    // Смяна на текста на бутона
    document.getElementById("leaveSubmitBtn").textContent = "Запази";
};


// ============================
// ADD / UPDATE
// ============================
document.getElementById("addLeaveForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const id      = document.getElementById("editId").value;
    const visitId = document.getElementById("lVisit").value;
    const start   = document.getElementById("lStart").value;
    const days    = Number(document.getElementById("lDays").value);

    if (!visitId) return alert("Моля изберете посещение.");
    if (!start)   return alert("Моля изберете начална дата.");
    if (!days)    return alert("Моля въведете брой дни.");

    const body = { startDate: start, days };

    try {
        if (id) {
            // UPDATE
            await API.put(`/leave/update/${id}?visitId=${visitId}`, body);
        } else {
            // CREATE
            await API.post(`/leave/add?visitId=${visitId}`, body);
        }

        resetForm();
        loadLeaves();

    } catch (err) {
        console.error(err);
        alert("Грешка при записване на болничния.");
    }
});


// ============================
// RESET FORM
// ============================
function resetForm() {
    document.getElementById("addLeaveForm").reset();
    document.getElementById("editId").value = "";
    document.getElementById("leaveSubmitBtn").textContent = "Добави болничен";
}


// ============================
// DELETE (универсално)
// ============================
window.deleteEntity = async function (type, id) {
    if (!confirm("Сигурни ли сте, че искате да изтриете този запис?")) return;

    try {
        await API.delete(`/${type}/delete/${id}`);
        loadLeaves();
    } catch (err) {
        console.error(err);
        alert("Грешка при изтриване!");
    }
};
