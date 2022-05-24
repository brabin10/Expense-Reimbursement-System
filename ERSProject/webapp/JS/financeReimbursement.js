window.onload = function() {
    if (window.localStorage.getItem('role_id') == 1) {
        document.getElementById('approveDeny').style.display = "none";
        document.getElementById('instructions').innerHTML = "<h3>Welcome to the Reimbursement Portal</h3><p>Here you will be able to add and view all reimbursements submitted to the system.</p><p>Finance Managers will view and approve or deny your reimbursements as they see fit.</p>";
        document.getElementById('actions').innerHTML = "Employee Actions:";
    }
    getReimbursements();
};

function searchTable() {
    var input, filter, found, table, tr, td, i, j;
    input = document.getElementById("search");
    filter = input.value.toUpperCase();
    table = document.getElementById("table");
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td");
        for (j = 0; j < td.length; j++) {
            if (td[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
                found = true;
            }
        }
        if (found) {
            tr[i].style.display = "";
            found = false;
        } else {
            tr[i].style.display = "none";
        }
    }
}

async function getReimbursements() {
    let response = await fetch(url +"/reimbursements", {
        method: "GET",
        headers: new Headers({
            'Authorization': window.localStorage.getItem('token'),
        }),
    });

    let txt = '';
    if (response.status == 200) {
        let reimbursement = await response.json();
        
        for (x in reimbursement) {
            let status = '';

            if(reimbursement[x].resolved == null){
                reimbursement[x].resolved = 'Unresolved';
                reimbursement[x].resolverId = 'Unresolved';
            }

            if(reimbursement[x].status === 1){
                reimbursement[x].status = 'Pending';
            }
            else if(reimbursement[x].status === 2){
                reimbursement[x].status = 'Approved';
                status = 'approved';
            }
            else{
                reimbursement[x].status = 'Denied';
                status = 'denied';
            }

            if(reimbursement[x].type === 1){
                reimbursement[x].type = 'Lodging';
            }
            else if(reimbursement[x].type === 2){
                reimbursement[x].type = 'Travel';
            }
             else if(reimbursement[x].type === 3){
                reimbursement[x].type = 'Food';
            }
            else{
                reimbursement[x].type = 'Other';
            }

            txt += "<tr class= " + status +">";
            txt += "<td>" + reimbursement[x].id + "</td>";
            txt += "<td>" + "$" + reimbursement[x].amount + "</td>";
            txt += "<td>" + reimbursement[x].submitted + "</td>";
            txt += "<td>" + reimbursement[x].resolved + "</td>";
            txt += "<td>" + reimbursement[x].description + "</td>";
            txt += "<td>" + reimbursement[x].authorId + "</td>";
            txt += "<td>" + reimbursement[x].resolverId + "</td>";
            txt += "<td>" + reimbursement[x].status + "</td>";
            txt += "<td>" + reimbursement[x].type + "</td>";
            txt += "</tr>";
            txt += "</tbody>"
            txt += "</table>"  
            document.getElementById("table").innerHTML = txt;
        }
    }
    else if (response.status == 401) {
        alert('Unauthorized User. Login Again');
        window.location.href = './login.html';
    }
}

async function submitReimbursement() {
    let type = document.getElementById('type').value;
    let amount = document.getElementById('amount').value;
    let description = document.getElementById('description').value;
    
    let newReimbursement = {
        amount,
        submitted: new Date().getTime(),
        resolved: null,
        description,
        authorId: null,
        resolverId: null,
        status: 1,
        type,
    }

    let response = await fetch(url +"/reimbursements/create",{
        method: "POST",
        body: JSON.stringify(newReimbursement),
        headers: new Headers({
            'Authorization': window.localStorage.getItem('token'),
        }),
    })


    if(response.status === 200){
        alert('Added new reimbursement');
        window.location.href = './dash.html';
    }
    else if (response.status == 401) {
        alert('Unauthorized User. Login Again');
        window.location.href = './login.html';
    }
    else {
        alert('Unable to add the reimbursement');
    }
}
    
async function approveDeny() {
    let status = document.getElementById('status').value;
    let id = document.getElementById('reimbId').value;
    
    
    let data = {
        id,
        status
    }

    let response = await fetch(url +"/reimbursements/approve_deny",{
        method: "POST",
        body: JSON.stringify(data),
        headers: new Headers({
            'Authorization': window.localStorage.getItem('token'),
        }),
    })
    

    if(response.status === 200){
        alert('Approve or Deny success');
        window.location.href = './dash.html';
    }
    else if (response.status == 401) {
        alert('Unable to add the reimbursement');
        window.location.href = './login.html';
    }
    else {
        alert('Unable to approve or deny');
    }
}
