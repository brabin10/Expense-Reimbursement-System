const url = "http://localhost:9898";
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

async function displayByUser() {
    let response = await fetch(url + "/reimbursements", {credentials: "include"});
    console.log(response);
    if(response.status === 200) {
        let data = await response.json();
        console.log(data);
        for(let reimbursement of data){
    
        if (response.readyState == 4 && response.status == 200) {
            if (response.readyState == 4 && response.status == 200) {
                let reimbursement = JSON.parse(response.responseText);
                
                for (x in reimbursement) {
                    let status = '';
                    let d = new Date(parseInt(reimbursement[x].submitted));
                    reimbursement[x].submitted = d.toLocaleDateString();
                    
                    if(reimbursement[x].resolved != null){
                        let rd = new Date(parseInt(reimbursement[x].resolved));
                        reimbursement[x].resolved = rd.toLocaleDateString();
                    }
                    else {
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
                    
                    data += "<tr class= " + status +">";
                    data += "<td>" + reimbursement[x].id + "</td>";
                    data += "<td>" + "$" + reimbursement[x].amount + "</td>";
                    data += "<td>" + reimbursement[x].submitted + "</td>";
                    data += "<td>" + reimbursement[x].resolved + "</td>";
                    data += "<td>" + reimbursement[x].description + "</td>";
                    data += "<td>" + reimbursement[x].authorId + "</td>";
                    data += "<td>" + reimbursement[x].resolverId + "</td>";
                    data += "<td>" + reimbursement[x].status + "</td>";
                    data += "<td>" + reimbursement[x].type + "</td>";
                    data += "</tr>"; 
                    document.getElementById("table").innerHTML = data;
                }
            }}
        }
    }
//     response.open('GET', '../reimbursements/user');
//     response.send();
 }

async function submitReimbursement() {
    let type = document.getElementById('type').value;
    let typeId;
    if(type === 'Lodging'){
        typeId = 1;
    }
    else if(type === 'Travel'){
        typeId = 2;
    }
    else if(type === 'Food'){
        typeId = 3;
    }
    else{
        typeId = 4;
    }
    
    let status = 1;
    let resolver = null;
    let amount = document.getElementById('amount').value;
    let description = document.getElementById('description').value;
    let submitted = Date.now();
    let author;
    
    let newReimbursement = {
        "amount": amount,
        "submitted": submitted,
        "resolved": null,
        "description": description,
        "authorId": author,
        "resolverId": resolver,
        "status": status,
        "type": typeId,
    }
    
    let response = await fetch(url+"/login",{
        method: ("POST", "../reimbursements/add"),
        body:JSON.stringify(newReimbursement),
        credentials: "include"
    })
    let data = await response.json();
        if(response.status === 200){
            alert('Added new reimbursement');
            window.location = './employee-dash.html';
        }
        else {
            alert('Unable to add the reimbursement');
        }
    
    
    // response.open('POST', '../reimbursements/add');
    // response.send(JSON.stringify(newReimbursement)); 
}

async function logout() {

    let response = await fetch(url+"/login",{
        method: ("GET", "../users/logout"),
        body:JSON.stringify(),
        credentials: "include"
    })
    
    
    let data = await response.json();
        if(response.status === 200){
            window.location = './login.html';
        }
        else {
            alert('failed to logout');
        }
    }
    // response.open('GET', '../users/logout');
    // response.send();
//}
