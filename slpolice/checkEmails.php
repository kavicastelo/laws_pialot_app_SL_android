<?php 
include("db.php");

$mail = $_POST['email'];

$sql="select * from logins where email='".$mail."'";
$res = mysqli_query($connect,$sql);

while($row = mysqli_fetch_array($res))
{
    if($row['email']==$mail)
    {
        echo "match";
    }
    else
    {
        echo "notmatch";
    }
}
?>