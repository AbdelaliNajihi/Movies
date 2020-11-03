/**
 * 
 */
function change() {
	usernameValue = document.getElementById("username").value;
	localStorage.setItem("usernameValue", usernameValue);
	console.log(usernameValue);
}