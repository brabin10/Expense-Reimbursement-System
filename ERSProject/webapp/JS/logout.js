const url = "http://localhost:9898";
async function logout() {
   let response = await fetch(url +"/logout",{
      method: "POST"
   });

   if (response.status ===202) {
      window.location.href = './login.html';
      window.localStorage.clearItem('token');
   } 
}
   