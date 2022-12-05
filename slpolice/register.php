
<?php include("db.php"); ?>

<?php 
$name = $_POST["name"];
$email = $_POST["email"];
$tel = $_POST["tp"];
$pass = $_POST["pwd"];
$date = $_POST["date"];

//if(!$name=="" or !$email=="" or !$tel=="" or !$pass=="" or !$date=="")
//{
	//add users table details
	$user = "INSERT INTO users(name, email, tp, date) VALUES('$name','$email','$tel','$date')";
	if(!mysqli_query($connect,$user))
	{
		echo ("not registered!");
		//printf("Error: %s\n", mysqli_error($connect));
		//echo $user;
	}
	else
	{
		echo ("registered!");
	}
	mysqli_errno($connect);
	
	//add login table details
	$login = "INSERT INTO logins(email, pwd, reason) VALUES('$email','".md5($pass)."','')";
	if(!mysqli_query($connect,$login))
	{
		echo "not login added!";
		printf("Error: %s\n", mysqli_error($connect));
		//echo $login;
	}
	else
	{
		//echo "login added!";
	}
	mysqli_close($connect);
/*}
else
{

}*/
?>

