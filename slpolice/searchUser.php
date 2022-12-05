<?php 
include("db.php");

$mail = $_POST['mail'];
//$mail = "kavi@gmail.com";

$sql = "select * from users where email='".$mail."'";
 
$res = mysqli_query($connect,$sql);
 
$result = array();
$result1 = array();

if(!$res) 
{
    printf("Error: %s\n", mysqli_error($connect));
    exit();
}
else
{
    while($row = mysqli_fetch_array($res))
    {
    	array_push($result,array('name'=>$row[1], 'tp'=>$row[3]));
    }
     
    echo json_encode(array("result"=>$result));
} 
mysqli_close($connect);
?>

