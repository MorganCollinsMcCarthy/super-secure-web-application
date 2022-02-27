function addReply(id) {
    var reply = document.getElementById("reply_"+id).value;
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "admin/forum/reply?id="+id+"&reply="+reply);
    xhr.send()
}

function addPost() {
    var content = document.getElementById("content").value;

    var xhr = new XMLHttpRequest();
    //xhr.onload=insertPost;
    xhr.open("POST", "/forum/add");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(content);
}

function insertPost() {
    var record = JSON.parse(this.responseText);

    var table = document.getElementById("posts");
    var rows = table.querySelectorAll("tr");
    var row = table.insertRow(rows.length-1);
    row.id="row_"+record.id;
    var username_cell = row.insertCell(0);
    var content_cell = row.insertCell(1);

    username_cell.innerHTML=record.user.userName
    content_cell.innerHTML=record.content

    document.getElementById("content").value="";
}

function updateAppointment(id, dose) {
    var xhr = new XMLHttpRequest();
    var vaccineReceived = document.getElementById("vaccineReceived_" + id).value;
    xhr.open("POST", "/admin/update?id="+id+"&vaccineReceived="+vaccineReceived);
    xhr.send();
    if (vaccineReceived === "")
        document.getElementById("received_" + id).innerHTML="false";
    else {
        document.getElementById("received_" + id).innerHTML = "true";
        document.getElementById("vaccineSelector").innerHTML = vaccineReceived;
        document.getElementById("button").innerHTML = "";
    }

    if (dose === 1 && vaccineReceived !== "")
        document.getElementById("message").innerHTML="Appointment for dose 2 automatically booked for this user. Refresh page to view the appointment.";
    else if (dose === 2 && vaccineReceived !== "")
        document.getElementById("message").innerHTML="Records have been updated to show this user is now fully vaccinated.";
}