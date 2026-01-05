import { API } from "./core/api.js";

// =====================================
//  INITIAL LOAD: Doctors + Patients
// =====================================
async function initReports() {
    const doctors = await API.get("/doctor/all");
    const patients = await API.get("/patient/all");

    // Dropdowns
    const selDocPatients = document.getElementById("rDoctorPatients");
    const selDocPeriod = document.getElementById("rDoctorVisitsPeriod");
    const selPatientVisits = document.getElementById("rPatientVisits");

    if (selDocPatients)
        selDocPatients.innerHTML = doctors
            .map(d => `<option value="${d.id}">${d.name}</option>`)
            .join("");

    if (selDocPeriod)
        selDocPeriod.innerHTML = doctors
            .map(d => `<option value="${d.id}">${d.name}</option>`)
            .join("");

    if (selPatientVisits)
        selPatientVisits.innerHTML = patients
            .map(p => `<option value="${p.id}">${p.name}</option>`)
            .join("");
}

document.addEventListener("DOMContentLoaded", initReports);

// =====================================
// 1) PATIENTS BY DIAGNOSIS
// =====================================
window.reportPatientsByDiagnosis = async function () {
    const name = document.getElementById("rDiagnosisName").value.trim();
    const box = document.getElementById("rPatientsByDiagnosis");

    if (!name) {
        box.innerHTML = "<p>Моля въведете диагноза.</p>";
        return;
    }

    const data = await API.get(`/reports/patients-by-diagnosis?name=${name}`);

    if (!data.length) {
        box.innerHTML = "<p>Няма пациенти с тази диагноза.</p>";
        return;
    }

    const rows = data.map(p => `
        <tr>
            <td>${p.id}</td>
            <td>${p.name}</td>
            <td>${p.egn}</td>
            <td>${p.personalDoctor?.name ?? "—"}</td>
        </tr>
    `).join("");

    box.innerHTML = renderTable(
        ["ID", "Име", "ЕГН", "Личен лекар"],
        rows
    );
};


// =====================================
// 2) PATIENTS BY PERSONAL DOCTOR
// =====================================
window.reportPatientsByDoctor = async function () {
    const doctorId = document.getElementById("rDoctorPatients").value;
    const box = document.getElementById("rPatientsByDoctor");

    const data = await API.get(`/reports/patients-by-doctor?doctorId=${doctorId}`);

    if (!data.length) {
        box.innerHTML = "<p>Няма пациенти при този лекар.</p>";
        return;
    }

    box.innerHTML = data
        .map(p => `<p>${p.name} – ЕГН: ${p.egn}</p>`)
        .join("");
};

// =====================================
// 3) VISITS OF PATIENT
// =====================================
window.reportVisitsOfPatient = async function () {
    const patientId = document.getElementById("rPatientVisits").value;
    const box = document.getElementById("rVisitsOfPatient");

    const data = await API.get(`/reports/visits-of-patient?patientId=${patientId}`);

    if (!data.length) {
        box.innerHTML = "<p>Няма регистрирани посещения.</p>";
        return;
    }

    box.innerHTML = data
        .map(v => `<p>Посещение #${v.id} – ${v.visitDate} – Лекар: ${v.doctor?.name}</p>`)
        .join("");
};

// =====================================
// 4) VISITS OF DOCTOR IN PERIOD
// =====================================
window.reportVisitsDoctorPeriod = async function () {
    const doctorId = document.getElementById("rDoctorVisitsPeriod").value;
    const start = document.getElementById("rStart").value;
    const end = document.getElementById("rEnd").value;
    const box = document.getElementById("rVisitsDoctorPeriod");

    if (!start || !end) {
        box.innerHTML = "<p>Моля изберете период.</p>";
        return;
    }

    const data = await API.get(
        `/reports/visits-doctor-period?doctorId=${doctorId}&start=${start}&end=${end}`
    );

    if (!data.length) {
        box.innerHTML = "<p>Няма посещения за този период.</p>";
        return;
    }

    box.innerHTML = data
        .map(v => `<p>#${v.id} – ${v.visitDate} – Пациент: ${v.patient?.name}</p>`)
        .join("");
};

// =====================================
// 5) MOST COMMON DIAGNOSIS (OPTIONAL UI)
// =====================================
window.loadMostCommonDiagnosis = async function () {
    const box = document.getElementById("mostCommonDiag");

    if (!box) return;

    const result = await API.get("/reports/most-common-diagnosis");
    box.innerHTML = `<p>Най-честата диагноза е: <b>${result}</b></p>`;
};

// =====================================
// 6) COUNT PATIENTS PER DOCTOR
// =====================================
window.loadPatientsCountPerDoctor = async function () {
    const box = document.getElementById("patientsPerDoctor");

    if (!box) return;

    const data = await API.get("/reports/count-patients-per-doctor");

    box.innerHTML = Object.entries(data)
        .map(([doctor, count]) => `<p>${doctor}: <b>${count}</b> пациенти</p>`)
        .join("");
};

// =====================================
// 7) COUNT VISITS PER DOCTOR
// =====================================
window.loadVisitsCountPerDoctor = async function () {
    const box = document.getElementById("visitsPerDoctor");

    if (!box) return;

    const data = await API.get("/reports/count-visits-per-doctor");

    box.innerHTML = Object.entries(data)
        .map(([doctor, count]) => `<p>${doctor}: <b>${count}</b> посещения</p>`)
        .join("");
};

// =====================================
// 8) MONTH WITH MOST LEAVES
// =====================================
window.loadMonthMostLeaves = async function () {
    const box = document.getElementById("monthMostLeaves");
    if (!box) return;

    const month = await API.get("/reports/month-most-leaves");
    box.innerHTML = `<p>Месец с най-много болнични: <b>${month}</b></p>`;
};

// =====================================
// 9) ALL VISITS IN PERIOD
// =====================================
window.reportVisitsInPeriod = async function () {
    const start = document.getElementById("rAllStart").value;
    const end = document.getElementById("rAllEnd").value;
    const box = document.getElementById("rVisitsInPeriod");

    if (!start || !end) {
        box.innerHTML = "<p>Моля изберете период.</p>";
        return;
    }

    try {
        const data = await API.get(
            `/reports/visits-period?start=${start}&end=${end}`
        );

        if (!data.length) {
            box.innerHTML = "<p>Няма прегледи за този период.</p>";
            return;
        }

        box.innerHTML = data
            .map(v => `
                <p>
                    #${v.id} – ${v.visitDate}
                    – Лекар: ${v.doctor?.name ?? "—"}
                    – Пациент: ${v.patient?.name ?? "—"}
                </p>
            `)
            .join("");

    } catch (err) {
        console.error("Visits in period error:", err);
        box.innerHTML = "<p>Грешка при зареждане на справката.</p>";
    }
};


// =====================================
// 10) DOCTORS WITH MOST LEAVES
// =====================================
window.loadDoctorsMostLeaves = async function () {
    const box = document.getElementById("doctorsMostLeaves");
    if (!box) return;

    const data = await API.get("/reports/doctors-most-leaves");

    box.innerHTML = Object.entries(data)
        .map(([doctor, count]) => `<p>${doctor}: <b>${count}</b> болнични</p>`)
        .join("");
};

async function loadTopReports() {

    // Най-честа диагноза
    const diag = await API.get("/reports/most-common-diagnosis");
    document.getElementById("mostCommonDiag").textContent = diag.value || "—";

    // Месец с най-много болнични
    const month = await API.get("/reports/month-most-leaves");
    document.getElementById("monthMostLeaves").textContent = month || "—";

    // Лекари с най-много болнични
    const docs = await API.get("/reports/doctors-most-leaves");
    document.getElementById("doctorsMostLeaves").innerHTML =
        Object.entries(docs).map(([name, count]) =>
            `<p>${name}: ${count}</p>`
        ).join("") || "—";
}

async function loadOverview() {
    const table = document.getElementById("overviewTable");
    table.innerHTML = "";

    try {
        const data = await API.get("/reports/overview");

        data.forEach(row => {
            table.innerHTML += `
                <tr>
                    <td>${row.type}</td>
                    <td>${row.description}</td>
                    <td>${row.count}</td>
                </tr>
            `;
        });

    } catch (err) {
        console.error("Overview error:", err);
        table.innerHTML = `<tr><td colspan="3">Грешка при зареждането</td></tr>`;
    }
}

function renderTable(headers, rowsHtml) {
    return `
        <div class="table-wrapper">
            <table class="reports-table">
                <thead>
                    <tr>
                        ${headers.map(h => `<th>${h}</th>`).join("")}
                    </tr>
                </thead>
                <tbody>
                    ${rowsHtml}
                </tbody>
            </table>
        </div>
    `;
}


document.addEventListener("DOMContentLoaded", loadOverview);

document.addEventListener("DOMContentLoaded", loadTopReports);

