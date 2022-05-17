function addReply(id) {
    var reply = document.getElementById("reply_"+id).value;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "admin/forum/reply?id="+id+"&reply="+reply);
    xhr.setRequestHeader(header, token);
    xhr.send()
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