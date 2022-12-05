<?php 
include("db.php");

$mail = $_POST['email'];

$sql = "select * from logins where email='".$mail."'";
 
$res = mysqli_query($connect,$sql);
 
$result = array();

if(!$res) 
{
    printf("Error: %s\n", mysqli_error($connect));
    exit();
}
else
{
    while($row = mysqli_fetch_array($res))
    {
    	array_push($result,array('reason'=>$row[4]));
    }
     
    echo json_encode(array("result"=>$result));
} 
mysqli_close($connect);
?>