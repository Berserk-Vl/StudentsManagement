const baseURL = "http://localhost:3000";

const nameAddStudentInput = document.getElementById("nameAddStudentInput");
const surnameAddStudentInput = document.getElementById("surnameAddStudentInput");
const patronymicAddStudentInput = document.getElementById("patronymicAddStudentInput");
const dateOfBirthAddStudentInput = document.getElementById("dateOfBirthAddStudentInput");
const groupAddStudentInput = document.getElementById("groupAddStudentInput");

const idDeleteStudentInput = document.getElementById("idDeleteStudentInput");

const studentsTable = document.getElementById("studentsTable");
const itemsPerPageSelect = document.getElementById("itemsPerPageSelect");
const shownAndTotalLabel = document.getElementById("shownAndTotalLabel");

const valueNames = ["id", "surname", "name", "patronymic", "dateOfBirth", "group"];
var itemsPerPage = 0;
var studentsData = undefined;
var page = 0;
var fromIndex = 0;

document.getElementById("addStudentButton").addEventListener("click", addStudentEventHandler);

document.getElementById("deleteStudentButton").addEventListener("click", deleteStudentEventHandler);

document.getElementById("loadStudentsButton").addEventListener("click", loadStudents);

document.getElementById("firstPageTable").addEventListener("click", firstPageEventHandler);
document.getElementById("nextPageTable").addEventListener("click", nextPageEventHandler);
document.getElementById("previousPageTable").addEventListener("click", previousPageEventHandler);
document.getElementById("lastPageTable").addEventListener("click", lastPageEventHandler);

itemsPerPageSelect.addEventListener("change", itemsPerPageEventHandler);

itemsPerPageEventHandler();
disableBackwardPageButtons();
disableForwardPageButtons();

function Student(surname, name, patronymic, dateOfBirth, group) {
    this.surname = surname;
    this.name = name;
    this.patronymic = patronymic;
    this.dateOfBirth = dateOfBirth;
    this.group = group;
}

function addStudentEventHandler() {
    let name = nameAddStudentInput.value;
    let surname = surnameAddStudentInput.value;
    let patronymic = patronymicAddStudentInput.value;
    let dateOfBirth = dateOfBirthAddStudentInput.value;
    let group = groupAddStudentInput.value;
    if (
        name.length > 0 &&
        surname.length > 0 &&
        patronymic.length > 0 &&
        dateOfBirth.length > 0 &&
        group.length > 0
    ) {
        let request = new XMLHttpRequest();
        request.open("POST", baseURL + "/student");
        request.send(JSON.stringify(new Student(surname, name, patronymic, dateOfBirth, group)));
        request.onload = function () {
            console.log(request.status);
        };
    }
}

function deleteStudentEventHandler() {
    let id = Number.parseInt(idDeleteStudentInput.value);
    if (Number.isInteger(id)) {
        let request = new XMLHttpRequest();
        request.open("DELETE", baseURL + "/student/" + id);
        request.send();
        request.onload = function () {
            console.log(request.status);
        };
    }
}

function loadStudents() {
    let request = new XMLHttpRequest();
    request.open("GET", baseURL + "/students");
    request.responseType = "json";
    request.send();
    request.onload = function () {
        if (request.status === 200) {
            studentsData = request.response;
            firstPageEventHandler();
        }
    };
}

function itemsPerPageEventHandler() {
    itemsPerPage = Number.parseInt(itemsPerPageSelect.value);
    const rowsLength = studentsTable.rows.length;
    if (rowsLength - 1 < itemsPerPage) {
        for (let i = 0; i < itemsPerPage - (rowsLength - 1); i++) {
            let newRow = studentsTable.insertRow();
            for (let j = 0; j < studentsTable.rows[0].cells.length; j++) {
                newRow.insertCell().textContent = "-";
            }
        }
        if (studentsData !== undefined) {
            firstPageEventHandler();
        }
    } else if (rowsLength - 1 > itemsPerPage) {
        for (let i = 0; i < rowsLength - 1 - itemsPerPage; i++) {
            studentsTable.deleteRow(rowsLength - 1 - i);
        }
        if (studentsData !== undefined) {
            firstPageEventHandler();
        }
    }
}

function firstPageEventHandler() {
    page = 0;
    if (studentsData.length > itemsPerPage) {
        enableForwardPageButtons();
    }
    disableBackwardPageButtons();
    toPage();
}

function previousPageEventHandler() {
    page--;
    if (page === 0) {
        disableBackwardPageButtons();
    }
    if (page + 1 === Math.trunc(studentsData.length / itemsPerPage)) {
        enableForwardPageButtons();
    }
    toPage();
}

function nextPageEventHandler() {
    page++;
    if (page === 1) {
        enableBackwardPageButtons();
    }
    if (page === Math.trunc(studentsData.length / itemsPerPage)) {
        disableForwardPageButtons();
    }
    console.log(page);
    toPage();
}

function lastPageEventHandler() {
    if (page === 0) {
        enableBackwardPageButtons();
    }
    page = Math.trunc(studentsData.length / itemsPerPage);
    disableForwardPageButtons();
    toPage();
}

function toPage() {
    fromIndex = page * itemsPerPage;
    let toIndex = fromIndex + Math.min(itemsPerPage, studentsData.length - fromIndex);
    showStudents(fromIndex, toIndex);
}

function showStudents(fromIndex, toIndex) {
    for (let i = fromIndex, j = 1; j < studentsTable.rows.length; i++, j++) {
        for (let c = 0; c < studentsTable.rows[0].cells.length; c++) {
            if (i < toIndex) {
                studentsTable.rows[j].cells[c].textContent = studentsData[i][valueNames[c]];
            } else {
                studentsTable.rows[j].cells[c].textContent = "-";
            }
        }
    }
    shownAndTotalLabel.textContent = fromIndex + 1 + " - " + toIndex + " of " + studentsData.length;
}

function disableBackwardPageButtons() {
    document.getElementById("firstPageTable").setAttribute("disabled", true);
    document.getElementById("previousPageTable").setAttribute("disabled", true);
}

function enableBackwardPageButtons() {
    document.getElementById("firstPageTable").removeAttribute("disabled");
    document.getElementById("previousPageTable").removeAttribute("disabled");
}

function disableForwardPageButtons() {
    document.getElementById("lastPageTable").setAttribute("disabled", true);
    document.getElementById("nextPageTable").setAttribute("disabled", true);
}

function enableForwardPageButtons() {
    document.getElementById("lastPageTable").removeAttribute("disabled");
    document.getElementById("nextPageTable").removeAttribute("disabled");
}
