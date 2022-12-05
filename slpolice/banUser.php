<?php
include("db.php");

$mail = $_POST['email'];
$reason = $_POST['reason'];

$update = "update logins set loginlevel= 2, reason='".$reason."' where email='".$mail."'";
$sql = "select * from logins where email='".$mail."'";
$res = mysqli_query($connect,$sql);
while($row=mysqli_fetch_array($res))
{
    if($row['email']==$mail)
    {
        try
        {
        	if(!mysqli_query($connect,$update))
        	{
        		echo "notadd";
        	}
        	else
        	{
        		echo "added";
        	}
        }
        catch(Exception $update)
        {
        	echo "dberror";
        }
    }
    else
    {
        echo "invalid";
    }
}
mysqli_close($connect);

?>