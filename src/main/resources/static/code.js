function addReply(id) {
    var reply = document.getElementById("reply_"+id).value;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "admin/forum/reply?id="+id+"&reply="+reply);
    xhr.setRequestHeader(header, token);
    xhr.send()
}

function addPost() {
    var content = document.getElementById("content").value;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var xhr = new XMLHttpRequest();
    //xhr.onload=insertPost;
    xhr.open("POST", "/forum/add");
    xhr.setRequestHeader(header, token);
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
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var vaccineReceived = document.getElementById("vaccineReceived_" + id).value;
    xhr.open("POST", "/admin/update?id="+id+"&vaccineReceived="+vaccineReceived);
    xhr.setRequestHeader(header, token);
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