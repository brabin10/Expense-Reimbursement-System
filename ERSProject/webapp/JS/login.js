const url = "http://localhost:9898";
async function login() {
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;
    
    let user = {
        "username": username,
        "password": password
    }
    let response = await fetch(url +"/login",{
        method: "POST",
        body: JSON.stringify(user),
        credentials: 'include'
    })
            
    if(response.status === 202){
        // console.log(response.body)
        let data = await response.json();
        window.localStorage.setItem('token', data.token);
        window.localStorage.setItem('role_id', data.role_id);

        window.location = './dash.html';
    }
    else {
        alert('invalid credentials');
    }
}
    


